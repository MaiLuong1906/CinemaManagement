package dao;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public abstract class BaseDAOTest {

    @BeforeAll
    public static void setupDatabase() throws Exception {
        // Override DBConnect variables for in-memory H2 Database (MSSQL compatibility mode)
        DBConnect.driver = "org.h2.Driver";
        DBConnect.url = "jdbc:h2:mem:cinemadb;DB_CLOSE_DELAY=-1;MODE=MSSQLServer";
        DBConnect.user = "sa";
        DBConnect.pass = "";

        // Re-establish connection and execute schema
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE ALIAS IF NOT EXISTS DATEFROMPARTS FOR \"util.H2Functions.dateFromParts\"");
                    stmt.execute("CREATE ALIAS IF NOT EXISTS GETDATE FOR \"util.H2Functions.getTimestamp\"");
                } catch (Exception e) {
                    // Ignore if already exists or built-in
                }
                InputStream schemaStream = BaseDAOTest.class.getResourceAsStream("/h2-schema.sql");
                if (schemaStream == null) {
                    throw new IllegalStateException("Schema file not found on classpath: /h2-schema.sql");
                }
                try (Reader reader = new InputStreamReader(schemaStream, StandardCharsets.UTF_8)) {
                    RunScript.execute(conn, reader);
                }
            } else {
                throw new IllegalStateException("Failed to connect to H2 in-memory database.");
            }
        }
    }

    @AfterAll
    public static void tearDownDatabase() throws Exception {
        // Clean up database schema after tests complete
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP ALL OBJECTS");
        }
    }
}
