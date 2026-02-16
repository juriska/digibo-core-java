# Digibo Core - Spring Boot API Gateway

Spring Boot 4.0.1 migration of the digibo-core Node.js Express application. Proxies requests to Oracle PL/SQL stored procedures — all business logic lives in Oracle, not in Java.

## Requirements

- Java 20 (pom.xml target), Docker image uses Eclipse Temurin 21
- Maven 3.9+
- Oracle Database (for non-mock profiles)

## Quick Start

### Run with Mock Profile (No Database Required)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mock
```

### Run with Development Profile

```bash
# Set environment variables
export DB_USER=your_user
export DB_PASSWORD=your_password
export DB_CONNECTION_STRING=localhost:1521/XEPDB1

mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Run Tests

```bash
# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=JwtTokenProviderTest

# Run a single test method
mvn test -Dtest=JwtTokenProviderTest#testGenerateToken

# Build without tests
mvn clean package -DskipTests
```

Server runs on port 3000 by default. Swagger UI at `/swagger-ui/`.

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active profile (dev, mock, prod) | dev |
| `DB_USER` | Oracle database username | - |
| `DB_PASSWORD` | Oracle database password | - |
| `DB_CONNECTION_STRING` | Oracle connection string | localhost:1521/XEPDB1 |
| `PORT` | Server port | 3000 |
| `JWT_SECRET` | JWT signing secret (min 256 bits) | - |
| `JWT_EXPIRATION` | Access token expiration (ms) | 86400000 |
| `JWT_REFRESH_EXPIRATION` | Refresh token expiration (ms) | 604800000 |
| `COOKIE_SECURE` | Use secure cookies (HTTPS only) | false |
| `COOKIE_SAME_SITE` | SameSite cookie attribute | Lax |

### Profiles

- **mock**: Uses mock services, no database connection required. 3 test users: user1/password1, user2/password2, user3/password3
- **dev**: Development with Oracle database and debug logging
- **prod**: Production settings with optimized connection pool (up to 20 connections)

---

## Authentication & Security

### Overview

The application uses JWT-based authentication with **httpOnly cookies** for secure token storage. Login passwords are RSA-encrypted in transit — the frontend encrypts with a server-provided public key, and the server decrypts before authentication.

### Authentication Flow

```
1. Frontend fetches RSA public key from GET /api/auth/public-key
2. Frontend encrypts password with RSA public key
3. User sends credentials to POST /api/auth/login
4. Server decrypts password, validates credentials
5. Server creates an officer session (registers in officers_online table)
6. JWT tokens are set as httpOnly cookies (not in response body)
7. Response contains user info: userId, username, roles, permissions, sessionId
8. Subsequent requests automatically include cookies
9. Server validates token from cookie on each request
10. On logout, officer session is closed and cookies are cleared
```

### Auth Endpoints

| Endpoint | Method | Auth Required | Description |
|----------|--------|---------------|-------------|
| `/api/auth/public-key` | GET | No | Get RSA public key for password encryption |
| `/api/auth/login` | POST | No | Authenticate and receive cookies |
| `/api/auth/refresh` | POST | No (uses refresh cookie) | Refresh access token |
| `/api/auth/logout` | POST | Yes | Close officer session and clear auth cookies |
| `/api/auth/validate` | GET | Yes | Validate current token |
| `/api/auth/me` | GET | Yes | Get current user info |

### Login Request

Password must be RSA-encrypted using the public key from `/api/auth/public-key`:

```json
POST /api/auth/login
{
  "username": "user1",
  "password": "<RSA-encrypted password>"
}
```

### Login Response

