package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeatUtilTest {

    @Test
    public void testRowIndexToChar() {
        assertEquals("A", SeatUtil.rowIndexToChar(0));
        assertEquals("B", SeatUtil.rowIndexToChar(1));
        assertEquals("C", SeatUtil.rowIndexToChar(2));
        assertEquals("K", SeatUtil.rowIndexToChar(10));
        assertEquals("Z", SeatUtil.rowIndexToChar(25));
    }
}
