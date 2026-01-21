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
-- ===== MOVIE GENRES =====
INSERT INTO movie_genres (genre_name)
VALUES
    (N'Action'),        -- 1
    (N'Horror'),        -- 2
    (N'Animation'),    -- 3
    (N'Adventure'),    -- 4
    (N'Sci-Fi'),        -- 5
    (N'Comedy'),        -- 6
    (N'Fantasy'),      -- 7
    (N'Crime'),         -- 8
    (N'Thriller'),     -- 9
    (N'Drama');         -- 10



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
INSERT INTO movies (title, duration, release_date, age_rating)
VALUES
    (N'Avatar 3', 190, '2026-01-01', 'T13'),
    (N'Dune Part Two', 165, '2025-12-20', 'T13'),
    (N'Kung Fu Panda 4', 95, '2025-12-15', 'P'),
    (N'John Wick 5', 145, '2025-11-10', 'T18'),
    (N'Spider-Man: New Saga', 150, '2025-12-05', 'T13'),
    (N'Fast & Furious 11', 155, '2025-11-25', 'T16'),
    (N'Frozen 3', 110, '2025-12-01', 'P'),
    (N'Transformers: Reborn', 160, '2025-10-30', 'T13'),
    (N'Insidious 6', 120, '2025-10-15', 'T18'),
    (N'The Batman: Dawn', 175, '2025-12-18', 'T16');

-- ===== MOVIE - GENRE RELATION =====
INSERT INTO movie_genre_rel (movie_id, genre_id)
VALUES
-- Avatar 3
(1, 4), -- Adventure
(1, 5), -- Sci-Fi
(1, 7), -- Fantasy

-- Dune Part Two
(2, 4), -- Adventure
(2, 5), -- Sci-Fi
(2, 10), -- Drama

-- Kung Fu Panda 4
(3, 3), -- Animation
(3, 6), -- Comedy
(3, 4), -- Adventure

-- John Wick 5
(4, 1), -- Action
(4, 8), -- Crime
(4, 9), -- Thriller

-- Spider-Man: New Saga
(5, 1), -- Action
(5, 4), -- Adventure
(5, 5), -- Sci-Fi

-- Fast & Furious 11
(6, 1), -- Action
(6, 8), -- Crime
(6, 9), -- Thriller

-- Frozen 3
(7, 3), -- Animation
(7, 7), -- Fantasy
(7, 6), -- Comedy

-- Transformers: Reborn
(8, 1), -- Action
(8, 5), -- Sci-Fi
(8, 4), -- Adventure

-- Insidious 6
(9, 2), -- Horror
(9, 9), -- Thriller

-- The Batman: Dawn
(10, 1), -- Action
(10, 8), -- Crime
(10, 10); -- Drama

-- ===== TIME SLOTS =====
INSERT INTO time_slots (slot_name, start_time, end_time, slot_price)
VALUES
    (N'Morning',    '08:00', '10:30',  80000),
    (N'Noon',       '11:00', '13:30',  90000),
    (N'Afternoon',  '14:00', '16:30', 100000),
    (N'Evening',    '17:30', '20:00', 120000),
    (N'Late Night', '20:30', '23:00',  95000);

DECLARE @today DATE = CAST(GETDATE() AS DATE);

-- ===== SHOWTIMES =====
INSERT INTO showtimes (movie_id, hall_id, show_date, slot_id)
VALUES
-- Today
(1, 1, @today, 1),
(2, 1, @today, 2),
(3, 2, @today, 3),
(4, 2, @today, 4),
(5, 3, @today, 5),

-- Tomorrow
(6, 1, DATEADD(DAY, 1, @today), 1),
(7, 1, DATEADD(DAY, 1, @today), 3),
(8, 2, DATEADD(DAY, 1, @today), 4),
(9, 3, DATEADD(DAY, 1, @today), 5),
(10,3, DATEADD(DAY, 1, @today), 2),

-- Day after tomorrow
(1, 2, DATEADD(DAY, 2, @today), 4),
(2, 3, DATEADD(DAY, 2, @today), 5),
(3, 1, DATEADD(DAY, 2, @today), 1),
(4, 2, DATEADD(DAY, 2, @today), 2),
(5, 3, DATEADD(DAY, 2, @today), 3);



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
(invoice_id, seat_id, showtime_id, actual_price)
VALUES
    (1, 1,  1, 90000),
    (1, 3, 1, 110000),
    (2, 2,  2, 100000),
    (3, 6,  3, 85000);


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
