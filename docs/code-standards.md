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
