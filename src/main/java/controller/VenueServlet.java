package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;
import service.*;
import util.MovieUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Unified servlet consolidating 9 venue/showtime/hall/seat servlets.
 * Handles all venue management operations through action-based routing.
 */
@WebServlet("/venue")
public class VenueServlet extends BaseServlet {

    private ShowtimeDAO showtimeDAO;
    private CinemaHallDAO hallDAO;
    private TimeSlotDAO timeSlotDAO;
    private MovieDAO movieDAO;

    private TimeSlotService timeSlotService;
    private SeatFillRate_ViewService seatFillService;
    private ShowtimeService showtimeService;

    @Override
    public void init() {
        showtimeDAO = new ShowtimeDAO();
        hallDAO = new CinemaHallDAO();
        timeSlotDAO = new TimeSlotDAO();
        movieDAO = new MovieDAO();
        timeSlotService = new TimeSlotService();
        seatFillService = new SeatFillRate_ViewService();
        showtimeService = new ShowtimeService();
    }

    @Override
    protected boolean requiresAuthentication() {
        return true;
    }

    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserDTO user = getCurrentUser(req);
        if (user == null || !"admin".equals(user.getRoleId())) {
            resp.sendError(403, "Admin access required");
            return;
        }

        String action = getStringParam(req, "action", "dashboard");

