package ai.skills;

import dev.langchain4j.agent.tool.Tool;
import service.IncomeStatictisService;
import service.TicketManagementService;
import service.TimeSlotService;
import service.SeatFillRate_ViewService;
import model.Movie_Ticket_ViewDTO;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Các công cụ dành cho CineAnalyst để truy xuất dữ liệu thống kê rạp phim.
 */
public class AdminSkills {

    private final IncomeStatictisService incomeService;
    private final TicketManagementService ticketService;
    private final TimeSlotService timeSlotService;
    private final SeatFillRate_ViewService seatService;

    public AdminSkills() {
        this.incomeService = new IncomeStatictisService();
        this.ticketService = new TicketManagementService();
        this.timeSlotService = new TimeSlotService();
        this.seatService = new SeatFillRate_ViewService();
    }

    // For testing
    public AdminSkills(IncomeStatictisService incomeService, 
                      TicketManagementService ticketService,
                      TimeSlotService timeSlotService,
                      SeatFillRate_ViewService seatService) {
        this.incomeService = incomeService;
        this.ticketService = ticketService;
        this.timeSlotService = timeSlotService;
        this.seatService = seatService;
    }

    @Tool("Lấy báo cáo doanh thu tổng quan bao gồm doanh thu ngày, tháng, năm và phân loại từ vé/sản phẩm")
    public String getRevenueSummary() {
        double daily = incomeService.getDaylyRevenue();
        double monthly = incomeService.calculateTotalRevenue();
        double fromTicket = incomeService.calculateTicketRevenue();
        double fromProduct = incomeService.calculateProductRevenue();
        double yearly = incomeService.getYearlyRevenue();

        return String.format(
            "Báo cáo doanh thu:\n" +
            "- Hôm nay: %,.0f VND\n" +
            "- Tháng này: %,.0f VND (Từ vé: %,.0f, Từ sản phẩm: %,.0f)\n" +
            "- Năm nay: %,.0f VND",
            daily, monthly, fromTicket, fromProduct, yearly
        );
    }

    @Tool("Lấy danh sách các phim bán chạy nhất (Top Movies) dựa trên số lượng vé và doanh thu")
    public String getTopPerformingMovies() {
        try {
            List<Movie_Ticket_ViewDTO> topMovies = ticketService.getAllOfPageNumber(1);
            if (topMovies == null || topMovies.isEmpty()) return "Chưa có dữ liệu phim bán chạy.";

            return topMovies.stream()
                .limit(5)
                .map(m -> String.format("- %s: %d vé, %,.0f VND", m.getTitle(), m.getTicketsSold(), m.getRevenue()))
                .collect(Collectors.joining("\n", "Top 5 phim bán chạy nhất:\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy dữ liệu phim: " + e.getMessage();
        }
    }

    @Tool("Lấy tỉ lệ lấp đầy ghế (Seat Fill Rate) trung bình của rạp trong tháng hiện tại")
    public String getMonthlySeatFillRate() {
        try {
            double rate = seatService.getSeatFillRateCurrentMonth();
            return String.format("Tỉ lệ lấp đầy ghế trung bình tháng này là: %.2f%%", rate * 100);
        } catch (Exception e) {
            return "Lỗi khi lấy dữ liệu độ phủ ghế: " + e.getMessage();
        }
    }

    @Tool("Lấy thống kê số lượng vé đã bán ra trong ngày, tháng và năm")
    public String getTicketSalesStats() {
        try {
            int daily = ticketService.getDailyTicketsSold();
            int monthly = ticketService.getMothlyTicketsSold();
            int yearly = ticketService.getYearlyTicketsSold();
            return String.format(
                "Thống kê vé bán:\n- Hôm nay: %,d vé\n- Tháng này: %,d vé\n- Năm nay: %,d vé",
                daily, monthly, yearly
            );
        } catch (Exception e) {
            return "Lỗi khi lấy thống kê vé: " + e.getMessage();
        }
    }
}
