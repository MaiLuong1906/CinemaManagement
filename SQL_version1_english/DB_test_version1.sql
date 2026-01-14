-- Admin
DROP DATABASE CinemaManagement
-- Bật chế độ insert IDENTITY cho accounts
SET IDENTITY_INSERT accounts ON;

-- Password: Admin123456 = 'sAjIU5Y3pTfRFR/N+l3vvMm9BKU='
INSERT INTO accounts (account_id, phone_number, password_hash, role_id, created_at)
VALUES (1, '0980', 'sAjIU5Y3pTfRFR/N+l3vvMm9BKU=', 'Admin', GETDATE()),
       (2, '0981', 'sAjIU5Y3pTfRFR/N+l3vvMm9BKU=', 'Admin', GETDATE()),
       (3, '0982', 'sAjIU5Y3pTfRFR/N+l3vvMm9BKU=', 'Admin', GETDATE()),
       (4, '0983', 'sAjIU5Y3pTfRFR/N+l3vvMm9BKU=', 'Admin', GETDATE()),
       (5, '0984', 'sAjIU5Y3pTfRFR/N+l3vvMm9BKU=', 'Admin', GETDATE());

-- Tắt chế độ insert IDENTITY cho accounts
SET IDENTITY_INSERT accounts OFF;

-- Insert user_profiles với user_id khớp với account_id
-- Vì user_id là FK reference đến account_id nên phải khớp
INSERT INTO user_profiles(user_id, full_name, email, gender, date_of_birth)
VALUES (1, 'Admin1', 'admin1@gmail.com', 1, '1995-03-10'),
       (2, 'Admin2', 'admin2@gmail.com', 1, '1995-03-10'),
       (3, 'Admin3', 'admin3@gmail.com', 1, '1995-03-10'),
       (4, 'Admin4', 'admin4@gmail.com', 1, '1995-03-10'),
       (5, 'Admin5', 'admin5@gmail.com', 1, '1995-03-10');


-- User

-- Bật chế độ insert IDENTITY cho accounts
SET IDENTITY_INSERT accounts ON;

-- Password: User1234 = 'Teet7gKhJrIe0PtnyD3KjqAC+CU='
INSERT INTO accounts (account_id, phone_number, password_hash, role_id, created_at)
VALUES (11, '1111', 'Teet7gKhJrIe0PtnyD3KjqAC+CU=', 'User', GETDATE()),
       (12, '2222', 'Teet7gKhJrIe0PtnyD3KjqAC+CU=', 'User', GETDATE()),
       (13, '3333', 'Teet7gKhJrIe0PtnyD3KjqAC+CU=', 'User', GETDATE()),
       (14, '4444', 'Teet7gKhJrIe0PtnyD3KjqAC+CU=', 'User', GETDATE()),
       (15, '5555', 'Teet7gKhJrIe0PtnyD3KjqAC+CU=', 'User', GETDATE());

-- Tắt chế độ insert IDENTITY cho accounts
SET IDENTITY_INSERT accounts OFF;

-- Insert user_profiles với user_id khớp với account_id
-- Vì user_id là FK reference đến account_id nên phải khớp
INSERT INTO user_profiles(user_id, full_name, email, gender, date_of_birth)
VALUES (11, 'User1', 'User1@gmail.com', 1, '1995-03-10'),
       (12, 'User2', 'User2@gmail.com', 1, '1995-03-10'),
       (13, 'User3', 'User3@gmail.com', 1, '1995-03-10'),
       (14, 'User4', 'User4@gmail.com', 1, '1995-03-10'),
       (15, 'User5', 'User5@gmail.com', 1, '1995-03-10');

--Thêm phim
INSERT INTO movie_genres (genre_name)
VALUES
    (N'Hành động'),
    (N'Kinh dị'),
    (N'Hoạt hình'),
    (N'Tình cảm'),
    (N'Khoa học viễn tưởng');


