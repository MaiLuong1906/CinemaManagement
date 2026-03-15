package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.*;
import model.ForecastDTO;
import model.ForecastResult;
import model.Movie_Ticket_ViewDTO;
import model.TicketSoldBySlot_ViewDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Unified Statistic Servlet handling:
 * - Overview Dashboard
 * - Revenue Breakdown
 * - Ticket Management/Stats
 */
@WebServlet("/admin/statistic")
public class StatisticServlet extends BaseServlet {

    private IncomeStatictisService incomeStatictisService;
    private TicketManagementService ticketManagementService;
    private TimeSlotService timeSlotService;
    private SeatFillRate_ViewService seatFillRateService;
    private ForecastService forecastService;

    @Override
    public void init() {
        incomeStatictisService = new IncomeStatictisService();
        ticketManagementService = new TicketManagementService();
        timeSlotService = new TimeSlotService();
        seatFillRateService = new SeatFillRate_ViewService();
        forecastService = new ForecastService();
    }

    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String action = getStringParam(req, "action", "overview");

        switch (action) {
            case "revenue":
                showRevenue(req, resp);
                break;
            case "tickets":
                showTickets(req, resp);
                break;
            case "export-excel":
                exportExcel(req, resp);
                break;
            case "overview":
            default:
                showOverview(req, resp);
                break;
        }
    }

    private void showOverview(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NumberFormat vndFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        double totalIncome = incomeStatictisService.calculateTotalRevenue();
        req.setAttribute("totalIncome", vndFormat.format(totalIncome));

        try {
            int monthlyTicketsSold = ticketManagementService.getMothlyTicketsSold();
            req.setAttribute("monthlyTicketsSold", monthlyTicketsSold);
        } catch (SQLException ex) {
            req.setAttribute("error_for_getAtribute", "null");
        }

        double totalIncomeToday = incomeStatictisService.getDaylyRevenue();
        req.setAttribute("totalIncomeToday", vndFormat.format(totalIncomeToday));

        try {
            List<Movie_Ticket_ViewDTO> topMovies = ticketManagementService.getAllOfPageNumber(1);
            int totalPages = ticketManagementService.returnNumberPage();
            List<Movie_Ticket_ViewDTO> badMovies = ticketManagementService.getAllOfPageNumber(totalPages);

            req.setAttribute("topMovies", topMovies);
            req.setAttribute("badMovies", badMovies);
        } catch (RuntimeException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
        }

        try {
            int numberOfShowtime = timeSlotService.countTimeSlot();
            req.setAttribute("numberOfShowtime", numberOfShowtime);
        } catch (RuntimeException ex) {
            req.setAttribute("msg", ex.getMessage());
        }

        // Forecast Data
        try {
            ForecastResult forecastResult = forecastService.get7DayForecast();
            req.setAttribute("forecastData", forecastResult.getDailyData());
            req.setAttribute("forecastAnalysis", forecastResult.getAnalysis());
        } catch (Exception e) {
            log("Error getting forecast data", e);
        }

        forward(req, resp, "/views/admin/users/admin-statictis.jsp");
    }

    private void showRevenue(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NumberFormat vndFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        double percentProduct = incomeStatictisService.calculatePercentProduct();
        double percentTicket = incomeStatictisService.calculatePercentTicket();
        req.setAttribute("percentProduct", percentProduct);
        req.setAttribute("percentTicket", percentTicket);

        double monthlyRevenue = incomeStatictisService.calculateTotalRevenue();
        double dailyRevenue = incomeStatictisService.getDaylyRevenue();
        double yearlyRevenue = incomeStatictisService.getYearlyRevenue();

        req.setAttribute("monthlyRevenue", vndFormat.format(monthlyRevenue));
        req.setAttribute("dailyRevenue", vndFormat.format(dailyRevenue));
        req.setAttribute("yearlyRevenue", vndFormat.format(yearlyRevenue));

        double ticketRevenue = incomeStatictisService.calculateTicketRevenue();
        double productRevenue = incomeStatictisService.calculateProductRevenue();
        req.setAttribute("ticketRevenue", vndFormat.format(ticketRevenue));
        req.setAttribute("productRevenue", vndFormat.format(productRevenue));

        forward(req, resp, "/views/admin/statistic/overal-revenue.jsp");
    }

    private void showTickets(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int monthlyTicketsSold = ticketManagementService.getMothlyTicketsSold();
            req.setAttribute("monthlyTicketsSold", monthlyTicketsSold);

            int dailyTicketsSold = ticketManagementService.getDailyTicketsSold();
            req.setAttribute("dailyTicketsSold", dailyTicketsSold);

            int yearlyTicketsSold = ticketManagementService.getYearlyTicketsSold();
            req.setAttribute("yearlyTicketsSold", yearlyTicketsSold);
        } catch (SQLException ex) {
            req.setAttribute("error_for_getAtribute", "null");
        }

        int page = getIntParam(req, "page", 1);
        try {
            List<Movie_Ticket_ViewDTO> movies = ticketManagementService.getAllOfPageNumber(page);
            int totalPages = ticketManagementService.returnNumberPage();
            req.setAttribute("movies", movies);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
        } catch (RuntimeException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
        }

        try {
            List<TicketSoldBySlot_ViewDTO> ticketSoldBySlot = ticketManagementService.getTicketSoldBySlotCurrentMonth();
            req.setAttribute("ticketSoldBySlot", ticketSoldBySlot);
        } catch (Exception ex) {
            req.setAttribute("errorMessage", ex.getMessage());
        }

        try {
            double seatFillRate = seatFillRateService.getSeatFillRateCurrentMonth();
            req.setAttribute("seatFillRate", seatFillRate);
        } catch (Exception ex) {
            req.setAttribute("errorMessage", ex.getMessage());
        }

        forward(req, resp, "/views/admin/statistic/ticket-management.jsp");
    }

    private void exportExcel(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition", "attachment; filename=bao_cao_thong_ke_phim.xlsx");

        try (Workbook workbook = new XSSFWorkbook(); OutputStream out = resp.getOutputStream()) {
            Sheet sheet = workbook.createSheet("Thống kê Phim");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "#", "Tên Phim", "Vé đã bán", "Doanh thu (VNĐ)" };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows - get Movie Stats data
            List<Movie_Ticket_ViewDTO> movieStats;
            try {
                movieStats = ticketManagementService.getAllMovieStats();
            } catch (Exception e) {
                movieStats = java.util.Collections.emptyList();
            }

            int rowNum = 1;
            for (Movie_Ticket_ViewDTO movie : movieStats) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(movie.getRowNum());
                row.createCell(1).setCellValue(movie.getTitle());
                row.createCell(2).setCellValue(movie.getTicketsSold());
                row.createCell(3).setCellValue(movie.getRevenue());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi xuất file Excel: " + e.getMessage());
        }
    }
}
