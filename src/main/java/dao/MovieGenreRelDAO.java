package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieGenreRelDAO {

    // Insert 1 quan hệ phim - thể loại
    public void insert(Connection conn, int movieId, int genreId)
            throws SQLException {

        String sql = """
            INSERT INTO movie_genre_rel (movie_id, genre_id)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setInt(2, genreId);
            ps.executeUpdate();
        }
    }

    // Update thể loại cho phim (DELETE cũ + INSERT mới)
    public void updateGenre(int movieId, int genreId) throws SQLException {

        String deleteSQL = """
            DELETE FROM movie_genre_rel
            WHERE movie_id = ?
        """;

        String insertSQL = """
            INSERT INTO movie_genre_rel (movie_id, genre_id)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            // Xóa quan hệ cũ
            try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                ps.setInt(1, movieId);
                ps.executeUpdate();
            }

            // Thêm quan hệ mới
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, movieId);
                ps.setInt(2, genreId);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
