package dao;

import model.Invoice;
import model.TicketDetail;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class InvoiceDAOTest extends BaseDAOTest {

    @Test
    public void testBookingFlow() throws SQLException {
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        TicketDetailDAO ticketDAO = new TicketDetailDAO();

        // 1. Setup prerequisite data
        int userId = 1;
        int showtimeId = 1;
        int seatId = 1;

        try (Connection conn = DBConnect.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            // Setup User
            stmt.execute("INSERT INTO accounts (account_id, phone_number, password_hash) VALUES (1, '111', 'hash')");
            stmt.execute("INSERT INTO user_profiles (user_id, full_name, email, gender, date_of_birth) VALUES (1, 'User 1', 'u@m.com', true, '1990-01-01')");
            
            // Setup Movie, Hall, Slot, Showtime
            stmt.execute("INSERT INTO movies (movie_id, title) VALUES (1, 'Movie 1')");
            stmt.execute("INSERT INTO cinema_halls (hall_id, hall_name, total_rows, total_cols) VALUES (1, 'Hall 1', 10, 10)");
            stmt.execute("INSERT INTO time_slots (slot_id, start_time, end_time, slot_price) VALUES (1, '08:00:00', '10:00:00', 50.0)");
            stmt.execute("INSERT INTO showtimes (showtime_id, movie_id, hall_id, show_date, slot_id) VALUES (1, 1, 1, '2026-03-20', 1)");
            
            // Setup Seat
            stmt.execute("INSERT INTO seat_types (seat_type_id, type_name) VALUES (1, 'Normal')");
            stmt.execute("INSERT INTO seats (seat_id, hall_id, seat_code, row_index, column_index, seat_type_id) VALUES (1, 1, 'A1', 0, 0, 1)");
        }

        // 2. Insert Invoice
        Invoice inv = new Invoice();
        inv.setUserId(userId);
        inv.setShowtimeId(showtimeId);
        inv.setTotalAmount(new BigDecimal("100.00"));
        
        int invoiceId;
        try (Connection conn = DBConnect.getConnection()) {
            invoiceId = invoiceDAO.insert(conn, inv);
        }
        assertTrue(invoiceId > 0);

        // 3. Insert Ticket Detail
        TicketDetail td = new TicketDetail();
        td.setInvoiceId(invoiceId);
        td.setSeatId(seatId);
        td.setShowtimeId(showtimeId);
        td.setActualPrice(new BigDecimal("100.00"));
        
        try (Connection conn = DBConnect.getConnection()) {
            ticketDAO.insert(conn, td);
        }

        // 4. Verify
        Invoice savedInv = invoiceDAO.findById(invoiceId);
        assertNotNull(savedInv);
        assertEquals("PENDING", savedInv.getStatus());

        List<TicketDetail> tickets = ticketDAO.getByInvoice(invoiceId);
        assertEquals(1, tickets.size());
        assertEquals(seatId, tickets.get(0).getSeatId());

        // 5. Update Status
        invoiceDAO.updateStatus(invoiceId, "Paid");
        assertEquals("Paid", invoiceDAO.findById(invoiceId).getStatus());

        // 6. Check Existence
        try (Connection conn = DBConnect.getConnection()) {
            assertTrue(ticketDAO.existsSeat(conn, showtimeId, seatId));
        }
    }
}
