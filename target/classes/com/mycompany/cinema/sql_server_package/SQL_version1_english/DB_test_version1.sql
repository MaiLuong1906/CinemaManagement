USE CinemaManagement;
GO

/* =========================
   1. Test Data for Accounts & User Profiles
   ========================= */
-- Password hash mẫu (giả lập)
INSERT INTO accounts (email, password_hash, role_id, status)
VALUES 
('admin@cinema.com', 'hash_admin_123', 'Admin', 1),
('user1@gmail.com', 'hash_user_456', 'User', 1),
('user2@gmail.com', 'hash_user_789', 'User', 1);

INSERT INTO user_profiles (user_id, full_name, phone_number, gender, address, date_of_birth)
VALUES 
(1, N'Nguyễn Quản Trị', '0901234567', 1, N'123 Lê Lợi, Đà Nẵng', '1990-01-01'),
(2, N'Trần Khách Hàng', '0912345678', 1, N'456 Hùng Vương, Đà Nẵng', '2005-05-20'),
(3, N'Lê Thị User', '0923456789', 0, N'789 Nguyễn Văn Linh, Đà Nẵng', '2010-10-10');

/* =========================
   2. Test Data for Movie Genres & Movies
   ========================= */
INSERT INTO movie_genres (genre_name)
VALUES (N'Action'), (N'Comedy'), (N'Horror'), (N'Romance'), (N'Sci-Fi');

INSERT INTO movies (title, duration, description, release_date, age_rating)
VALUES 
(N'Spider-Man: No Way Home', 148, N'Spider-Man seeks help from Doctor Strange.', '2025-12-15', 'T13'),
(N'The Conjuring', 112, N'Paranormal investigators work to help a family.', '2025-11-20', 'T18'),
(N'Doraemon: Nobita Mini-Dora', 95, N'Animation for children.', '2026-01-01', 'P');

INSERT INTO movie_genre_rel (movie_id, genre_id)
VALUES (1, 1), (1, 5), (2, 3), (3, 2);

/* =========================
   3. Test Data for Halls, Seat Types & Seats
   ========================= */
INSERT INTO cinema_halls (hall_name)
VALUES (N'Hall 01 - IMAX'), (N'Hall 02 - Standard');

INSERT INTO seat_types (type_name, extra_fee)
VALUES 
(N'Standard', 0), 
(N'VIP', 20000), 
(N'Double (Sweetbox)', 50000);

-- Thêm ghế cho Hall 1 (Dòng A: Standard, Dòng B: VIP)
INSERT INTO seats (hall_id, seat_code, seat_type_id)
VALUES 
(1, 'A1', 1), (1, 'A2', 1), (1, 'A3', 1),
(1, 'B1', 2), (1, 'B2', 2), (1, 'B3', 2);

-- Thêm ghế cho Hall 2
INSERT INTO seats (hall_id, seat_code, seat_type_id)
VALUES (2, 'A1', 1), (2, 'A2', 1), (2, 'S1', 3);

/* =========================
   4. Test Data for Showtimes
   ========================= */
-- Lưu ý: start_time phải lớn hơn ngày hiện tại
INSERT INTO showtimes (movie_id, hall_id, start_time, base_price)
VALUES 
(1, 1, '2026-02-01 19:00:00', 80000),
(2, 1, '2026-02-01 22:30:00', 90000),
(3, 2, '2026-02-02 10:00:00', 60000);

/* =========================
   5. Test Data for Foods & Drinks
   ========================= */
INSERT INTO foods_drinks (item_name, price, stock_quantity)
VALUES 
(N'Popcorn Size L', 55000, 100),
(N'Pepsi Size L', 35000, 200),
(N'Combo 1 (1 Pop + 1 Water)', 80000, 50);

/* =========================
   6. Test Data for Invoices (Booking)
   ========================= */
-- Giả lập 1 hóa đơn đang chờ thanh toán cho User 2
INSERT INTO invoices (user_id, showtime_id, booking_time, expiry_time, status, total_amount)
VALUES (2, 1, GETDATE(), DATEADD(MINUTE, 5, GETDATE()), N'Pending', 100000);

-- Chi tiết ghế cho hóa đơn trên (Ghế B1 là VIP: 80k + 20k = 100k)
INSERT INTO ticket_details (invoice_id, seat_id, hall_id, actual_price)
VALUES (1, 4, 1, 100000);

-- Chi tiết đồ ăn cho hóa đơn trên
INSERT INTO food_order_details (invoice_id, item_id, quantity)
VALUES (1, 1, 1); -- Mua thêm 1 bắp L
GO