# Cinema Management System - AI Agent Instructions

## Project Overview
**CinemaManagement** is a Jakarta EE (v10) web application for managing cinema operations: movie listings, showtimes, seat reservations, ticket sales, and payments. Built with Maven, Servlets, JSP, and SQL Server.

- **Tech Stack**: Java 17, Jakarta EE 10, MS SQL Server, JDBC (no ORM)
- **Build**: Maven (compile target: Java 17)
- **Database**: SQL Server at `localhost:1433` - database `CinemaManagement` (credentials: `sa`/`12345`)
- **Architecture**: Layered (Servlet → Service → DAO → DB)

---

## Architecture Pattern

### Layered Structure (Root src/main/java/)
1. **controller/** - Servlets handling HTTP requests
   - File naming: `{Action}Servlet.java` (e.g., `AccountServlet.java`, `AddMovieServlet.java`)
   - All extend `HttpServlet`, use `@WebServlet("/path")` annotations
   - Action routing via `request.getParameter("action")` (e.g., `action=login`, `action=register`)
   - Session management via `request.getSession()`, store user in session attribute
   - Use `request.getRequestDispatcher().forward()` to preserve attributes; use `response.sendRedirect()` for navigation

2. **service/** - Business logic with transaction handling
   - Examples: `BookingService`, `ShowtimeService`, `CartService`, `CustomerProductService`
   - Manage DB connections and transactions (e.g., `conn.setAutoCommit(false)`, `conn.commit()`, `conn.rollback()`)
   - DAOs are instantiated as instance variables
   - **Critical**: Always close connections in finally blocks

3. **dao/** - Direct JDBC data access
   - Single class per entity (e.g., `UserDAO`, `MovieDAO`, `TicketDetailDAO`)
   - Static methods for queries; instance methods return new connection via `DBConnect.getConnection()`
   - Direct SQL strings with `PreparedStatement` (all parameters use `?` placeholders)
   - Join queries return combined DTOs (e.g., `UserDTO` combines Account + UserProfile fields)

4. **model/** - Data classes (POJOs)
   - `*DTO` suffix for view/transfer objects (e.g., `UserDTO`, `MovieDetailDTO`, `MovieShowtimeDTO`)
   - Plain classes for database entities (e.g., `Movie`, `Seat`, `Showtime`, `Invoice`)
   - No annotations; getters/setters only

5. **util/** - Helper classes
   - `Encoding.java` - SHA1 password hashing (used in `UserDAO.login()`)
   - `MovieUtils.java` - Movie-related utilities
   - `SeatUtil.java` - Seat grid/layout utilities

6. **config/** - Application configuration
   - `VNPAYConfig.java` - Payment gateway configuration
   - Database properties hardcoded in `DBConnect.java` (not externalized)

---

## Data Flow Examples

### User Login (AccountServlet)
1. Request → `AccountServlet.doPost("action=login")`
2. Extract `phoneNumber`, `password` from parameters
3. Call `UserDAO.login(phoneNumber, password)` which:
   - Hashes password with `Encoding.toSHA1()`
   - Joins `accounts` + `user_profiles` tables
   - Returns `UserDTO` with combined fields
4. Store user in session: `session.setAttribute("user", user)`
5. Redirect to home or forward to login.jsp with error message

### Booking Workflow (CartServlet → BookingService)
1. User selects seats and checkout
2. `CreateInvoiceServlet` calls `BookingService.createInvoice()`
3. Service:
   - Gets connection with `DBConnect.getConnection()`
   - Disables auto-commit for transaction control
   - Inserts invoice header via `InvoiceDAO.insert()`
   - Inserts ticket details via `TicketDetailDAO.insertBatch()` (UNIQUE seat constraint checked at DB level)
   - Commits on success; rolls back on exception
4. Returns `invoiceId` for confirmation

---

## Database Connection Pattern

**All DAO operations follow this pattern:**
```java
try (Connection con = DBConnect.getConnection()) {
    PreparedStatement pst = con.prepareStatement(sql);
    // set parameters with pst.setXxx(index, value)
    ResultSet rs = pst.executeQuery();
    // process results
} catch (Exception e) {
    System.out.println(e); // Log to console
}
```

**For transactions (Service layer):**
```java
Connection conn = null;
try {
    conn = DBConnect.getConnection();
    conn.setAutoCommit(false);
    // multiple DAO operations with shared conn
    conn.commit();
} catch (Exception e) {
    if (conn != null) conn.rollback();
    throw e;
} finally {
    if (conn != null) conn.close();
}
```

---

## Project Conventions

### Naming
- Servlet classes: PascalCase + `Servlet` suffix (e.g., `AddMovieServlet`)
- DAO classes: Entity name + `DAO` (e.g., `MovieDAO`, `TicketDetailDAO`)
- Service classes: Entity/action name + `Service` (e.g., `BookingService`)
- Request parameters: camelCase (e.g., `phoneNumber`, `showtimeId`)

### URL Routing
- Servlet URLs: `/path` (e.g., `@WebServlet("/AccountServlet")`)
- JSP views: `/views/auth/login.jsp`, `/views/user/cart.jsp`, `/views/admin/products.jsp`
- Static assets: `/css/`, `/js/`, `/images/`

### Multipart File Upload
- Configured in [web.xml](web.xml#L23-L28) for `AddMovieServlet`
- Limits: 5MB per file, 10MB total request size

### Session Management
- User object type: `UserDTO` stored in session attribute `"user"`
- Check user existence: `HttpSession.getAttribute("user") != null`
- Logout: `session.invalidate()` then redirect

### Error Handling
- No custom exceptions; catch `Exception` and print to console
- Forward to JSP with `request.setAttribute(key, message)` for validation errors
- Redirect for success to prevent form resubmission

---

## Common Development Tasks

### Adding a New Feature (e.g., Delete Movie)
1. Create Servlet in [controller/](controller/) inheriting `HttpServlet`
2. Add `@WebServlet("/path")` annotation
3. Implement `doPost()` with action routing
4. Create/update DAO method (static or instance) in appropriate [dao/](dao/) class
5. Call DAO from Servlet, handle result
6. Forward/redirect to JSP
7. Add servlet mapping in [web.xml](web.xml)
8. Create/update JSP view in [webapp/views/](webapp/views/)

### Building & Deployment
```bash
mvn clean package  # Creates CinemaManagement-1.0-SNAPSHOT.war in target/
# Deploy .war to Tomcat/Payara application server
```

### Database Connection Issues
- Verify SQL Server running on `localhost:1433`
- Check credentials in [DBConnect.java](src/main/java/dao/DBConnect.java#L16-L18): `sa`/`12345`
- Database name: `CinemaManagement` (not `Cinema_Prj` in db.properties - use hardcoded value)
- Encryption required: `encrypt=true;trustServerCertificate=true`

### Payment Integration
- See [config/VNPAYConfig.java](src/main/java/config/VNPAYConfig.java) for payment gateway setup
- Payment responses handled in [controller/payment/](controller/payment/) package

---

## Key Files & Patterns

| File | Purpose |
|------|---------|
| [pom.xml](pom.xml) | Maven dependencies (Gson, Jakarta EE, MSSQL JDBC, JSTL) |
| [DBConnect.java](src/main/java/dao/DBConnect.java) | Connection pooling (basic DriverManager) |
| [UserDAO.java](src/main/java/dao/UserDAO.java) | Auth queries, table joins example |
| [BookingService.java](src/main/java/service/BookingService.java) | Transaction control pattern |
| [AccountServlet.java](src/main/java/controller/AccountServlet.java) | Session & redirect/forward usage |
| [web.xml](src/main/webapp/WEB-INF/web.xml) | Servlet mappings, multipart config |

---

## Red Flags & Gotchas

1. **Connection Management**: Always use try-with-resources or finally blocks; open connections leak if not closed
2. **Parameter Validation**: No framework validation; manually check for null/empty in Servlets before DAO calls
3. **SQL Injection**: Always use `PreparedStatement` with `?` placeholders; never concatenate user input
4. **Transaction Isolation**: Only `Service` layer manages transactions; DAO methods are auto-commit
5. **Session Objects**: User is `UserDTO` not separate Account/Profile classes; map DAO result carefully
6. **Password Hashing**: Always use `Encoding.toSHA1()` for user passwords; compare hashed values
7. **No Framework Validation**: Form validation must be explicit in Servlet (no annotations/constraints)
8. **Hardcoded DB Credentials**: In DBConnect.java, not externalized to properties file
9. **JSP Attribute Scope**: Use `request.setAttribute()` for forward, session attribute for logged-in user

---

## Questions Before Starting

When assigned a task, clarify:
- Should this add/modify DB schema or work with existing tables?
- Is this user-facing (Servlet+JSP) or backend (DAO+Service)?
- Does this require transaction handling (multiple table updates)?
- Are there existing models/DTOs to reuse or should new ones be created?

