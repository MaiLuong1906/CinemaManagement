package util;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class H2Functions {
    public static Date dateFromParts(int year, int month, int day) {
        return Date.valueOf(LocalDate.of(year, month, day));
    }
    public static Timestamp getTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
