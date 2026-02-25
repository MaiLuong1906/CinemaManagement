/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package ai.environment;

import model.SeatFillRate_ViewDTO;
import service.SeatFillRate_ViewService;
import service.MovieService;

import java.util.List;

/**
 * StatisticsEnvironment - Môi trường cung cấp các tools thống kê
 * Tất cả các hàm ở đây sẽ được đăng ký tự động thành tools
 */
public class StatisticsEnvironment {
    // Services - inject từ ngoài vào
    private final SeatFillRate_ViewService seatService;
    private final MovieService movieService;

    public StatisticsEnvironment(SeatFillRate_ViewService seatService,
                                 MovieService movieService) {
        this.seatService = seatService;
        this.movieService = movieService;
    }

    // ==================== SEAT FILL TOOLS ====================

    /**
     * Lấy độ phủ ghế tháng hiện tại (%)
     */
    public String getCurrentMonthSeatCoverage() {
        try {
            double rate = seatService.getSeatFillRateCurrentMonth();
            return String.format("%.2f%%", rate);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Lấy độ phủ theo từng suất chiếu
     */
    public String getShowtimeCoverage() {
        try {
            List<SeatFillRate_ViewDTO> data = seatService.getSeatFillRateForShowtimeCurrentMonth();
            
            if (data.isEmpty()) {
                return "Không có dữ liệu suất chiếu";
            }

            StringBuilder result = new StringBuilder();
            result.append("Độ phủ ghế theo suất chiếu:\n");
            
            for (SeatFillRate_ViewDTO item : data) {
                result.append(String.format("- Suất %s: %.2f%% (%d/%d ghế)\n",
                    item.getShowtimeId(),
                    item.getFillRate(),
                    item.getTotalSeats()
                ));
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ==================== REVENUE TOOLS ====================
    // Có thể thêm khi có RevenueService

    /**
     * Lấy doanh thu tháng hiện tại
     */
//    public String getCurrentMonthRevenue() {
//        try {
//            if (revenueService == null) {
//                return "RevenueService chưa được cấu hình";
//            }
//            
//            double revenue = revenueService.getCurrentMonthRevenue();
//            return String.format("%.0f VNĐ", revenue);
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }

    /**
     * Lấy doanh thu theo từng phim
     */
//    public String getRevenueByMovie() {
//        try {
//            if (revenueService == null) {
//                return "RevenueService chưa được cấu hình";
//            }
//            
//            // Giả sử có method này
//            var data = revenueService.getRevenueByMovie();
//            
//            StringBuilder result = new StringBuilder();
//            result.append("Doanh thu theo phim:\n");
//            
//            // Format data (tùy thuộc vào cấu trúc thực tế)
//            result.append(data.toString());
//            
//            return result.toString();
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }

    // ==================== MOVIE PERFORMANCE TOOLS ====================

    /**
     * Lấy top phim bán chạy nhất
     */
//    public String getTopMovies() {
//        try {
//            if (movieService == null) {
//                return "MovieService chưa được cấu hình";
//            }
//            
//            var topMovies = movieService.getTopMovies(5);
//            
//            StringBuilder result = new StringBuilder();
//            result.append("Top 5 phim bán chạy:\n");
//            
//            for (int i = 0; i < topMovies.size(); i++) {
//                var movie = topMovies.get(i);
//                result.append(String.format("%d. %s\n", i + 1, movie.toString()));
//            }
//            
//            return result.toString();
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }

    /**
     * Lấy thông tin chi tiết một phim
     */
//    public String getMovieDetails(String movieId) {
//        try {
//            if (movieService == null) {
//                return "MovieService chưa được cấu hình";
//            }
//            
//            var movie = movieService.getMovieById(movieId);
//            
//            if (movie == null) {
//                return "Không tìm thấy phim với ID: " + movieId;
//            }
//            
//            return movie.toString();
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }

    // ==================== COMPARISON TOOLS ====================

    /**
     * So sánh doanh thu tháng này với tháng trước
     */
//    public String compareRevenueWithLastMonth() {
//        try {
//            if (revenueService == null) {
//                return "RevenueService chưa được cấu hình";
//            }
//            
//            double currentMonth = revenueService.getCurrentMonthRevenue();
//            double lastMonth = revenueService.getLastMonthRevenue();
//            double change = ((currentMonth - lastMonth) / lastMonth) * 100;
//            
//            return String.format(
//                "Tháng này: %.0f VNĐ\nTháng trước: %.0f VNĐ\nThay đổi: %+.2f%%",
//                currentMonth, lastMonth, change
//            );
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }

    /**
     * So sánh độ phủ ghế giữa các suất chiếu
     */
    public String compareSeatFillRates() {
        try {
            List<SeatFillRate_ViewDTO> data = seatService.getSeatFillRateForShowtimeCurrentMonth();
            
            if (data.isEmpty()) {
                return "Không có dữ liệu";
            }

            // Tìm suất cao nhất và thấp nhất
            SeatFillRate_ViewDTO highest = data.get(0);
            SeatFillRate_ViewDTO lowest = data.get(0);
            
            for (SeatFillRate_ViewDTO item : data) {
                if (item.getFillRate() > highest.getFillRate()) {
                    highest = item;
                }
                if (item.getFillRate() < lowest.getFillRate()) {
                    lowest = item;
                }
            }
            
            return String.format(
                "Cao nhất: Suất %s (%.2f%%)\nThấp nhất: Suất %s (%.2f%%)",
                highest.getShowtimeId(), highest.getFillRate(),
                lowest.getShowtimeId(), lowest.getFillRate()
            );
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
