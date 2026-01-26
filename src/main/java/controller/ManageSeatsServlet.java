package controller;

import jakarta.servlet.annotation.WebServlet;
import service.SeatService;
import service.CinemaHallService;
import dao.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Seat;
import model.SeatType;
import model.CinemaHall;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
@WebServlet("/admin/halls/seats")
public class ManageSeatsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        try {
            int hallId = Integer.parseInt(req.getParameter("hallId"));
            
            try (Connection conn = DBConnect.getConnection()) {
                SeatService seatService = new SeatService(conn);
                CinemaHallService hallService = new CinemaHallService(conn);
                
                // Lấy thông tin phòng
                CinemaHall hall = hallService.getHallById(hallId);
                
                // Lấy danh sách ghế
                List<Seat> seats = seatService.getSeatsByHall(hallId);
                
                // Lấy danh sách loại ghế
                List<SeatType> seatTypes = seatService.getAllSeatTypes();
                
                req.setAttribute("hall", hall);
                req.setAttribute("seats", seats);
                req.setAttribute("seatTypes", seatTypes);

                req.getRequestDispatcher("/views/admin/seats/manage-seats.jsp")
                        .forward(req, resp);

            }
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID phòng không hợp lệ");
        } catch (Exception e) {
            throw new ServletException("Lỗi khi tải quản lý ghế", e);
        }
    }
}