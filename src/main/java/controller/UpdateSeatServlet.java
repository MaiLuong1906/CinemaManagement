package controller;

import dao.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.SeatService;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/seats/update")
public class UpdateSeatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String[] seatIdArr = req.getParameterValues("seatIds");
            if (seatIdArr == null || seatIdArr.length == 0) {
                resp.sendRedirect(req.getHeader("Referer"));
                return;
            }

            String seatTypeIdStr = req.getParameter("seatTypeId");
            String seatStatusStr = req.getParameter("seatStatus");
            int hallId = Integer.parseInt(req.getParameter("hallId"));

            try (Connection conn = DBConnect.getConnection()) {
                SeatService service = new SeatService(conn);

                List<Integer> seatIds = new ArrayList<>();
                for (String id : seatIdArr) {
                    seatIds.add(Integer.parseInt(id));
                }

                if (seatTypeIdStr != null && !seatTypeIdStr.isEmpty()) {
                    service.updateSeatTypeBulk(seatIds, Integer.parseInt(seatTypeIdStr));
                }

                if (seatStatusStr != null && !seatStatusStr.isEmpty()) {
                    boolean active = seatStatusStr.equals("1");
                    service.updateSeatStatusBulk(seatIds, active);
                }
            }

            resp.sendRedirect(req.getContextPath() + "/admin/halls/seats?hallId=" + hallId);

        } catch (Exception e) {
            throw new ServletException("Lỗi cập nhật ghế", e);
        }
    }
}
