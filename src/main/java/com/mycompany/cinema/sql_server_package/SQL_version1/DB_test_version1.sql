
/* =========================
   15. Dữ liệu mẫu (TEST)
   ========================= */

-- Tài khoản & người dùng
INSERT INTO tai_khoan (email, mat_khau_hash, id_vai_tro)
VALUES 
('admin@cinema.com', '1', 'Admin'),
('user1@gmail.com', '1', 'User');

INSERT INTO thong_tin_nguoi_dung (id_nguoi_dung, ho_ten, so_dien_thoai, gioi_tinh, dia_chi, ngay_sinh)
VALUES
(1, N'NGuyễn Tam Quân', '0909123456', 1, N'123 Đường A, Hà Nội', '2005-05-20'),
(2, N'Nguyễn Thành Đạt', '0912345678', 0, N'456 Đường B, Hà Nội', '2005-10-15');

-- Thể loại phim
INSERT INTO the_loai_phim (ten_the_loai)
VALUES (N'Hành động'), (N'Tình cảm'), (N'Hài');

-- Phim
INSERT INTO phim (tieu_de, thoi_luong, mo_ta, ngay_khoi_chieu, phan_loai_tuoi)
VALUES
(N'Fast & Furious 10', 145, N'Xe cộ và hành động mạnh', '2026-01-20', N'T18'),
(N'Tình yêu mùa đông', 120, N'Câu chuyện tình cảm', '2026-01-22', N'T13');

-- Phim-Thể loại
INSERT INTO phim_thuoc_the_loai (id_phim, id_the_loai)
VALUES (1, 1), (2, 2);

-- Phòng chiếu
INSERT INTO phong_chieu (ten_phong)
VALUES (N'Phòng 1'), (N'Phòng 2');

-- Loại ghế
INSERT INTO loai_ghe (ten_loai_ghe, phu_phi)
VALUES (N'Thường', 0), (N'VIP', 50), (N'Đôi', 30);

-- Danh sách ghế
INSERT INTO danh_sach_ghe (id_phong, ma_so_ghe, id_loai_ghe)
VALUES 
(1,'A1',1),(1,'A2',1),(1,'B1',2),
(2,'A1',1),(2,'A2',1),(2,'B1',2);

-- Suất chiếu
INSERT INTO suat_chieu (id_phim, id_phong, thoi_gian_bat_dau, gia_ve_co_ban)
VALUES
(1,1,'2026-01-20 19:00',100),
(2,2,'2026-01-22 20:00',80);

-- Hóa đơn vé
INSERT INTO hoa_don_ve (id_nguoi_dung, id_suat_chieu, trang_thai, tong_tien)
VALUES
(2,1,N'Chờ thanh toán',100);

-- Chi tiết ghế đặt
INSERT INTO chi_tiet_ghe_dat (id_ve, id_ghe, id_phong, gia_tai_thoi_diem_ban)
VALUES (1,1,1,100);

-- Đồ ăn
INSERT INTO do_an_thuc_uong (ten_mon, gia_ban, so_luong_ton)
VALUES (N'Bắp rang bơ', 30, 100), (N'Nước ngọt', 20, 100);

-- Chi tiết đồ ăn đặt
INSERT INTO chi_tiet_do_an_dat (id_ve, id_mon, so_luong)
VALUES (1,1,2),(1,2,1);
GO

