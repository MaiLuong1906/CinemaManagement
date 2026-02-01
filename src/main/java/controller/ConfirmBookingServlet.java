package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.DBConnect;
import dao.InvoiceDAO;
import model.Invoice;
import model.Product;
import model.Account;
import service.CartService;

@WebServlet("/confirm-booking")
public class ConfirmBookingServlet extends HttpServlet {

    private CartService cartService;
    private InvoiceDAO invoiceDAO;

    @Override
    public void init() {
        cartService = new CartService();
        invoiceDAO = new InvoiceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 1. Kiểm tra đăng nhập
        // Trong AccountServlet dùng: session.setAttribute("user", user); (UserDTO)
        Object userObj = session.getAttribute("user");

        if (userObj == null) {
            System.out.println("User not logged in, redirecting to login.");
            // Nếu chưa đăng nhập, lưu trang hiện tại để quay lại (tùy logic login của bạn)
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
            return;
        }

        int userId = -1;
        if (userObj instanceof model.UserDTO) {
            // UserDTO có thể có getUserId() hoặc getId(), cần check file model
            userId = ((model.UserDTO) userObj).getAccountId();
        } else {
            System.out.println("User object in session is not of type UserDTO: " + userObj.getClass().getName());
        }

        // 2. Lấy thông tin ghế từ Session
        String seatIds = (String) session.getAttribute("BOOKING_SEAT_IDS");
        String showtimeIdStr = (String) session.getAttribute("BOOKING_SHOWTIME_ID");
        String seatTotalPriceStr = (String) session.getAttribute("BOOKING_SEAT_TOTAL");

        // Nếu không có ghế (người dùng truy cập thẳng link này mà chưa đặt ghế)
        if (seatIds == null || showtimeIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        try {
            int showtimeId = Integer.parseInt(showtimeIdStr);
            BigDecimal seatTotal = new BigDecimal(seatTotalPriceStr.replace(",", "").replace(".", ""));

            // 3. Tính tổng tiền Cart
            Map<Product, Integer> cartDetails = cartService.getCartDetails(session);
            BigDecimal productTotal = BigDecimal.ZERO;

            for (Map.Entry<Product, Integer> entry : cartDetails.entrySet()) {
                BigDecimal price = entry.getKey().getPrice();
                BigDecimal qty = new BigDecimal(entry.getValue());
                productTotal = productTotal.add(price.multiply(qty));
            }

            BigDecimal grandTotal = seatTotal.add(productTotal);

            // 4. Tạo đối tượng Invoice
            Invoice invoice = new Invoice();
            invoice.setUserId(userId);
            invoice.setShowtimeId(showtimeId);
            invoice.setTotalAmount(grandTotal);
            invoice.setStatus("PENDING");

            // 5. Lưu xuống DB
            try (Connection conn = DBConnect.getConnection()) {
                int invoiceId = invoiceDAO.insert(conn, invoice);
                invoice.setInvoiceId(invoiceId); // Cập nhật ID cho đối tượng

                // 6. Chuẩn bị dữ liệu cho trang payment.jsp (vì nó yêu cầu list "invoices")
                java.util.List<Invoice> invoices = new java.util.ArrayList<>();
                invoices.add(invoice);
                request.setAttribute("invoices", invoices);

                request.setAttribute("grandTotal", grandTotal); // Có thể giữ hoặc không cần nếu JSP lấy từ invoice

                // Forward sang payment.jsp
                request.getRequestDispatcher("/views/payment/payment.jsp").forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/home?error=db");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Thay vì redirect, in lỗi ra màn hình để debug
            response.setContentType("text/plain");
            response.getWriter().println("LOI XAY RA: " + e.getMessage());
            e.printStackTrace(response.getWriter());
        }
    }
}
