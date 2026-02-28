/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.MovieGenre;

public class MovieGenreDAO {

    private static final String GET_ALL_GENRES = "SELECT genre_id, genre_name FROM movie_genres";

    public List<MovieGenre> getAllGenres() {
        List<MovieGenre> list = new ArrayList<>();

        try (
                Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_ALL_GENRES);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                MovieGenre genre = new MovieGenre(
                        rs.getInt("genre_id"),
                        rs.getString("genre_name"));
                list.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách thể loại của một phim theo movieId
     */
    public List<MovieGenre> getGenresByMovieId(int movieId) {
        List<MovieGenre> list = new ArrayList<>();
        String sql = """
                    SELECT mg.genre_id, mg.genre_name
                    FROM movie_genres mg
                    JOIN movie_genre_rel mgr ON mg.genre_id = mgr.genre_id
                    WHERE mgr.movie_id = ?
                """;

        try (
                Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MovieGenre(
                            rs.getInt("genre_id"),
                            rs.getString("genre_name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
