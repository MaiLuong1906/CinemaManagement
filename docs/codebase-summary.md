# Codebase Summary

## High-Level Architecture
The project follows a classic **3-Tier Architecture** implemented with Jakarta EE (Servlets/JSP) and a Microsoft SQL Server database.

### 1. Presentation Layer (JSP + Servlets)
- **Customer Views**: Located in `src/main/webapp/views/user/`, utilizing JSTL for dynamic rendering.
- **Admin Dashboard**: Located in `src/main/webapp/views/admin/`, providing management interfaces.
- **Servlets**: Acting as controllers to handle HTTP requests, coordinate with services/DAOs, and manage session state.
  - **VenueServlet**: Consolidated servlet using action-based routing for showtime/hall/seat management (replaces 9 legacy servlets).

### 2. Business Logic Layer (Services)
- **Services**: Located in `src/main/java/service/`, these classes encapsulate business rules such as revenue calculation, seat availability logic, and booking coordination.
- **Utils**: Helper classes in `src/main/java/util/` for encoding, seat mapping, and general utilities.

### 3. Data Access Layer (DAOs)
- **DAOs**: Located in `src/main/java/dao/`, providing a clean interface to the SQL Server database using direct JDBC with `PreparedStatement` for security and performance.
- **Models/DTOs**: Located in `src/main/java/model/`, representing domain entities and data transfer objects for complex views.

## File Organization
```text
src/main/java/
├── config/       # Global configuration (VNPAY)
├── controller/   # Servlets for request handling
├── dao/          # Database Access Objects
├── filter/       # Authentication & Authorization filters
├── model/        # POJOs and DTOs
├── service/      # Business logic implementation
└── util/         # Common utility functions

src/main/webapp/
├── css/, js/     # Frontend assets
├── layout/       # Reusable JSP components (header/footer)
└── views/        # Page-specific JSP files
```

## Technology Stack
- **Backend**: Java 17+, Servlet API, JDBC
- **Database**: Microsoft SQL Server
- **Frontend**: JSP, JSTL, Bootstrap 3/5, jQuery
- **Payment**: VNPAY API
- **Security**: SHA-1 hashing, Filter-based access control

## Critical Code Paths

### 1. Booking Flow
1. `MovieDetailServlet` displays available showtimes.
2. `SeatController` manages interactive seat selection.
3. `HoldSeatServlet` temporarily reserves seats in the session.
4. `ConfirmBookingServlet` initiates the database transaction for the invoice.
5. `ajaxServlet` redirects to VNPAY for payment.
6. `PaymentReturnServlet` validates payment and finalizes the booking.

### 2. Payment Integration
- **[VNPAYConfig.java](src/main/java/config/VNPAYConfig.java)**: Stores merchant credentials and API endpoints.
- **[ajaxServlet.java](src/main/java/controller/payment/ajaxServlet.java)**: Generates the secure payment URL with HMAC-SHA512 checksum.

### 3. Reporting & Analytics
The system heavily utilizes SQL Server Views for complex reporting, accessed through specialized DAOs:
- `SeatFillRate_ViewDAO` for hall occupancy.
- `AdminRevenueServlet` for financial tracking.
