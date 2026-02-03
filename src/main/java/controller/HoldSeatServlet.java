package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/hold-seats")
public class HoldSeatServlet extends BaseServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Get seat selection data from form
        String showtimeId = getStringParam(request, "showtimeId");
        String seatIds = getStringParam(request, "seatIds");
        String totalPrice = getStringParam(request, "totalPrice");

        // Store in session
        HttpSession session = request.getSession();
        session.setAttribute("BOOKING_SHOWTIME_ID", showtimeId);
        session.setAttribute("BOOKING_SEAT_IDS", seatIds);
        session.setAttribute("BOOKING_SEAT_TOTAL", totalPrice);

        // Debug logging
        log("HoldSeatServlet - SeatIDs: " + seatIds + ", redirecting to /product");

        // Redirect to product selection page
        redirect(response, request.getContextPath() + "/product");
    }
}
