# DigiBo Bank Backoffice System - Improvement & Realization Plan

## Current System Overview

| Component | Technology | Status |
|-----------|------------|--------|
| **digibo-core-java** | Spring Boot 4.0, Java 21, Oracle DB | Production-ready, 60+ controllers |
| **digibo-ui** | Angular 19, TypeScript 5.7 | Core framework complete, mock data |

---

## Phase 1: Foundation & Integration (Priority: Critical)

### 1.1 Environment Configuration
**Goal:** Proper environment management for dev/staging/prod

**Backend (digibo-core-java):**
- [x] Already has profiles: mock, dev, prod
- [ ] Add staging profile
- [ ] Externalize all secrets to environment variables

**Frontend (digibo-ui):**
- [ ] Create `src/environments/environment.ts` (development)
- [ ] Create `src/environments/environment.prod.ts` (production)
- [ ] Create `src/environments/environment.staging.ts` (staging)
- [ ] Update `auth.service.ts` to use environment config
- [ ] Configure `angular.json` file replacements

**Files to modify:**
- `digibo-ui/src/environments/environment.ts`
- `digibo-ui/src/environments/environment.prod.ts`
- `digibo-ui/src/app/core/services/auth.service.ts`
- `digibo-ui/angular.json`

---

### 1.2 API Integration - Connect UI to Real Backend
**Goal:** Replace mock data with real API calls

**Orders Module:**
- [ ] Create `OrdersService` in `digibo-ui/src/app/core/services/`
- [ ] Define `Order` interface in `digibo-ui/src/app/core/models/`
- [ ] Implement API calls: GET /api/orders/search, GET /api/orders/{id}
- [ ] Update `orders.component.ts` to use OrdersService
- [ ] Add loading states and error handling

**Payments Module:**
- [ ] Create `PaymentsService` in `digibo-ui/src/app/core/services/`
- [ ] Define `Payment` interface in `digibo-ui/src/app/core/models/`
- [ ] Implement API calls: GET /api/payments/search, POST /api/payments/approve
- [ ] Update `payments.component.ts` to use PaymentsService
- [ ] Add loading states and error handling

**Files to create:**
- `digibo-ui/src/app/core/services/orders.service.ts`
- `digibo-ui/src/app/core/services/payments.service.ts`
- `digibo-ui/src/app/core/models/order.model.ts`
- `digibo-ui/src/app/core/models/payment.model.ts`

---

### 1.3 Error Handling & User Feedback
**Goal:** Consistent error handling and user notifications

**Backend:**
- [x] GlobalExceptionHandler already implemented
- [ ] Add request correlation IDs for tracing
- [ ] Standardize error response format

**Frontend:**
- [ ] Create `NotificationService` for toast messages
- [ ] Create toast/snackbar component
- [ ] Update HTTP interceptor to show error toasts
- [ ] Add success notifications for actions (approve, reject, etc.)

**Files to create:**
- `digibo-ui/src/app/core/services/notification.service.ts`
- `digibo-ui/src/app/shared/components/toast/toast.component.ts`

---

### 1.4 Loading States
**Goal:** Better UX during data fetching

- [ ] Create reusable loading spinner component
- [ ] Create skeleton loader components for tables
- [ ] Add loading states to all data-fetching components
- [ ] Implement optimistic UI updates where appropriate

**Files to create:**
- `digibo-ui/src/app/shared/components/spinner/spinner.component.ts`
- `digibo-ui/src/app/shared/components/skeleton-table/skeleton-table.component.ts`

---

## Phase 2: Security Enhancements (Priority: High)

### 2.1 CSRF Protection
**Goal:** Protect against Cross-Site Request Forgery

**Backend:**
- [ ] Enable CSRF protection in SecurityConfig
- [ ] Configure CSRF token cookie (XSRF-TOKEN)
- [ ] Exclude safe methods (GET, HEAD, OPTIONS)

**Frontend:**
- [ ] Configure HttpClientXsrfModule
- [ ] Ensure XSRF-TOKEN cookie is read and sent as X-XSRF-TOKEN header

**Files to modify:**
- `digibo-core-java/src/main/java/com/digibo/core/config/SecurityConfig.java`
- `digibo-ui/src/app/app.config.ts`

---

### 2.2 Rate Limiting
**Goal:** Prevent brute force and DDoS attacks

**Backend:**
- [ ] Add Bucket4j or Resilience4j dependency
- [ ] Implement rate limiting filter
- [ ] Configure limits: 100 requests/minute per IP for general, 5 requests/minute for login
- [ ] Return 429 Too Many Requests when exceeded

**Files to create:**
- `digibo-core-java/src/main/java/com/digibo/core/config/RateLimitConfig.java`
- `digibo-core-java/src/main/java/com/digibo/core/filter/RateLimitFilter.java`

---

### 2.3 Audit Logging
**Goal:** Track all user actions for compliance

**Backend:**
- [ ] Create AuditLog entity/DTO
- [ ] Create AuditService for logging actions
- [ ] Add AOP aspect to log controller method calls
- [ ] Store: timestamp, userId, action, resource, details, IP address
- [ ] Create audit log query endpoints

