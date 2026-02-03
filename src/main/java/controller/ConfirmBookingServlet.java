package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.DBConnect;
import dao.InvoiceDAO;
import model.Invoice;
import model.Product;
import service.CartService;
import exception.ValidationException;
import exception.BusinessException;

@WebServlet("/confirm-booking")
public class ConfirmBookingServlet extends BaseServlet {

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
    protected boolean requiresAuthentication() {
        return true;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        int userId = getCurrentUserId(request);

        // Get booking info from session
        String seatIdsStr = (String) session.getAttribute("BOOKING_SEAT_IDS");
        String showtimeIdStr = (String) session.getAttribute("BOOKING_SHOWTIME_ID");

        if (seatIdsStr == null || showtimeIdStr == null || seatIdsStr.trim().isEmpty()) {
            throw new ValidationException("Missing booking information. Please select seats again.");
        }

        Connection conn = null;
        try {
            int showtimeId = Integer.parseInt(showtimeIdStr);

            // Start transaction early to reuse connection
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // Re-hydrate seat details to get prices
            java.util.List<model.SeatSelectionDTO> allSeats = seatDAO.getSeatsByShowtime(conn, showtimeId);

            // Parse selected seat IDs
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

            // Calculate product total
            Map<Product, Integer> cartDetails = cartService.getCartDetails(session);
            BigDecimal productTotal = BigDecimal.ZERO;

            for (Map.Entry<Product, Integer> entry : cartDetails.entrySet()) {
                BigDecimal price = entry.getKey().getPrice();
                BigDecimal qty = new BigDecimal(entry.getValue());
                productTotal = productTotal.add(price.multiply(qty));
            }

            BigDecimal grandTotal = seatTotal.add(productTotal);

            // Create invoice
            Invoice invoice = new Invoice();
            invoice.setUserId(userId);
            invoice.setShowtimeId(showtimeId);
            invoice.setTotalAmount(grandTotal);
            invoice.setStatus("PENDING");

            int invoiceId = invoiceDAO.insert(conn, invoice);
            invoice.setInvoiceId(invoiceId);

            // Save ticket details
            model.Showtime showtime = showtimeDAO.findById(conn, showtimeId);
            int hallId = (showtime != null) ? showtime.getHallId() : 0;

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

            // Save product details and update stock
            for (Map.Entry<Product, Integer> entry : cartDetails.entrySet()) {
                Product p = entry.getKey();
                int qty = entry.getValue();

                model.ProductDetail pd = new model.ProductDetail();
                pd.setInvoiceId(invoiceId);
                pd.setItemId(p.getItemId());
                pd.setQuantity(qty);
                productDetailDAO.insert(conn, pd);

                productDAO.updateStock(conn, p.getItemId(), -qty);
            }

            // Commit transaction
            conn.commit();

            // Forward to payment page
            java.util.List<Invoice> invoices = new java.util.ArrayList<>();
            invoices.add(invoice);
            request.setAttribute("invoices", invoices);
            forward(request, response, "/views/payment/payment.jsp");

        } catch (NumberFormatException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    log("Rollback failed", ex);
                }
            }
            throw new ValidationException("Invalid booking data format");
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    log("Rollback failed", ex);
                }
            }
            throw new BusinessException("Failed to create booking: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log("Connection close failed", e);
                }
            }
        }
    }
}