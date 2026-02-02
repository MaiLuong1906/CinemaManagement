package dao;

import model.TicketSoldBySlot_ViewDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketSoldBySlot_ViewDAO {
    public List<TicketSoldBySlot_ViewDTO> getAll() throws SQLException {
        String sql = """
            SELECT
                slot_id,
                slot_name,
                start_time,
                end_time,
                tickets_sold,
                slot_revenue
            FROM vw_ticket_sold_by_slot_current_month
            ORDER BY start_time
        """;
        List<TicketSoldBySlot_ViewDTO> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TicketSoldBySlot_ViewDTO dto = new TicketSoldBySlot_ViewDTO();

                dto.setSlotId(rs.getInt("slot_id"));
                dto.setSlotName(rs.getString("slot_name"));
                dto.setStartTime(rs.getTime("start_time").toLocalTime());
                dto.setEndTime(rs.getTime("end_time").toLocalTime());
                dto.setTicketsSold(rs.getInt("tickets_sold"));
                dto.setSlotRevenue(rs.getDouble("slot_revenue"));

                list.add(dto);
            }
        }
        return list;
    }
}
