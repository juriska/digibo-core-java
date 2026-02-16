# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Is

Digibo Core is a Spring Boot API gateway for a bank backoffice system, migrated from Node.js Express. It proxies requests to Oracle PL/SQL stored procedures — all business logic lives in Oracle, not in Java. The app serves 57+ controller route groups covering orders, payments, customers, reports, admin, etc.

## Build & Run Commands

```bash
# Run with mock profile (no database needed)
mvn spring-boot:run -Dspring-boot.run.profiles=mock

# Run with dev profile (requires Oracle DB + env vars: DB_USER, DB_PASSWORD, DB_CONNECTION_STRING)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

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

## Architecture

### Profile-Based Dual Service Implementations

Every service has an interface and two implementations selected by Spring profile:

- **Real** (`@Profile("!mock")`) — extends `BaseService`, calls Oracle PL/SQL via JDBC
- **Mock** (`@Profile("mock")`) — returns hardcoded data, no DB dependency

```
service/
├── CustomerService.java              # Interface
├── impl/CustomerServiceImpl.java     # @Profile("!mock") - Oracle calls
└── mock/CustomerServiceMock.java     # @Profile("mock") - test data
```

When adding a new service, you must create all three files and use the correct `@Profile` annotations.

### BaseService — Oracle PL/SQL Execution Framework

`service/base/BaseService.java` is the foundation for all real service implementations. Key methods:

| Method | Use Case |
|--------|----------|
| `executeCursorProcedure()` | Procedure returning a result set (REF CURSOR) |
| `executeProcedure()` | Procedure with output parameters |
| `executeScalarFunction()` | Function returning a single value |
| `executeVoidProcedure()` | Procedure with no return value |
| `executeProcedureWithOutputs()` | Multiple output parameters, no cursor |
| `executeCursorProcedureWithOutputs()` | Cursor + additional output parameters |

Parameters are built with helper methods: `inParam()`, `outParam()`, `cursorParam()`.

### Security & Authorization

- **JWT tokens in httpOnly cookies** (not response body, not localStorage)
- `JwtAuthenticationFilter` extracts token from cookies first, then falls back to Authorization header
- Permissions follow format `PACKAGE_NAME.PROCEDURE_NAME` (derived from Oracle EXECUTE grants via `BO_AUTH` PL/SQL package)
- Permissions are embedded in the JWT at login time
- Controllers use `@PreAuthorize("hasPermission(null, 'BO_CUSTOMER.FIND')")` — checked by `OraclePermissionEvaluator`
- `MockAuthenticationProvider` provides 3 test users: user1/password1, user2/password2, user3/password3

### Controller Pattern

All controllers follow the same structure:
- `@RestController` + `@RequestMapping("/api/...")`
- Inject service interface (not implementation)
- Protected endpoints use `@PreAuthorize` with permission strings
- Return `ResponseEntity<Map<String, Object>>` or `ResponseEntity<List<Map<String, Object>>>`

### Configuration

`application.yml` uses multi-document format with three profiles: `dev`, `mock`, `prod`. The mock profile excludes all DataSource auto-configuration. CORS origins are configured in the yaml under `cors.allowed-origins`.

## Package Structure

```
com.digibo.core
├── config/          # SecurityConfig, CorsConfig, DatabaseConfig, MockDatabaseConfig, OpenApiConfig
├── controller/      # 57 REST controllers
├── security/        # JWT provider, auth filter, permission evaluator, UserPrincipal
├── service/
│   ├── base/        # BaseService (Oracle PL/SQL execution framework)
│   ├── impl/        # Real implementations (@Profile("!mock"))
│   └── mock/        # Mock implementations (@Profile("mock"))
├── dto/
│   ├── request/     # LoginRequest, etc.
│   └── response/    # AuthResponse, ErrorResponse, etc.
└── exception/       # GlobalExceptionHandler, custom exceptions
```

## Key Technical Details

- **Spring Boot 4.0.1**, Java 20, Oracle JDBC 23.3, JJWT 0.12.3, Lombok, SpringDoc OpenAPI
- `pom.xml` sets `java.version` to 20 (README references Java 21 — there is a discrepancy)
- `GlobalExceptionHandler` includes detailed error info only in dev/mock profiles
- Docker build uses `eclipse-temurin:21` image
