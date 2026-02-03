package controller;

import dao.InvoiceDAO;
import dao.SeatDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.SeatSelectionDTO;

@WebServlet("/seat-selection")
public class SeatController extends BaseServlet {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private SeatDAO seatDAO = new SeatDAO();

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int showtimeId = getIntParam(request, "showtimeId");

        // Cleanup expired invoices
        invoiceDAO.cleanupExpiredInvoices();

        // Load seats for showtime
        List<SeatSelectionDTO> seats = seatDAO.getSeatsByShowtime(showtimeId);

        request.setAttribute("seats", seats);
        request.setAttribute("showtimeId", showtimeId);

        forward(request, response, "/views/user/seat-selection.jsp");
    }
}
