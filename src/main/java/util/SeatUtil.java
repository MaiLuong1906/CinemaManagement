package util;

public class SeatUtil {

    // 0 -> A, 1 -> B, 2 -> C
    public static String rowIndexToChar(int rowIndex) {
        return String.valueOf((char) ('A' + rowIndex));
    }
}