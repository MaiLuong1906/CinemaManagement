package dao;

import model.TimeSlot;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotDAO {

    /* =========================
       FIND BY ID
       ========================= */
    public TimeSlot findById(Connection conn, int slotId) throws SQLException {
        String sql = "SELECT * FROM time_slots WHERE slot_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /* =========================
       FIND ALL
       ========================= */
    public List<TimeSlot> findAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM time_slots ORDER BY start_time";

        List<TimeSlot> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }

        return list;
    }

    /* =========================
       INSERT
       ========================= */
    public void insert(Connection conn, TimeSlot slot) throws SQLException {
        String sql = """
            INSERT INTO time_slots (slot_name, start_time, end_time, slot_price)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slot.getSlotName());
            ps.setTime(2, Time.valueOf(slot.getStartHour()));
            ps.setTime(3, Time.valueOf(slot.getEndHour()));
            ps.setBigDecimal(4, slot.getPrice());
            ps.executeUpdate();
        }
    }

    /* =========================
       UPDATE
       ========================= */
    public void update(Connection conn, TimeSlot slot) throws SQLException {
        String sql = """
            UPDATE time_slots
            SET slot_name = ?, start_time = ?, end_time = ?, slot_price = ?
            WHERE slot_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slot.getSlotName());
            ps.setTime(2, Time.valueOf(slot.getStartHour()));
            ps.setTime(3, Time.valueOf(slot.getEndHour()));
            ps.setBigDecimal(4, slot.getPrice());
            ps.setInt(5, slot.getSlotId());
            ps.executeUpdate();
        }
    }

    /* =========================
       DELETE
       ========================= */
    public void delete(Connection conn, int slotId) throws SQLException {
        String sql = "DELETE FROM time_slots WHERE slot_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ps.executeUpdate();
        }
    }

    /* =========================
       MAP RESULTSET
       ========================= */
    private TimeSlot mapRow(ResultSet rs) throws SQLException {
        TimeSlot slot = new TimeSlot();
        slot.setSlotId(rs.getInt("slot_id"));
        slot.setSlotName(rs.getString("slot_name"));
        slot.setStartHour(rs.getTime("start_time").toLocalTime());
        slot.setEndHour(rs.getTime("end_time").toLocalTime());
        slot.setPrice(rs.getBigDecimal("slot_price"));
        return slot;
    }
    // lay slot available
    public List<TimeSlot> getAvailableSlots(Connection conn, int hallId, LocalDate showDate, 
            int excludeShowtimeId) throws SQLException {
        String sql = """
            SELECT ts.slot_id, ts.slot_name, ts.start_time, ts.end_time, ts.slot_price
            FROM time_slots ts
            WHERE NOT EXISTS (
                SELECT 1
                FROM showtimes s
                WHERE s.hall_id = ?
                  AND s.show_date = ?
                  AND s.slot_id = ts.slot_id
                  AND s.showtime_id != ?
            )
            ORDER BY ts.start_time
        """;

        List<TimeSlot> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            ps.setDate(2, Date.valueOf(showDate));
            ps.setInt(3, excludeShowtimeId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

}
