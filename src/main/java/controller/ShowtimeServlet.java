package controller;

import dao.CinemaHallDAO;
import dao.DBConnect;
import dao.MovieDAO;
import dao.ShowtimeDAO;
import dao.TimeSlotDAO;
import model.CinemaHall;
import model.Movie;
import model.Showtime;
import model.TimeSlot;
import model.SeatFillRate_ViewDTO;
import model.TimeSlotKpiDTO;
import service.SeatFillRate_ViewService;
import service.ShowtimeService;
import service.TimeSlotService;
import util.MovieUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import model.UserDTO;

@WebServlet("/showtime")
public class ShowtimeServlet extends BaseServlet {

    private ShowtimeDAO showtimeDAO;
    private ShowtimeService showtimeService;
    private TimeSlotService timeslotService;
    private SeatFillRate_ViewService seatFillRateService;
    private MovieDAO movieDAO;
    private CinemaHallDAO cinemaHallDAO;
    private TimeSlotDAO timeSlotDAO;

    @Override
    public void init() {
        showtimeDAO = new ShowtimeDAO();
        showtimeService = new ShowtimeService();
        timeslotService = new TimeSlotService();
        seatFillRateService = new SeatFillRate_ViewService();
        movieDAO = new MovieDAO();
        try {
            cinemaHallDAO = new CinemaHallDAO(DBConnect.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
        timeSlotDAO = new TimeSlotDAO();
    }

    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // Permission check: only Admin can manage showtimes
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRoleId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        String action = getStringParam(req, "action", "admin-manage");

        switch (action) {
            case "admin-manage":
                showAdminManage(req, resp);
                break;
            case "add":
                if ("POST".equals(req.getMethod())) {
                    addShowtime(req, resp);
                } else {
                    showAddForm(req, resp);
                }
                break;
            case "update":
                if ("POST".equals(req.getMethod())) {
                    updateShowtime(req, resp);
                } else {
                    showUpdateForm(req, resp);
                }
                break;
            case "delete":
                if ("POST".equals(req.getMethod())) {
                    deleteShowtime(req, resp);
                }
                break;
        }
    }

    private void showAdminManage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int numberOfShowtime = timeslotService.countTimeSlot();
            request.setAttribute("numberOfShowtime", numberOfShowtime);
        } catch (RuntimeException ex) {
            request.setAttribute("msg", ex.getMessage());
        } 
        try {
            List<SeatFillRate_ViewDTO> list = seatFillRateService.getSeatFillRateForShowtimeCurrentMonth();
            request.setAttribute("list", list);
        } catch (RuntimeException ex) {
            request.setAttribute("msg", ex.getMessage());
        } 
        try {
            List<TimeSlotKpiDTO> listKpi = timeslotService.getTimeSlotKpiCurrentMonth();
            request.setAttribute("listKpi", listKpi);
        } catch (RuntimeException ex) {
            request.setAttribute("msg", ex.getMessage());
        } 
        List<int[]> ticketBySlot = timeslotService.getTotalTicketsBySlotCurrentMonth();
        request.setAttribute("ticketBySlot", ticketBySlot);     
        forward(request, response, "/views/admin/statistic/showtime-management.jsp");
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Movie> movieList = movieDAO.getAllMovies();
        request.setAttribute("movieList", movieList);

        List<CinemaHall> hallList = new ArrayList<>();
        try {
            hallList = cinemaHallDAO.getAllHalls();
        } catch (SQLException ex) {
            System.out.println("Khong co phong nao");
        }
        request.setAttribute("hallList", hallList);

        List<TimeSlot> listChieu = null;
        try {
            listChieu = timeSlotDAO.findAll(DBConnect.getConnection());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        request.setAttribute("listChieu", listChieu);
        forward(request, response, "/views/admin/movies/addShowTime.jsp");
    }

    private void addShowtime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        int hallId = Integer.parseInt(request.getParameter("hallId"));
        int startTimeId = Integer.parseInt(request.getParameter("timeSlotId"));
        LocalDate showDate = LocalDate.parse(request.getParameter("showDate"));
        Showtime showtime = new Showtime(movieId, hallId, showDate, startTimeId);
        
        boolean flag = true;
        try {
            showtimeDAO.insert(DBConnect.getConnection(), showtime);
            flag = true;
            session.setAttribute("dbError", "");
        } catch(SQLException ex) {
            flag = false;
            if(ex.getMessage().contains("UQ_Showtime")) {
                session.setAttribute("dbError", "Lịch chiếu bị trùng!");
            } else {
                session.setAttribute("dbError", "Lỗi từ hệ thống SQL!");
            }
        }
        if(flag) {
            session.setAttribute("message", "Thêm lịch chiếu thành công");
        } else {
            session.setAttribute("message", "Thêm phim thất bại");
        }
        session.setAttribute("success", flag);
        response.sendRedirect(request.getContextPath() + "/showtime?action=add");
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int showtimeId = MovieUtils.getIntParameter(request, "showtimeId");
        Showtime st = showtimeDAO.findById(DBConnect.getConnection(), showtimeId);
        List<Movie> movieList = movieDAO.findAll(DBConnect.getConnection());
        List<CinemaHall> hallList = cinemaHallDAO.getAllHalls();
        List<TimeSlot> availableSlots = timeSlotDAO.getAvailableSlots(
                DBConnect.getConnection(),
                st.getHallId(),
                st.getShowDate(),
                st.getShowtimeId()
        );
        request.setAttribute("showtime", st);
        request.setAttribute("movieList", movieList);
        request.setAttribute("hallList", hallList);
        request.setAttribute("slotList", availableSlots);
        forward(request, response, "/views/admin/movies/UpdateShowtime.jsp");
    }

    private void updateShowtime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        try {
            int showtimeId = MovieUtils.getIntParameter(request, "showtimeId");
            int movieId = MovieUtils.getIntParameter(request, "movieId");
            int hallId = MovieUtils.getIntParameter(request, "hallId");
            LocalDate showDate = MovieUtils.getLocalDateParameter(request, "showDate");
            int slotId = MovieUtils.getIntParameter(request, "slotId");
            Showtime showtime = new Showtime(showtimeId, movieId, hallId, showDate, slotId);
            showtimeDAO.update(DBConnect.getConnection(), showtime);
            session.setAttribute("flash_success", "Cập nhật suất chiếu thành công!");
        } catch (IllegalArgumentException e) {
            session.setAttribute("flash_error", e.getMessage());
        } catch (SQLException e) {
            session.setAttribute("flash_error", "Lỗi hệ thống khi cập nhật suất chiếu!");
        }
        response.sendRedirect(request.getContextPath() + "/movie?action=admin-list");
    }

    private void deleteShowtime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int showtimeId = MovieUtils.getIntParameter(request, "showtimeId");
        try {
            showtimeService.deleteShowtime(showtimeId);
            session.setAttribute("flash_success", "Xóa suất chiếu thành công!");
        } catch (Exception ex) {
            session.setAttribute("flash_error", ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/movie?action=admin-list");
    }
}
