/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SeatFillRate_ViewDTO;

/**
 *
 * @author nguye
 */
public class SeatFillRate_ViewDAO {
    // lay do phu ghe theo thang bang ghe da ban / tong so ghe
    public double getCinemaFillRateCurrentMonth() {

        String sql = """
                SELECT
                SUM(seats_sold) * 100.0 / NULLIF(SUM(total_seats), 0)
                FROM vw_seat_coverage_detail
                WHERE year = YEAR(GETDATE())
                AND month = MONTH(GETDATE())
        """;

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                return rs.getDouble(1);
                }
                return 0.0;

        } catch (SQLException e) {
                throw new RuntimeException("Cannot calculate cinema fill rate", e);
        }
        }
    // do phu ghe theo khung gio
     private static final String SQL_FILL_RATE_BY_SLOT_MONTH =
        """
        SELECT
            ts.start_time,
            ts.end_time,
            SUM(v.seats_sold)  AS total_tickets_sold,
            SUM(v.total_seats) AS total_seats,
            CAST(
                SUM(v.seats_sold) * 100.0 /
                NULLIF(SUM(v.total_seats), 0)
                AS DECIMAL(5,2)
            ) AS fill_rate
        FROM vw_seat_coverage_detail v
        JOIN time_slots ts
            ON v.slot_id = ts.slot_id
        WHERE
            v.show_date >= DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)
            AND v.show_date <  DATEADD(MONTH, 1,
                DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1))
        GROUP BY
            ts.start_time,
            ts.end_time
        ORDER BY
            fill_rate DESC
        """;

    public List<SeatFillRate_ViewDTO> getSeatFillRateByTimeSlotCurrentMonth()
            throws Exception {

        List<SeatFillRate_ViewDTO> list = new ArrayList<>();

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FILL_RATE_BY_SLOT_MONTH);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SeatFillRate_ViewDTO dto = new SeatFillRate_ViewDTO();
                // Không dùng ở level tổng hợp
                dto.setShowtimeId(0);
                dto.setShowDate(null);
                dto.setMovieTitle("ALL");
                dto.setHallName("ALL");
                dto.setStartTime(rs.getTime("start_time").toLocalTime());
                dto.setEndTime(rs.getTime("end_time").toLocalTime());
                dto.setTicketsSold(rs.getInt("total_tickets_sold"));
                dto.setTotalSeats(rs.getInt("total_seats"));
                dto.setFillRate(rs.getDouble("fill_rate"));
                list.add(dto);
            }

        }

        return list;
    }
    // du lieu day cho gemini
    public List<SeatFillRate_ViewDTO> getAllSeatCoverageCurrentMonth() throws Exception {
    String sql = """
        SELECT
            show_date,
            movie_title,
            hall_name,
            start_time,
            end_time,
            seats_sold,
            total_seats,
            seat_coverage_percent
        FROM vw_seat_coverage_detail
        WHERE year  = YEAR(GETDATE())
          AND month = MONTH(GETDATE())
        ORDER BY show_date, start_time
    """;

    List<SeatFillRate_ViewDTO> list = new ArrayList<>();

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            SeatFillRate_ViewDTO dto = new SeatFillRate_ViewDTO();
            dto.setShowDate(rs.getDate("show_date").toLocalDate());
            dto.setMovieTitle(rs.getString("movie_title"));
            dto.setHallName(rs.getString("hall_name"));
            dto.setStartTime(rs.getTime("start_time").toLocalTime());
            dto.setEndTime(rs.getTime("end_time").toLocalTime());
            dto.setTicketsSold(rs.getInt("seats_sold"));
            dto.setTotalSeats(rs.getInt("total_seats"));
            dto.setFillRate(rs.getDouble("seat_coverage_percent"));
            list.add(dto);
        }
    }

    return list;
}

}
