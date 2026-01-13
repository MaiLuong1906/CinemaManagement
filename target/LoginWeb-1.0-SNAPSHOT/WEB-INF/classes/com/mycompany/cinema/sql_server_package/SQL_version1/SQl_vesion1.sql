/* =========================================================
   DATABASE: QUAN LY RAP PHIM (Phiên bản Tiếng Việt)
   ========================================================= */
DROP  DATABASE Version1;

CREATE DATABASE Version1;
GO
USE Version1;
GO

/* =========================
   2. tai_khoan (Dùng để Đăng nhập & Bảo mật)
   ========================= */

CREATE TABLE tai_khoan (
    id_tai_khoan INT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    mat_khau_hash VARCHAR(500) NOT NULL, -- Mật khẩu đã băm
    id_vai_tro VARCHAR(20) DEFAULT 'User'
		CONSTRAINT CK_id_vai_tro CHECK (id_vai_tro IN ('Admin', 'User')),
    trang_thai BIT DEFAULT 1 , -- Hoạt động(1), Bị khóa(0)
    ngay_tao DATETIME DEFAULT GETDATE(	)
    
);

/* =========================
   3. thong_tin_nguoi_dung (Hồ sơ cá nhân khách hàng)
   ========================= */
CREATE TABLE thong_tin_nguoi_dung (
    id_nguoi_dung INT PRIMARY KEY, 
    ho_ten NVARCHAR(100) NOT NULL,
    so_dien_thoai VARCHAR(15) UNIQUE NOT NULL,
	gioi_tinh BIT NOT NULL,
    dia_chi NVARCHAR(255),
    ngay_sinh DATE NOT NULL
    -- Kết nối 1-1 với bảng tai_khoan
    FOREIGN KEY (id_nguoi_dung) REFERENCES tai_khoan(id_tai_khoan) 
);

/* =========================
   4. the_loai_phim (Hành động, Tình cảm, Hài...)
   ========================= */
CREATE TABLE the_loai_phim (
    id_the_loai INT IDENTITY(1,1) PRIMARY KEY,
    ten_the_loai NVARCHAR(50) NOT NULL
);

/* =========================
   5. phim
   ========================= */
CREATE TABLE phim (
    id_phim INT IDENTITY(1,1) PRIMARY KEY,
    tieu_de NVARCHAR(200) NOT NULL,
    thoi_luong INT, -- đơn vị: phút
    mo_ta NVARCHAR(MAX),
    ngay_khoi_chieu DATE,
    phan_loai_tuoi NVARCHAR(10) DEFAULT N'P', -- P, T13, T16, T18
    hinh_anh_poster VARCHAR(500)
);

/* =========================
   6. phim_thuoc_the_loai (Bảng trung gian: 1 phim có nhiều thể loại)
   ========================= */
CREATE TABLE phim_thuoc_the_loai (
    id_phim INT,
    id_the_loai INT,
    PRIMARY KEY (id_phim, id_the_loai),
    FOREIGN KEY (id_phim) REFERENCES phim(id_phim) ON DELETE CASCADE,
    FOREIGN KEY (id_the_loai) REFERENCES the_loai_phim(id_the_loai) ON DELETE CASCADE
);

/* =========================
   7. phong_chieu
   ========================= */
CREATE TABLE phong_chieu (
    id_phong INT IDENTITY(1,1) PRIMARY KEY,
    ten_phong NVARCHAR(50),
);

/* =========================
   8. loai_ghe (Thường, VIP, Đôi)
   ========================= */
CREATE TABLE loai_ghe (
    id_loai_ghe INT IDENTITY(1,1) PRIMARY KEY,
    ten_loai_ghe NVARCHAR(50), 
    phu_phi DECIMAL(10,2) DEFAULT 0 -- Số tiền cộng thêm nếu là ghế xịn
);

/* =========================
   9. danh_sach_ghe (Sơ đồ ghế trong phòng)
   ========================= */
CREATE TABLE danh_sach_ghe (
    id_ghe INT IDENTITY(1,1) PRIMARY KEY,
    id_phong INT,
    ma_so_ghe NVARCHAR(10), -- Ví dụ: A1, A2, B10
    id_loai_ghe INT,
    FOREIGN KEY (id_phong) REFERENCES phong_chieu(id_phong),
    FOREIGN KEY (id_loai_ghe) REFERENCES loai_ghe(id_loai_ghe),
    UNIQUE (id_phong, ma_so_ghe) -- Một phòng không thể có 2 ghế trùng mã
);

