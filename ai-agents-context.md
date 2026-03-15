This file is a merged representation of a subset of the codebase, containing specifically included files, combined into a single document by Repomix.

<file_summary>
This section contains a summary of this file.

<purpose>
This file contains a packed representation of a subset of the repository's contents that is considered the most important context.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.
</purpose>

<file_format>
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Repository files (if enabled)
5. Multiple file entries, each consisting of:
  - File path as an attribute
  - Full contents of the file
</file_format>

<usage_guidelines>
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.
</usage_guidelines>

<notes>
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Only files matching these patterns are included: src/main/java/ai/CineAgentProvider.java, src/main/java/controller/ChatServlet.java, src/main/java/dao/ChatMessageDAO.java, src/main/java/ai/skills/user/InfoBotSkills.java, src/main/java/ai/skills/user/BookBotSkills.java, src/main/java/ai/skills/admin/AnalystBotSkills.java, src/main/java/ai/skills/admin/MarketingBotSkills.java, src/main/java/ai/skills/admin/ModerateBotSkills.java, src/main/webapp/views/common/ai-chat-widget.jsp, pom.xml, src/main/resources/db.properties, SQL_version1_english/chat_persistence.sql, src/test/java/ai/AgentRealApiTest.java
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Files are sorted by Git change count (files with more changes are at the bottom)
</notes>

</file_summary>

<directory_structure>
pom.xml
SQL_version1_english/chat_persistence.sql
src/main/java/ai/CineAgentProvider.java
src/main/java/ai/skills/admin/AnalystBotSkills.java
src/main/java/ai/skills/admin/MarketingBotSkills.java
src/main/java/ai/skills/admin/ModerateBotSkills.java
src/main/java/ai/skills/user/BookBotSkills.java
src/main/java/ai/skills/user/InfoBotSkills.java
src/main/java/controller/ChatServlet.java
src/main/java/dao/ChatMessageDAO.java
src/main/webapp/views/common/ai-chat-widget.jsp
src/test/java/ai/AgentRealApiTest.java
</directory_structure>

<files>
This section contains the contents of the repository's files.

<file path="src/test/java/ai/AgentRealApiTest.java">
// package ai;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

// /**
// * Integration tests using real Groq API and CineAgentProvider.
// * Verifies that the LLM can use InfoBot and BookBot skills to answer queries.
// */
// public class AgentRealApiTest {

// @Test
// public void testUserAgentWithInfoBot() throws InterruptedException {
// System.out.println("--- Testing UserAgent with InfoBot (Real API) ---");
// CineAgentProvider.CineAgent agent = CineAgentProvider.createUserAgent();
// Thread.sleep(15000); // 15 seconds to be safe with TPM

// // Query that requires searching for movies
// long start = System.currentTimeMillis();
// String response = agent.chat("test-session-info", "Rạp đang có những phim
// gì?");
// long end = System.currentTimeMillis();

// System.out.println("AI Response: " + response);
// System.out.println("Time taken: " + (end - start) + "ms");

// assertNotNull(response);
// assertFalse(response.isEmpty());
// assertTrue(response.length() > 20);
// }

// @Test
// public void testUserAgentWithBookBot() throws InterruptedException {
// System.out.println("--- Testing UserAgent with BookBot (Real API) ---");
// CineAgentProvider.CineAgent agent = CineAgentProvider.createUserAgent();
// Thread.sleep(15000);

// // Query that requires getting a seat map
// long start = System.currentTimeMillis();
// String response = agent.chat("test-session-book", "Cho tôi xem sơ đồ ghế của
// suất chiếu ID 1");
// long end = System.currentTimeMillis();

// System.out.println("AI Response: " + response);
// System.out.println("Time taken: " + (end - start) + "ms");

// assertNotNull(response);
// assertTrue(response.contains("ghế") || response.contains("A1") ||
// response.contains("BOOKED") || response.contains("AVAILABLE"));
// }

// @Test
// public void testUserAgentAdvancedFlow() throws InterruptedException {
// System.out.println("--- Testing UserAgent Advanced Flow (Real API) ---");
// CineAgentProvider.CineAgent agent = CineAgentProvider.createUserAgent();
// Thread.sleep(15000);
// // Complex query: find a movie, then ask for showtimes
// agent.chat("adv-session", "Rạp có phim Spider-man không?");
// Thread.sleep(15000);
// String response = agent.chat("adv-session", "Vậy phim đó có lịch chiếu lúc
// nào?");

// System.out.println("AI Response: " + response);

// assertNotNull(response);
// assertTrue(response.contains("suất chiếu") || response.contains("lịch chiếu")
// || response.contains("ID"));
// }
// }
</file>

<file path="SQL_version1_english/chat_persistence.sql">
/* =========================================================
   Chat Persistence Schema for AI Agents
   Adds long-term memory capabilities to CineGuide and CineAnalyst
   ========================================================= */

USE CinemaManagement;
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='chat_messages' AND xtype='U')
BEGIN
    CREATE TABLE chat_messages (
        id INT IDENTITY(1,1) PRIMARY KEY,
        session_id VARCHAR(100) NOT NULL,
        user_id INT NULL, -- NULL if guest
        role VARCHAR(20) NOT NULL, -- 'user', 'assistant', 'system'
        content NVARCHAR(MAX) NOT NULL,
        created_at DATETIME DEFAULT GETDATE(),
        
        -- Optional foreign key if user logs out, we still keep history? 
        -- Yes, standard practice. But since user_id is nullable, we shouldn't enforce strict CASCADE unless needed.
        CONSTRAINT FK_Chat_User FOREIGN KEY (user_id) REFERENCES user_profiles(user_id) ON DELETE SET NULL
    );

    -- Indexes for fast retrieval
    CREATE INDEX idx_chat_session ON chat_messages(session_id, created_at);
    CREATE INDEX idx_chat_user ON chat_messages(user_id, created_at);

    PRINT 'Table chat_messages created successfully.';
