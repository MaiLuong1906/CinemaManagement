# Deployment Guide

## System Requirements
- **Java Development Kit (JDK)**: Version 17 or higher.
- **Application Server**: Apache Tomcat 10.x (Jakarta EE 10 compatible).
- **Database**: Microsoft SQL Server (2019 or later recommended).
- **Build Tool**: Maven.

## Database Setup
1. **Initialize Schema**: Execute the SQL scripts located in the `sql/` directory (if available) or refer to the `dao` layer for table definitions.
2. **Connection String**: Update `src/main/java/dao/DBConnect.java` with your database credentials.
   - Default pattern: `jdbc:sqlserver://localhost:1433;databaseName=CinemaManagement;encrypt=true;trustServerCertificate=true;`
3. **Admin User**: Ensure the `accounts` table has at least one user with administrative privileges.

## External Dependencies

### VNPAY Integration
- Obtain `vnp_TmnCode` and `vnp_HashSecret` from the VNPAY Sandbox portal.
- Configure these values in `src/main/java/config/VNPAYConfig.java`.
- Set the `vnp_ReturnUrl` to point to your deployed `PaymentReturnServlet` endpoint.

### File System Configuration
- The system currently requires a directory named `C:/imgForCinema` to store and serve movie posters.
- Create this directory on your Windows machine or modify `ImageServlet.java` and `AddMovieServlet.java` to point to a different location.

## Build & Deployment Steps

1. **Clean & Package**:
   ```bash
   mvn clean package
   ```
2. **Deploy to Tomcat**:
   - Copy the generated `.war` file from the `target/` directory to the `webapps/` folder of your Tomcat installation.
3. **Start Server**:
   - Run `bin/startup.sh` (Linux/macOS) or `bin/startup.bat` (Windows).
4. **Verification**:
   - Access the application at `http://localhost:8080/CinemaManagement` (replace with your actual context path).

## Troubleshooting

### Common Issues
- **Database Connection Failure**: Verify that SQL Server is running and that TCP/IP is enabled in SQL Server Configuration Manager.
- **Image Not Loading**: Check if the `C:/imgForCinema` directory exists and has the correct permissions.
- **VNPAY Checksum Error**: Ensure the `vnp_HashSecret` in your code matches the one in the VNPAY merchant portal.
- **Jakarta EE Compatibility**: Ensure you are using Tomcat 10+; Tomcat 9 and earlier use the `javax.*` namespace and are NOT compatible with this project's `jakarta.*` namespace.
