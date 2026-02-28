package controller;

import dao.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CinemaHall;
import model.SeatType;
import service.CinemaHallService;
import service.SeatService;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/admin/halls")
public class CinemaHallServlet extends BaseServlet {

    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String action = getStringParam(req, "action", "list");

        switch (action) {
            case "list":
                listHalls(req, resp);
                break;
            case "add":
                if ("POST".equals(req.getMethod())) {
                    addHall(req, resp);
                } else {
                    showAddForm(req, resp);
                }
                break;
            case "toggle-status":
                if ("POST".equals(req.getMethod())) {
                    toggleHallStatus(req, resp);
                }
                break;
        }
    }

    private void listHalls(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DBConnect.getConnection()) {
            CinemaHallService hallService = new CinemaHallService(conn);
            List<CinemaHall> halls = hallService.getAllHalls();
            req.setAttribute("halls", halls);
            forward(req, resp, "/views/admin/halls/list-halls.jsp");
        } catch (Exception e) {
            throw new ServletException("Lỗi khi tải danh sách phòng chiếu", e);
        }
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DBConnect.getConnection()) {
            SeatService seatService = new SeatService(conn);
            List<SeatType> seatTypes = seatService.getAllSeatTypes();
            req.setAttribute("seatTypes", seatTypes);
            forward(req, resp, "/views/admin/halls/add-hall.jsp");
        } catch (Exception e) {
            throw new ServletException("Lỗi khi tải trang thêm phòng", e);
        }
    }

    private void addHall(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            showAddForm(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMsg", e.getMessage());
            showAddForm(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMsg", "Lỗi hệ thống: " + e.getMessage());
            showAddForm(req, resp);
        }
    }

    private void toggleHallStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
