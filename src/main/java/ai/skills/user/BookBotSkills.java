package ai.skills.user;

import dao.DBConnect;
import dao.InvoiceDAO;
import dao.SeatDAO;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.P;
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
    private final int userId;

    public BookBotSkills(int userId) {
        this.userId = userId;
    }

    @Tool("Lấy lịch sử đặt vé của tôi")
    public String getMyBookingHistory() {
        System.out.println("[AI-DEBUG] Tool getMyBookingHistory called for UserID: " + this.userId);
        try {
            List<BookingHistoryDTO> history = invoiceDAO.getBookingHistory(this.userId);
            System.out.println("[AI-DEBUG] Found " + history.size() + " history records");
            if (history.isEmpty()) return "Bạn chưa có giao dịch nào.";

            return history.stream()
                .map(h -> String.format("- %s: %s | Suất chiếu: %s %s | Trạng thái: %s", 
                    h.getMovieTitle(), h.getTicketCode() != null ? h.getTicketCode() : "N/A",
                    h.getShowDate(), h.getStartTime(), h.getStatus()))
                .collect(Collectors.joining("\n", "Lịch sử đặt vé của bạn:\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getMyBookingHistory error: " + e.getMessage());
            return "Lỗi khi lấy lịch sử: " + e.getMessage();
        }
    }

    @Tool("Xem sơ đồ ghế và chọn ghế cho suất chiếu (showtimeId)")
    public String getSeatMap(@P("ID suất chiếu (showtimeId)") int showtimeId) {
        System.out.println("[AI-DEBUG] Tool getSeatMap called for ShowtimeID: " + showtimeId);
        try {
            List<SeatSelectionDTO> seats = seatDAO.getSeatsByShowtime(showtimeId);
            System.out.println("[AI-DEBUG] Found " + seats.size() + " seats");
            if (seats.isEmpty()) return "Không tìm thấy sơ đồ ghế cho suất chiếu này.";

            return seats.stream()
                .map(s -> String.format("[%s: %s Price:%,.0f]", s.getSeatCode(), s.getStatus(), s.getPrice()))
                .collect(Collectors.joining(" ", "Sơ đồ ghế (AVAILABLE/BOOKED):\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getSeatMap error: " + e.getMessage());
            return "Lỗi khi lấy sơ đồ ghế: " + e.getMessage();
        }
    }

    @Tool("Chuẩn bị xác nhận đặt vé (Cần showtimeId, movieName và danh sách seatIds)")
    public String prepareBooking(@P("ID suất chiếu") int showtimeId, @P("Tên phim") String movieName, @P("Mã ghế (ví dụ: A1, A2)") String seatCodes, @P("Tổng tiền") double totalAmount) {
        System.out.println("[AI-DEBUG] Tool prepareBooking called: " + showtimeId + ", " + seatCodes);
        return String.format(
            "{\"actionType\": \"BOOKING_CONFIRM\", \"data\": {\"showtimeId\": %d, \"movieName\": \"%s\", \"seats\": \"%s\", \"total\": %.0f}, \"message\": \"Vui lòng xác nhận để tiến hành đặt ghế và thanh toán.\"}",
            showtimeId, movieName, seatCodes, totalAmount
        );
    }

    @Tool("Xác nhận và tiến hành đặt vé sau khi người dùng đã đồng ý (Cần showtimeId, seatCodes, totalAmount)")
    public String confirmBooking(@P("ID suất chiếu") int showtimeId, @P("Mã ghế") String seatCodes, @P("Tổng tiền") double totalAmount) {
        System.out.println("[AI-DEBUG] Tool confirmBooking called for UserID: " + this.userId + ", Showtime: " + showtimeId + ", Seats: " + seatCodes);
        
        if (this.userId <= 0) {
            return "Vui lòng đăng nhập để thực hiện đặt vé.";
        }
        
        try {
            // 1. Prepare seats to register
            String[] targetCodesArray = seatCodes.toUpperCase().split("[,\\s/]+");
            java.util.Set<String> targetCodes = java.util.Arrays.stream(targetCodesArray)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toSet());
                
            List<SeatSelectionDTO> allSeats = seatDAO.getSeatsByShowtime(showtimeId);
            List<SeatSelectionDTO> matchedSeats = allSeats.stream()
                .filter(s -> targetCodes.contains(s.getSeatCode().toUpperCase()))
                .collect(Collectors.toList());

            if (matchedSeats.size() < targetCodes.size()) {
                return "Một số ghế bạn chọn không tồn tại trong hệ thống. Vui lòng kiểm tra sơ đồ ghế.";
            }
            
            // Check availability
            for (SeatSelectionDTO seat : matchedSeats) {
                if ("BOOKED".equalsIgnoreCase(seat.getStatus())) {
                    return "Ghế " + seat.getSeatCode() + " đã có người đặt trước. Vui lòng chọn ghế khác.";
                }
            }

            // 2. Create Invoice object
            Invoice invoice = new Invoice();
            invoice.setUserId(this.userId);
            invoice.setShowtimeId(showtimeId);
            invoice.setTotalAmount(new java.math.BigDecimal(totalAmount));
            invoice.setStatus("PENDING");
            
            // 3. Persist to DB
            try (Connection conn = DBConnect.getConnection()) {
                if (conn == null) throw new Exception("Không thể kết nối database.");
                conn.setAutoCommit(false);
                try {
                    int invoiceId = invoiceDAO.insert(conn, invoice);
                    
                    // 4. Register tickets
                    dao.TicketDetailDAO ticketDAO = new dao.TicketDetailDAO();
                    List<model.TicketDetail> tickets = matchedSeats.stream().map(s -> {
                        model.TicketDetail t = new model.TicketDetail();
                        t.setInvoiceId(invoiceId);
                        t.setSeatId(s.getSeatId());
                        t.setShowtimeId(showtimeId);
                        t.setActualPrice(new java.math.BigDecimal(s.getPrice()));
                        return t;
                    }).collect(Collectors.toList());
                    
                    ticketDAO.insertBatch(conn, tickets);
                    
                    conn.commit();
                    return String.format(
                        "{\"actionType\": \"BOOKING_SUCCESS\", \"data\": {\"invoiceId\": %d, \"showtimeId\": %d, \"seats\": \"%s\", \"total\": %.0f}, \"message\": \"Đặt vé thành công! Mã hóa đơn của bạn là #%d. Vui lòng hoàn tất thanh toán.\"}",
                        invoiceId, showtimeId, seatCodes, totalAmount, invoiceId
                    );
                } catch (Exception e) {
                    if (conn != null) conn.rollback();
                    throw e;
                }
            }
        } catch (Exception e) {
            System.err.println("[AI-ERROR] confirmBooking error: " + e.getMessage());
            e.printStackTrace();
            return "Lỗi khi xử lý đặt vé: " + e.getMessage();
        }
    }

    @Tool("Huỷ hoá đơn hoặc booking đang chờ (invoiceId)")
    public String cancelInvoice(@P("ID hóa đơn (invoiceId)") int invoiceId) {
        System.out.println("[AI-DEBUG] Tool cancelInvoice called for InvoiceID: " + invoiceId);
        try {
            invoiceDAO.updateStatus(invoiceId, "Canceled");
            return "Đã hủy hóa đơn thành công.";
        } catch (Exception e) {
            System.err.println("[AI-ERROR] cancelInvoice error: " + e.getMessage());
            return "Lỗi khi hủy hóa đơn: " + e.getMessage();
        }
    }
}
