# Cinema Management System

A full-stack Java web application for managing movie theater operations, ticket bookings, and concessions, featuring VNPAY payment integration and real-time analytics.

## Project Overview
This system provides a comprehensive platform for both theater customers and administrators. Customers can browse movies, select seats in real-time, and pay securely via VNPAY. Administrators can manage the movie catalog, schedule showtimes, and monitor revenue through a detailed dashboard.

## Key Features
- **Booking Flow**: Seamless seat selection and ticket purchase.
- **Concessions**: Add snacks and drinks to bookings.
- **Payment Integration**: Secure, real-time payments via VNPAY.
- **Admin Dashboard**: Comprehensive management of movies, users, and schedules.
- **Analytics**: Real-time revenue and seat occupancy reporting.

## Tech Stack
- **Backend**: Java 17+, Servlet API, JDBC, SQL Server.
- **Frontend**: JSP, JSTL, Bootstrap 5, jQuery.
- **Integrations**: VNPAY (Payment Gateway).

## Getting Started

### Prerequisites
- JDK 17 or higher.
- Apache Tomcat 10.x.
- Microsoft SQL Server.
- Maven.

### Quick Setup
1. Clone the repository.
2. Configure your database connection in `src/main/java/dao/DBConnect.java`.
3. Set up your VNPAY credentials in `src/main/java/config/VNPAYConfig.java`.
4. Ensure the directory `C:/imgForCinema` exists for movie posters.
5. Run `mvn clean package` and deploy the generated `.war` file to Tomcat.

## Documentation
Comprehensive documentation is available in the `docs/` directory:
- [Project Overview & PDR](./docs/project-overview-pdr.md)
- [Codebase Summary](./docs/codebase-summary.md)
- [System Architecture](./docs/system-architecture.md)
- [Code Standards](./docs/code-standards.md)
- [Project Roadmap](./docs/project-roadmap.md)
- [Deployment Guide](./docs/deployment-guide.md)
- [Design Guidelines](./docs/design-guidelines.md)

## License
[Insert License Information]
