package service;

import dao.SeatDAO;
import dao.SeatTypeDAO;
import model.Seat;
import model.SeatType;
import util.SeatUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SeatService {
    private SeatDAO seatDAO = new SeatDAO();
    private SeatTypeDAO seatTypeDAO = new SeatTypeDAO();

    // Tạo ghế mới
    public void createSeat(Connection conn, int hallId, String seatCode, int rowIndex,
                           int colIndex, int seatTypeId, boolean active)
            throws SQLException {
        if (!seatTypeDAO.exists(conn, seatTypeId)) {
            throw new IllegalArgumentException("Loại ghế không tồn tại");
        }
        seatDAO.insert(conn, hallId, seatCode, rowIndex, colIndex, seatTypeId, active);
    }

    // Update type ghế hàng loạt (dùng batch)
    public void updateSeatTypeBulk(Connection conn, List<Integer> seatIds, int seatTypeId) throws SQLException {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ghế rỗng");
        }
        if (!seatTypeDAO.exists(conn, seatTypeId)) {
            throw new IllegalArgumentException("Loại ghế không tồn tại");
        }

        seatDAO.batchUpdateSeatType(conn, seatIds, seatTypeId);
    }

    // Update trạng thái ghế hàng loạt (SỬA LẠI - dùng batch)
    public void updateSeatStatusBulk(Connection conn, List<Integer> seatIds, boolean status) throws SQLException {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ghế rỗng");
        }

        seatDAO.batchUpdateSeatStatus(conn, seatIds, status);
    }

    // Tạo ghế hàng loạt cho phòng mới
    public void generateSeatsForHall(Connection conn, int hallId, int rows, int cols,
                                     int defaultSeatTypeId) throws SQLException {
        if (!seatTypeDAO.exists(conn, defaultSeatTypeId)) {
            throw new IllegalArgumentException("Loại ghế mặc định không tồn tại");
        }

        for (int r = 0; r < rows; r++) {
            String rowChar = SeatUtil.rowIndexToChar(r);

            for (int c = 0; c < cols; c++) {
                String seatCode = rowChar + (c + 1);
                seatDAO.insert(conn, hallId, seatCode, r, c, defaultSeatTypeId, true);
            }
        }
    }

    // Lấy danh sách ghế theo phòng
    public List<Seat> getSeatsByHall(Connection conn, int hallId) throws SQLException {
        return seatDAO.findByHall(conn, hallId);
    }

    // Lấy thông tin 1 ghế
    public Seat getSeatById(Connection conn, int seatId) throws SQLException {
        Seat seat = seatDAO.findById(conn, seatId);
        if (seat == null) {
            throw new IllegalArgumentException("Không tìm thấy ghế với ID: " + seatId);
        }
        return seat;
    }
    // Lấy tất cả loại ghế
    public List<SeatType> getAllSeatTypes(Connection conn) throws SQLException {
        return seatTypeDAO.findAll(conn);
    }

    // Xóa tất cả ghế của phòng
    public void deleteAllSeatsInHall(Connection conn, int hallId) throws SQLException {
        seatDAO.deleteByHall(conn, hallId);
    }

    // Đếm số ghế hoạt động trong phòng
    public int countActiveSeats(Connection conn, int hallId) throws SQLException {
        return seatDAO.countActiveSeats(conn, hallId);
    }
}