```json
{
  "userId": "user-001",
  "username": "user1",
  "roles": ["RBOFFORDERS", "RBOPAYMENT"],
  "permissions": ["BO_CUSTOMER.FIND", "BO_PAYMENT.FIND"],
  "sessionId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

Tokens are stored in httpOnly cookies (not in the JSON response). The `sessionId` identifies the officer's Oracle session in the `officers_online` table, used by 3rd party systems to verify officer permissions.

### Cookie Configuration

| Attribute | Value | Purpose |
|-----------|-------|---------|
| `HttpOnly` | true | Prevents JavaScript access (XSS protection) |
| `Secure` | configurable | HTTPS only in production |
| `SameSite` | Lax | CSRF protection |
| `Path` | / | Available for all routes |

---

## Officer Session Management

On login, the system creates a dedicated Oracle connection using the officer's own credentials and registers a session in the `officers_online` table. This allows a 3rd party system to verify officer permissions by looking up the session ID.

- **Login**: Opens a dedicated Oracle connection with officer credentials, calls `BO_AUTH.REGISTER_SESSION`, returns a `sessionId`
- **Logout**: Calls `BO_AUTH.REMOVE_SESSION`, closes the dedicated connection
- **Refresh**: Preserves the existing `sessionId` in the new JWT token
- **Shutdown**: All active sessions are cleaned up via `@PreDestroy`

The `sessionId` is embedded in the JWT and returned in the `AuthResponse` so the frontend can pass it to 3rd party systems via URL.

---

## Authorization

### Permission Model

Permissions are fetched from Oracle using the `BO_AUTH` PL/SQL package. Each permission follows the format: `PACKAGE_NAME.PROCEDURE_NAME`.

### Server-Side Permission Checks

Controllers use `@PreAuthorize` annotations to enforce permissions:

```java
@GetMapping("/search")
@PreAuthorize("hasPermission(null, 'BO_CUSTOMER.FIND')")
public ResponseEntity<List<Map<String, Object>>> searchUsers(...) {
    // Only accessible if user has BO_CUSTOMER.FIND permission
}
```

The `OraclePermissionEvaluator` checks the user's JWT permissions against the required permission string.

---

## Security Configuration

### SecurityConfig.java

```java
.authorizeHttpRequests(auth -> auth
    // Public endpoints
    .requestMatchers("/api/auth/login", "/api/auth/refresh", "/api/auth/public-key").permitAll()
    .requestMatchers("/actuator/health").permitAll()
    .requestMatchers("/swagger-ui/**").permitAll()
    // All other endpoints require authentication
    .anyRequest().authenticated()
)
```

### CORS Configuration

Allowed origins are configured in `application.yml` under `cors.allowed-origins`:

```yaml
cors:
  allowed-origins:
    - https://preview--digi-backstage-haven.lovable.app
    - http://localhost:3000
    - http://localhost:4200
    - http://localhost:5173
