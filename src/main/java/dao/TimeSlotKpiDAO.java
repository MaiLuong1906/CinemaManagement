package dao;

import dao.DBConnect;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.TimeSlotKpiDTO;

public class TimeSlotKpiDAO {
    // lay so ve + revenue cua tung khung gio hien tai trong thang
     private static final String SQL_GET_KPI_TIMESLOT_MONTH =
        """
        SELECT
            slot_id,
            slot_name,
            start_time,
            end_time,
            SUM(tickets_sold) AS total_tickets_sold,
            SUM(revenue)      AS total_revenue
        FROM vw_kpi_timeslot_revenue
        WHERE
            show_date >= DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)
            AND show_date < DATEADD(MONTH, 1,
                DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1))
        GROUP BY
            slot_id, slot_name, start_time, end_time
        ORDER BY
            total_tickets_sold DESC
        """;

    public List<TimeSlotKpiDTO> getTimeSlotKpiCurrentMonth() throws Exception {
        List<TimeSlotKpiDTO> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_KPI_TIMESLOT_MONTH);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TimeSlotKpiDTO dto = new TimeSlotKpiDTO();
                dto.setSlotId(rs.getInt("slot_id"));
                dto.setSlotName(rs.getString("slot_name"));
                dto.setStartTime(rs.getTime("start_time").toLocalTime());
                dto.setEndTime(rs.getTime("end_time").toLocalTime());
                dto.setTicketsSold(rs.getInt("total_tickets_sold"));
                dto.setRevenue(rs.getDouble("total_revenue"));
                list.add(dto);
            }
        }
        return list;
    }
    // dung cho bieu do
    public List<int[]> getTotalTicketsBySlotCurrentMonth() throws Exception {
    List<int[]> list = new ArrayList<>();

    String sql = """
        SELECT slot_id, SUM(tickets_sold) AS total_tickets
        FROM vw_kpi_timeslot_revenue
        WHERE
            show_date >= DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)
            AND show_date < DATEADD(
                MONTH, 1,
                DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)
            )
        GROUP BY slot_id
        ORDER BY slot_id
    """;

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(new int[]{
                rs.getInt("slot_id"),
                rs.getInt("total_tickets")
            });
        }
    }
    return list;
}


}