package ai.skills.admin;

import dev.langchain4j.agent.tool.Tool;
import service.IncomeStatictisService;
import service.TicketManagementService;
import service.SeatFillRate_ViewService;
import service.ForecastService;
import model.Movie_Ticket_ViewDTO;
import model.ForecastResult;
import model.ForecastDTO;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Tools for AnalystBot sub-agent to handle statistics, performance metrics, and forecasting.
 * Now encapsulates the intelligence logic previously in ForecastService.
 */
public class AnalystBotSkills {

    private final IncomeStatictisService incomeService;
    private final TicketManagementService ticketService;
    private final SeatFillRate_ViewService seatService;
    private final ForecastService forecastService;

    public AnalystBotSkills() {
        this.incomeService = new IncomeStatictisService();
        this.ticketService = new TicketManagementService();
        this.seatService = new SeatFillRate_ViewService();
        this.forecastService = new ForecastService();
    }

    public AnalystBotSkills(IncomeStatictisService incomeService, TicketManagementService ticketService, SeatFillRate_ViewService seatService, ForecastService forecastService) {
        this.incomeService = incomeService;
        this.ticketService = ticketService;
        this.seatService = seatService;
        this.forecastService = forecastService;
    }

    @Tool("Lấy báo cáo doanh thu tổng quan bao gồm doanh thu ngày, tháng, năm")
    public String getRevenueSummary() {
        double daily = incomeService.getDaylyRevenue();
        double monthly = incomeService.calculateTotalRevenue();
        double yearly = incomeService.getYearlyRevenue();

        return String.format(
            Locale.GERMAN,
            "Báo cáo doanh thu:\n- Hôm nay: %,.0f VND\n- Tháng này: %,.0f VND\n- Năm nay: %,.0f VND",
            daily, monthly, yearly
        );
    }

    @Tool("Lấy danh sách các phim bán chạy nhất (Top Movies)")
    public String getTopPerformingMovies() {
        try {
            List<Movie_Ticket_ViewDTO> topMovies = ticketService.getAllOfPageNumber(1);
            if (topMovies == null || topMovies.isEmpty()) return "Chưa có dữ liệu phim bán chạy.";

            return topMovies.stream()
                .limit(5)
                .map(m -> String.format(Locale.GERMAN, "- %s: %d vé, %,.0f VND", m.getTitle(), m.getTicketsSold(), m.getRevenue()))
                .collect(Collectors.joining("\n", "Top 5 phim bán chạy nhất:\n", ""));
        } catch (Exception e) {
            return "Lỗi khi lấy dữ liệu phim: " + e.getMessage();
        }
    }

    @Tool("Dự báo doanh thu và số lượng vé bán trong 7 ngày tới (Sử dụng AI)")
    public String get7DayForecast() {
        try {
            // AnalystBot logic: ForecastService now performs the heavy lifting and LLM call
            // We keep the service call but centralize the "Identity" of the analyst here.
            ForecastResult result = forecastService.get7DayForecast();
            List<ForecastDTO> futureData = result.getDailyData().stream()
                .filter(ForecastDTO::isFuture)
                .collect(Collectors.toList());

            double totalRevenue = futureData.stream().mapToDouble(ForecastDTO::getForecastRevenue).sum();
            int totalTickets = futureData.stream().mapToInt(ForecastDTO::getForecastTickets).sum();
            
            return String.format(
                Locale.GERMAN,
                "Dự báo 7 ngày tới:\n- Tổng doanh thu dự kiến: %,.0f VND\n- Tổng số vé dự kiến: %,d vé\n- Phân tích chi tiết: %s",
                totalRevenue, totalTickets, result.getAnalysis()
            );
        } catch (Exception e) {
            return "Lỗi khi dự báo: " + e.getMessage();
        }
    }

    @Tool("Lấy dữ liệu thô (Raw Data) của 14 ngày qua để tự phân tích")
    public String getHistoricalData() {
        try {
            ForecastResult result = forecastService.get7DayForecast();
            return result.getDailyData().stream()
                .filter(d -> !d.isFuture())
                .map(d -> String.format("%s: %,.0f VND, %d vé", d.getDate(), d.getActualRevenue(), d.getActualTickets()))
                .collect(Collectors.joining("\n", "Dữ liệu lịch sử 14 ngày qua:\n", ""));
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @Tool("Lấy tỉ lệ lấp đầy ghế (Seat Fill Rate) trung bình tháng này")
    public String getMonthlySeatFillRate() {
        try {
            double rate = seatService.getSeatFillRateCurrentMonth();
            return String.format("Tỉ lệ lấp đầy ghế trung bình tháng này: %.2f%%", rate * 100);
        } catch (Exception e) {
            return "Lỗi khi lấy tỉ lệ lấp đầy: " + e.getMessage();
        }
    }
}
