/* =========================================================
   DATABASE: CINEMA MANAGEMENT SYSTEM (Hệ thống Quản lý Rạp phim)
   ========================================================= */

CREATE DATABASE CinemaManagement;
GO
USE CinemaManagement;
GO

/* =========================
   1. Accounts (Tài khoản - Đăng nhập & Bảo mật)
   ========================= */
CREATE TABLE accounts (
    account_id INT IDENTITY(1,1) PRIMARY KEY,
    phone_number VARCHAR(12) UNIQUE NOT NULL,
    password_hash VARCHAR(500) NOT NULL,
    role_id VARCHAR(20) DEFAULT 'User' 
        CONSTRAINT CK_role_id CHECK (role_id IN ('Admin', 'User')),
    status BIT DEFAULT 1, -- 1: Active (Hoạt động), 0: Locked (Khóa)
    created_at DATETIME DEFAULT GETDATE()
);

/* =========================
   2. User_Profiles (Thông tin người dùng)
   ========================= */
CREATE TABLE user_profiles (
    user_id INT PRIMARY KEY, 
    full_name NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    gender BIT NOT NULL, -- 1: Male, 0: Female
    address NVARCHAR(255),
    date_of_birth DATE NOT NULL,
    -- FK kết nối 1-1 với accounts
    FOREIGN KEY (user_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

/* =========================
   3. Movie_Genres (Thể loại phim)
   ========================= */
CREATE TABLE movie_genres (
    genre_id INT IDENTITY(1,1) PRIMARY KEY,
    genre_name NVARCHAR(50) NOT NULL
);

/* =========================
   4. Movies (Phim)
   ========================= */
CREATE TABLE movies (
    movie_id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    duration INT, -- Unit: minutes (phút)
    description NVARCHAR(MAX),
    release_date DATE,
    age_rating NVARCHAR(10) DEFAULT 'P', -- P, T13, T16, T18
    poster_url VARCHAR(500)
);

/* =========================
   5. Movie_Genre_Rel (Phim thuộc thể loại - Bảng trung gian)
   ========================= */
CREATE TABLE movie_genre_rel (
    movie_id INT,
    genre_id INT,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES movie_genres(genre_id) ON DELETE CASCADE
);

/* =========================
   6. Cinema_Halls (Phòng chiếu)
   ========================= */
CREATE TABLE cinema_halls (
    hall_id INT IDENTITY(1,1) PRIMARY KEY,
    hall_name NVARCHAR(50)
);

/* =========================
   7. Seat_Types (Loại ghế: Thường, VIP, Đôi)
   ========================= */
CREATE TABLE seat_types (
    seat_type_id INT IDENTITY(1,1) PRIMARY KEY,
    type_name NVARCHAR(50), 
    extra_fee DECIMAL(10,2) DEFAULT 0 -- Phụ phí cho loại ghế đặc biệt
);

/* =========================
   8. Seats (Danh sách ghế trong phòng)
   ========================= */
CREATE TABLE seats (
    seat_id INT IDENTITY(1,1) PRIMARY KEY,
    hall_id INT,
    seat_code NVARCHAR(10), -- Ex: A1, B10
    seat_type_id INT,
    FOREIGN KEY (hall_id) REFERENCES cinema_halls(hall_id),
    FOREIGN KEY (seat_type_id) REFERENCES seat_types(seat_type_id),
    UNIQUE (hall_id, seat_code) -- Tránh trùng mã ghế trong cùng 1 phòng
);

/* =========================
   9. Showtimes (Suất chiếu)
   ========================= */
CREATE TABLE showtimes (
    showtime_id INT IDENTITY(1,1) PRIMARY KEY,
    movie_id INT,
    hall_id INT,
    start_time DATETIME NOT NULL,
    base_price DECIMAL(10,2) NOT NULL, -- Giá gốc của suất chiếu
    CONSTRAINT CK_start_time CHECK (start_time >= GETDATE()),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (hall_id) REFERENCES cinema_halls(hall_id)
);

/* =========================
   10. Invoices (Hóa đơn vé tổng quát)
   ========================= */
CREATE TABLE invoices (
    invoice_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT,
    showtime_id INT,
    booking_time DATETIME DEFAULT GETDATE(),
    expiry_time DATETIME, -- Hết hạn sau 5 phút nếu không thanh toán
    status NVARCHAR(30) DEFAULT N'Pending', -- Pending (Chờ), Paid (Đã trả), Canceled (Hủy)
    total_amount DECIMAL(15,2),
    ticket_code AS ('TIC' + RIGHT('000000' + CAST(invoice_id AS VARCHAR(10)), 6)) PERSISTED,
    FOREIGN KEY (user_id) REFERENCES user_profiles(user_id),
    FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id)
);

/* =========================
   11. Ticket_Details (Chi tiết ghế đã đặt)
   ========================= */
CREATE TABLE ticket_details (
    invoice_id INT NOT NULL,       -- liên kết hóa đơn
    seat_id INT NOT NULL,          -- ghế được đặt
    hall_id INT NOT NULL,          -- phòng chứa ghế
    showtime_id INT NOT NULL,      -- suất chiếu ghế thuộc
    actual_price DECIMAL(10,2),   -- base_price + extra_fee

    -- PRIMARY KEY (1 lần đặt ghế duy nhất trong 1 invoice)
    CONSTRAINT PK_TicketDetails PRIMARY KEY (invoice_id, seat_id, hall_id),

    -- UNIQUE constraint để chống trùng ghế cùng suất chiếu
    CONSTRAINT UQ_Showtime_Seat UNIQUE (showtime_id, seat_id),

    -- FOREIGN KEY
    CONSTRAINT FK_Ticket_Invoice FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
    CONSTRAINT FK_Ticket_Seat FOREIGN KEY (seat_id) REFERENCES seats(seat_id),
    CONSTRAINT FK_Ticket_Hall FOREIGN KEY (hall_id) REFERENCES cinema_halls(hall_id),
    CONSTRAINT FK_Ticket_Showtime FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id)
);


/* =========================
   12. Foods_Drinks (Danh mục đồ ăn thức uống)
   ========================= */

CREATE TABLE products (
    item_id INT IDENTITY(1,1) PRIMARY KEY,
    item_name NVARCHAR(100),
    price DECIMAL(10,2),
    img_user_url VARCHAR(500),
    stock_quantity INT DEFAULT 0
);

/* =========================
   13. Food_Order_Details (Chi tiết đồ ăn kèm theo hóa đơn)
   ========================= */
CREATE TABLE products_details (
    invoice_id INT,
    item_id INT,
    quantity INT,
    PRIMARY KEY (invoice_id, item_id),
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
    FOREIGN KEY (item_id) REFERENCES products(item_id)
);

/* =========================
   14. INDEXES (Chỉ mục tối ưu tìm kiếm)
   ========================= */

-- Tìm nhanh ghế đã đặt theo suất chiếu và trạng thái
CREATE INDEX idx_showtime_status ON invoices(showtime_id, status);

-- Soát vé nhanh bằng Ticket Code
CREATE UNIQUE INDEX idx_ticket_code_unique ON invoices(ticket_code);

-- Tìm lịch chiếu theo thời gian và phim
CREATE INDEX idx_search_showtime ON showtimes(start_time, movie_id);

-- Tìm tên phim nhanh hơn
CREATE INDEX idx_movie_title ON movies(title);

GO