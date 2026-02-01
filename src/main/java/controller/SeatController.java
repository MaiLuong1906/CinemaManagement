package controller;

import dao.InvoiceDAO;
import dao.SeatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.SeatSelectionDTO;

@WebServlet("/seat-selection")
public class SeatController extends HttpServlet {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private SeatDAO seatDAO = new SeatDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int showtimeId = Integer.parseInt(request.getParameter("showtimeId"));

        // BƯỚC 2.2 – CLEANUP
        invoiceDAO.cleanupExpiredInvoices();

        // BƯỚC 2.3 – LOAD GHẾ
        List<SeatSelectionDTO> seats =
                seatDAO.getSeatsByShowtime(showtimeId);

        request.setAttribute("seats", seats);
        request.setAttribute("showtimeId", showtimeId);

        request.getRequestDispatcher("/views/user/seat-selection.jsp")
               .forward(request, response);
    }
}
