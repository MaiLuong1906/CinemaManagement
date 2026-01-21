/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieGenreRelDAO {

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
}
