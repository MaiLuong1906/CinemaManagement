package dao;

import model.Seat;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SeatDAOTest extends BaseDAOTest {

    @Test
    public void testSeatOperations() throws SQLException {
        SeatDAO dao = new SeatDAO();
        
        // 1. Setup prerequisite data (Hall and Seat Type)
        int hallId = 1;
        try (java.sql.Connection conn = DBConnect.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO cinema_halls (hall_id, hall_name, total_rows, total_cols) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, hallId);
            ps.setString(2, "Hall 1");
            ps.setInt(3, 10);
            ps.setInt(4, 10);
            ps.executeUpdate();
        }
        
        int typeId = 1;
        try (java.sql.Connection conn = DBConnect.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO seat_types (seat_type_id, type_name, extra_fee) VALUES (?, ?, ?)")) {
            ps.setInt(1, typeId);
            ps.setString(2, "Normal");
            ps.setBigDecimal(3, new java.math.BigDecimal("0.00"));
            ps.executeUpdate();
        }

        // 2. Insert Seat
        dao.insert(hallId, "A1", 0, 0, typeId, true);
        
        // 3. Exists and Find
        assertTrue(dao.isSeatCodeExists(hallId, "A1"));
        assertTrue(dao.isCoordinateExists(hallId, 0, 0));
        
        List<Seat> hallSeats = dao.findByHall(hallId);
        assertEquals(1, hallSeats.size());
        assertEquals("A1", hallSeats.get(0).getSeatCode());
        
        int seatId = hallSeats.get(0).getSeatId();
        assertTrue(dao.exists(seatId));
        
        Seat seat = dao.findById(seatId);
        assertNotNull(seat);
        assertEquals("A1", seat.getSeatCode());

        // 4. Update
        dao.updateActive(seatId, false);
        seat = dao.findById(seatId);
        assertFalse(seat.isActive());
        
        // 5. Count
        assertEquals(1, dao.countSeatsByHall(hallId));
        assertEquals(0, dao.countActiveSeats(hallId)); // We just set it to false

        // 6. Delete
        assertTrue(dao.deleteSeat(seatId));
        assertFalse(dao.exists(seatId));
    }
}
