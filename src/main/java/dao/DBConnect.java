/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author LENOVO
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    public static Connection getConnection() {
        try {
            // Check if running on Heroku (JAWSDB_URL environment variable exists)
            String jawsDbUrl = System.getenv("JAWSDB_URL");

            if (jawsDbUrl != null && !jawsDbUrl.isEmpty()) {
                // Heroku MySQL connection
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(jawsDbUrl);
            } else {
                // Local SQL Server connection
                String user = "sa";
                String pass = "Mailuong@2025";
                String url = "jdbc:sqlserver://localhost:1433;databaseName=CinemaManagement;encrypt=true;trustServerCertificate=true";
                String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

                Class.forName(driver);
                return DriverManager.getConnection(url, user, pass);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("loi connect : " + e.getMessage());
            return null;
        }
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