        switch (action) {
            case "dashboard":
                handleDashboard(req, resp);
                break;
            case "add-showtime":
                handleAddShowtime(req, resp);
                break;
            case "update-showtime":
                handleUpdateShowtime(req, resp);
                break;
            case "delete-showtime":
                handleDeleteShowtime(req, resp);
                break;
            case "halls":
                handleHalls(req, resp);
                break;
            case "add-hall":
                handleAddHall(req, resp);
                break;
            case "toggle-hall":
                handleToggleHall(req, resp);
                break;
            case "seats":
                handleSeats(req, resp);
                break;
            case "update-seat":
                handleUpdateSeat(req, resp);
                break;
            default:
                handleDashboard(req, resp);
        }
    }

    private void handleDashboard(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try {
            int numberOfShowtime = timeSlotService.countTimeSlot();
            req.setAttribute("numberOfShowtime", numberOfShowtime);

            List<SeatFillRate_ViewDTO> list = seatFillService.getSeatFillRateForShowtimeCurrentMonth();
            req.setAttribute("list", list);

            List<TimeSlotKpiDTO> listKpi = timeSlotService.getTimeSlotKpiCurrentMonth();
            req.setAttribute("listKpi", listKpi);

            List<int[]> ticketBySlot = timeSlotService.getTotalTicketsBySlotCurrentMonth();
            req.setAttribute("ticketBySlot", ticketBySlot);
        } catch (RuntimeException ex) {
            req.setAttribute("msg", ex.getMessage());
        }

        forward(req, resp, "/views/admin/statistic/showtime-management.jsp");
    }

    private void handleAddShowtime(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if ("GET".equals(req.getMethod())) {
            List<Movie> movieList = movieDAO.getAllMovies();
            req.setAttribute("movieList", movieList);

            try (Connection conn = DBConnect.getConnection()) {
                List<CinemaHall> hallList = hallDAO.getAllHalls(conn);
                req.setAttribute("hallList", hallList);

                List<TimeSlot> listChieu = timeSlotDAO.findAll(conn);
                req.setAttribute("listChieu", listChieu);
            }

            forward(req, resp, "/views/admin/movies/addShowTime.jsp");
        } else {
            HttpSession session = req.getSession();
            int movieId = getIntParam(req, "movieId");
            int hallId = getIntParam(req, "hallId");
            int startTimeId = getIntParam(req, "timeSlotId");
            LocalDate showDate = getDateParam(req, "showDate");

            Showtime showtime = new Showtime(movieId, hallId, showDate, startTimeId);

            boolean flag = true;
            try (Connection conn = DBConnect.getConnection()) {
                showtimeDAO.insert(conn, showtime);
                session.setAttribute("dbError", "");
            } catch (SQLException ex) {
                flag = false;
                if (ex.getMessage().contains("UQ_Showtime")) {
                    session.setAttribute("dbError", "Lịch chiếu bị trùng!");
                } else {
                    session.setAttribute("dbError", "Lỗi từ hệ thống SQL!");
                }
            }

            session.setAttribute("message", flag ? "Thêm phim thành công" : "Thêm phim thất bại");
            session.setAttribute("success", flag);

            redirect(resp, req.getContextPath() + "/venue?action=add-showtime");
        }
    }

    private void handleUpdateShowtime(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if ("GET".equals(req.getMethod())) {
            int showtimeId = MovieUtils.getIntParameter(req, "showtimeId");
            try (Connection conn = DBConnect.getConnection()) {
                Showtime st = showtimeDAO.findById(conn, showtimeId);
                List<Movie> movieList = movieDAO.findAll(conn);
                List<CinemaHall> hallList = hallDAO.getAllHalls(conn);
                List<TimeSlot> availableSlots = timeSlotDAO.getAvailableSlots(
                        conn, st.getHallId(), st.getShowDate(), st.getShowtimeId());

                req.setAttribute("showtime", st);
                req.setAttribute("movieList", movieList);
                req.setAttribute("hallList", hallList);
                req.setAttribute("slotList", availableSlots);
            }

            forward(req, resp, "views/admin/movies/UpdateShowtime.jsp");
        } else {
            HttpSession session = req.getSession();
            try {
                int showtimeId = MovieUtils.getIntParameter(req, "showtimeId");
                int movieId = MovieUtils.getIntParameter(req, "movieId");
                int hallId = MovieUtils.getIntParameter(req, "hallId");
                LocalDate showDate = MovieUtils.getLocalDateParameter(req, "showDate");
                int slotId = MovieUtils.getIntParameter(req, "slotId");

                Showtime showtime = new Showtime(showtimeId, movieId, hallId, showDate, slotId);
                try (Connection conn = DBConnect.getConnection()) {
                    showtimeDAO.update(conn, showtime);
                }

                session.setAttribute("flash_success", "Cập nhật suất chiếu thành công!");
            } catch (IllegalArgumentException e) {
                session.setAttribute("flash_error", e.getMessage());
            } catch (SQLException e) {
                session.setAttribute("flash_error", "Lỗi hệ thống khi cập nhật suất chiếu!");
            }

            redirect(resp, req.getContextPath() + "/ListMovieForAdmin");
        }
    }

    private void handleDeleteShowtime(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        int showtimeId = MovieUtils.getIntParameter(req, "showtimeId");

        try {
            showtimeService.deleteShowtime(showtimeId);
            session.setAttribute("flash_success", "Xóa suất chiếu thành công!");
        } catch (Exception ex) {
            session.setAttribute("flash_error", ex.getMessage());
        }

        redirect(resp, req.getContextPath() + "/ListMovieForAdmin");
    }

    private void handleHalls(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            CinemaHallService hallService = new CinemaHallService();
            List<CinemaHall> halls = hallService.getAllHalls(conn);
            req.setAttribute("halls", halls);
        }

        forward(req, resp, "/views/admin/halls/list-halls.jsp");
    }

    private void handleAddHall(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if ("GET".equals(req.getMethod())) {
            try (Connection conn = DBConnect.getConnection()) {
                SeatService seatService = new SeatService();
                List<SeatType> seatTypes = seatService.getAllSeatTypes(conn);
                req.setAttribute("seatTypes", seatTypes);
            }

            forward(req, resp, "/views/admin/halls/add-hall.jsp");
        } else {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");

            try {
                String hallName = req.getParameter("hallName");
                int rows = Integer.parseInt(req.getParameter("rows"));
                int cols = Integer.parseInt(req.getParameter("cols"));
                int defaultSeatTypeId = Integer.parseInt(req.getParameter("defaultSeatType"));

                try (Connection conn = DBConnect.getConnection()) {
                    CinemaHallService hallService = new CinemaHallService();
                    SeatService seatService = new SeatService();

                    int hallId = hallService.createHall(conn, hallName, rows, cols);
                    seatService.generateSeatsForHall(conn, hallId, rows, cols, defaultSeatTypeId);

                    req.getSession().setAttribute("successMsg", "Thêm phòng chiếu '" + hallName + "' thành công!");
                    redirect(resp, req.getContextPath() + "/venue?action=halls");
                }
            } catch (IllegalArgumentException e) {
                req.setAttribute("errorMsg", e.getMessage());
                handleAddHall(req, resp);
            }
        }
    }

    private void handleToggleHall(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try {
            int hallId = Integer.parseInt(req.getParameter("hallId"));
            boolean status = Boolean.parseBoolean(req.getParameter("status"));

            try (Connection conn = DBConnect.getConnection()) {
                CinemaHallService hallService = new CinemaHallService();
                hallService.toggleHallStatus(conn, hallId, status);
                req.getSession().setAttribute("successMsg", status ? "Đã kích hoạt phòng chiếu" : "Đã tắt phòng chiếu");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }

        redirect(resp, req.getContextPath() + "/venue?action=halls");
    }

    private void handleSeats(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int hallId = Integer.parseInt(req.getParameter("hallId"));

        try (Connection conn = DBConnect.getConnection()) {
            SeatService seatService = new SeatService();
            CinemaHallService hallService = new CinemaHallService();

            CinemaHall hall = hallService.getHallById(conn, hallId);
            List<Seat> seats = seatService.getSeatsByHall(conn, hallId);
            List<SeatType> seatTypes = seatService.getAllSeatTypes(conn);

            req.setAttribute("hall", hall);
            req.setAttribute("seats", seats);
            req.setAttribute("seatTypes", seatTypes);

            forward(req, resp, "/views/admin/seats/manage-seats.jsp");
        }
    }

    private void handleUpdateSeat(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String[] seatIdArr = req.getParameterValues("seatIds");
        if (seatIdArr == null || seatIdArr.length == 0) {
            redirect(resp, req.getHeader("Referer"));
            return;
        }

        String seatTypeIdStr = req.getParameter("seatTypeId");
        String seatStatusStr = req.getParameter("seatStatus");
        int hallId = Integer.parseInt(req.getParameter("hallId"));

        try (Connection conn = DBConnect.getConnection()) {
            SeatService service = new SeatService();

            java.util.List<Integer> seatIds = new java.util.ArrayList<>();
            for (String id : seatIdArr) {
                seatIds.add(Integer.parseInt(id));
            }

            if (seatTypeIdStr != null && !seatTypeIdStr.isEmpty()) {
                service.updateSeatTypeBulk(conn, seatIds, Integer.parseInt(seatTypeIdStr));
            }

            if (seatStatusStr != null && !seatStatusStr.isEmpty()) {
                boolean active = seatStatusStr.equals("1");
                service.updateSeatStatusBulk(conn, seatIds, active);
            }
        }

        redirect(resp, req.getContextPath() + "/venue?action=seats&hallId=" + hallId);
    }
}
