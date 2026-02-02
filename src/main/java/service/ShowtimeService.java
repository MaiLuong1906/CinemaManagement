/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.DBConnect;
import dao.ShowtimeDAO;
import java.sql.SQLException;

/**
 *
 * @author nguye
 */
public class ShowtimeService {
    ShowtimeDAO showtimeDAO = new ShowtimeDAO();
    // xoa xuat chieu
    public void deleteShowtime(int showtimeId) throws Exception{
         try{
        boolean deleted = showtimeDAO.delete(DBConnect.getConnection(), showtimeId);
        if (!deleted) {
            throw new Exception("Suất chiếu không tồn tại!");
        }
    } catch (SQLException e) {
    if ("23000".equals(e.getSQLState())) {
        // mã lỗi 23000: Vi phạm ràng buộc toàn vẹn dữ liệu, Bạn đang làm điều DB KHÔNG CHO PHÉP
            throw new Exception(
                "Có sự ràng buộc giữ liệu. Vui lòng xóa các dữ liệu bị tham chiếu trước khi thực hiện thao tác này!"
            );
        }
        throw new Exception("Lỗi hệ thống khi xóa suất chiếu!");
    }

    }
    
}
