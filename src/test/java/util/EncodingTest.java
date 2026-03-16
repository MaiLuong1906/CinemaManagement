package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EncodingTest {

    @Test
    public void testToSHA1_Consistency() {
        String input = "password123";
        String hash1 = Encoding.toSHA1(input);
        String hash2 = Encoding.toSHA1(input);
        
        assertNotNull(hash1);
        assertEquals(hash1, hash2, "Hash must be consistent for the same input");
    }

    @Test
    public void testToSHA1_DifferentInputs() {
        String input1 = "password123";
        String input2 = "password124";
        
        String hash1 = Encoding.toSHA1(input1);
        String hash2 = Encoding.toSHA1(input2);
        
        assertNotEquals(hash1, hash2, "Different inputs must produce different hashes");
    }

    @Test
    public void testToSHA1_EmptyString() {
        String hash = Encoding.toSHA1("");
        assertNotNull(hash, "Empty string should produce a non-null hash");
        assertFalse(hash.isEmpty(), "Hash should not be empty");
    }
}
