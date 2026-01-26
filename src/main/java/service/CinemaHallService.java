package service;

import dao.CinemaHallDAO;
import dao.SeatDAO;
import model.CinemaHall;
import model.Seat;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CinemaHallService {
    private CinemaHallDAO hallDAO;
    private SeatDAO seatDAO;

    public CinemaHallService(Connection conn) {
        this.hallDAO = new CinemaHallDAO(conn);
        this.seatDAO = new SeatDAO();
    }

    // Tạo phòng mới
    public int createHall(String hallName, int rows, int cols) throws SQLException {
        // Validate input
        if (hallName == null || hallName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        if (rows < 1 || rows > 20) {
            throw new IllegalArgumentException("Số hàng phải từ 1-20");
        }
        if (cols < 1 || cols > 30) {
            throw new IllegalArgumentException("Số cột phải từ 1-30");
        }

        CinemaHall hall = new CinemaHall(hallName, rows, cols, true, LocalDate.now());
        return hallDAO.insert(hall);
    }

    // Lấy tất cả phòng
    public List<CinemaHall> getAllHalls() throws SQLException {
        return hallDAO.getAllHalls();
    }

    // Lấy phòng theo ID
    public CinemaHall getHallById(int hallId) throws SQLException {
        return hallDAO.getHallById(hallId);
    }

    // Cập nhật trạng thái
    public void toggleHallStatus(int hallId, boolean status) throws SQLException {
        hallDAO.updateStatus(hallId, status);
    }

    // Xóa phòng (cẩn thận với foreign key)
    public boolean deleteHall(int hallId) throws SQLException {
        // Xóa tất cả ghế trước
        seatDAO.deleteByHall(hallId);
        return hallDAO.deleteHall(hallId);
    }
}