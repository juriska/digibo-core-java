# Digibo Core - Spring Boot API Gateway

Spring Boot 3.2 migration of the digibo-core Node.js Express application.

## Requirements

- Java 21 LTS
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
mvn test
```

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

- **mock**: Uses mock services, no database connection required
- **dev**: Development with Oracle database and debug logging
- **prod**: Production settings with optimized connection pool

---

## Authentication & Security

### Overview

The application uses JWT-based authentication with **httpOnly cookies** for secure token storage. This protects against XSS attacks by making tokens inaccessible to JavaScript.

### Authentication Flow

```
1. User sends credentials to POST /api/auth/login
2. Server validates credentials and generates JWT tokens
3. Tokens are set as httpOnly cookies (not in response body)
4. Response contains user info: userId, username, roles, permissions
5. Subsequent requests automatically include cookies
6. Server validates token from cookie on each request
```

### Auth Endpoints

| Endpoint | Method | Auth Required | Description |
|----------|--------|---------------|-------------|
| `/api/auth/login` | POST | No | Authenticate and receive cookies |
| `/api/auth/refresh` | POST | No (uses refresh cookie) | Refresh access token |
| `/api/auth/logout` | POST | Yes | Clear auth cookies |
| `/api/auth/validate` | GET | Yes | Validate current token |
| `/api/auth/me` | GET | Yes | Get current user info |

### Login Request

```json
POST /api/auth/login
{
  "username": "user1",
  "password": "password123"
}
```

### Login Response

```json
{
  "accessToken": null,
  "refreshToken": null,
  "tokenType": "Bearer",
  "userId": "user-001",
  "username": "user1",
  "roles": ["RBOFFORDERS", "RBOPAYMENT"],
  "permissions": ["BO_CUSTOMER.FIND", "BO_PAYMENT.FIND"]
}
```

Note: Tokens are `null` in response because they are stored in httpOnly cookies.

### Cookie Configuration

Cookies are configured with security best practices:

| Attribute | Value | Purpose |
|-----------|-------|---------|
| `HttpOnly` | true | Prevents JavaScript access (XSS protection) |
| `Secure` | configurable | HTTPS only in production |
| `SameSite` | Lax | CSRF protection |
| `Path` | / | Available for all routes |

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

### Adding Permission Checks to Controllers

1. Import the annotation:
```java
import org.springframework.security.access.prepost.PreAuthorize;
```

2. Add to endpoint methods:
```java
@PreAuthorize("hasPermission(null, 'PACKAGE.PROCEDURE')")
```

3. The `OraclePermissionEvaluator` checks the user's JWT permissions.

### Permission Evaluator

The `OraclePermissionEvaluator` class checks permissions from the JWT token:

```java
// In controller
@PreAuthorize("hasPermission(null, 'BO_PAYMENT.FIND')")

// Evaluator checks if user's permissions contain 'BO_PAYMENT.FIND'
```

---

## Security Configuration

### SecurityConfig.java

```java
.authorizeHttpRequests(auth -> auth
    // Public endpoints
    .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
    .requestMatchers("/actuator/health").permitAll()
    .requestMatchers("/swagger-ui/**").permitAll()
    // All other endpoints require authentication
    .anyRequest().authenticated()
)
```

### CORS Configuration

Allowed origins are configured in `CorsConfig.java`:

```java
private List<String> allowedOrigins = List.of(
    "http://localhost:3000",
    "http://localhost:4200"
);
```

For production, update via environment variables or application properties.

---

## BO_AUTH Package

The `BO_AUTH` PL/SQL package manages user permissions in Oracle:

### Installation

```sql
-- Run the package creation script
@src/main/resources/db/BO_AUTH_PACKAGE.sql

-- Grant execute to the application user
GRANT EXECUTE ON BO_AUTH TO app_service_account;
```

### Procedures

| Procedure | Description |
|-----------|-------------|
| `GET_USER_PERMISSIONS` | Returns all permissions for a user |
| `HAS_PERMISSION` | Checks if user has specific permission |

### How It Works

1. User logs in with Oracle credentials
2. `BO_AUTH.GET_USER_PERMISSIONS` queries Oracle's data dictionary
3. Returns packages/procedures the user has EXECUTE grants on
4. Permissions are embedded in JWT token
5. Server validates permissions on each protected endpoint

---

## API Endpoints

The API maintains compatibility with the Node.js version:

- `GET /` - Health check
- `GET /api/status` - Application status
- `POST /api/auth/login` - JWT authentication
- `POST /api/auth/refresh` - Refresh token
- `POST /api/auth/logout` - Logout (clear cookies)
- `GET /api/auth/me` - Current user info
- `GET /api/customers/*` - Customer operations
- `POST /api/payments/*` - Payment operations
- ... (55+ route groups)

---

## Project Structure

```
src/main/java/com/digibo/core/
├── Application.java           # Main entry point
├── config/
│   ├── SecurityConfig.java    # Spring Security configuration
│   ├── CorsConfig.java        # CORS settings
│   └── ...
├── controller/
│   ├── AuthController.java    # Authentication endpoints
│   ├── CustomerController.java # Customer operations (with @PreAuthorize)
│   ├── PaymentController.java  # Payment operations (with @PreAuthorize)
│   └── ...
├── security/
│   ├── JwtTokenProvider.java         # JWT generation/validation
│   ├── JwtAuthenticationFilter.java  # Cookie/header token extraction
│   ├── OraclePermissionEvaluator.java # Permission checking
│   └── UserPrincipal.java            # Authenticated user details
├── service/
│   ├── AuthPermissionService.java    # Permission service interface
│   ├── impl/
│   │   └── AuthPermissionServiceImpl.java # Oracle implementation
│   └── mock/
│       └── AuthPermissionServiceMock.java # Mock implementation
├── dto/
│   ├── request/
│   │   └── LoginRequest.java
│   └── response/
│       └── AuthResponse.java
└── exception/
    ├── UnauthorizedException.java
    └── GlobalExceptionHandler.java
```

---

## Security Best Practices Implemented

| Practice | Implementation |
|----------|----------------|
| httpOnly Cookies | Tokens stored in cookies, not localStorage |
| SameSite Cookies | CSRF protection via SameSite attribute |
| Server-side Auth | All protected endpoints require valid JWT |
| Permission-based Access | @PreAuthorize on controller methods |
| Secure Defaults | All routes require auth except login/refresh |
| Token Expiration | Configurable access/refresh token TTL |
| CORS Restrictions | Whitelist of allowed origins |

---

## Migration from Node.js

This application is a direct migration from the Express.js version, maintaining:

- Same endpoint paths and HTTP methods
- Same request/response formats
- Same Oracle PL/SQL procedure calls
- Mock mode support for testing without database
- **Enhanced security** with httpOnly cookies and server-side permission checks
