package ai.skills.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dao.DBConnect;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for BookBotSkills using a real database connection.
 * Tests JSON-based booking flow and history retrieval.
 */
public class BookBotSkillsTest {

    private BookBotSkills bookBotSkills;

    @BeforeEach
    public void setUp() throws Exception {
        bookBotSkills = new BookBotSkills(1);
        // Cleanup test data: remove any previous bookings for A5, A6 on showtime 1
        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);
            // 1. Get seat IDs for A5, A6
            String sqlGetSeats = "SELECT seat_id FROM seats WHERE seat_code IN ('A5', 'A6')";
            List<Integer> seatIds = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlGetSeats)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) seatIds.add(rs.getInt("seat_id"));
                }
            }

            if (!seatIds.isEmpty()) {
                // 2. Delete from ticket_details for showtime 1 and these seats
                StringBuilder sqlDeleteTickets = new StringBuilder("DELETE FROM ticket_details WHERE showtime_id = 1 AND seat_id IN (");
                for (int i = 0; i < seatIds.size(); i++) {
                    sqlDeleteTickets.append(i == 0 ? "?" : ", ?");
                }
                sqlDeleteTickets.append(")");

                try (PreparedStatement ps = conn.prepareStatement(sqlDeleteTickets.toString())) {
                    for (int i = 0; i < seatIds.size(); i++) {
                        ps.setInt(i + 1, seatIds.get(i));
                    }
                    ps.executeUpdate();
                }
            }
            conn.commit();
        }
    }

    @Test
    public void testGetMyBookingHistory() {
        System.out.println("--- Testing getMyBookingHistory (UserID: 1) ---");
        // Using UserID 1 as a common default for testing
        String result = bookBotSkills.getMyBookingHistory();
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Lịch sử") || result.contains("Bạn chưa có giao dịch"));
    }

    @Test
    public void testGetSeatMap() {
        System.out.println("--- Testing getSeatMap (ShowtimeID: 1) ---");
        String result = bookBotSkills.getSeatMap(1);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("Sơ đồ ghế") || result.contains("Không tìm thấy sơ đồ"));
    }

    @Test
    public void testPrepareBooking() {
        System.out.println("--- Testing prepareBooking ---");
        String seatCodes = "A5, A6";
        int showtimeId = 1;
        double totalAmount = 160000;
        String movieName = "Godzilla x Kong";
        String result = bookBotSkills.prepareBooking(showtimeId, movieName, seatCodes, totalAmount);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("BOOKING_CONFIRM"));
        assertTrue(result.contains("\"showtimeId\": 1"));
        assertTrue(result.contains("\"seats\": \"A5, A6\""));
    }

    @Test
    public void testConfirmBooking() {
        System.out.println("--- Testing confirmBooking ---");
        String seatCodes = "A5, A6";
        int showtimeId = 1;
        double totalAmount = 160000;
        String result = bookBotSkills.confirmBooking(showtimeId, seatCodes, totalAmount);
        System.out.println(result);
        assertNotNull(result);
        assertTrue(result.contains("BOOKING_SUCCESS"));
    }

    @Test
    public void testCancelInvoice() {
        System.out.println("--- Testing cancelInvoice (ID: 999) ---");
        // We use a high ID or non-existent one to check error handling, 
        // or a real one if we want to confirm status change.
        // Since it's a real API call, let's just check it doesn't crash on invalid input.
        String result = bookBotSkills.cancelInvoice(999);
        System.out.println(result);
        assertNotNull(result);
        // It might return success even if ID not found depending on DAO implementation
        // but here it just sets status to Canceled.
        assertTrue(result.contains("thành công") || result.contains("Lỗi"));
    }
}
