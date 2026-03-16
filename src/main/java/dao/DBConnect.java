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
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import util.ConfigLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    private static final Logger LOGGER = Logger.getLogger(DBConnect.class.getName());
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(ConfigLoader.get("db.url"));
            config.setUsername(ConfigLoader.get("db.username"));
            config.setPassword(ConfigLoader.get("db.password"));
            config.setDriverClassName(ConfigLoader.get("db.driver-class-name"));
            
            // Pool configuration
            config.setMaximumPoolSize(20);
            config.setMinimumIdle(5);
            config.setIdleTimeout(300000); // 5 minutes
            config.setConnectionTimeout(10000); // 10 seconds timeout to get a connection
            config.setMaxLifetime(1800000); // 30 minutes
            
            dataSource = new HikariDataSource(config);
            LOGGER.info("[DB] HikariCP Connection Pool initialized successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[DB] Failed to initialize HikariCP Connection Pool", e);
            throw new RuntimeException("Failed to initialize database connection pool", e);
        }
    }

    public static Connection getConnection() {
        LOGGER.fine("[DB] Attempting to obtain a database connection from pool.");
        try {
            Connection conn = dataSource.getConnection();
            if (conn != null) {
                LOGGER.fine("[DB] Database connection obtained successfully from pool.");
            } else {
                LOGGER.warning("[DB] Database connection obtained from pool is null.");
                throw new IllegalStateException("Database connection from pool is null.");
            }
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[DB] Failed to obtain database connection from pool.", e);
            throw new IllegalStateException("Failed to obtain database connection: " + e.getMessage(), e);
        }
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "[DB] Error closing connection", e);
        }
    }
    
    // Allows shutting down the pool on application termination if needed
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    
    // For testing purposes: Reinitialize with test database
    public static void initForTest(String jdbcUrl, String username, String password, String driverClassName) {
        shutdown();
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName(driverClassName);
            
            config.setMaximumPoolSize(5); // Smaller pool for tests
            
            dataSource = new HikariDataSource(config);
            LOGGER.info("[DB] HikariCP test connection pool initialized.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[DB] Failed to initialize test connection pool", e);
            throw new RuntimeException("Failed to initialize test DB pool", e);
        }
    }
}
