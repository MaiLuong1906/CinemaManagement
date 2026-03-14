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

import utils.ConfigLoader;

public class DBConnect {
    static String user = ConfigLoader.get("db.username");
    static String pass = ConfigLoader.get("db.password");
    static String url = ConfigLoader.get("db.url");
    static String driver = ConfigLoader.get("db.driver-class-name");

    public static Connection getConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, pass);
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
