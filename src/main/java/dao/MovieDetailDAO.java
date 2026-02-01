package dao;

import model.MovieDetailDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailDAO extends DBConnect {

    private static final String GET_ALL =
        "SELECT * FROM vw_movie_showtime_detail";

    private static final String GET_BY_ID =
        "SELECT * FROM vw_movie_showtime_detail WHERE showtime_id = ?";

    /* ===================== GET ALL ===================== */
    public List<MovieDetailDTO> getAllMovieDetails() {
        List<MovieDetailDTO> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===================== GET BY SHOWTIME ID ===================== */
    public MovieDetailDTO getByShowtimeId(int showtimeId) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(GET_BY_ID)) {

            ps.setInt(1, showtimeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<MovieDetailDTO> findMovieDetailByMovieId(Connection conn, int movieId)
        throws SQLException {

    List<MovieDetailDTO> list = new ArrayList<>();

    String sql = """
        SELECT
            showtime_id,
            movie_title,
            slot_name,
            show_date,
            start_time,
            end_time,
            hall_name,
            genres
        FROM vw_movie_showtime_detail
        WHERE movie_id = ?
        ORDER BY hall_name, start_time
    """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, movieId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            MovieDetailDTO dto = new MovieDetailDTO(
                rs.getInt("showtime_id"),
                rs.getString("movie_title"),
                rs.getString("slot_name"),
                rs.getDate("show_date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime(),
                rs.getString("hall_name"),
                rs.getString("genres")
            );
            list.add(dto);
        }
    }
    return list;
}


    /* ===================== MAP RESULT ===================== */
    private MovieDetailDTO mapRow(ResultSet rs) throws Exception {
        return new MovieDetailDTO(
            rs.getInt("showtime_id"),
            rs.getString("movie_title"),
            rs.getString("slot_name"),
            rs.getDate("show_date").toLocalDate(),
            rs.getTime("start_time").toLocalTime(),
            rs.getTime("end_time").toLocalTime(),
            rs.getString("hall_name"),
            rs.getString("genres")
        );
    }
}
