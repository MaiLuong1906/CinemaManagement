package controller;

import jakarta.servlet.annotation.WebServlet;
import service.CinemaHallService;
import dao.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CinemaHall;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
@WebServlet("/admin/halls")
public class ListCinemaHallsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        try (Connection conn = DBConnect.getConnection()) {
            CinemaHallService hallService = new CinemaHallService(conn);
            
            List<CinemaHall> halls = hallService.getAllHalls();
            req.setAttribute("halls", halls);
            
            req.getRequestDispatcher("/views/admin/halls/list-halls.jsp").forward(req, resp);
            
        } catch (Exception e) {
            throw new ServletException("Lỗi khi tải danh sách phòng chiếu", e);
        }
    }
}