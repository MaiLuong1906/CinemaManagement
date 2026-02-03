# Project Roadmap

## Current State Assessment
The Cinema Management System is a functional, feature-rich Java web application. It successfully handles the core "happy path" of movie discovery, booking, and payment. The database design is robust, and the 3-tier architecture provides a solid foundation.

### Technical Debt Identified
1. **Security**: Password hashing uses SHA-1, which is considered cryptographically weak.
2. **Portability**: Hardcoded Windows file paths (`C:/imgForCinema`) prevent easy deployment on Linux/macOS.
3. **Frontend**: Coexistence of Bootstrap 3 and 5 leads to visual inconsistencies and larger asset payloads.
4. **Maintenance**: Inconsistent naming conventions for Servlets (e.g., `AddMovieServlet` vs `addShowTimeServlet`).
5. **Layout**: Manual `<jsp:include>` usage instead of a unified layout decorator (e.g., Sitemesh).

## Recommended Improvements

### Phase 1: Security & Stability (High Priority)
- **Password Upgrade**: Migrate from SHA-1 to BCrypt for user password hashing.
- **Path Externalization**: Move hardcoded file paths to a `config.properties` file or environment variables.
- **Route Protection**: Conduct a full audit of `AdminFilter` to ensure all administrative servlets are properly secured.

### Phase 2: Standardization & Cleanup (Medium Priority)
- **Naming Unification**: Rename servlets and classes to follow a consistent PascalCase convention.
- **DAO/Service Consolidation**: Ensure all controllers interact with the database via the Service layer rather than calling DAOs directly.
- **Frontend Refactor**: Migrate legacy Bootstrap 3 components to Bootstrap 5.

### Phase 3: Enhanced Features (Low Priority)
- **Caching**: Implement a simple caching mechanism (e.g., Caffeine or Ehcache) for frequently accessed movie listings.
- **Notifications**: Add email confirmation for bookings using JavaMail API.
- **Reporting**: Expand the admin dashboard with more granular visualizations (e.g., heatmaps for seat occupancy).

## Action Plan (Next Steps)
1. **Immediate**: Create a comprehensive `deployment-guide.md` to assist with environment setup.
2. **Short-term**: Fix the hardcoded Windows paths to improve portability.
3. **Mid-term**: Implement BCrypt password migration strategy.
4. **Long-term**: Refactor the frontend to a single Bootstrap version.
