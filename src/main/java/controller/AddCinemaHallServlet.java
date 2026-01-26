package controller;

import jakarta.servlet.annotation.WebServlet;
import service.CinemaHallService;
import service.SeatService;
import dao.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SeatType;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
@WebServlet("/admin/halls/add")
public class AddCinemaHallServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        try (Connection conn = DBConnect.getConnection()) {
            SeatService seatService = new SeatService(conn);
            
            // Lấy danh sách loại ghế để chọn loại ghế mặc định
            List<SeatType> seatTypes = seatService.getAllSeatTypes();
            req.setAttribute("seatTypes", seatTypes);
            
            req.getRequestDispatcher("/views/admin/halls/add-hall.jsp").forward(req, resp);
            
        } catch (Exception e) {
            throw new ServletException("Lỗi khi tải trang thêm phòng", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        try {
            String hallName = req.getParameter("hallName");
            int rows = Integer.parseInt(req.getParameter("rows"));
            int cols = Integer.parseInt(req.getParameter("cols"));
            int defaultSeatTypeId = Integer.parseInt(req.getParameter("defaultSeatType"));
            
            try (Connection conn = DBConnect.getConnection()) {
                CinemaHallService hallService = new CinemaHallService(conn);
                SeatService seatService = new SeatService(conn);
                
                // Tạo phòng
                int hallId = hallService.createHall(hallName, rows, cols);
                
                // Tự động tạo ghế
                seatService.generateSeatsForHall(hallId, rows, cols, defaultSeatTypeId);
                
                req.getSession().setAttribute("successMsg", "Thêm phòng chiếu '" + hallName + "' thành công!");
                resp.sendRedirect(req.getContextPath() + "/admin/halls");
            }
            
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "Dữ liệu nhập vào không hợp lệ");
            doGet(req, resp);
            
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMsg", e.getMessage());
            doGet(req, resp);
            
        } catch (Exception e) {
            req.setAttribute("errorMsg", "Lỗi hệ thống: " + e.getMessage());
            doGet(req, resp);
        }
    }
}