```

---

## BO_AUTH Package

The `BO_AUTH` PL/SQL package manages user permissions and officer sessions in Oracle:

### Procedures

| Procedure | Description |
|-----------|-------------|
| `GET_USER_PERMISSIONS` | Returns all permissions for a user |
| `HAS_PERMISSION` | Checks if user has specific permission |
| `REGISTER_SESSION` | Registers an officer session in officers_online |
| `REMOVE_SESSION` | Removes an officer session from officers_online |

### How It Works

1. User logs in with Oracle credentials (RSA-encrypted password)
2. `BO_AUTH.GET_USER_PERMISSIONS` queries Oracle's data dictionary
3. Returns packages/procedures the user has EXECUTE grants on
4. Permissions are embedded in JWT token
5. A dedicated Oracle connection is opened with officer credentials
6. `BO_AUTH.REGISTER_SESSION` registers the session in `officers_online`
7. Server validates permissions on each protected endpoint

---

## API Endpoints

The API maintains compatibility with the Node.js version. 57 controllers covering:

- `GET /` - Health check
- `GET /api/status` - Application status
- `GET /api/auth/public-key` - RSA public key for password encryption
- `POST /api/auth/login` - JWT authentication with officer session creation
- `POST /api/auth/refresh` - Refresh token (preserves session)
- `POST /api/auth/logout` - Logout (closes officer session, clears cookies)
- `GET /api/auth/validate` - Validate current token
- `GET /api/auth/me` - Current user info
- `GET /api/customers/*` - Customer operations
- `POST /api/payments/*` - Payment operations
- ... (57 controller route groups total)

---

## Project Structure

```
src/main/java/com/digibo/core/
├── Application.java
├── config/
│   ├── SecurityConfig.java         # Spring Security configuration
│   ├── CorsConfig.java             # CORS settings (from application.yml)
│   ├── DatabaseConfig.java         # Oracle DataSource (dev/prod profiles)
│   ├── MockDatabaseConfig.java     # Excludes DataSource (mock profile)
│   └── OpenApiConfig.java          # Swagger/OpenAPI configuration
├── controller/
│   ├── AuthController.java         # Authentication + officer session management
│   ├── CustomerController.java     # Customer operations
│   ├── PaymentController.java      # Payment operations
│   └── ... (57 controllers total)
├── security/
│   ├── JwtTokenProvider.java          # JWT generation/validation (incl. sessionId claim)
│   ├── JwtAuthenticationFilter.java   # Cookie/header token extraction
│   ├── OraclePermissionEvaluator.java # Permission checking from JWT
│   ├── MockAuthenticationProvider.java # Mock users for testing
│   ├── RsaKeyProvider.java           # RSA key pair for password encryption
│   └── UserPrincipal.java            # Authenticated user details
├── service/
│   ├── AuthPermissionService.java     # Permission service interface
│   ├── OfficerSessionService.java     # Officer session management interface
│   ├── base/
│   │   └── BaseService.java           # Oracle PL/SQL execution framework
│   ├── impl/
│   │   ├── AuthPermissionServiceImpl.java    # Oracle permission lookup
│   │   ├── OfficerSessionServiceImpl.java    # Oracle officer session management
│   │   └── ...
│   └── mock/
│       ├── AuthPermissionServiceMock.java    # Mock permissions
│       ├── OfficerSessionServiceMock.java    # Mock officer sessions
│       └── ...
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   └── ...
│   └── response/
│       ├── AuthResponse.java          # Includes sessionId field
│       ├── ErrorResponse.java
│       └── ...
└── exception/
    ├── GlobalExceptionHandler.java    # Detailed errors in dev/mock only
    ├── UnauthorizedException.java
    ├── DatabaseException.java
    ├── ValidationException.java
    └── ResourceNotFoundException.java
```

---

## Tech Stack

| Component | Version |
|-----------|---------|
| Spring Boot | 4.0.1 |
| Java | 20 (compile target) |
| Oracle JDBC (ojdbc11) | 23.3.0.23.09 |
| JJWT | 0.12.3 |
| SpringDoc OpenAPI | 2.8.4 |
| Lombok | managed by Spring Boot |
| Docker base image | eclipse-temurin:21 |

---

## Docker

```bash
# Build
docker build -t digibo-core .

# Run
docker run -p 3000:3000 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_USER=your_user \
  -e DB_PASSWORD=your_password \
  -e DB_CONNECTION_STRING=host:1521/SERVICE \
  -e JWT_SECRET=your-secret-key \
  digibo-core
```

---

## Security Best Practices Implemented

| Practice | Implementation |
|----------|----------------|
| RSA Password Encryption | Passwords encrypted in transit with server-generated RSA key pair |
| httpOnly Cookies | Tokens stored in cookies, not localStorage |
| SameSite Cookies | CSRF protection via SameSite attribute |
| Server-side Auth | All protected endpoints require valid JWT |
| Permission-based Access | @PreAuthorize on controller methods |
| Secure Defaults | All routes require auth except login/refresh/public-key |
| Token Expiration | Configurable access/refresh token TTL |
| CORS Restrictions | Whitelist of allowed origins |
| Officer Sessions | Dedicated Oracle connections per officer for 3rd party verification |

---

## Migration from Node.js

This application is a direct migration from the Express.js version, maintaining:

- Same endpoint paths and HTTP methods
- Same request/response formats
- Same Oracle PL/SQL procedure calls
- Mock mode support for testing without database
- **Enhanced security** with httpOnly cookies, RSA password encryption, and server-side permission checks