/* =========================
   10. suat_chieu (Lịch chiếu phim cụ thể)
   ========================= */
CREATE TABLE suat_chieu (
    id_suat_chieu INT IDENTITY(1,1) PRIMARY KEY,
    id_phim INT,
    id_phong INT,
    thoi_gian_bat_dau DATETIME NOT NULL,
    gia_ve_co_ban DECIMAL(10,2) NOT NULL, -- Giá gốc của khung giờ này
    CONSTRAINT Kiem_Tra_Thoi_Gian CHECK (thoi_gian_bat_dau >= GETDATE()),
    FOREIGN KEY (id_phim) REFERENCES phim(id_phim),
    FOREIGN KEY (id_phong) REFERENCES phong_chieu(id_phong)
);

/* =========================
   11. hoa_don_ve (Thông tin đặt vé tổng quát)
   ========================= */
CREATE TABLE hoa_don_ve (
    id_ve INT IDENTITY(1,1) PRIMARY KEY,
    id_nguoi_dung INT,
    id_suat_chieu INT,
    thoi_gian_dat DATETIME DEFAULT GETDATE(),
    thoi_gian_het_han DATETIME, -- Dùng để giữ chỗ tạm thời
    trang_thai NVARCHAR(20) DEFAULT N'Chờ thanh toán', -- Đã thanh toán, Đã hủy...
    tong_tien DECIMAL(15,2),
    ma_tra_cuu_ve AS ('TIC' + RIGHT('000000' + CAST(id_ve AS VARCHAR(10)), 6)) PERSISTED,
    FOREIGN KEY (id_nguoi_dung) REFERENCES thong_tin_nguoi_dung(id_nguoi_dung),
    FOREIGN KEY (id_suat_chieu) REFERENCES suat_chieu(id_suat_chieu)
);

/* =========================
   12. chi_tiet_ghe_dat (Lưu từng ghế khách chọn)
   ========================= */
CREATE TABLE chi_tiet_ghe_dat (
    id_ve INT,
    id_ghe INT,
	id_phong INT,
    gia_tai_thoi_diem_ban DECIMAL(10,2), -- = Giá suất chiếu + Phụ phí ghế
    PRIMARY KEY (id_ve, id_ghe,id_phong),
    FOREIGN KEY (id_ve) REFERENCES hoa_don_ve(id_ve),
    FOREIGN KEY (id_ghe) REFERENCES danh_sach_ghe(id_ghe),
	FOREIGN KEY (id_phong) REFERENCES phong_chieu(id_phong)
);

/* =========================
   13. do_an_thuc_uong
   ========================= */
CREATE TABLE do_an_thuc_uong (
    id_mon INT IDENTITY(1,1) PRIMARY KEY,
    ten_mon NVARCHAR(100),
    gia_ban DECIMAL(10,2),
    so_luong_ton INT DEFAULT 0
);

/* =========================
   14. chi_tiet_do_an_dat (Khách mua bắp nước kèm vé)
   ========================= */
CREATE TABLE chi_tiet_do_an_dat (
    id_ve INT,
    id_mon INT,
    so_luong INT,
    PRIMARY KEY (id_ve, id_mon),
    FOREIGN KEY (id_ve) REFERENCES hoa_don_ve(id_ve),
    FOREIGN KEY (id_mon) REFERENCES do_an_thuc_uong(id_mon)
);

/* =========================
   15. CHI MUC (Tối ưu tốc độ tìm kiếm)
   ========================= */

-- Tìm nhanh xem ghế nào đã có người ngồi trong 1 suất chiếu
CREATE INDEX idx_suat_chieu_trang_thai ON hoa_don_ve(id_suat_chieu, trang_thai);

-- Soát vé cực nhanh bằng mã code TIC...
CREATE UNIQUE INDEX idx_ma_ve_duy_nhat ON hoa_don_ve(ma_tra_cuu_ve);

-- Tìm lịch chiếu theo thời gian và phim nhanh hơn
CREATE INDEX idx_tim_suat_chieu ON suat_chieu(thoi_gian_bat_dau, id_phim);

-- Tìm tên phim nhanh hơn khi gõ từ khóa
CREATE INDEX idx_ten_phim ON phim(tieu_de);

GO
