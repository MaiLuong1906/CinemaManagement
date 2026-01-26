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

    public Movie findById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE movie_id = ?";
        Connection con = DBConnect.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
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

    /**
     * Lấy phim theo nhiều thể loại (OR logic) Phim sẽ hiển thị nếu thuộc ít
     * nhất 1 trong các genre được chọn
     */
    public List<Movie> getMoviesByMultipleGenres(String[] genres) {
        List<Movie> list = new ArrayList<>();

        if (genres == null || genres.length == 0) {
            return list;
        }

        // Tạo SQL động với IN clause
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.* ")
                .append("FROM movie_genre_rel mgl ")
                .append("JOIN movies m ON mgl.movie_id = m.movie_id ")
                .append("JOIN movie_genres mg ON mg.genre_id = mgl.genre_id ")
                .append("WHERE mg.genre_name IN (");

        // Thêm placeholders cho từng genre
        for (int i = 0; i < genres.length; i++) {
            sql.append("?");
            if (i < genres.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(") ORDER BY m.release_date DESC");

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            // Set tất cả genre parameters
            for (int i = 0; i < genres.length; i++) {
                ps.setString(i + 1, genres[i]);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Tìm kiếm phim trong danh sách theo keyword Search trong title và
     * description
     */
    public List<Movie> searchMovies(List<Movie> movies, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return movies;
        }

        List<Movie> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Movie movie : movies) {
            String title = movie.getTitle() != null ? movie.getTitle().toLowerCase() : "";
            String description = movie.getDescription() != null ? movie.getDescription().toLowerCase() : "";

            if (title.contains(lowerKeyword) || description.contains(lowerKeyword)) {
                result.add(movie);
            }
        }

        return result;
    }

    // insert va tra ve id cua phim vua them
    public int insertAndReturnId(Connection conn, Movie movie) throws SQLException {

        String sql = """
        INSERT INTO movies
        (title, duration, description, release_date, age_rating, poster_url)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setString(3, movie.getDescription());
            ps.setDate(4, Date.valueOf(movie.getReleaseDate()));
            ps.setString(5, movie.getAgeRating());
            ps.setString(6, movie.getPosterUrl());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // movie_id vừa insert
            }
        }

        throw new SQLException("Insert movie failed, no ID returned.");
    }

    public int getGenreIdByMovieId(int movieId) {
        String sql = """
        SELECT movie_genre_id
        FROM MovieGenreRel
        WHERE movie_id = ?
    """;

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("movie_genre_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // không tìm thấy
    }

}
