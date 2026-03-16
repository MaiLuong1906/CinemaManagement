package dao;

import model.SeatFillRate_ViewDTO;
import model.TicketSoldBySlot_ViewDTO;
import model.Movie_Ticket_ViewDTO;
import model.TimeSlotKpiDTO;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class KpiAndViewDAOTest extends BaseDAOTest {

    @org.junit.jupiter.api.BeforeAll
    public static void setupKpiClassData() throws Exception {
        // This runs AFTER BaseDAOTest.setupDatabase()
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement()) {
            // Setup base data
            stmt.execute("INSERT INTO accounts (account_id, phone_number, password_hash) VALUES (300, '333', 'h')");
            stmt.execute("INSERT INTO user_profiles (user_id, full_name, email, gender, date_of_birth) VALUES (300, 'u', 'u3@m.com', true, '2000-01-01')");
            stmt.execute("INSERT INTO movies (movie_id, title) VALUES (300, 'KPI Movie')");
            stmt.execute("INSERT INTO cinema_halls (hall_id, hall_name, total_rows, total_cols) VALUES (300, 'KPI Hall', 10, 10)");
            stmt.execute("INSERT INTO movie_genres (genre_id, genre_name) VALUES (300, 'KPI Genre')");
            stmt.execute("INSERT INTO movie_genre_rel (movie_genre_id, movie_id, genre_id) VALUES (300, 300, 300)");
            stmt.execute("INSERT INTO time_slots (slot_id, slot_name, start_time, end_time, slot_price) VALUES (300, 'KPI Slot', '18:00:00', '20:00:00', 100.0)");
            stmt.execute("INSERT INTO showtimes (showtime_id, movie_id, hall_id, show_date, slot_id) VALUES (300, 300, 300, CURRENT_DATE, 300)");
            
            // Paid Invoice
            stmt.execute("INSERT INTO invoices (invoice_id, user_id, showtime_id, total_amount, status, booking_time) VALUES (300, 300, 300, 100.0, 'Paid', CURRENT_TIMESTAMP)");
            
            // Seats and Tickets
            stmt.execute("INSERT INTO seat_types (seat_type_id, type_name) VALUES (300, 'Standard')");
            stmt.execute("INSERT INTO seats (seat_id, hall_id, seat_code, row_index, column_index, seat_type_id) VALUES (300, 300, 'A1', 1, 1, 300)");
            stmt.execute("INSERT INTO ticket_details (invoice_id, seat_id, showtime_id, actual_price) VALUES (300, 300, 300, 100.0)");
        }
    }

    @Test
    public void testSeatFillRate() throws Exception {
        SeatFillRate_ViewDAO dao = new SeatFillRate_ViewDAO();
        double fillRate = dao.getCinemaFillRateCurrentMonth();
        assertTrue(fillRate > 0);
        
        List<SeatFillRate_ViewDTO> bySlot = dao.getSeatFillRateByTimeSlotCurrentMonth();
        assertFalse(bySlot.isEmpty());
    }

    @Test
    public void testTicketSoldBySlot() throws SQLException {
        TicketSoldBySlot_ViewDAO dao = new TicketSoldBySlot_ViewDAO();
        List<TicketSoldBySlot_ViewDTO> list = dao.getAll();
        assertFalse(list.isEmpty());
        assertTrue(list.stream().anyMatch(d -> d.getSlotId() == 300));
    }

    @Test
    public void testTicketMovieView() throws SQLException {
        Ticket_Movie_ViewDAO dao = new Ticket_Movie_ViewDAO();
        List<Movie_Ticket_ViewDTO> all = dao.getAll();
        assertFalse(all.isEmpty());
        assertTrue(dao.getTotalPages() >= 1);
    }

    @Test
    public void testTicketsSoldDAO() throws SQLException {
        TicketsSoldDAO dao = new TicketsSoldDAO();
        assertTrue(dao.countSoldTicketsThisMonth() > 0);
        assertTrue(dao.countSoldTicketsToday() > 0);
        
        Map<LocalDate, Integer> history = dao.getDailyTicketHistory(7);
        assertFalse(history.isEmpty());
    }

    @Test
    public void testTimeSlotKpiDAO() throws Exception {
        TimeSlotKpiDAO dao = new TimeSlotKpiDAO();
        List<TimeSlotKpiDTO> list = dao.getTimeSlotKpiCurrentMonth();
        assertFalse(list.isEmpty());
        
        List<int[]> chartData = dao.getTotalTicketsBySlotCurrentMonth();
        assertFalse(chartData.isEmpty());
    }
}
