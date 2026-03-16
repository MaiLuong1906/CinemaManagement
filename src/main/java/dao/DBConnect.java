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

import util.ConfigLoader;

public class DBConnect {
    static String user = ConfigLoader.get("db.username");
    static String pass = ConfigLoader.get("db.password");
    static String url = ConfigLoader.get("db.url");
    static String driver = ConfigLoader.get("db.driver-class-name");

    public static Connection getConnection() {
        System.out.println("[DB-DEBUG] Attempting to get connection to: " + url + " with driver: " + driver);
        try {
            Class.forName(driver);
            // Set login timeout to 5 seconds to prevent indefinite hangs
            DriverManager.setLoginTimeout(5);
            Connection conn = DriverManager.getConnection(url, user, pass);
            if (conn != null) {
                System.out.println("[DB-DEBUG] Connection established successfully.");
            } else {
                System.out.println("[DB-DEBUG] Connection received is null.");
            }
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[DB-ERROR] Connection failed: " + e.getMessage());
            e.printStackTrace();
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
