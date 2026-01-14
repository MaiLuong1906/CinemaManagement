package dao;

import model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    /* =========================
       FIND BY ID
       ========================= */
    public Movie findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE movie_id = ?";
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
       FIND ALL
       ========================= */
    public List<Movie> findAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM movies ORDER BY release_date DESC";
        List<Movie> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       SEARCH BY TITLE
       ========================= */
    public List<Movie> searchByTitle(Connection conn, String keyword) throws SQLException {
        String sql = "SELECT * FROM movies WHERE title LIKE ?";
        List<Movie> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
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
    public void insert(Connection conn, Movie movie) throws SQLException {
        String sql = """
            INSERT INTO movies
            (title, duration, description, release_date, age_rating, poster_url)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setString(3, movie.getDescription());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setString(5, movie.getAgeRating());
            ps.setString(6, movie.getPosterUrl());
            ps.executeUpdate();
        }
    }

    /* =========================
       UPDATE (ADMIN)
       ========================= */
    public void update(Connection conn, Movie movie) throws SQLException {
        String sql = """
            UPDATE movies
            SET title = ?, duration = ?, description = ?, 
                release_date = ?, age_rating = ?, poster_url = ?
            WHERE movie_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setString(3, movie.getDescription());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setString(5, movie.getAgeRating());
            ps.setString(6, movie.getPosterUrl());
            ps.setInt(7, movie.getMovieId());
            ps.executeUpdate();
        }
    }

    /* =========================
       DELETE (ADMIN)
       ========================= */
    public void delete(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE movie_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* =========================
       MAP RESULTSET
       ========================= */
    private Movie mapRow(ResultSet rs) throws SQLException {
        Movie m = new Movie();
        m.setMovieId(rs.getInt("movie_id"));
        m.setTitle(rs.getString("title"));
        m.setDuration(rs.getInt("duration"));
        m.setDescription(rs.getString("description"));
        Date d = rs.getDate("release_date");
        if (d != null) {
            m.setReleaseDate(d.toLocalDate());
        }
        m.setAgeRating(rs.getString("age_rating"));
        m.setPosterUrl(rs.getString("poster_url"));
        return m;
    }

    // lay toan bo film
    public List<Movie> getAllMovies() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY release_date DESC";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // lay toan bo phim dang chieu
    public List<Movie> getNowShowingMovies() {
        List<Movie> list = new ArrayList<>();
        String sql = """
        SELECT DISTINCT m.*
        FROM movies m
        WHERE EXISTS (
            SELECT 1
            FROM showtimes s
            WHERE s.movie_id = m.movie_id
              AND s.start_time >= GETDATE()
        )
        ORDER BY m.release_date DESC
    """;
        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
