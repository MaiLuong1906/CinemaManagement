package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Product;
import service.ProductService;
import service.CartService;

import java.io.IOException;
import java.util.List;

@WebServlet("/product")
public class CustomerProductServlet extends HttpServlet {

    private ProductService productService;
    private CartService cartService;
    private dao.SeatDAO seatDAO;

    @Override
    public void init() {
        productService = new ProductService();
        cartService = new CartService();
        seatDAO = new dao.SeatDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Product> products = productService.findAll();

            // DEBUG
            System.out.println("===== CustomerProductServlet =====");
            System.out.println("Số lượng sản phẩm: " + products.size());

            request.setAttribute("products", products);

            // Load cart details for sidebar
            // Load cart details for sidebar
            request.setAttribute(
                    "cartDetails",
                    cartService.getCartDetails(request.getSession()));

            // Re-hydrate Seat Details from Session IDs
            HttpSession session = request.getSession();
            String seatIdsStr = (String) session.getAttribute("BOOKING_SEAT_IDS");
            String showtimeIdStr = (String) session.getAttribute("BOOKING_SHOWTIME_ID");

            if (seatIdsStr != null && showtimeIdStr != null) {
                try {
                    int showtimeId = Integer.parseInt(showtimeIdStr);
                    java.util.List<model.SeatSelectionDTO> allSeats = seatDAO.getSeatsByShowtime(showtimeId);
                    java.util.List<model.SeatSelectionDTO> selectedSeats = new java.util.ArrayList<>();
                    String[] ids = seatIdsStr.split(",");
                    for (String id : ids) {
                        try {
                            int seatId = Integer.parseInt(id.trim());
                            for (model.SeatSelectionDTO s : allSeats) {
                                if (s.getSeatId() == seatId) {
                                    selectedSeats.add(s);
                                    break;
                                }
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    session.setAttribute("cartSeats", selectedSeats);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            request.getRequestDispatcher("/views/user/product.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra");
            request.getRequestDispatcher("/views/user/product.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
