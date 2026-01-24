package dao;

import model.MovieDetailDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailDAO extends DBConnect {

    private static final String GET_ALL =
        "SELECT * FROM vw_movie_showtime_details";

    public List<MovieDetailDTO> getAllMovieDetails() {
        List<MovieDetailDTO> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MovieDetailDTO dto = new MovieDetailDTO(
                    rs.getInt("movie_id"),
                    rs.getString("movie_title"),
                    rs.getString("slot_name"),
                    // SQL TIME -> LocalTime
                    rs.getTime("start_time").toLocalTime(),
                    rs.getTime("end_time").toLocalTime(),
                    rs.getString("hall_name"),
                    rs.getString("genres")
                );
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
