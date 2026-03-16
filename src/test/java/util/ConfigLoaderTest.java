package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigLoaderTest {

    @Test
    void testGet_ExistingKey() {
        String dbUrl = ConfigLoader.get("db.url");
        assertNotNull(dbUrl, "db.url should not be null in db.properties");
        assertTrue(dbUrl.contains("jdbc:sqlserver"), "db.url should start with jdbc:sqlserver");
    }

    @Test
    void testGet_NonExistingKey_ReturnsNull() {
        assertNull(ConfigLoader.get("non.existing.key"));
    }

    @Test
    void testGet_WithDefaultValue() {
        assertEquals("default", ConfigLoader.get("non.existing.key", "default"));
        String dbUrl = ConfigLoader.get("db.url", "default");
        assertNotEquals("default", dbUrl);
    }
}
