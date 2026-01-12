package com.mycompany.cinema.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
public class JDBCUtils {

    // Phương thức trả về Connection
    public static Connection getConnection() throws SQLException {
        Connection c = null;
        try {
            // Load driver SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // URL JDBC: SQL Server Express, tắt SSL để tránh lỗi PKIX
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Version1;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "30042005";

            // Tạo kết nối
            c = DriverManager.getConnection(url, user, password);
            System.out.println("Connect successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("SQLServer JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Cannot connect to database NVDB!");
            e.printStackTrace();
            throw e; // để caller biết lỗi
        }

        return c;
    }
    public static void closeConnection(Connection c){
        try{
            if(c!=null) c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void show(Connection c){
        if(c!=null) {
            try {
                DatabaseMetaData mtdt = c.getMetaData();
                System.out.println(mtdt.getDatabaseProductName());
                System.out.println(mtdt.getDatabaseProductVersion());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
