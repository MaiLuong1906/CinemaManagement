package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CinemaHallService;
import dao.DBConnect;
import java.io.IOException;
import java.sql.Connection;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "ToggleHallStatusServlet", urlPatterns = { "/admin/halls/toggle-status" })
public class ToggleHallStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String hallIdStr = req.getParameter("hallId");
            String statusStr = req.getParameter("status");
            System.out
                    .println("DEBUG: ToggleHallStatusServlet - hallIdStr: " + hallIdStr + ", statusStr: " + statusStr);

            int hallId = Integer.parseInt(hallIdStr);
            boolean status = Boolean.parseBoolean(statusStr);
            System.out.println("DEBUG: Parsed - hallId: " + hallId + ", status: " + status);

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