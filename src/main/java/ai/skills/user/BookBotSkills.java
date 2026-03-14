package ai.skills.user;

import dao.DBConnect;
import dao.InvoiceDAO;
import dao.SeatDAO;
import dev.langchain4j.agent.tool.Tool;
import model.BookingHistoryDTO;
import model.Invoice;
import model.SeatSelectionDTO;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

/**
 * Tools for BookBot sub-agent to handle booking flows, history, and interactive confirmations.
 */
public class BookBotSkills {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final SeatDAO seatDAO = new SeatDAO();

    @Tool("Lấy lịch sử đặt vé của tôi")
    public String getMyBookingHistory(int userId) {
        try {
            List<BookingHistoryDTO> history = invoiceDAO.getBookingHistory(userId);
            if (history.isEmpty()) return "Bạn chưa có giao dịch nào.";

            return history.stream()
                .map(h -> String.format("- %s: %s | Suất chiếu: %s %s | Trạng thái: %s", 
                    h.getMovieTitle(), h.getTicketCode() != null ? h.getTicketCode() : "N/A",
                    h.getShowDate(), h.getStartTime(), h.getStatus()))
                .collect(Collectors.joining("\n", "Lịch sử đặt vé của bạn:\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy lịch sử: " + e.getMessage();
        }
    }

    @Tool("Xem sơ đồ ghế và chọn ghế cho suất chiếu (showtimeId)")
    public String getSeatMap(int showtimeId) {
        try {
            List<SeatSelectionDTO> seats = seatDAO.getSeatsByShowtime(showtimeId);
            if (seats.isEmpty()) return "Không tìm thấy sơ đồ ghế cho suất chiếu này.";

            return seats.stream()
                .map(s -> String.format("[%s: %s Price:%,.0f]", s.getSeatCode(), s.getStatus(), s.getPrice()))
                .collect(Collectors.joining(" ", "Sơ đồ ghế (AVAILABLE/BOOKED):\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy sơ đồ ghế: " + e.getMessage();
        }
    }

    @Tool("Chuẩn bị xác nhận đặt vé (Cần showtimeId và danh sách seatIds)")
    public String prepareBooking(int showtimeId, String seatCodes, double totalAmount) {
        return String.format(
            "{\"actionType\": \"BOOKING_CONFIRM\", \"data\": {\"showtimeId\": %d, \"seats\": \"%s\", \"total\": %.0f}, \"message\": \"Vui lòng xác nhận để tiến hành đặt ghế và thanh toán.\"}",
            showtimeId, seatCodes, totalAmount
        );
    }

    @Tool("Xác nhận và tiến hành đặt vé sau khi người dùng đã đồng ý (Cần showtimeId, seatCodes, totalAmount)")
    public String confirmBooking(int showtimeId, String seatCodes, double totalAmount) {
        try {
            return String.format(
                "{\"actionType\": \"BOOKING_SUCCESS\", \"data\": {\"showtimeId\": %d, \"seats\": \"%s\", \"total\": %.0f}, \"message\": \"Đã ghi nhận yêu cầu đặt vé. Vui lòng hoàn tất thanh toán.\"}",
                showtimeId, seatCodes, totalAmount
            );
        } catch (Exception e) {
            return "Lỗi khi xác nhận đặt vé: " + e.getMessage();
        }
    }

    @Tool("Huỷ hoá đơn hoặc booking đang chờ (invoiceId)")
    public String cancelInvoice(int invoiceId) {
        try {
            invoiceDAO.updateStatus(invoiceId, "Canceled");
            return "Đã hủy hóa đơn thành công.";
        } catch (Exception e) {
            return "Lỗi khi hủy hóa đơn: " + e.getMessage();
        }
    }
}
