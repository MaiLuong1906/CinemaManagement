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
    private SeatDAO seatDAO;
    private SeatTypeDAO seatTypeDAO;
    private Connection conn;

    public SeatService(Connection conn) {
        this.conn = conn;
        this.seatDAO = new SeatDAO();
        this.seatTypeDAO = new SeatTypeDAO();
    }

    // Tạo ghế mới
    public void createSeat(int hallId, String seatCode, int rowIndex,
                           int colIndex, int seatTypeId, boolean active)
            throws SQLException {
        if (!seatTypeDAO.exists(conn, seatTypeId)) {
            throw new IllegalArgumentException("Loại ghế không tồn tại");
        }
        seatDAO.insert(hallId, seatCode, rowIndex, colIndex, seatTypeId, active);
    }

    // Update type ghế hàng loạt (dùng batch)
    public void updateSeatTypeBulk(List<Integer> seatIds, int seatTypeId) throws SQLException {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ghế rỗng");
        }
        if (!seatTypeDAO.exists(conn, seatTypeId)) {
            throw new IllegalArgumentException("Loại ghế không tồn tại");
        }

        seatDAO.batchUpdateSeatType(seatIds, seatTypeId);
    }

    // Update trạng thái ghế hàng loạt (SỬA LẠI - dùng batch)
    public void updateSeatStatusBulk(List<Integer> seatIds, boolean status) throws SQLException {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ghế rỗng");
        }

        seatDAO.batchUpdateSeatStatus(seatIds, status);
    }

    // Tạo ghế hàng loạt cho phòng mới
    public void generateSeatsForHall(int hallId, int rows, int cols,
                                     int defaultSeatTypeId) throws SQLException {
        if (!seatTypeDAO.exists(conn, defaultSeatTypeId)) {
            throw new IllegalArgumentException("Loại ghế mặc định không tồn tại");
        }

        for (int r = 0; r < rows; r++) {
            String rowChar = SeatUtil.rowIndexToChar(r);

            for (int c = 0; c < cols; c++) {
                String seatCode = rowChar + (c + 1);
                seatDAO.insert(hallId, seatCode, r, c, defaultSeatTypeId, true);
            }
        }
    }

    // Lấy danh sách ghế theo phòng
    public List<Seat> getSeatsByHall(int hallId) throws SQLException {
        return seatDAO.findByHall(hallId);
    }

    // Lấy thông tin 1 ghế
    public Seat getSeatById(int seatId) throws SQLException {
        Seat seat = seatDAO.findById(seatId);
        if (seat == null) {
            throw new IllegalArgumentException("Không tìm thấy ghế với ID: " + seatId);
        }
        return seat;
    }
    // Lấy tất cả loại ghế
    public List<SeatType> getAllSeatTypes() throws SQLException {
        return seatTypeDAO.findAll(conn);
    }

    // Xóa tất cả ghế của phòng
    public void deleteAllSeatsInHall(int hallId) throws SQLException {
        seatDAO.deleteByHall(hallId);
    }

    // Đếm số ghế hoạt động trong phòng
    public int countActiveSeats(int hallId) throws SQLException {
        return seatDAO.countActiveSeats(hallId);
    }
}