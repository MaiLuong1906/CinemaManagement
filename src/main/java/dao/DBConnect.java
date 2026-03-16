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
import java.util.logging.Level;
import java.util.logging.Logger;
public class DBConnect {
    private static final Logger LOGGER = Logger.getLogger(DBConnect.class.getName());
    static String user = ConfigLoader.get("db.username");
    static String pass = ConfigLoader.get("db.password");
    static String url = ConfigLoader.get("db.url");
    static String driver = ConfigLoader.get("db.driver-class-name");

    public static Connection getConnection() {
        // Debug-level log without exposing full connection details at higher levels
        LOGGER.fine("[DB] Attempting to obtain a database connection.");
        try {
            Class.forName(driver);
            // Set login timeout to 5 seconds to prevent indefinite hangs
            DriverManager.setLoginTimeout(5);
            Connection conn = DriverManager.getConnection(url, user, pass);
            if (conn != null) {
                LOGGER.fine("[DB] Database connection established successfully.");
            } else {
                LOGGER.warning("[DB] Database connection obtained is null.");
            }
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            // Log the exception with stack trace at SEVERE level, without printing to stdout/stderr directly
            LOGGER.log(Level.SEVERE, "[DB] Failed to obtain database connection.", e);
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
