package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/hold-seats")
public class HoldSeatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Lấy dữ liệu từ form chọn ghế
        String showtimeId = request.getParameter("showtimeId");
        String seatIds = request.getParameter("seatIds");
        String totalPrice = request.getParameter("totalPrice");

        // 2. Lưu vào Session (Bộ nhớ tạm)
        HttpSession session = request.getSession();
        session.setAttribute("BOOKING_SHOWTIME_ID", showtimeId);
        session.setAttribute("BOOKING_SEAT_IDS", seatIds);
        session.setAttribute("BOOKING_SEAT_TOTAL", totalPrice);

        // DEBUG: In ra console để kiểm tra
        System.out.println("=== HoldSeatServlet ===");
        System.out.println("SeatIDs: " + seatIds);
        System.out.println("Redirecting to /product...");

        // 3. Chuyển hướng sang trang chọn sản phẩm
        response.sendRedirect(request.getContextPath() + "/product");
    }
}
