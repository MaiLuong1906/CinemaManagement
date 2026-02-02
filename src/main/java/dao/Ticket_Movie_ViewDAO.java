package dao;

import model.Movie_Ticket_ViewDTO;
import dao.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ticket_Movie_ViewDAO {

    private static final String BASE_SELECT =
        "SELECT movie_id, movie_title, tickets_sold, movie_revenue, row_num, page_number " +
        "FROM vw_movie_ticket_paging_5 ";

    /**
     * Lấy danh sách phim theo page
     */
    public List<Movie_Ticket_ViewDTO> getByPage(int pageNumber) throws SQLException {
        List<Movie_Ticket_ViewDTO> list = new ArrayList<>();
        String sql = BASE_SELECT +
                     "WHERE page_number = ? " +
                     "ORDER BY row_num";

        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, pageNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movie_Ticket_ViewDTO dto = new Movie_Ticket_ViewDTO(
                        rs.getInt("movie_id"),
                        rs.getString("movie_title"),
                        rs.getInt("tickets_sold"),
                        rs.getDouble("movie_revenue"),
                        rs.getInt("row_num"),
                        rs.getInt("page_number")
                    );
                    list.add(dto);
                }
            }
        }

        return list;
    }

    /**
     * Tổng số page
     */
    public int getTotalPages() throws SQLException {
        String sql = "SELECT MAX(page_number) FROM vw_movie_ticket_paging_5";

        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
