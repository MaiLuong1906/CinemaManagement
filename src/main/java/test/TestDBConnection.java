package test;

import dao.DBConnect;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        System.out.println("--- Testing Database Connection ---");
        
        Connection conn = DBConnect.getConnection();
        
        if (conn != null) {
            System.out.println("SUCCESS: Connected to the database successfully!");
            try {
                System.out.println("Database Product Name: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Database Product Version: " + conn.getMetaData().getDatabaseProductVersion());
                
                // Close the connection
                DBConnect.closeConnection(conn);
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error getting metadata: " + e.getMessage());
            }
        } else {
            System.err.println("FAILURE: Could not establish a database connection.");
            System.err.println("Please check your 'src/main/resources/db.properties' configuration.");
        }
    }
}