**Frontend:**
- [ ] Add audit log viewer for admin users
- [ ] Display user action history

**Files to create:**
- `digibo-core-java/src/main/java/com/digibo/core/service/AuditService.java`
- `digibo-core-java/src/main/java/com/digibo/core/aspect/AuditAspect.java`
- `digibo-core-java/src/main/java/com/digibo/core/dto/AuditLogDto.java`
- `digibo-ui/src/app/features/audit-log/audit-log.component.ts`

---

### 2.4 Session Management
**Goal:** Secure session handling with timeout

**Backend:**
- [ ] Add session timeout configuration (default: 30 minutes)
- [ ] Implement token refresh mechanism
- [ ] Add "remember me" functionality (extended token lifetime)

**Frontend:**
- [ ] Implement idle detection service
- [ ] Show warning modal before session expires
- [ ] Auto-logout after inactivity
- [ ] Implement token refresh on activity

**Files to create:**
- `digibo-ui/src/app/core/services/idle.service.ts`
- `digibo-ui/src/app/shared/components/session-timeout-modal/session-timeout-modal.component.ts`

---

## Phase 3: New Features (Priority: Medium)

### 3.1 User Management Module
**Goal:** Admin panel for user/role management

**Backend endpoints:**
- [ ] GET /api/admin/users - List all users
- [ ] GET /api/admin/users/{id} - Get user details
- [ ] PUT /api/admin/users/{id} - Update user
- [ ] POST /api/admin/users/{id}/roles - Assign roles
- [ ] GET /api/admin/roles - List all roles
- [ ] GET /api/admin/permissions - List all permissions

**Frontend:**
- [ ] Create user-management feature module
- [ ] User list with search/filter
- [ ] User detail/edit form
- [ ] Role assignment interface
- [ ] Add route with ADMIN role requirement

**Files to create:**
- `digibo-core-java/src/main/java/com/digibo/core/controller/AdminController.java`
- `digibo-ui/src/app/features/user-management/user-management.component.ts`
- `digibo-ui/src/app/features/user-management/user-list/user-list.component.ts`
- `digibo-ui/src/app/features/user-management/user-detail/user-detail.component.ts`

---

### 3.2 Dashboard Analytics
**Goal:** Visual insights into system activity

**Backend endpoints:**
- [ ] GET /api/dashboard/stats - Summary statistics
- [ ] GET /api/dashboard/charts/transactions - Transaction volume data
- [ ] GET /api/dashboard/charts/approvals - Approval rate data

**Frontend:**
- [ ] Add chart library (ng2-charts or ngx-echarts)
- [ ] Transaction volume chart (line/bar)
- [ ] Approval rate chart (pie/donut)
- [ ] Pending items counter
- [ ] Recent activity feed

**Files to modify/create:**
- `digibo-ui/src/app/features/dashboard/dashboard.component.ts`
- `digibo-ui/src/app/core/services/dashboard.service.ts`

---

### 3.3 Notifications System
**Goal:** Real-time alerts for pending actions

**Backend:**
- [ ] Add WebSocket support (Spring WebSocket)
- [ ] Create notification service
- [ ] Send notifications for: new pending approvals, status changes

**Frontend:**
- [ ] Add WebSocket client service
- [ ] Notification bell icon in header
- [ ] Notification dropdown list
- [ ] Unread count badge
- [ ] Mark as read functionality

**Files to create:**
- `digibo-core-java/src/main/java/com/digibo/core/config/WebSocketConfig.java`
- `digibo-core-java/src/main/java/com/digibo/core/service/NotificationService.java`
- `digibo-ui/src/app/core/services/websocket.service.ts`
- `digibo-ui/src/app/shared/components/notifications/notifications.component.ts`

---

### 3.4 Export & Reports
**Goal:** Export data to PDF/Excel

**Backend:**
- [ ] Add Apache POI dependency for Excel
- [ ] Add OpenPDF/iText dependency for PDF
- [ ] Create ExportService
- [ ] Endpoints: GET /api/export/orders, GET /api/export/payments
- [ ] Support query parameters for filtering

**Frontend:**
- [ ] Add export buttons to data tables
- [ ] Download file handling
- [ ] Export options modal (format, date range, filters)

**Files to create:**
- `digibo-core-java/src/main/java/com/digibo/core/service/ExportService.java`
- `digibo-core-java/src/main/java/com/digibo/core/controller/ExportController.java`

---

### 3.5 Advanced Data Tables
**Goal:** Enhanced table functionality

**Frontend features:**
- [ ] Server-side pagination
- [ ] Column sorting
- [ ] Advanced filtering (date range, status, amount range)
- [ ] Column visibility toggle
- [ ] Row selection for bulk actions
- [ ] Create reusable data-table component

**Files to create:**
- `digibo-ui/src/app/shared/components/data-table/data-table.component.ts`
- `digibo-ui/src/app/core/models/pagination.model.ts`

