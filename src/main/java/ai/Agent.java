package ai;

import ai.memory.Memory;
import model.Movie_Ticket_ViewDTO;
import model.SeatFillRate_ViewDTO;
import model.TicketSoldBySlot_ViewDTO;
import model.TimeSlotKpiDTO;
import service.IncomeStatictisService;
import service.SeatFillRate_ViewService;
import service.TicketManagementService;
import service.TimeSlotService;
import java.util.List;

public class Agent {

    private final Memory memory;
    private final LLM llm;
    private final IncomeStatictisService incomeService;
    private final TicketManagementService ticketService;
    private final TimeSlotService timeSlotService;
    private final SeatFillRate_ViewService seatService;

    private static final String SYSTEM_PROMPT = """
    Bạn là CineBot — chuyên gia phân tích kinh doanh của rạp chiếu phim.

    Phạm vi: doanh thu, vé & phim, khung giờ vàng, độ phủ ghế, xu hướng 6 tháng gần nhất.

    Ngưỡng độ phủ ghế: <40% Kém | 40-60% TB | 60-80% Tốt | >80% Xuất sắc

    Quy tắc BẮT BUỘC:
    - Nếu người dùng CHÀO HỎI (hello, hi, xin chào, chào, hey...):
      Trả lời: "Xin chào! 👋 Tôi là CineBot. Tôi có thể giúp gì cho bạn?"
      Không làm gì thêm, không phân tích dữ liệu.
    - Nếu câu hỏi có SỐ LIỆU CỤ THỂ (doanh thu, vé, fill rate...):
      Trả lời THẲNG con số trước, rồi mới nhận xét ngắn (nếu cần).
      Ví dụ: "Doanh thu tháng 3: 50,000,000 ₫ (tăng 10% so với tháng trước)."
    - Trả lời KHÔNG QUÁ 100 TỪ. Bỏ lời thừa, đi thẳng vào vấn đề.
    - Dữ liệu trống → "Chưa có dữ liệu cho khoảng thời gian này."
    - Câu hỏi ngoài phạm vi → từ chối hài hước 1-2 câu, gợi ý 1 câu hỏi liên quan.
    - Trả lời tiếng Việt, dùng bullet/bold khi thực sự cần thiết.
    """;

    public Agent(Memory memory, LLM llm,
                 IncomeStatictisService incomeService,
                 TicketManagementService ticketService,
                 TimeSlotService timeSlotService,
                 SeatFillRate_ViewService seatService) {
        this.memory = memory;
        this.llm = llm;
        this.incomeService = incomeService;
        this.ticketService = ticketService;
        this.timeSlotService = timeSlotService;
        this.seatService = seatService;
    }

    private String buildPrompt(String userMessage, String dataContext) {
        return String.format("""
            === DỮ LIỆU THỐNG KÊ (6 THÁNG GẦN NHẤT) ===
            %s

            === LỊCH SỬ HỘI THOẠI GẦN ĐÂY ===
            %s

            === CÂU HỎI ===
            %s

            Yêu cầu: Trả lời ĐÚNG TRỌNG TÂM câu hỏi, KHÔNG QUÁ 100 TỪ.
            Chỉ dùng dữ liệu liên quan trực tiếp đến câu hỏi.
            """, dataContext, formatHistory(), userMessage);
    }

    // Giới hạn tối đa ký tự gửi lên LLM để tránh vượt token limit của Groq
    private static final int MAX_DATA_CHARS = 6000;

