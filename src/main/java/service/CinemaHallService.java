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
    private CinemaHallDAO hallDAO = new CinemaHallDAO();
    private SeatDAO seatDAO = new SeatDAO();

    // Tạo phòng mới
    public int createHall(Connection conn, String hallName, int rows, int cols) throws SQLException {
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
        return hallDAO.insert(conn, hall);
    }

    // Lấy tất cả phòng
    public List<CinemaHall> getAllHalls(Connection conn) throws SQLException {
        return hallDAO.getAllHalls(conn);
    }

    // Lấy phòng theo ID
    public CinemaHall getHallById(Connection conn, int hallId) throws SQLException {
        return hallDAO.getHallById(conn, hallId);
    }

    // Cập nhật trạng thái
    public void toggleHallStatus(Connection conn, int hallId, boolean status) throws SQLException {
        hallDAO.updateStatus(conn, hallId, status);
    }

    // Xóa phòng (cẩn thận với foreign key)
    public boolean deleteHall(Connection conn, int hallId) throws SQLException {
        // Xóa tất cả ghế trước
        seatDAO.deleteByHall(conn, hallId);
        return hallDAO.deleteHall(conn, hallId);
    }
}