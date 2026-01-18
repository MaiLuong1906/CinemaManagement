package dao;

import model.MovieShowtimeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieShowtimeDAO {

    // Lấy TẤT CẢ dữ liệu từ VIEW
    public List<MovieShowtimeDTO> getAll() {
        List<MovieShowtimeDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM vw_movie_showtime_basic ORDER BY startTime";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MovieShowtimeDTO dto = new MovieShowtimeDTO();

                dto.setMovieId(rs.getInt("movieId"));
                dto.setMovieTitle(rs.getString("movieTitle"));
                dto.setHallName(rs.getString("hallName"));
                dto.setGenreName(rs.getString("genreName"));
                dto.setAgeRating(rs.getString("ageRating"));
                dto.setDescription(rs.getString("description"));
                dto.setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
                dto.setDuration(rs.getInt("duration"));
                dto.setPosterUrl(rs.getString("posterUrl"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy theo movieId (phục vụ trang chi tiết phim)
    public List<MovieShowtimeDTO> getByMovieId(int movieId) {
        List<MovieShowtimeDTO> list = new ArrayList<>();

        String sql = """
            SELECT * FROM vw_movie_showtime_basic
            WHERE movieId = ?
            ORDER BY startTime
        """;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovieShowtimeDTO dto = new MovieShowtimeDTO();

                    dto.setMovieId(rs.getInt("movieId"));
                    dto.setMovieTitle(rs.getString("movieTitle"));
                    dto.setHallName(rs.getString("hallName"));
                    dto.setGenreName(rs.getString("genreName"));
                    dto.setAgeRating(rs.getString("ageRating"));
                    dto.setDescription(rs.getString("description"));
                    dto.setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
                    dto.setDuration(rs.getInt("duration"));
                    dto.setPosterUrl(rs.getString("posterUrl"));

                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // lay toan bo phim dang chieu
    // phim dang chieu neu no nam trong khoang tu thoi gian bat dau toi luc thoi gian bat dau cong duration
    public List<MovieShowtimeDTO> getNowShowing() {

    List<MovieShowtimeDTO> list = new ArrayList<>();

    String sql = """
        SELECT *
        FROM vw_movie_showtime_basic
        WHERE
            startTime <= GETDATE()
            AND DATEADD(MINUTE, duration, startTime) >= GETDATE()
        ORDER BY startTime
    """;

    try (Connection con = DBConnect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            MovieShowtimeDTO dto = new MovieShowtimeDTO();

            dto.setMovieId(rs.getInt("movieId"));
            dto.setMovieTitle(rs.getString("movieTitle"));
            dto.setHallName(rs.getString("hallName"));
            dto.setGenreName(rs.getString("genreName"));
            dto.setAgeRating(rs.getString("ageRating"));
            dto.setDescription(rs.getString("description"));
            dto.setStartTime(
                rs.getTimestamp("startTime").toLocalDateTime()
            );
            dto.setDuration(rs.getInt("duration"));
            dto.setPosterUrl(rs.getString("posterUrl"));
            list.add(dto);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
    }
    // lay ra phim sap chieu 
    // phim sap chieu bang 1 ngay tinh tu phim dang chieu
    public List<MovieShowtimeDTO> getComingSoon() {
    List<MovieShowtimeDTO> list = new ArrayList<>();
    String sql = """
        SELECT *
        FROM vw_movie_showtime_basic
        WHERE
            startTime >= DATEADD(DAY, 1, GETDATE())
        ORDER BY startTime
    """;
    try (Connection con = DBConnect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            MovieShowtimeDTO dto = new MovieShowtimeDTO();

            dto.setMovieId(rs.getInt("movieId"));
            dto.setMovieTitle(rs.getString("movieTitle"));
            dto.setHallName(rs.getString("hallName"));
            dto.setGenreName(rs.getString("genreName"));
            dto.setAgeRating(rs.getString("ageRating"));
            dto.setDescription(rs.getString("description"));
            dto.setStartTime(
                rs.getTimestamp("startTime").toLocalDateTime()
            );
            dto.setDuration(rs.getInt("duration"));
            dto.setPosterUrl(rs.getString("posterUrl"));

            list.add(dto);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
    }
    // lay ra tat ca phim la Xmax
    public List<MovieShowtimeDTO> getByHallNameLike(String hallName) {
    List<MovieShowtimeDTO> list = new ArrayList<>();
    String sql = """
        SELECT *
        FROM vw_movie_showtime_basic
        WHERE hallName LIKE ?
        ORDER BY startTime
    """;
    try (Connection con = DBConnect.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, "%" + hallName + "%");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MovieShowtimeDTO dto = new MovieShowtimeDTO();
                dto.setMovieId(rs.getInt("movieId"));
                dto.setMovieTitle(rs.getString("movieTitle"));
                dto.setHallName(rs.getString("hallName"));
                dto.setGenreName(rs.getString("genreName"));
                dto.setAgeRating(rs.getString("ageRating"));
                dto.setDescription(rs.getString("description"));
                dto.setStartTime(
                    rs.getTimestamp("startTime").toLocalDateTime()
                );
                dto.setDuration(rs.getInt("duration"));
                dto.setPosterUrl(rs.getString("posterUrl"));

                list.add(dto);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
    }
}