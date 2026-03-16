package dao;

import model.Showtime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShowtimeDAOTest extends BaseDAOTest {

    private ShowtimeDAO showtimeDAO;

    @BeforeEach
    public void setup() throws Exception {
        showtimeDAO = new ShowtimeDAO();
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM showtimes");
            stmt.execute("DELETE FROM movies");
            stmt.execute("DELETE FROM cinema_halls");
            stmt.execute("DELETE FROM time_slots");
            
            // Insert parent movies
            stmt.execute("INSERT INTO movies (movie_id, title) VALUES (1, 'Test Movie 1')");
            stmt.execute("INSERT INTO movies (movie_id, title) VALUES (2, 'Test Movie 2')");
            
            // Insert cinema halls
            stmt.execute("INSERT INTO cinema_halls (hall_id, hall_name, total_rows, total_cols) VALUES (1, 'Hall 1', 10, 10)");
            stmt.execute("INSERT INTO cinema_halls (hall_id, hall_name, total_rows, total_cols) VALUES (2, 'Hall 2', 5, 5)");
            stmt.execute("INSERT INTO cinema_halls (hall_id, hall_name, total_rows, total_cols) VALUES (3, 'Hall 3', 8, 8)");

            // Insert time slots
            stmt.execute("INSERT INTO time_slots (slot_id, slot_name, start_time, end_time, slot_price) VALUES (1, 'Slot 1', '08:00:00', '10:00:00', 50.0)");
            stmt.execute("INSERT INTO time_slots (slot_id, slot_name, start_time, end_time, slot_price) VALUES (2, 'Slot 2', '10:30:00', '12:30:00', 60.0)");
            stmt.execute("INSERT INTO time_slots (slot_id, slot_name, start_time, end_time, slot_price) VALUES (3, 'Slot 3', '13:00:00', '15:00:00', 70.0)");
            stmt.execute("INSERT INTO time_slots (slot_id, slot_name, start_time, end_time, slot_price) VALUES (4, 'Slot 4', '15:30:00', '17:30:00', 80.0)");
            stmt.execute("INSERT INTO time_slots (slot_id, slot_name, start_time, end_time, slot_price) VALUES (5, 'Slot 5', '18:00:00', '20:00:00', 90.0)");

            LocalDate today = LocalDate.now();
            
            // Insert showtimes
            stmt.execute("INSERT INTO showtimes (showtime_id, movie_id, hall_id, show_date, slot_id) " +
                         "VALUES (1, 1, 1, '" + today.minusDays(1) + "', 1)"); // past
            stmt.execute("INSERT INTO showtimes (showtime_id, movie_id, hall_id, show_date, slot_id) " +
                         "VALUES (2, 1, 1, '" + today + "', 2)"); // today
            stmt.execute("INSERT INTO showtimes (showtime_id, movie_id, hall_id, show_date, slot_id) " +
                         "VALUES (3, 2, 2, '" + today.plusDays(1) + "', 3)"); // future

            stmt.execute("ALTER TABLE showtimes ALTER COLUMN showtime_id RESTART WITH 10");
            stmt.execute("ALTER TABLE movies ALTER COLUMN movie_id RESTART WITH 10");
            stmt.execute("ALTER TABLE cinema_halls ALTER COLUMN hall_id RESTART WITH 10");
            stmt.execute("ALTER TABLE time_slots ALTER COLUMN slot_id RESTART WITH 10");
        }
    }

    @Test
    public void testFindById() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            Showtime st = showtimeDAO.findById(conn, 1);
            assertNotNull(st);
            assertEquals(1, st.getMovieId());
            
            assertNull(showtimeDAO.findById(conn, 999));
        }
    }

    @Test
    public void testFindByMovie() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            List<Showtime> list = showtimeDAO.findByMovie(conn, 1);
            assertEquals(2, list.size());
            
            List<Showtime> emptyList = showtimeDAO.findByMovie(conn, 999);
            assertEquals(0, emptyList.size());
        }
    }

    @Test
    public void testFindByDate() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            List<Showtime> list = showtimeDAO.findByDate(conn, LocalDate.now());
            assertEquals(1, list.size());
            assertEquals(2, list.get(0).getShowtimeId());
        }
    }

    @Test
    public void testFindUpcoming() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            List<Showtime> list = showtimeDAO.findUpcoming(conn);
            assertEquals(2, list.size()); // Expect today and future dates
        }
    }
    
    @Test
    public void testInsertUpdateDelete() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            // Test Insert
            Showtime st = new Showtime();
            st.setMovieId(2);
            st.setHallId(3);
            st.setShowDate(LocalDate.now().plusDays(5));
            st.setSlotId(4);
            
            showtimeDAO.insert(conn, st);
            
            // Check Inserted
            List<Showtime> upcomings = showtimeDAO.findUpcoming(conn);
            assertEquals(3, upcomings.size());
            
            List<Showtime> movie2List = showtimeDAO.findByMovie(conn, 2);
            assertEquals(2, movie2List.size());
            Showtime inserted = movie2List.get(1); // latest slot
            
            // Test Update
            inserted.setSlotId(5);
            showtimeDAO.update(conn, inserted);
            
            Showtime updated = showtimeDAO.findById(conn, inserted.getShowtimeId());
            assertEquals(5, updated.getSlotId());
            
            // Test Delete
            assertTrue(showtimeDAO.delete(conn, updated.getShowtimeId()));
            assertNull(showtimeDAO.findById(conn, updated.getShowtimeId()));
            assertFalse(showtimeDAO.delete(conn, 999));
        }
    }
}
