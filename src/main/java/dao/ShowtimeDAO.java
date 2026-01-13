package dao;


import model.Showtime;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO {

    /* =========================
       FIND BY ID
       ========================= */
    public Showtime findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM showtimes WHERE showtime_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /* =========================
       FIND BY MOVIE
       ========================= */
    public List<Showtime> findByMovie(Connection conn, int movieId) throws SQLException {
        String sql = """
            SELECT * FROM showtimes
            WHERE movie_id = ?
            ORDER BY start_time
        """;

        List<Showtime> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       FIND BY DATE
       ========================= */
    public List<Showtime> findByDate(Connection conn, LocalDate date) throws SQLException {
        String sql = """
            SELECT * FROM showtimes
            WHERE CAST(start_time AS DATE) = ?
            ORDER BY start_time
        """;

        List<Showtime> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       FIND UPCOMING
       ========================= */
    public List<Showtime> findUpcoming(Connection conn) throws SQLException {
        String sql = """
            SELECT * FROM showtimes
            WHERE start_time >= GETDATE()
            ORDER BY start_time
        """;

        List<Showtime> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       INSERT (ADMIN)
       ========================= */
    public void insert(Connection conn, Showtime st) throws SQLException {
        String sql = """
            INSERT INTO showtimes
            (movie_id, hall_id, start_time, base_price)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, st.getMovieId());
            ps.setInt(2, st.getHallId());
            ps.setTimestamp(3, Timestamp.valueOf(st.getStartTime()));
            ps.setBigDecimal(4, st.getBasePrice());
            ps.executeUpdate();
        }
    }

    /* =========================
       UPDATE (ADMIN)
       ========================= */
    public void update(Connection conn, Showtime st) throws SQLException {
        String sql = """
            UPDATE showtimes
            SET movie_id = ?, hall_id = ?, start_time = ?, base_price = ?
            WHERE showtime_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, st.getMovieId());
            ps.setInt(2, st.getHallId());
            ps.setTimestamp(3, Timestamp.valueOf(st.getStartTime()));
            ps.setBigDecimal(4, st.getBasePrice());
            ps.setInt(5, st.getShowtimeId());
            ps.executeUpdate();
        }
    }

    /* =========================
       DELETE (ADMIN)
       ========================= */
    public void delete(Connection conn, int showtimeId) throws SQLException {
        String sql = "DELETE FROM showtimes WHERE showtime_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            ps.executeUpdate();
        }
    }

    /* =========================
       MAP RESULTSET
       ========================= */
    private Showtime mapRow(ResultSet rs) throws SQLException {
        Showtime st = new Showtime();
        st.setShowtimeId(rs.getInt("showtime_id"));
        st.setMovieId(rs.getInt("movie_id"));
        st.setHallId(rs.getInt("hall_id"));
        st.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        st.setBasePrice(rs.getBigDecimal("base_price"));
        return st;
    }
}

