package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.*;
import service.*;
import exception.ValidationException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Consolidated admin servlet handling all admin functions:
 * - Products (CRUD)
 * - Revenue reporting
 * - Statistics dashboard
 * - Ticket management
 * - User management (delegated to AdminUserController for now)
 */
@WebServlet("/admin")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)
public class AdminServlet extends BaseServlet {

    private ProductService productService;
    private IncomeStatictisService incomeStatisticsService;
    private TicketManagementService ticketManagementService;
    private TimeSlotService timeSlotService;
    private SeatFillRate_ViewService seatFillRateService;
    private NumberFormat vndFormat;

    @Override
    public void init() {
        productService = new ProductService();
        incomeStatisticsService = new IncomeStatictisService();
        ticketManagementService = new TicketManagementService();
        timeSlotService = new TimeSlotService();
        seatFillRateService = new SeatFillRate_ViewService();
        vndFormat = NumberFormat.getInstance(Locale.of("vi", "VN"));
    }

    @Override
    protected boolean requiresAuthentication() {
        return true;
    }

    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        // TODO: Add admin role check
        // UserDTO user = getCurrentUser(req);
        // if (!user.isAdmin()) throw new ValidationException("Admin access required");

        String action = getStringParam(req, "action", "statistics");

        switch (action) {
            // Products
            case "products":
            case "product-list":
                listProducts(req, resp);
                break;
            case "product-insert":
                insertProduct(req, resp);
                break;
            case "product-update":
                updateProduct(req, resp);
                break;
            case "product-delete":
                deleteProduct(req, resp);
                break;

            // Revenue
            case "revenue":
                showRevenue(req, resp);
                break;

            // Statistics (default dashboard)
            case "statistics":
            default:
                showStatistics(req, resp);
                break;

            // Tickets
            case "tickets":
                showTickets(req, resp);
                break;
        }
    }

    // ==================== Products Management ====================

    private void listProducts(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        List<Product> products = productService.findAll();
        req.setAttribute("products", products);
        forward(req, resp, "/views/admin/products/list.jsp");
    }

    private void insertProduct(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String name = getStringParam(req, "name");
        BigDecimal price = new BigDecimal(getStringParam(req, "price"));
        int quantity = getIntParam(req, "quantity");

        String imageUrl = handleFileUpload(req, "image", "C:/imgForCinema/products");

        Product product = new Product();
        product.setItemName(name);
        product.setPrice(price);
        product.setStockQuantity(quantity);
        if (imageUrl != null) {
            product.setProductImgUrl(imageUrl);
        }

        productService.insert(product);
        resp.sendRedirect(req.getContextPath() + "/admin?action=products");
    }

    private void updateProduct(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int itemId = getIntParam(req, "itemId");
        String name = getStringParam(req, "name");
        BigDecimal price = new BigDecimal(getStringParam(req, "price"));
        int quantity = getIntParam(req, "quantity");

        Product product = productService.findById(itemId);
        if (product == null) {
            throw new ValidationException("Product not found");
        }

        product.setItemName(name);
        product.setPrice(price);
        product.setStockQuantity(quantity);

        // Handle image upload if provided
        String imageUrl = handleFileUpload(req, "image", "C:/imgForCinema/products");
        if (imageUrl != null) {
            product.setProductImgUrl(imageUrl);
        }

        productService.update(product);
        resp.sendRedirect(req.getContextPath() + "/admin?action=products");
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int itemId = getIntParam(req, "itemId");
        productService.delete(itemId);
        resp.sendRedirect(req.getContextPath() + "/admin?action=products");
    }

    // ==================== Revenue Reporting ====================

    private void showRevenue(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Percentages
        double percentProduct = incomeStatisticsService.calculatePercentProduct();
        double percentTicket = incomeStatisticsService.calculatePercentTicket();
        req.setAttribute("percentProduct", percentProduct);
        req.setAttribute("percentTicket", percentTicket);

        // Revenue figures
        double monthlyRevenue = incomeStatisticsService.calculateTotalRevenue();
        double dailyRevenue = incomeStatisticsService.getDaylyRevenue();
        double yearlyRevenue = incomeStatisticsService.getYearlyRevenue();
        req.setAttribute("monthlyRevenue", vndFormat.format(monthlyRevenue));
        req.setAttribute("dailyRevenue", vndFormat.format(dailyRevenue));
        req.setAttribute("yearlyRevenue", vndFormat.format(yearlyRevenue));

        // Breakdown by type
        double ticketRevenue = incomeStatisticsService.calculateTicketRevenue();
        double productRevenue = incomeStatisticsService.calculateProductRevenue();
        req.setAttribute("ticketRevenue", vndFormat.format(ticketRevenue));
        req.setAttribute("productRevenue", vndFormat.format(productRevenue));

        forward(req, resp, "/views/admin/statistic/overal-revenue.jsp");
    }

    // ==================== Statistics Dashboard ====================

    private void showStatistics(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Total income
        double totalIncome = incomeStatisticsService.calculateTotalRevenue();
        req.setAttribute("totalIncome", vndFormat.format(totalIncome));

        // Monthly tickets sold
        try {
            int monthlyTicketsSold = ticketManagementService.getMothlyTicketsSold();
            req.setAttribute("monthlyTicketsSold", monthlyTicketsSold);
        } catch (SQLException ex) {
            req.setAttribute("error_for_getAtribute", "null");
        }

        // Daily income
        double totalIncomeToday = incomeStatisticsService.getDaylyRevenue();
        req.setAttribute("totalIncomeToday", vndFormat.format(totalIncomeToday));

        // Top and worst movies
        try {
            List<Movie_Ticket_ViewDTO> topMovies = ticketManagementService.getAllOfPageNumber(1);
            int totalPages = ticketManagementService.returnNumberPage();
            List<Movie_Ticket_ViewDTO> badMovies = ticketManagementService.getAllOfPageNumber(totalPages);
            req.setAttribute("topMovies", topMovies);
            req.setAttribute("badMovies", badMovies);
        } catch (RuntimeException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
        }

        // Showtime count
        try {
            int numberOfShowtime = timeSlotService.countTimeSlot();
            req.setAttribute("numberOfShowtime", numberOfShowtime);
        } catch (RuntimeException ex) {
            req.setAttribute("msg", ex.getMessage());
        }

        forward(req, resp, "/views/admin/users/admin-statictis.jsp");
    }

    // ==================== Ticket Management ====================

    private void showTickets(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Ticket counts
        try {
            int monthlyTicketsSold = ticketManagementService.getMothlyTicketsSold();
            req.setAttribute("monthlyTicketsSold", monthlyTicketsSold);
        } catch (SQLException ex) {
            req.setAttribute("error_for_getAtribute", "null");
        }

        try {
            int dailyTicketsSold = ticketManagementService.getDailyTicketsSold();
            req.setAttribute("dailyTicketsSold", dailyTicketsSold);
        } catch (SQLException ex) {
            req.setAttribute("error_for_getAtribute", "null");
        }

        try {
            int yearlyTicketsSold = ticketManagementService.getYearlyTicketsSold();
            req.setAttribute("yearlyTicketsSold", yearlyTicketsSold);
        } catch (SQLException ex) {
            req.setAttribute("error_for_getAtribute", "null");
        }

        // Movie tickets pagination
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

        // Tickets sold by time slot
        try {
            List<TicketSoldBySlot_ViewDTO> ticketSoldBySlot =
                ticketManagementService.getTicketSoldBySlotCurrentMonth();
            req.setAttribute("ticketSoldBySlot", ticketSoldBySlot);
        } catch (Exception ex) {
            req.setAttribute("errorMessage", ex.getMessage());
        }

        // Seat fill rate
        try {
            double seatFillRate = seatFillRateService.getSeatFillRateCurrentMonth();
            req.setAttribute("seatFillRate", seatFillRate);
        } catch (Exception ex) {
            req.setAttribute("errorMessage", ex.getMessage());
        }

        forward(req, resp, "/views/admin/statistic/ticket-statictis.jsp");
    }

    // ==================== Utility Methods ====================

    private String handleFileUpload(HttpServletRequest req, String paramName, String uploadDir)
            throws IOException, ServletException {
        Part filePart = req.getPart(paramName);
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String fileName = System.currentTimeMillis() + "_"
            + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        filePart.write(uploadDir + File.separator + fileName);
        return fileName;
    }
}