END
ELSE
BEGIN
    PRINT 'Table chat_messages already exists.';
END
GO
</file>

<file path="src/main/java/ai/skills/admin/AnalystBotSkills.java">
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
import java.util.stream.Collectors;

/**
 * Tools for AnalystBot sub-agent to handle statistics, performance metrics, and forecasting.
 * Now encapsulates the intelligence logic previously in ForecastService.
 */
public class AnalystBotSkills {

    private final IncomeStatictisService incomeService = new IncomeStatictisService();
    private final TicketManagementService ticketService = new TicketManagementService();
    private final SeatFillRate_ViewService seatService = new SeatFillRate_ViewService();
    private final ForecastService forecastService = new ForecastService();

    @Tool("Lấy báo cáo doanh thu tổng quan bao gồm doanh thu ngày, tháng, năm")
    public String getRevenueSummary() {
        double daily = incomeService.getDaylyRevenue();
        double monthly = incomeService.calculateTotalRevenue();
        double yearly = incomeService.getYearlyRevenue();

        return String.format(
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
                .map(m -> String.format("- %s: %d vé, %,.0f VND", m.getTitle(), m.getTicketsSold(), m.getRevenue()))
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
</file>

<file path="src/main/java/ai/skills/admin/MarketingBotSkills.java">
package ai.skills.admin;

import dao.DBConnect;
import dao.MovieDAO;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.P;
import model.Movie;
import service.TicketManagementService;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tools for MarketingBot sub-agent to retrieve context for content generation.
 */
public class MarketingBotSkills {

    private final MovieDAO movieDAO = new MovieDAO();

    @Tool("Lấy thông tin chi tiết phim để viết bài quảng cáo (ID phim)")
    public String getMovieDetailsForMarketing(@P("ID phim") int movieId) {
        try (Connection conn = DBConnect.getConnection()) {
            Movie movie = movieDAO.findById(conn, movieId);
            if (movie == null) return "Không tìm thấy phim.";
            return String.format("Phim: %s\nRating: %s\nMô tả: %s", 
                movie.getTitle(), movie.getAgeRating(), movie.getDescription());
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách phim để chọn làm mục tiêu marketing")
    public String getMoviesForMarketing() {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            return movies.stream()
                .limit(10)
                .map(m -> String.format("- %s (ID: %d)", m.getTitle(), m.getMovieId()))
                .collect(Collectors.joining("\n", "Danh sách phim:\n", ""));
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}
</file>

<file path="src/main/java/ai/skills/admin/ModerateBotSkills.java">
package ai.skills.admin;

import dao.UserProfileDAO;
import dao.DBConnect;
import dev.langchain4j.agent.tool.Tool;
import model.UserDTO;
import service.LogService;
import service.CinemaHallService;
import model.CinemaHall;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tools for ModerateBot sub-agent to handle system monitoring and moderation tasks.
 */
public class ModerateBotSkills {

    private final UserProfileDAO userProfileDAO = new UserProfileDAO();

    @Tool("Lấy danh sách người dùng trong hệ thống (Tên, Email, Vai trò)")
    public String getUserList() {
        try {
            List<UserDTO> users = userProfileDAO.getAllUsers();
            if (users.isEmpty()) return "Không có người dùng nào.";
            return users.stream()
                .limit(10)
                .map(u -> String.format("- %s (%s) | Role: %s", u.getFullName(), u.getEmail(), u.getRoleId()))
                .collect(Collectors.joining("\n", "Danh sách 10 khách hàng gần nhất:\n", ""));
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @Tool("Xem lịch sử log hệ thống gần nhất (audit logs)")
    public String getSystemLogs() {
        return LogService.getLatestLogs();
    }

    @Tool("Xem trạng thái các phòng chiếu (Hall status)")
    public String getHallStatus() {
        try (Connection conn = DBConnect.getConnection()) {
            CinemaHallService hallService = new CinemaHallService(conn);
            List<CinemaHall> halls = hallService.getAllHalls();
            return halls.stream()
                .map(h -> String.format("- Phòng %s (ID: %d): %dx%d, Trạng thái: %s", 
                    h.getHallName(), h.getHallId(), h.getTotal_rows(), h.getTotal_cols(), h.isStatus() ? "Hoạt động" : "Bảo trì"))
                .collect(Collectors.joining("\n", "Trạng thái các phòng chiếu:\n", ""));
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}
</file>

<file path="src/main/java/ai/skills/user/BookBotSkills.java">
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

    @Tool("Lấy lịch sử đặt vé của tôi")
    public String getMyBookingHistory(@P("ID người dùng") int userId) {
        System.out.println("[AI-DEBUG] Tool getMyBookingHistory called for UserID: " + userId);
        try {
            List<BookingHistoryDTO> history = invoiceDAO.getBookingHistory(userId);
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

    @Tool("Chuẩn bị xác nhận đặt vé (Cần showtimeId và danh sách seatIds)")
    public String prepareBooking(@P("ID suất chiếu") int showtimeId, @P("Mã ghế (ví dụ: A1, A2)") String seatCodes, @P("Tổng tiền") double totalAmount) {
        System.out.println("[AI-DEBUG] Tool prepareBooking called: " + showtimeId + ", " + seatCodes);
        return String.format(
            "{\"actionType\": \"BOOKING_CONFIRM\", \"data\": {\"showtimeId\": %d, \"seats\": \"%s\", \"total\": %.0f}, \"message\": \"Vui lòng xác nhận để tiến hành đặt ghế và thanh toán.\"}",
            showtimeId, seatCodes, totalAmount
        );
    }

    @Tool("Xác nhận và tiến hành đặt vé sau khi người dùng đã đồng ý (Cần showtimeId, seatCodes, totalAmount)")
    public String confirmBooking(@P("ID suất chiếu") int showtimeId, @P("Mã ghế") String seatCodes, @P("Tổng tiền") double totalAmount) {
        System.out.println("[AI-DEBUG] Tool confirmBooking called: " + showtimeId + ", " + seatCodes);
        try {
            return String.format(
                "{\"actionType\": \"BOOKING_SUCCESS\", \"data\": {\"showtimeId\": %d, \"seats\": \"%s\", \"total\": %.0f}, \"message\": \"Đã ghi nhận yêu cầu đặt vé. Vui lòng hoàn tất thanh toán.\"}",
                showtimeId, seatCodes, totalAmount
            );
        } catch (Exception e) {
            System.err.println("[AI-ERROR] confirmBooking error: " + e.getMessage());
            return "Lỗi khi xác nhận đặt vé: " + e.getMessage();
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
</file>

<file path="src/main/java/ai/skills/user/InfoBotSkills.java">
package ai.skills.user;

import dao.DBConnect;
import dao.MovieDAO;
import dao.ShowtimeDAO;
import dao.ProductDAO;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.P;
import model.Movie;
import model.Showtime;
import model.Product;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tools for InfoBot sub-agent to handle inquiries about movies, showtimes, and products.
 */
public class InfoBotSkills {

    private final MovieDAO movieDAO = new MovieDAO();
    private final ShowtimeDAO showtimeDAO = new ShowtimeDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Tool("Tìm kiếm phim theo tên hoặc từ khóa liên quan")
    public String searchMovies(@P("Tên phim hoặc từ khóa tìm kiếm") String query) {
        System.out.println("[AI-DEBUG] Tool searchMovies called with query: " + query);
        try (Connection conn = DBConnect.getConnection()) {
            List<Movie> movies = movieDAO.searchByTitle(conn, query);
            System.out.println("[AI-DEBUG] Found " + movies.size() + " movies for query: " + query);
            if (movies.isEmpty()) return "Không tìm thấy phim nào khớp với từ khóa '" + query + "'.";

            return movies.stream()
                .map(m -> String.format("- %s (ID: %d): %s", m.getTitle(), m.getMovieId(), m.getDescription()))
                .collect(Collectors.joining("\n", "Tìm thấy các phim sau:\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] searchMovies error: " + e.getMessage());
            return "Lỗi khi tìm kiếm phim: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách phim đang có tại rạp")
    public String getAllMovies() {
        System.out.println("[AI-DEBUG] Tool getAllMovies called");
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            System.out.println("[AI-DEBUG] Found " + movies.size() + " movies in theater");
            if (movies.isEmpty()) return "Hiện tại rạp chưa có phim nào.";

            return movies.stream()
                .map(m -> String.format("- %s (ID: %d)", m.getTitle(), m.getMovieId()))
                .collect(Collectors.joining("\n", "Danh sách phim tại rạp:\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getAllMovies error: " + e.getMessage());
            return "Lỗi khi lấy danh sách phim: " + e.getMessage();
        }
    }

    @Tool("Lấy lịch chiếu (Showtimes) của một bộ phim dựa trên ID phim")
    public String getShowtimesForMovie(@P("ID của bộ phim") int movieId) {
        System.out.println("[AI-DEBUG] Tool getShowtimesForMovie called with ID: " + movieId);
        try (Connection conn = DBConnect.getConnection()) {
            List<Showtime> showtimes = showtimeDAO.findByMovie(conn, movieId);
            System.out.println("[AI-DEBUG] Found " + showtimes.size() + " showtimes for ID: " + movieId);
            if (showtimes.isEmpty()) return "Hiện chưa có lịch chiếu cho phim này.";

            return showtimes.stream()
                .map(s -> String.format("- Suất chiếu ID: %d | Ngày %s | Slot ID: %s", s.getShowtimeId(), s.getShowDate(), s.getSlotId()))
                .collect(Collectors.joining("\n", "Lịch chiếu cho phim (ID " + movieId + "):\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getShowtimesForMovie error: " + e.getMessage());
            return "Lỗi khi lấy lịch chiếu: " + e.getMessage();
        }
    }

    @Tool("Lấy danh sách combo bắp nước và giá cả")
    public String getComboProducts() {
        System.out.println("[AI-DEBUG] Tool getComboProducts called");
        try {
            List<Product> products = productDAO.findAll();
            System.out.println("[AI-DEBUG] Found " + products.size() + " combos");
            if (products.isEmpty()) return "Hiện không có sản phẩm combo nào.";

            return products.stream()
                .map(p -> String.format("- %s (ID: %d): %,.0f VND", p.getItemName(), p.getItemId(), p.getPrice()))
                .collect(Collectors.joining("\n", "Danh sách bắp nước & combo:\n", ""));
        } catch (Exception e) {
            System.err.println("[AI-ERROR] getComboProducts error: " + e.getMessage());
            return "Lỗi khi lấy danh sách sản phẩm: " + e.getMessage();
        }
    }
}
</file>

<file path="src/main/webapp/views/common/ai-chat-widget.jsp">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="chat-widget-container" id="chatWidgetContainer">
    <!-- Chat Toggle Button -->
    <button class="chat-toggle-btn" id="chatToggleBtn" onclick="toggleChat()">
        <i class="fas fa-robot"></i>
        <span class="chat-badge" id="chatBadge" style="display: none;">1</span>
    </button>

    <!-- Chat Box -->
    <div class="chat-box" id="chatBox">
        <div class="chat-header">
            <div class="d-flex align-items-center gap-2">
                <div class="bot-avatar">
                    <i class="fas fa-robot text-white"></i>
                </div>
                <div>
                    <h6 class="mb-0 fw-bold text-white">Cine AI Assistant</h6>
                    <small class="text-white-50">Online</small>
                </div>
            </div>
            <div class="d-flex gap-2">
                <button class="btn btn-sm text-white-50 hover-white" onclick="resetChat()" title="Reset session">
                    <i class="fas fa-sync-alt"></i>
                </button>
                <button class="btn btn-sm text-white-50 hover-white" onclick="toggleChat()">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        </div>

        <div class="chat-messages" id="chatMessages">
            <div class="message bot-message">
                <div class="message-content">
                    Xin chào! Tôi là trợ lý AI của rạp chiếu phim. Bạn cần hỗ trợ gì hôm nay (tìm phim, lịch chiếu, đặt vé...)?
                </div>
            </div>
        </div>

        <div class="chat-input-area">
            <form id="chatForm" onsubmit="handleChatSubmit(event)">
                <div class="input-group">
                    <input type="text" id="chatInput" class="form-control" placeholder="Nhập tin nhắn..." autocomplete="off">
                    <button class="btn btn-primary" type="submit" id="sendBtn">
                        <i class="fas fa-paper-plane"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<style>
/* Chat Widget Styles */
.chat-widget-container {
    position: fixed;
    bottom: 30px;
    right: 30px;
    z-index: 9999;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.chat-toggle-btn {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    color: white;
    font-size: 24px;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    cursor: pointer;
    transition: transform 0.3s ease;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
}

.chat-toggle-btn:hover {
    transform: scale(1.1);
}

.chat-badge {
    position: absolute;
    top: -5px;
    right: -5px;
    background: #ff4757;
    color: white;
    font-size: 12px;
    font-weight: bold;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 2px solid #0a0a0a;
}

.chat-box {
    position: absolute;
    bottom: 80px;
    right: 0;
    width: 350px;
    height: 500px;
    background: #1a1a2e;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.5);
    display: none;
    flex-direction: column;
    overflow: hidden;
    border: 1px solid rgba(255,255,255,0.1);
    transform-origin: bottom right;
    animation: scaleIn 0.3s ease;
}

.chat-box.active {
    display: flex;
}

@keyframes scaleIn {
    from { transform: scale(0); opacity: 0; }
    to { transform: scale(1); opacity: 1; }
}

.chat-header {
    background: linear-gradient(135deg, #16222A 0%, #3A6073 100%);
    padding: 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid rgba(255,255,255,0.1);
}

.bot-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
}

.chat-messages {
    flex: 1;
    padding: 15px;
    overflow-y: auto;
    background: #0f0f1a;
    display: flex;
    flex-direction: column;
    gap: 15px;
}

/* Custom scrollbar */
.chat-messages::-webkit-scrollbar { width: 6px; }
.chat-messages::-webkit-scrollbar-track { background: transparent; }
.chat-messages::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.2); border-radius: 10px; }

.message {
    max-width: 85%;
    display: flex;
    flex-direction: column;
}

.user-message {
    align-self: flex-end;
}

.bot-message {
    align-self: flex-start;
}

.message-content {
    padding: 10px 14px;
    border-radius: 15px;
    font-size: 14px;
    line-height: 1.4;
    word-wrap: break-word;
}

.user-message .message-content {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-bottom-right-radius: 4px;
}

.bot-message .message-content {
    background: #2a2a40;
    color: #e0e0e0;
    border-bottom-left-radius: 4px;
    border: 1px solid rgba(255,255,255,0.05);
}

/* Typing indicator */
.typing-indicator {
    display: flex;
    gap: 4px;
    padding: 14px 18px;
    background: #2a2a40;
    border-radius: 15px;
    border-bottom-left-radius: 4px;
    width: fit-content;
}

.typing-dot {
    width: 6px;
    height: 6px;
    background: #8892b0;
    border-radius: 50%;
    animation: typing 1.4s infinite ease-in-out both;
}

.typing-dot:nth-child(1) { animation-delay: -0.32s; }
.typing-dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
    0%, 80%, 100% { transform: scale(0); }
    40% { transform: scale(1); }
}

/* Interactive Actions (JSON payload) */
.action-card {
    background: rgba(102, 126, 234, 0.1);
    border: 1px solid #667eea;
    border-radius: 10px;
    padding: 12px;
    margin-top: 10px;
}

.action-btn {
    width: 100%;
    margin-top: 8px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    padding: 8px;
    color: white;
    border-radius: 6px;
    font-weight: 600;
    font-size: 13px;
    transition: opacity 0.3s;
}

.action-btn:hover { opacity: 0.9; }

.chat-input-area {
    padding: 15px;
    background: #1a1a2e;
    border-top: 1px solid rgba(255,255,255,0.1);
}

.chat-input-area .form-control {
    background: rgba(255,255,255,0.05);
    border: 1px solid rgba(255,255,255,0.1);
    color: white;
}

.chat-input-area .form-control:focus {
    background: rgba(255,255,255,0.08);
    border-color: #667eea;
    box-shadow: none;
    color: white;
}

.chat-input-area .btn-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
}

.hover-white:hover {
    color: white !important;
}

@media (max-width: 480px) {
    .chat-box {
        width: calc(100vw - 40px);
        bottom: 90px;
        right: 20px;
        height: 60vh;
    }
}
</style>

<script>
    // Context Path from global or default logic
    const chatContextPath = '${pageContext.request.contextPath}';
    console.log("Cine AI Widget initialized with context path:", chatContextPath);

    function toggleChat() {
        const chatBox = document.getElementById('chatBox');
        chatBox.classList.toggle('active');
        if(chatBox.classList.contains('active')) {
            document.getElementById('chatBadge').style.display = 'none';
            document.getElementById('chatInput').focus();
        }
    }

    function appendMessage(sender, content, isHtml = false) {
        const chatMessages = document.getElementById('chatMessages');
        const msgDiv = document.createElement('div');
        msgDiv.className = 'message ' + (sender === 'user' ? 'user-message' : 'bot-message');
        
        const contentDiv = document.createElement('div');
        contentDiv.className = 'message-content';
        
        if (isHtml) {
            contentDiv.innerHTML = content;
        } else {
            contentDiv.textContent = content; // format using markdown optionally if library available
        }
        
        msgDiv.appendChild(contentDiv);
        chatMessages.appendChild(msgDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
        return contentDiv; // Return reference for appending streaming text
    }

    function showTyping() {
        const chatMessages = document.getElementById('chatMessages');
        const typingDiv = document.createElement('div');
        typingDiv.className = 'message bot-message typing-indicator-wrapper';
        typingDiv.id = 'typingIndicator';
        typingDiv.innerHTML = `
            <div class="typing-indicator">
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
            </div>
        `;
        chatMessages.appendChild(typingDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function removeTyping() {
        const typingItem = document.getElementById('typingIndicator');
        if (typingItem) typingItem.remove();
    }

    async function handleChatSubmit(e) {
        e.preventDefault();
        const input = document.getElementById('chatInput');
        const message = input.value.trim();
        if (!message) return;

        input.value = '';
        appendMessage('user', message);
        document.getElementById('sendBtn').disabled = true;
        
        // Start streaming logic
        showTyping();
        
        try {
            // Initiate SSE connection
            const response = await fetch(chatContextPath + '/ChatServlet/stream', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({ 'message': message })
            });

            if (!response.ok) {
                const errorText = await response.text().catch(() => "Unknown error");
                throw new Error(`Server returned status \${response.status}: \${errorText}`);
            }

            removeTyping();
            
            // Render a placeholder bot message for streaming text
            const botMessageContainer = appendMessage('bot', '');
            let accumulatedJson = ""; // For structured JSON fallback

            // Read SSE stream
            const reader = response.body.getReader();
            const decoder = new TextDecoder('utf-8');
            let done = false;
            let buffer = ""; // Buffer for partial lines

            while (!done) {
                const { value, done: readerDone } = await reader.read();
                done = readerDone;
                if (value) {
                    buffer += decoder.decode(value, { stream: !done });
                    const lines = buffer.split('\n');
                    
                    // Keep the last partial line in the buffer
                    buffer = lines.pop();

                    for (const line of lines) {
                        const trimmedLine = line.trim();
                        if (!trimmedLine || !trimmedLine.startsWith('data: ')) continue;
                        
                        const dataStr = trimmedLine.substring(6);
                        try {
                            const parsed = JSON.parse(dataStr);
                            if (parsed.status === 'complete') {
                                checkStructuredJson(accumulatedJson, botMessageContainer);
                            } else if (parsed.token) {
                                const rawToken = parsed.token.replace(/\\n/g, '\n').replace(/\\r/g, '\r');
                                accumulatedJson += rawToken;
                                botMessageContainer.innerHTML += rawToken.replace(/\n/g, '<br>');
                                document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;
                            } else if (parsed.error) {
                                botMessageContainer.innerHTML += `<div class="alert alert-danger p-2 small mt-2">AI Error: ${parsed.error}</div>`;
                            }
                        } catch (err) {
                            console.warn("SSE JSON parse error", err, dataStr);
                        }
                    }
                }
            }
            
            // Process any remaining data in buffer
            if (buffer.trim().startsWith('data: ')) {
                 const dataStr = buffer.trim().substring(6);
                 try {
                     const parsed = JSON.parse(dataStr);
                     if (parsed.token) {
                         botMessageContainer.innerHTML += parsed.token.replace(/\\n/g, '\n').replace(/\n/g, '<br>');
                     }
                 } catch(e) {}
            }
            
        } catch (error) {
            removeTyping();
            appendMessage('bot', 'Xin lỗi, đã có lỗi kết nối tới Server. Vui lòng thử lại sau.', false);
            console.error('Lỗi khi gửi chat:', error);
        } finally {
            document.getElementById('sendBtn').disabled = false;
            document.getElementById('chatInput').focus();
        }
    }

    // Function to handle interactive JSON actions
    function checkStructuredJson(fullText, containerNode) {
        fullText = fullText.trim();
        if (fullText.startsWith('{') && fullText.endsWith('}')) {
            try {
                const data = JSON.parse(fullText);
                if (data.actionType === 'BOOKING_CONFIRM') {
                    // It's an interactive action, replace the raw JSON text with UI
                    const details = data.details || {};
                    const html = `
                        <div class="mb-2">Đây là thông tin xác nhận đặt vé của bạn:</div>
                        <div class="action-card">
                            <div class="small mb-1"><strong>Phim:</strong> \${details.movieName || 'N/A'}</div>
                            <div class="small mb-1"><strong>Suất:</strong> \${details.showTime || 'N/A'} - \${details.showDate || 'N/A'}</div>
                            <div class="small mb-1"><strong>Ghế:</strong> \${details.seats || 'N/A'}</div>
                            <div class="small mb-1"><strong>Tổng tiền:</strong> \${details.totalPrice || '0'} VNĐ</div>
                            
                            <button class="action-btn" onclick="executeSilentAction('Tôi xác nhận đặt vé cho phim \${details.movieName}')">
                                Xác Nhận & Thanh Toán <i class="fas fa-check-circle ms-1"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-secondary w-100 mt-2" onclick="executeSilentAction('Hủy quá trình đặt vé')">
                                Hủy Bỏ
                            </button>
                        </div>
                    `;
                    containerNode.innerHTML = html;
                }
            } catch (e) {
                // Not valid JSON, leave as text
            }
        }
    }

    async function executeSilentAction(message) {
        const input = document.getElementById('chatInput');
        input.value = message;
        document.getElementById('chatForm').dispatchEvent(new Event('submit'));
    }

    async function resetChat() {
        try {
            const resp = await fetch(chatContextPath + '/ChatServlet/reset', { method: 'POST' });
            if (resp.ok) {
                const msgList = document.getElementById('chatMessages');
                msgList.innerHTML = `
                    <div class="message bot-message">
                        <div class="message-content">
                            Phiên hội thoại đã được làm mới. Bạn cần hỗ trợ gì tiếp theo?
                        </div>
                    </div>`;
            }
        } catch(e) {
            console.error('Reset failed', e);
        }
    }
</script>
</file>

<file path="src/main/java/dao/ChatMessageDAO.java">
package dao;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom ChatMemoryStore that persists messages to SQL Server database.
 * Auto-called by LangChain4j when managing chat memory.
 */
public class ChatMessageDAO implements ChatMemoryStore {

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        List<ChatMessage> history = new ArrayList<>();
        
        // Retrieve the last 20 messages, ordered oldest to newest for LangChain context window
        String sql = "SELECT role, content FROM (" +
                     "   SELECT TOP 20 role, content, created_at FROM chat_messages " +
                     "   WHERE session_id = ? ORDER BY created_at DESC" +
                     ") AS recent_msgs ORDER BY created_at ASC";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sessionId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String role = rs.getString("role");
                    String content = rs.getString("content");
                    
                    if ("user".equalsIgnoreCase(role)) {
                        history.add(new UserMessage(content));
                    } else if ("assistant".equalsIgnoreCase(role)) {
                        history.add(new AiMessage(content));
                    } else if ("system".equalsIgnoreCase(role)) {
                        history.add(new SystemMessage(content));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading chat memory from DB: " + e.getMessage());
        }
        
        return history;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String sessionId = memoryId.toString();
        Integer userId = extractUserIdFromSessionId(sessionId); // Or null if unavailable here
        
        // Logic: The most efficient way is to append ONLY the newest message. 
        // Langchain provides the FULL list. We need to persist only what's missing, OR just the last message.
        // For simplicity and to avoid duplicates, we can clear the session and re-insert the list, 
        // OR better: we handle insert manually in Servlet, but wait, ChatMemoryStore is meant to sync.
        
        // Actually, deleting old and re-inserting is safest for window sliding (it keeps only 20 in DB too).
        // Let's implement full sync to keep the window perfectly mirrored in DB.
        
        String deleteSql = "DELETE FROM chat_messages WHERE session_id = ?";
        String insertSql = "INSERT INTO chat_messages (session_id, user_id, role, content) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return;
            conn.setAutoCommit(false); // Transaction
            
            try {
                try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                    psDel.setString(1, sessionId);
                    psDel.executeUpdate();
                }
                
                try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                    for (ChatMessage msg : messages) {
                        psIns.setString(1, sessionId);
                        if (userId != null && userId > 0) {
                            psIns.setInt(2, userId);
                        } else {
                            psIns.setNull(2, java.sql.Types.INTEGER);
                        }
                        
                        psIns.setString(3, msg.type().name().toLowerCase());
                        String text = (msg.text() != null) ? msg.text() : "";
                        psIns.setNString(4, text);
                        psIns.addBatch();
                    }
                    psIns.executeBatch();
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error syncing chat memory to DB: " + e.getMessage());
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        String sql = "DELETE FROM chat_messages WHERE session_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting chat memory from DB: " + e.getMessage());
        }
    }
    
    // Utility to map a session pattern to a user ID if needed, though typically session_id alone is fine
    private Integer extractUserIdFromSessionId(String sessionId) {
        if (sessionId.contains("_User_")) {
            try {
                return Integer.parseInt(sessionId.split("_User_")[1]);
            } catch (Exception e) {}
        }
        return null;
    }
}
</file>

<file path="src/main/java/ai/CineAgentProvider.java">
package ai;

import ai.skills.admin.AnalystBotSkills;
import ai.skills.admin.MarketingBotSkills;
import ai.skills.admin.ModerateBotSkills;
import ai.skills.user.BookBotSkills;
import ai.skills.user.InfoBotSkills;
import dao.ChatMessageDAO;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import utils.ConfigLoader;

/**
 * Lớp cung cấp các Agent chuyên biệt sử dụng LangChain4j.
 */
public class CineAgentProvider {

    private static final String API_KEY = ConfigLoader.get("ai.api.key");
    private static final String MODEL_NAME = "llama-3.3-70b-versatile";
    private static final String GROQ_URL = "https://api.groq.com/openai/v1";

    /**
     * Giao diện chung cho các AI Agent.
     */
    public interface CineAgent {
        String chat(@MemoryId String memoryId, @UserMessage String userMessage);
    }

    /**
     * Giao diện cho Streaming AI Agent.
     */
    public interface StreamingCineAgent {
        dev.langchain4j.service.TokenStream chat(@MemoryId String memoryId, @UserMessage String userMessage);
    }

    /**
     * Khởi tạo Agent cho người dùng cuối (Customer).
     */
    public static CineAgent createUserAgent() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
                .build();

        return AiServices.builder(CineAgent.class)
                .chatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new InfoBotSkills(), new BookBotSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineGuide, một trợ lý rạp phim thông minh. " +
                    "Bạn có 2 bộ phận hỗ trợ:\n" +
                    "1. InfoBot: Tra cứu phim, suất chiếu, giá combo.\n" +
                    "2. BookBot: Hỗ trợ đặt vé, xem lịch sử và chuẩn bị xác nhận đặt vé.\n" +
                    "Khi khách hàng muốn đặt vé, hãy dùng BookBot để lấy sơ đồ ghế, sau đó dùng tool prepareBooking để hiển thị xác nhận."
                )
                .build();
    }

    /**
     * Khởi tạo Agent cho quản trị viên (Admin).
     */
    public static CineAgent createAdminAgent() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
                .build();

        return AiServices.builder(CineAgent.class)
                .chatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new AnalystBotSkills(), new MarketingBotSkills(), new ModerateBotSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineAnalyst, trợ lý quản trị cấp cao. " +
                    "Bạn điều hành 3 chuyên gia:\n" +
                    "1. AnalystBot: Thống kê doanh thu, vé và dự báo.\n" +
                    "2. MarketingBot: Tạo nội dung quảng cáo dựa trên dữ liệu phim.\n" +
                    "3. ModerateBot: Kiểm soát người dùng, logs hệ thống và trạng thái phòng.\n" +
                    "Hãy cung cấp báo cáo chuyên sâu và chuyên nghiệp."
                )
                .build();
    }

    /**
     * Khởi tạo Streaming Agent cho người dùng cuối.
     */
    public static StreamingCineAgent createStreamingUserAgent() {
        dev.langchain4j.model.chat.StreamingChatLanguageModel model = dev.langchain4j.model.openai.OpenAiStreamingChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
                .build();

        return AiServices.builder(StreamingCineAgent.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new InfoBotSkills(), new BookBotSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineGuide, một trợ lý rạp phim thông minh. Giúp người dùng tra cứu phim, lịch chiếu và đặt vé qua InfoBot và BookBot."
                )
                .build();
    }

    /**
     * Khởi tạo Streaming Agent cho quản trị viên.
     */
    public static StreamingCineAgent createStreamingAdminAgent() {
        dev.langchain4j.model.chat.StreamingChatLanguageModel model = dev.langchain4j.model.openai.OpenAiStreamingChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(GROQ_URL)
                .modelName(MODEL_NAME)
                .build();

        return AiServices.builder(StreamingCineAgent.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(30)
                        .chatMemoryStore(new ChatMessageDAO())
                        .build())
                .tools(new AnalystBotSkills(), new MarketingBotSkills(), new ModerateBotSkills())
                .systemMessageProvider(chatId -> 
                    "Bạn là CineAnalyst, trợ lý quản trị cấp cao. Hỗ trợ thống kê, marketing và điều hành hệ thống rạp phim."
                )
                .build();
    }
}
</file>

<file path="pom.xml">
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.mycompany</groupId>
    <artifactId>CinemaManagement</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>CinemaManagement</name>
    
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <jakartaee>10.0.0</jakartaee>
    </properties>
    
    <dependencies>

        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-api</artifactId>
            <version>${jakartaee}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <version>12.6.1.jre11</version>
            <!--  Chạy được trên JDK 11+ (bao gồm JDK 21)  -->
        </dependency>
        <dependency>
            <groupId>jakarta.servlet.jsp</groupId>
            <artifactId>jakarta.servlet.jsp-api</artifactId>
            <version>3.1.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>6.0.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>2.0.0</version>
        </dependency>
         <!-- Jackson JSON -->
        <dependency>
          <groupId>com.fasterxml.jackson.core</groupId>
          <artifactId>jackson-databind</artifactId>
          <version>2.17.1</version>
        </dependency>
        <!-- LangChain4j -->
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j</artifactId>
            <version>0.36.2</version>
        </dependency>
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-open-ai</artifactId>
            <version>0.36.2</version>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>5.15.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>5.15.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.platform</groupId>
            <artifactId>junit-platform-launcher</artifactId>
            <version>1.11.4</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.2</version>
                <configuration>
                    <argLine>-Dnet.bytebuddy.experimental=true</argLine>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <parameters>true</parameters>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.3.2</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals><goal>copy</goal></goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>com.heroku</groupId>
                                    <artifactId>webapp-runner</artifactId>
                                    <version>10.1.36.0</version>
                                    <destFileName>webapp-runner.jar</destFileName>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
</file>

<file path="src/main/java/controller/ChatServlet.java">
package controller;

import ai.CineAgentProvider;
import model.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet xử lý Chatbot sử dụng kiến trúc AI Agent mới.
 */
@WebServlet(urlPatterns = "/ChatServlet/*", asyncSupported = true)
public class ChatServlet extends HttpServlet {

    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            objectMapper = new ObjectMapper();
            log("ChatServlet (Agentic) initialized successfully");
        } catch (Exception e) {
            log("Error initializing ChatServlet", e);
            throw new ServletException("Failed to initialize ChatServlet", e);
        }
    }

    /**
     * Lấy hoặc tạo AI Agent cho session hiện tại.
     * Phân biệt AdminAgent và UserAgent dựa trên role của user.
     */
    private CineAgentProvider.CineAgent getOrCreateAgent(HttpSession session) {
        CineAgentProvider.CineAgent agent = (CineAgentProvider.CineAgent) session.getAttribute("aiAgent");

        if (agent == null) {
            UserDTO user = (UserDTO) session.getAttribute("user");
            // Mặc định là UserAgent, nếu role là Admin thì dùng AdminAgent
            if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
                agent = CineAgentProvider.createAdminAgent();
            } else {
                agent = CineAgentProvider.createUserAgent();
            }
            session.setAttribute("aiAgent", agent);
            log("Created new AI Agent (" + (user != null ? user.getRoleId() : "Guest") + ") for session: " + session.getId());
        }

        return agent;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[AI-DEBUG] ChatServlet POST request received");
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String pathInfo = request.getPathInfo();
        
        System.out.println("[AI-DEBUG] RequestURI: " + requestURI + ", PathInfo: " + pathInfo);
        log("ChatServlet POST: requestURI=" + requestURI + ", pathInfo=" + pathInfo);

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/chat")) {
            handleChat(request, response);
        } else if (pathInfo.startsWith("/reset")) {
            handleReset(request, response);
        } else if (pathInfo.startsWith("/stream")) {
            handleStreamChat(request, response);
        } else {
            handleChat(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    private void handleChat(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String userMessage = request.getParameter("message");

            if (userMessage == null || userMessage.trim().isEmpty()) {
                sendErrorResponse(out, "Message không được để trống");
                return;
            }

            HttpSession session = request.getSession(true);
            CineAgentProvider.CineAgent agent = getOrCreateAgent(session);

            long startTime = System.currentTimeMillis();
            // Gọi AI Agent xử lý (AI sẽ tự động gọi Tool nếu cần)
            String reply = agent.chat(session.getId(), userMessage);
            long endTime = System.currentTimeMillis();

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            
            // Hỗ trợ Structured Response (JSON trong reply)
            if (reply.trim().startsWith("{") && reply.trim().endsWith("}")) {
                try {
                    JsonNode structured = objectMapper.readTree(reply);
                    if (structured.has("actionType")) {
                        jsonResponse.set("action", structured);
                        jsonResponse.put("reply", structured.has("message") ? structured.get("message").asText() : "Yêu cầu hành động từ hệ thống.");
                    } else {
                        jsonResponse.put("reply", reply);
                    }
                } catch (Exception e) {
                    jsonResponse.put("reply", reply);
                }
            } else {
                jsonResponse.put("reply", reply);
            }

            jsonResponse.put("processingTime", endTime - startTime);
            jsonResponse.put("sessionId", session.getId());

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();

            log("Processed Agent message in " + (endTime - startTime) + "ms");

        } catch (Exception e) {
            log("Error processing chat message", e);
            sendErrorResponse(out, "Đã xảy ra lỗi hệ thống AI: " + e.getMessage());
        }
    }

    private CineAgentProvider.StreamingCineAgent getOrCreateStreamingAgent(HttpSession session) {
        CineAgentProvider.StreamingCineAgent agent = (CineAgentProvider.StreamingCineAgent) session.getAttribute("aiStreamingAgent");

        if (agent == null) {
            UserDTO user = (UserDTO) session.getAttribute("user");
            if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
                agent = CineAgentProvider.createStreamingAdminAgent();
            } else {
                agent = CineAgentProvider.createStreamingUserAgent();
            }
            session.setAttribute("aiStreamingAgent", agent);
            log("Created new Streaming AI Agent (" + (user != null ? user.getRoleId() : "Guest") + ") for session: " + session.getId());
        }

        return agent;
    }

    private void handleStreamChat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        PrintWriter out = response.getWriter();
        String userMessage = request.getParameter("message");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            out.print("event: error\ndata: Message cannot be empty\n\n");
            out.flush();
            return;
        }

        HttpSession session = request.getSession(true);
        CineAgentProvider.StreamingCineAgent agent = getOrCreateStreamingAgent(session);

        // Bật AsyncContext để không block thread của Tomcat/Servlet
        jakarta.servlet.AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(120000); // 2 minutes timeout for slow AI
        
        // Cần lấy writer từ asyncContext để đảm bảo an toàn trong môi trường async
        PrintWriter asyncOut = asyncContext.getResponse().getWriter();

        try {
            dev.langchain4j.service.TokenStream tokenStream = agent.chat(session.getId(), userMessage);

            tokenStream
                .onNext(token -> {
                    try {
                        log("[STREAM] Received token: " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
                        // Escape newline and quotes for JSON safety
                        String cleanToken = token.replace("\\", "\\\\")
                                               .replace("\n", "\\n")
                                               .replace("\r", "\\r")
                                               .replace("\"", "\\\"");
                        asyncOut.print("data: {\"token\": \"" + cleanToken + "\"}\n\n");
                        asyncOut.flush();
                    } catch (Exception e) {
                        log("Error sending token", e);
                    }
                })
                .onComplete(aiResponse -> {
                    try {
                        AiMessage aiMessage = aiResponse.content();
                        log("[STREAM] Completed successfully. Response length: " + (aiMessage != null ? aiMessage.text().length() : 0));
                        asyncOut.print("data: {\"status\": \"complete\"}\n\n");
                        asyncOut.flush();
                        asyncContext.complete();
                    } catch (Exception e) {
                        log("Error completing stream", e);
                    }
                })
                .onError(error -> {
                    try {
                        String errMsg = error.getMessage() != null ? error.getMessage() : "Unknown AI Error";
                        log("[STREAM-ERROR] AI Stream Error: " + errMsg);
                        error.printStackTrace(); // Log full stack trace to server console
                        
                        String cleanErr = errMsg.replace("\\", "\\\\").replace("\n", " ").replace("\"", "\\\"");
                        asyncOut.print("data: {\"error\": \"" + cleanErr + "\"}\n\n");
                        asyncOut.flush();
                        asyncContext.complete();
                    } catch (Exception e) {
                        log("Error sending error stream", e);
                    }
                })
                .start();
        } catch (Exception e) {
            log("Error starting stream", e);
            asyncOut.print("data: {\"error\": \"Internal Server Error: " + e.getMessage().replace("\"", "'") + "\"}\n\n");
            asyncOut.flush();
            asyncContext.complete();
        }
    }

    private void handleReset(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.removeAttribute("aiAgent");
                session.removeAttribute("aiStreamingAgent");
                log("Reset AI Agent for session: " + session.getId());
            }

            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Phiên thảo luận AI đã được làm mới");

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();

        } catch (Exception e) {
            log("Error resetting agent", e);
            sendErrorResponse(out, "Lỗi khi reset: " + e.getMessage());
        }
    }

    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        try {
            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("success", false);
            jsonResponse.put("error", errorMessage);

            out.print(objectMapper.writeValueAsString(jsonResponse));
            out.flush();
        } catch (Exception e) {
            log("Error sending error response", e);
        }
    }

    @Override
    public void destroy() {
        log("ChatServlet destroyed");
        super.destroy();
    }
}
</file>

</files>
