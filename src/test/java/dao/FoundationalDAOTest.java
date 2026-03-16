package dao;

import model.CinemaHall;
import model.SeatType;
import model.TimeSlot;
import model.MovieGenre;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FoundationalDAOTest extends BaseDAOTest {

    @Test
    public void testCinemaHallDAO() throws SQLException {
        try (Connection conn = DBConnect.getConnection()) {
            CinemaHallDAO dao = new CinemaHallDAO(conn);
            
            // Test Insert
            CinemaHall hall = new CinemaHall("IMAX 1", 10, 10, true, java.time.LocalDate.now());
            
            int id = dao.insert(hall);
            assertTrue(id > 0);
            
            // Test Find
            CinemaHall saved = dao.getHallById(id);
            assertNotNull(saved);
            assertEquals("IMAX 1", saved.getHallName());
            
            // Test Update
            saved.setHallName("IMAX Updated");
            assertTrue(dao.updateHall(saved));
            assertEquals("IMAX Updated", dao.getHallById(id).getHallName());
            
            // Test Count
            assertTrue(dao.countTotalHalls() > 0);
        }
    }

    @Test
    public void testSeatTypeDAO() throws SQLException {
        try (Connection conn = DBConnect.getConnection()) {
            SeatTypeDAO dao = new SeatTypeDAO();
            
            // Test Insert
            int id = dao.insert(conn, "VIP", new BigDecimal("20.00"));
            assertTrue(id > 0);
            
            // Test Find
            SeatType saved = dao.findById(conn, id);
            assertNotNull(saved);
            assertEquals("VIP", saved.getTypeName());
            
            // Test Find All
            List<SeatType> all = dao.findAll(conn);
            assertTrue(all.stream().anyMatch(s -> s.getTypeName().equals("VIP")));
        }
    }

    @Test
    public void testTimeSlotDAO() throws SQLException {
        try (Connection conn = DBConnect.getConnection()) {
            TimeSlotDAO dao = new TimeSlotDAO();
            
            // Test Insert
            TimeSlot slot = new TimeSlot();
            slot.setSlotName("Evening");
            slot.setStartHour(LocalTime.of(18, 0));
            slot.setEndHour(LocalTime.of(20, 0));
            slot.setPrice(new BigDecimal("80.00"));
            
            dao.insert(conn, slot);
            
            // Verify
            List<TimeSlot> all = dao.findAll(conn);
            assertTrue(all.stream().anyMatch(s -> s.getSlotName().equals("Evening")));
        }
    }

    @Test
    public void testMovieGenreDAO() throws SQLException {
        try (Connection conn = DBConnect.getConnection()) {
            // Setup data manually since MovieGenreDAO is read-only in its current implementation
            try (java.sql.Statement stmt = conn.createStatement()) {
                stmt.execute("INSERT INTO movie_genres (genre_id, genre_name) VALUES (1, 'Action')");
                stmt.execute("INSERT INTO movie_genres (genre_id, genre_name) VALUES (2, 'Comedy')");
            }
            
            MovieGenreDAO dao = new MovieGenreDAO();
            List<MovieGenre> all = dao.getAllGenres();
            assertTrue(all.size() >= 2);
            assertTrue(all.stream().anyMatch(g -> g.getGenreName().equals("Action")));
        }
    }
}