---

## Phase 4: DevOps & Quality (Priority: Medium)

### 4.1 CI/CD Pipeline
**Goal:** Automated build, test, and deployment

**GitHub Actions workflows:**
- [ ] `.github/workflows/backend-ci.yml` - Build & test Java
- [ ] `.github/workflows/frontend-ci.yml` - Build & test Angular
- [ ] `.github/workflows/deploy.yml` - Deploy to staging/production

**Pipeline steps:**
1. Checkout code
2. Setup environment (Java 21 / Node 18)
3. Install dependencies
4. Run linting
5. Run unit tests
6. Build artifacts
7. Run integration tests
8. Deploy (on main branch)

---

### 4.2 Unit Tests
**Goal:** Increase test coverage to 80%+

**Backend:**
- [ ] Controller tests with MockMvc
- [ ] Service layer tests
- [ ] Security configuration tests
- [ ] JWT token tests (already exists)

**Frontend:**
- [ ] Service tests (AuthService, etc.)
- [ ] Component tests
- [ ] Guard tests
- [ ] Interceptor tests

---

### 4.3 E2E Tests
**Goal:** End-to-end testing of critical flows

- [ ] Add Cypress or Playwright
- [ ] Test scenarios:
  - Login flow (success/failure)
  - Navigation based on roles
  - Orders CRUD operations
  - Payments approval flow
  - Logout flow

**Files to create:**
- `digibo-ui/cypress/e2e/login.cy.ts`
- `digibo-ui/cypress/e2e/orders.cy.ts`
- `digibo-ui/cypress/e2e/payments.cy.ts`

---

### 4.4 API Documentation
**Goal:** Complete Swagger documentation

**Backend:**
- [ ] Add @Operation annotations to all controllers
- [ ] Add @ApiResponse annotations
- [ ] Add @Schema annotations to DTOs
- [ ] Group endpoints by tags
- [ ] Add authentication documentation

---

## Phase 5: UI/UX Improvements (Priority: Low)

### 5.1 Dark Mode
- [ ] Create theme service
- [ ] Define dark/light CSS variables
- [ ] Add theme toggle in header
- [ ] Persist preference in localStorage

### 5.2 Responsive Design
- [ ] Collapsible sidebar for mobile
- [ ] Responsive data tables
- [ ] Touch-friendly buttons
- [ ] Mobile navigation menu

### 5.3 Accessibility
- [ ] ARIA labels on interactive elements
- [ ] Keyboard navigation support
- [ ] Focus indicators
- [ ] Screen reader compatibility
- [ ] Color contrast compliance

### 5.4 Navigation Enhancements
- [ ] Breadcrumb component
- [ ] Quick search/command palette (Ctrl+K)
- [ ] Recent items list
- [ ] Favorites/bookmarks

---

## Implementation Timeline

| Phase | Duration | Dependencies |
|-------|----------|--------------|
| Phase 1: Foundation | 2-3 weeks | None |
| Phase 2: Security | 1-2 weeks | Phase 1 |
| Phase 3: Features | 4-6 weeks | Phase 1, 2 |
| Phase 4: DevOps | 1-2 weeks | Can run parallel |
| Phase 5: UI/UX | 2-3 weeks | Phase 1 |

---

## Quick Wins (Can be done immediately)

1. **Environment configuration** - 1 hour
2. **Loading spinner component** - 30 minutes
3. **Toast notification service** - 1 hour
4. **Connect one API endpoint** - 2 hours
5. **Add Swagger annotations** - 2 hours

---

## Repository Structure After Implementation

```
github.com/juriska/
├── digibo-core-java/          # Backend API
│   ├── .github/workflows/     # CI/CD
│   ├── src/
│   │   ├── main/java/com/digibo/core/
│   │   │   ├── aspect/        # AOP (audit logging)
│   │   │   ├── config/        # Configuration
│   │   │   ├── controller/    # REST controllers
│   │   │   ├── dto/           # Data transfer objects
│   │   │   ├── exception/     # Exception handling
│   │   │   ├── filter/        # HTTP filters
│   │   │   ├── mapper/        # Object mappers
│   │   │   ├── security/      # Security components
│   │   │   ├── service/       # Business logic
│   │   │   └── util/          # Utilities
│   │   └── resources/
│   ├── Dockerfile
│   └── pom.xml
│
└── digibo-ui/                 # Frontend
    ├── .github/workflows/     # CI/CD
    ├── src/
    │   ├── app/
    │   │   ├── core/          # Services, guards, models
    │   │   ├── features/      # Feature modules
    │   │   ├── layout/        # Layout components
    │   │   └── shared/        # Shared components
    │   ├── environments/      # Environment configs
    │   └── assets/
    ├── cypress/               # E2E tests
    └── package.json
```

---

## Notes

- All tasks should be tracked in GitHub Issues
- Create feature branches for each phase
- Code reviews required before merging
- Update this plan as requirements evolve
- Prioritize based on business needs

---

*Document created: 2026-02-03*
*Last updated: 2026-02-03*
