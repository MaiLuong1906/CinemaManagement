package dao;


import model.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    /* =========================
       FIND BY HALL
       ========================= */
    public List<Seat> findByHall(Connection conn, int hallId) throws SQLException {
        String sql = """
            SELECT * FROM seats
            WHERE hall_id = ?
            ORDER BY seat_code
        """;

        List<Seat> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       FIND BY ID
       ========================= */
    public Seat findById(Connection conn, int seatId) throws SQLException {
        String sql = "SELECT * FROM seats WHERE seat_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /* =========================
       INSERT (SETUP BAN ĐẦU)
       ========================= */
    public void insert(Connection conn, Seat seat) throws SQLException {
        String sql = """
            INSERT INTO seats (hall_id, seat_code, seat_type_id)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seat.getHallId());
            ps.setString(2, seat.getSeatCode());
            ps.setInt(3, seat.getSeatTypeId());
            ps.executeUpdate();
        }
    }

    /* =========================
       UPDATE SEAT TYPE (ADMIN)
       ========================= */
    public void updateSeatType(Connection conn, int seatId, int seatTypeId)
            throws SQLException {
        String sql = """
            UPDATE seats
            SET seat_type_id = ?
            WHERE seat_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatTypeId);
            ps.setInt(2, seatId);
            ps.executeUpdate();
        }
    }

    /* =========================
       DELETE (HIẾM)
       ========================= */
    public void delete(Connection conn, int seatId) throws SQLException {
        String sql = "DELETE FROM seats WHERE seat_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ps.executeUpdate();
        }
    }

    /* =========================
       MAP RESULTSET
       ========================= */
    private Seat mapRow(ResultSet rs) throws SQLException {
        Seat s = new Seat();
        s.setSeatId(rs.getInt("seat_id"));
        s.setHallId(rs.getInt("hall_id"));
        s.setSeatCode(rs.getString("seat_code"));
        s.setSeatTypeId(rs.getInt("seat_type_id"));
        return s;
    }
}