--Set loại ghế
INSERT INTO seat_types (type_name, extra_fee)
VALUES
    (N'Thường', 0),
    (N'VIP', 20000),
    (N'Ghế đôi', 50000);

--Phòng chiếu
INSERT INTO cinema_halls (hall_name)
VALUES
    (N'Phòng 1'),
    (N'Phòng 2'),
    (N'Phòng IMAX');

--Set ghế
-- Phòng 1
INSERT INTO seats (hall_id, seat_code, seat_type_id)
VALUES
    (1, 'A1', 1),
    (1, 'A2', 1),
    (1, 'A3', 2),
    (1, 'B1', 2),
    (1, 'B2', 3);

-- Phòng 2
INSERT INTO seats (hall_id, seat_code, seat_type_id)
VALUES
    (2, 'A1', 1),
    (2, 'A2', 1),
    (2, 'B1', 2);

-- Phòng IMAX
INSERT INTO seats (hall_id, seat_code, seat_type_id)
VALUES
    (3, 'A1', 2),
    (3, 'A2', 2),
    (3, 'A3', 3);


--Thêm phim
INSERT INTO movies (title, duration, description, release_date, age_rating, poster_url)
VALUES
    (N'Avengers: Endgame', 181, N'Biệt đội siêu anh hùng đối đầu Thanos', '2019-04-26', 'T13', 'avengers.jpg'),
    (N'The Nun', 96, N'Ác quỷ Valak ám ảnh tu viện', '2018-09-07', 'T18', 'thenun.jpg'),
    (N'Doraemon Movie', 110, N'Cuộc phiêu lưu của Doraemon', '2024-06-01', 'P', 'doraemon.jpg');

INSERT INTO movie_genre_rel (movie_id, genre_id)
VALUES
    (1, 1), -- Avengers - Action
    (1, 5), -- Avengers - Sci-Fi
    (2, 2), -- The Nun - Horror
    (3, 3); -- Doraemon - Animation

--Lich chieu
INSERT INTO showtimes (movie_id, hall_id, start_time, base_price)
VALUES
    (1, 1, DATEADD(HOUR, 2, GETDATE()), 90000),
    (1, 1, DATEADD(HOUR, 5, GETDATE()), 100000),
    (2, 2, DATEADD(DAY, 1, GETDATE()), 85000),
    (3, 3, DATEADD(DAY, 1, DATEADD(HOUR, 3, GETDATE())), 70000);


--Product
INSERT INTO products (item_name, price, img_user_url, stock_quantity)
VALUES
    (N'Bắp rang bơ', 45000, 'popcorn.jpg', 100),
    (N'Coca Cola', 30000, 'coca.jpg', 200),
    (N'Combo Bắp + Coca', 70000, 'combo.jpg', 50);


--Hóa Đơn
INSERT INTO invoices (user_id, showtime_id, expiry_time, status, total_amount)
VALUES
    (11, 1, DATEADD(MINUTE, 5, GETDATE()), N'Paid', 160000),
    (12, 2, DATEADD(MINUTE, 5, GETDATE()), N'Pending', 120000),
    (13, 3, DATEADD(MINUTE, 5, GETDATE()), N'Paid', 135000);


--ghế đã đặt
INSERT INTO ticket_details
(invoice_id, seat_id, hall_id, showtime_id, actual_price)
VALUES
    (1, 1, 1, 1, 90000),
    (1, 3, 1, 1, 110000),
    (2, 2, 1, 2, 100000),
    (3, 6, 2, 3, 85000);


INSERT INTO products_details (invoice_id, item_id, quantity)
VALUES
    (1, 1, 1),
    (1, 2, 1),
    (3, 3, 1);


-- Login test
SELECT * FROM accounts WHERE phone_number = '1111';

-- Vé user
SELECT i.invoice_id, i.ticket_code, i.status, i.total_amount
FROM invoices i
WHERE i.user_id = 11;

-- Ghế đã đặt theo suất chiếu
SELECT *
FROM ticket_details
WHERE showtime_id = 1;
