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
    private dao.TicketDetailDAO ticketDetailDAO;
    private dao.ProductDAO productDAO;
    private dao.ProductDetailDAO productDetailDAO;
    private dao.ShowtimeDAO showtimeDAO;
    private dao.SeatDAO seatDAO;

    @Override
    public void init() {
        cartService = new CartService();
        invoiceDAO = new InvoiceDAO();
        ticketDetailDAO = new dao.TicketDetailDAO();
        productDAO = new dao.ProductDAO();
        productDetailDAO = new dao.ProductDetailDAO();
        showtimeDAO = new dao.ShowtimeDAO();
        seatDAO = new dao.SeatDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 1. Kiểm tra đăng nhập
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
            return;
        }

        int userId = -1;
        if (userObj instanceof model.UserDTO) {
            userId = ((model.UserDTO) userObj).getAccountId();
        }

        // 2. Lấy thông tin ghế từ Session
        String seatIdsStr = (String) session.getAttribute("BOOKING_SEAT_IDS"); // "1,2,3"
        String showtimeIdStr = (String) session.getAttribute("BOOKING_SHOWTIME_ID");

        if (seatIdsStr == null || showtimeIdStr == null || seatIdsStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Connection conn = null;
        try {
            int showtimeId = Integer.parseInt(showtimeIdStr);

            // Re-hydrate seat details to get prices if not in session, or just fetch all
            // for showtime
            // Better: use SeatDAO.getSeatsByShowtime to get prices
            java.util.List<model.SeatSelectionDTO> allSeats = seatDAO.getSeatsByShowtime(showtimeId);

            // Parse selected IDs
            String[] ids = seatIdsStr.split(",");
            java.util.List<model.SeatSelectionDTO> selectedSeatDTOs = new java.util.ArrayList<>();
            BigDecimal seatTotal = BigDecimal.ZERO;

            for (String id : ids) {
                int sId = Integer.parseInt(id.trim());
                for (model.SeatSelectionDTO dto : allSeats) {
                    if (dto.getSeatId() == sId) {
                        selectedSeatDTOs.add(dto);
                        seatTotal = seatTotal.add(BigDecimal.valueOf(dto.getPrice()));
                        break;
                    }
                }
            }

            // 3. Tính tổng tiền Cart (Products)
            Map<Product, Integer> cartDetails = cartService.getCartDetails(session);
            BigDecimal productTotal = BigDecimal.ZERO;

            for (Map.Entry<Product, Integer> entry : cartDetails.entrySet()) {
                BigDecimal price = entry.getKey().getPrice();
                BigDecimal qty = new BigDecimal(entry.getValue());
                productTotal = productTotal.add(price.multiply(qty));
            }

            BigDecimal grandTotal = seatTotal.add(productTotal);

            // 4. BẮT ĐẦU TRANSACTION
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // A. Tạo Invoice
            Invoice invoice = new Invoice();
            invoice.setUserId(userId);
            invoice.setShowtimeId(showtimeId);
            invoice.setTotalAmount(grandTotal);
            invoice.setStatus("PENDING");

            int invoiceId = invoiceDAO.insert(conn, invoice);
            invoice.setInvoiceId(invoiceId);

            // B. Lưu Ticket Details
            // Cần HallID. Lấy từ showtimeId
            model.Showtime showtime = showtimeDAO.findById(conn, showtimeId);
            int hallId = (showtime != null) ? showtime.getHallId() : 0; // fallback

            java.util.List<model.TicketDetail> tickets = new java.util.ArrayList<>();
            for (model.SeatSelectionDTO s : selectedSeatDTOs) {
                model.TicketDetail td = new model.TicketDetail();
                td.setInvoiceId(invoiceId);
                td.setSeatId(s.getSeatId());
                td.setHallId(hallId);
                td.setShowtimeId(showtimeId);
                td.setActualPrice(BigDecimal.valueOf(s.getPrice()));
                tickets.add(td);
            }
            ticketDetailDAO.insertBatch(conn, tickets);

            // C. Lưu Product Details & Giảm Tồn Kho
            for (Map.Entry<Product, Integer> entry : cartDetails.entrySet()) {
                Product p = entry.getKey();
                int qty = entry.getValue();

                // Lưu detail
                model.ProductDetail pd = new model.ProductDetail();
                pd.setInvoiceId(invoiceId);
                pd.setItemId(p.getItemId());
                pd.setQuantity(qty);
                productDetailDAO.insert(conn, pd);

                // Giảm tồn kho
                productDAO.updateStock(conn, p.getItemId(), -qty);
            }

            // COMMIT
            conn.commit();

            // 6. Forward sang payment.jsp
            java.util.List<Invoice> invoices = new java.util.ArrayList<>();
            invoices.add(invoice);
            request.setAttribute("invoices", invoices);
            request.getRequestDispatcher("/views/payment/payment.jsp").forward(request, response);

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            // Handle error (show user)
            String errorMsg = (e.getMessage() != null) ? e.getMessage() : e.toString();
            response.sendRedirect(request.getContextPath() + "/home?error=transaction_failed&msg="
                    + java.net.URLEncoder.encode(errorMsg, "UTF-8"));
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}