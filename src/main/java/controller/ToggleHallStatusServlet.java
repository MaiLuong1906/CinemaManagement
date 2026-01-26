package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CinemaHallService;
import dao.DBConnect;
import java.io.IOException;
import java.sql.Connection;

public class ToggleHallStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int hallId = Integer.parseInt(req.getParameter("hallId"));
            boolean status = Boolean.parseBoolean(req.getParameter("status"));

            try (Connection conn = DBConnect.getConnection()) {
                CinemaHallService hallService = new CinemaHallService(conn);
                hallService.toggleHallStatus(hallId, status);

                req.getSession().setAttribute("successMsg",
                        status ? "Đã kích hoạt phòng chiếu" : "Đã tắt phòng chiếu");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/halls");

        } catch (Exception e) {
            req.getSession().setAttribute("errorMsg", "Lỗi: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/halls");
        }
    }
}