    public String handle(String userMessage) {
        try {
            String dataContext = fetchDataAsText();
            // Cắt bớt nếu data quá dài (Groq free tier giới hạn token)
            if (dataContext.length() > MAX_DATA_CHARS) {
                dataContext = dataContext.substring(0, MAX_DATA_CHARS) + "\n...[dữ liệu bị rút gọn do giới hạn token]";
            }

            String prompt = buildPrompt(userMessage, dataContext);

            memory.addMessage("user", userMessage);
            String response = llm.generate(List.of(
                new Message("system", SYSTEM_PROMPT),
                new Message("user", prompt)
            ));

            memory.addMessage("assistant", response);
            return response;

        } catch (Exception e) {
            // Hiển thị loại lỗi để dễ debug
            String errType = e.getClass().getSimpleName();
            String errMsg  = e.getMessage() != null ? e.getMessage() : "(không có chi tiết)";
            String fallback = "⚠️ Lỗi kỹ thuật: **" + errType + "**\n```\n" + errMsg + "\n```\n" +
                "Bạn có thể thử hỏi lại, hoặc reload trang nếu lỗi liên tục nhé!";
            memory.addMessage("assistant", fallback);
            return fallback;
        }
    }

    // =========================================================
    // DATA FETCHING — gộp toàn bộ 4 nguồn thống kê
    // =========================================================

    private String fetchDataAsText() {
        StringBuilder sb = new StringBuilder();

        sb.append(fetchRevenueData());
        sb.append("\n");
        sb.append(fetchTicketData());
        sb.append("\n");
        sb.append(fetchTimeSlotKpiData());
        sb.append("\n");
        sb.append(fetchSeatFillData());

        return sb.toString();
    }

    /** Section 1: Doanh thu tổng hợp (từ IncomeStatictisService) */
    private String fetchRevenueData() {
        try {
            double daily   = incomeService.getDaylyRevenue();
            double monthly = incomeService.calculateTotalRevenue();
            double ticket  = incomeService.calculateTicketRevenue();
            double product = incomeService.calculateProductRevenue();
            double yearly  = incomeService.getYearlyRevenue();
            double pctTicket  = monthly > 0 ? (ticket  / monthly * 100) : 0;
            double pctProduct = monthly > 0 ? (product / monthly * 100) : 0;

            return String.format("""
                [DOANH THU]
                - Doanh thu hôm nay  : %,.0f ₫
                - Doanh thu tháng này: %,.0f ₫
                    + Từ vé           : %,.0f ₫ (%.1f%%)
                    + Từ sản phẩm    : %,.0f ₫ (%.1f%%)
                - Doanh thu năm nay  : %,.0f ₫
                """,
                daily, monthly, ticket, pctTicket, product, pctProduct, yearly);
        } catch (Exception e) {
            return "[DOANH THU] Lỗi khi lấy dữ liệu: " + e.getMessage() + "\n";
        }
    }

    /** Section 2: Thống kê vé (từ TicketManagementService) */
    private String fetchTicketData() {
        StringBuilder sb = new StringBuilder("[VÉ BÁN]\n");
        try {
            sb.append(String.format("- Vé hôm nay : %d\n", ticketService.getDailyTicketsSold()));
            sb.append(String.format("- Vé tháng này: %d\n", ticketService.getMothlyTicketsSold()));
            sb.append(String.format("- Vé năm nay : %d\n", ticketService.getYearlyTicketsSold()));
        } catch (Exception e) {
            sb.append("  (Lỗi lấy số vé: ").append(e.getMessage()).append(")\n");
        }

        // Vé theo phim (tất cả trang)
        try {
            int totalPages = ticketService.returnNumberPage();
            sb.append("\nVé bán theo phim (tháng này):\n");
            sb.append(String.format("  %-30s %8s %15s\n", "Phim", "Số vé", "Doanh thu (₫)"));
            sb.append("  " + "-".repeat(57) + "\n");
            for (int p = 1; p <= Math.min(totalPages, 5); p++) {  // tối đa 5 trang để không quá dài
                List<Movie_Ticket_ViewDTO> movies = ticketService.getAllOfPageNumber(p);
                for (Movie_Ticket_ViewDTO m : movies) {
                    sb.append(String.format("  %-30s %8d %15,.0f\n",
                        truncate(m.getTitle(), 30), m.getTicketsSold(), m.getRevenue()));
                }
            }
        } catch (Exception e) {
            sb.append("  (Lỗi lấy danh sách phim: ").append(e.getMessage()).append(")\n");
        }

        // Vé theo khung giờ
        try {
            List<TicketSoldBySlot_ViewDTO> slots = ticketService.getTicketSoldBySlotCurrentMonth();
            sb.append("\nVé theo khung giờ (tháng này):\n");
            sb.append(String.format("  %-20s %8s %15s\n", "Khung giờ", "Số vé", "Doanh thu (₫)"));
            sb.append("  " + "-".repeat(45) + "\n");
            for (TicketSoldBySlot_ViewDTO s : slots) {
                sb.append(String.format("  %s-%s %8d %15,.0f\n",
                    s.getStartTime(), s.getEndTime(), s.getTicketsSold(), s.getSlotRevenue()));
            }
        } catch (Exception e) {
            sb.append("  (Lỗi lấy vé theo slot: ").append(e.getMessage()).append(")\n");
        }

        return sb.toString();
    }

