# Code Standards & Guidelines

## Naming Conventions
- **Java Classes**: PascalCase (e.g., `MovieDAO`, `AccountServlet`)
- **Variables/Methods**: camelCase (e.g., `getMovieById`, `totalPrice`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `VNP_HASH_SECRET`)
- **JSP Files**: kebab-case (e.g., `movie-detail.jsp`, `seat-selection.jsp`)
- **Packages**: lowercase (e.g., `com.mycompany.cinema.dao`)

## Architecture Patterns
- **DAO Pattern**: Every database table should have a corresponding DAO class in `src/main/java/dao/`.
- **DTO Pattern**: Use Data Transfer Objects in `src/main/java/model/` for combining data from multiple tables or views.
- **Service Layer**: Complex business logic (e.g., booking validation, revenue calculation) should be placed in `src/main/java/service/` rather than directly in Servlets or DAOs.

## Data Access Guidelines
1. **PreparedStatements**: ALWAYS use `PreparedStatement` to prevent SQL Injection.
2. **Resource Management**: Use try-with-resources or ensure `Connection`, `Statement`, and `ResultSet` are closed in a `finally` block.
3. **Transactions**: Use `connection.setAutoCommit(false)` for operations involving multiple tables (e.g., creating an invoice and its ticket details).
4. **Manual Mapping**: Follow the pattern of `mapRow(ResultSet rs)` to convert database rows to Java objects for consistency.

## JSP/Servlet Best Practices
- **Separation of Concerns**: Do not write Java logic (scriptlets) inside JSP files. Use JSTL (`c:if`, `c:forEach`) and EL (`${user.name}`).
- **Input Validation**: Validate all user inputs on both the frontend (JS) and backend (Servlet).
- **Session Management**: Use the session for authentication (`user` object) and temporary booking state (`BOOKING_SEAT_IDS`). Avoid bloating the session with large objects.
- **Error Handling**: Use `try-catch` blocks in Servlets and redirect to appropriate error pages or display user-friendly messages.

### BaseServlet Pattern (NEW)
All servlets MUST extend `BaseServlet` for consistency and reduced boilerplate:

```java
@WebServlet("/my-servlet")
public class MyServlet extends BaseServlet {

    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        // Your business logic here
        int id = getIntParam(req, "id"); // Built-in parameter extraction
        forward(req, resp, "/views/my-page.jsp"); // Built-in forward helper
    }

    @Override
    protected boolean requiresAuthentication() {
        return true; // Enable authentication check
    }
}
```

**Benefits**:
- Automatic UTF-8 encoding
- Built-in parameter extraction (`getIntParam`, `getStringParam`, `getDateParam`)
- Centralized error handling
- Authentication hooks
- Response helpers (`forward`, `sendJson`, `redirect`)

### Consolidated Servlet Pattern
For related functionality, use action-based routing with switch-case:

```java
@WebServlet("/admin")
public class AdminServlet extends BaseServlet {

    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String action = getStringParam(req, "action", "dashboard");

        switch (action) {
            case "users":
                handleUsers(req, resp);
                break;
            case "products":
                handleProducts(req, resp);
                break;
            default:
                showDashboard(req, resp);
        }
    }

    private void handleUsers(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        // Handle user management
    }
}
```

**URL Pattern**: `/admin?action=users`, `/admin?action=products`

### File Upload Pattern
For servlets handling file uploads, use `@MultipartConfig` and the file upload utility:

```java
@WebServlet("/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)
public class UploadServlet extends BaseServlet {

    private String handleFileUpload(HttpServletRequest req, String paramName, String uploadDir)
            throws IOException, ServletException {
        Part filePart = req.getPart(paramName);
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }
        String fileName = System.currentTimeMillis() + "_"
            + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        filePart.write(uploadDir + File.separator + fileName);
        return fileName;
    }
}
```

## File Organization Rules
- Keep Controllers in `controller/` package.
- Keep Data Access in `dao/` package.
- Keep Models in `model/` package.
- Keep Filters in `filter/` package.
- Keep frontend assets (CSS/JS) in their respective directories under `src/main/webapp/`.

## Security Standards
- **Password Hashing**: Currently using SHA-1 (via `Encoding.toSHA1`). New implementations should consider moving to BCrypt.
- **Access Control**: Use `AuthFilter` and `AdminFilter` to protect restricted routes.
- **SQL Security**: Strict adherence to parameterized queries.
- **Sensitive Data**: Never hardcode API keys or database credentials in the main codebase; use configuration files or environment variables.