    /** Section 3: KPI Khung giờ chi tiết (từ TimeSlotService) */
    private String fetchTimeSlotKpiData() {
        StringBuilder sb = new StringBuilder("[KPI KHUNG GIỜ - CHI TIẾT THEO NGÀY]\n");
        try {
            List<TimeSlotKpiDTO> kpis = timeSlotService.getTimeSlotKpiCurrentMonth();
            if (kpis.isEmpty()) {
                sb.append("  Không có dữ liệu KPI khung giờ tháng này.\n");
                return sb.toString();
            }
            sb.append(String.format("  %-12s %-20s %8s %15s\n",
                "Ngày", "Khung giờ", "Số vé", "Doanh thu (₫)"));
            sb.append("  " + "-".repeat(57) + "\n");
            for (TimeSlotKpiDTO k : kpis) {
                sb.append(String.format("  %-12s %s-%s %8d %15,.0f\n",
                    k.getShowDate(),
                    k.getStartTime(), k.getEndTime(),
                    k.getTicketsSold(), k.getRevenue()));
            }
        } catch (Exception e) {
            sb.append("  (Lỗi lấy KPI slot: ").append(e.getMessage()).append(")\n");
        }
        return sb.toString();
    }

    /** Section 4: Độ phủ ghế chi tiết (từ SeatFillRate_ViewService) */
    private String fetchSeatFillData() {
        StringBuilder sb = new StringBuilder("[ĐỘ PHỦ GHẾ - CHI TIẾT TỪNG SUẤT CHIẾU]\n");
        try {
            List<SeatFillRate_ViewDTO> data = seatService.getAllCurrentMonth();
            if (data.isEmpty()) {
                sb.append("  Không có dữ liệu độ phủ ghế tháng này.\n");
                return sb.toString();
            }
            sb.append(String.format("  %-12s %-22s %-10s %-15s %8s %8s %8s\n",
                "Ngày", "Phim", "Phòng", "Khung giờ", "Đã bán", "Tổng ghế", "Độ phủ%"));
            sb.append("  " + "-".repeat(87) + "\n");
            for (SeatFillRate_ViewDTO d : data) {
                sb.append(String.format("  %-12s %-22s %-10s %s-%s %8d %8d %7.1f%%\n",
                    d.getShowDate(),
                    truncate(d.getMovieTitle(), 22),
                    truncate(d.getHallName(), 10),
                    d.getStartTime(), d.getEndTime(),
                    d.getTicketsSold(), d.getTotalSeats(),
                    d.getFillRate()));
            }
        } catch (Exception e) {
            sb.append("  (Lỗi lấy dữ liệu fill rate: ").append(e.getMessage()).append(")\n");
        }
        return sb.toString();
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private String formatHistory() {
        List<Message> messages = memory.getConversation();
        if (messages.isEmpty()) return "(Chưa có lịch sử)";
        StringBuilder sb = new StringBuilder();
        int limit = Math.max(0, messages.size() - 8);
        for (int i = limit; i < messages.size(); i++) {
            Message msg = messages.get(i);
            sb.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }
        return sb.toString();
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    public void clearMemory() {
        memory.clear();
    }
}