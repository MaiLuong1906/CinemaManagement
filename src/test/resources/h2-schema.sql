DROP TABLE IF EXISTS chat_messages CASCADE;
DROP TABLE IF EXISTS ticket_details CASCADE;
DROP TABLE IF EXISTS products_details CASCADE;
DROP TABLE IF EXISTS invoices CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS showtimes CASCADE;
DROP TABLE IF EXISTS seats CASCADE;
DROP TABLE IF EXISTS seat_types CASCADE;
DROP TABLE IF EXISTS cinema_halls CASCADE;
DROP TABLE IF EXISTS time_slots CASCADE;
DROP TABLE IF EXISTS movie_genre_rel CASCADE;
DROP TABLE IF EXISTS movie_genres CASCADE;
DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS user_profiles CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
SET NON_KEYWORDS YEAR, MONTH, DAY;



-- 1. Accounts
CREATE TABLE accounts (
    account_id INT IDENTITY PRIMARY KEY,
    phone_number VARCHAR(12) UNIQUE NOT NULL,
    password_hash VARCHAR(500) NOT NULL,
    role_id VARCHAR(20) DEFAULT 'User',
    status BIT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. User Profiles
CREATE TABLE user_profiles (
    user_id INT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    gender BIT NOT NULL,
    address VARCHAR(255),
    date_of_birth DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- 3. Movie Genres
CREATE TABLE movie_genres (
    genre_id INT IDENTITY PRIMARY KEY,
    genre_name VARCHAR(255)
);

-- 4. Movies
CREATE TABLE movies (
    movie_id INT IDENTITY PRIMARY KEY,
    title VARCHAR(255),
    duration INT,
    description VARCHAR(2000),
    release_date DATE,
    age_rating VARCHAR(50),
    poster_url VARCHAR(2000)
);

-- 5. Movie Genre Relation
CREATE TABLE movie_genre_rel (
    movie_genre_id INT IDENTITY PRIMARY KEY,
    movie_id INT,
    genre_id INT,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES movie_genres(genre_id) ON DELETE CASCADE
);

-- 6. Cinema Halls
CREATE TABLE cinema_halls (
    hall_id INT IDENTITY PRIMARY KEY,
    hall_name VARCHAR(50) NOT NULL,
    total_rows INT NOT NULL,
    total_cols INT NOT NULL,
    status BIT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Seat Types
CREATE TABLE seat_types (
    seat_type_id INT IDENTITY PRIMARY KEY,
    type_name VARCHAR(50),
    extra_fee DECIMAL(10, 2) DEFAULT 0
);

-- 8. Seats
CREATE TABLE seats (
    seat_id INT IDENTITY PRIMARY KEY,
    hall_id INT NOT NULL,
    seat_code VARCHAR(10) NOT NULL,
    row_index INT NOT NULL,
    column_index INT NOT NULL,
    seat_type_id INT NOT NULL,
    is_active BIT DEFAULT 1,
    FOREIGN KEY (hall_id) REFERENCES cinema_halls(hall_id),
    FOREIGN KEY (seat_type_id) REFERENCES seat_types(seat_type_id)
);

-- 9. Time Slots
CREATE TABLE time_slots (
    slot_id INT IDENTITY PRIMARY KEY,
    slot_name VARCHAR(50),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_price DECIMAL(10, 2) NOT NULL
);

-- 10. Showtimes
CREATE TABLE showtimes (
    showtime_id INT IDENTITY PRIMARY KEY,
    movie_id INT NOT NULL,
    hall_id INT NOT NULL,
    show_date DATE NOT NULL,
    slot_id INT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (hall_id) REFERENCES cinema_halls(hall_id),
    FOREIGN KEY (slot_id) REFERENCES time_slots(slot_id)
);

-- 11. Invoices
CREATE TABLE invoices (
    invoice_id INT IDENTITY PRIMARY KEY,
    user_id INT,
    showtime_id INT,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_time TIMESTAMP,
    status VARCHAR(30) DEFAULT 'Pending',
    total_amount DECIMAL(15, 2),
    ticket_code VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES user_profiles(user_id),
    FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id)
);

-- 12. Ticket Details
CREATE TABLE ticket_details (
    invoice_id INT NOT NULL,
    seat_id INT NOT NULL,
    showtime_id INT NOT NULL,
    actual_price DECIMAL(10, 2),
    PRIMARY KEY (invoice_id, seat_id, showtime_id),
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
    FOREIGN KEY (seat_id) REFERENCES seats(seat_id),
    FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id)
);

-- 13. Products
CREATE TABLE products (
    item_id INT IDENTITY PRIMARY KEY,
    item_name VARCHAR(255),
    price DECIMAL(18, 2),
    stock_quantity INT,
    img_user_url VARCHAR(2000)
);

-- 14. Product Details (Food/Drink Orders)
CREATE TABLE products_details (
    invoice_id INT,
    item_id INT,
    quantity INT,
    PRIMARY KEY (invoice_id, item_id),
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
    FOREIGN KEY (item_id) REFERENCES products(item_id)
);

-- 15. Chat Messages
CREATE TABLE chat_messages (
    id INT IDENTITY PRIMARY KEY,
    session_id VARCHAR(100) NOT NULL,
    user_id INT NULL,
    role VARCHAR(50) NOT NULL,
    content VARCHAR(MAX) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_profiles(user_id) ON DELETE SET NULL
);

-- Views for DAO verification
CREATE VIEW vw_movie_showtime_detail AS
SELECT
    s.showtime_id,              
    m.movie_id,
    m.title AS movie_title,
    s.show_date,                
    ts.slot_id,
    ts.slot_name,
    ts.start_time,
    ts.end_time,
    h.hall_id,
    h.hall_name,
    STRING_AGG(g.genre_name, ', ') AS genres
FROM showtimes s
JOIN movies m ON s.movie_id = m.movie_id
JOIN time_slots ts ON s.slot_id = ts.slot_id
JOIN cinema_halls h ON s.hall_id = h.hall_id
LEFT JOIN movie_genre_rel mgr ON m.movie_id = mgr.movie_id
LEFT JOIN movie_genres g ON mgr.genre_id = g.genre_id
GROUP BY
    s.showtime_id, s.show_date, m.movie_id, m.title, ts.slot_id, ts.slot_name,
    ts.start_time, ts.end_time, h.hall_id, h.hall_name;

CREATE VIEW vw_sold_tickets AS
SELECT
    td.invoice_id,
    td.seat_id,
    td.showtime_id,
    td.actual_price,
    i.booking_time
FROM ticket_details td
JOIN invoices i ON td.invoice_id = i.invoice_id
WHERE i.status = 'Paid';

CREATE VIEW vw_movie_ticket_paging_5 AS
WITH movie_stats AS (
    SELECT
        m.movie_id,
        m.title AS movie_title,
        COUNT(td.seat_id) AS tickets_sold,
        SUM(td.actual_price) AS movie_revenue
    FROM movies m
    JOIN showtimes s ON m.movie_id = s.movie_id
    JOIN ticket_details td ON s.showtime_id = td.showtime_id
    JOIN invoices i ON td.invoice_id = i.invoice_id
    WHERE i.status = 'Paid'
      AND MONTH(i.booking_time) = MONTH(CURRENT_TIMESTAMP)
      AND YEAR(i.booking_time) = YEAR(CURRENT_TIMESTAMP)
    GROUP BY m.movie_id, m.title
),
ranked_movies AS (
    SELECT
        movie_id, movie_title, tickets_sold, movie_revenue,
        ROW_NUMBER() OVER (ORDER BY tickets_sold DESC, movie_revenue DESC) AS row_num
    FROM movie_stats
)
SELECT
    movie_id, movie_title, tickets_sold, movie_revenue, row_num,
    ((row_num - 1) / 5) + 1 AS page_number
FROM ranked_movies;

CREATE VIEW vw_ticket_sold_by_slot_current_month AS
SELECT
    ts.slot_id, ts.slot_name, ts.start_time, ts.end_time,
    COUNT(td.seat_id) AS tickets_sold,
    SUM(td.actual_price) AS slot_revenue
FROM invoices i
JOIN ticket_details td ON i.invoice_id = td.invoice_id
JOIN showtimes s ON i.showtime_id = s.showtime_id
JOIN time_slots ts ON s.slot_id = ts.slot_id
WHERE i.status = 'Paid'
  AND MONTH(i.booking_time) = MONTH(CURRENT_TIMESTAMP)
  AND YEAR(i.booking_time) = YEAR(CURRENT_TIMESTAMP)
GROUP BY ts.slot_id, ts.slot_name, ts.start_time, ts.end_time;

CREATE VIEW vw_seat_coverage_detail AS
SELECT
    s.show_date,
    YEAR(s.show_date) AS "year",
    MONTH(s.show_date) AS "month",
    DAY(s.show_date) AS "day",
    m.movie_id,
    m.title AS movie_title,
    h.hall_id,
    h.hall_name,
    h.total_rows * h.total_cols AS total_seats,
    ts.slot_id,
    ts.slot_name,
    ts.start_time,
    ts.end_time,
    COUNT(td.seat_id) AS seats_sold,
    CAST(COUNT(td.seat_id) * 100.0 / NULLIF(h.total_rows * h.total_cols, 0) AS DECIMAL(5,2)) AS seat_coverage_percent
FROM showtimes s
JOIN movies m ON s.movie_id = m.movie_id
JOIN cinema_halls h ON s.hall_id = h.hall_id
JOIN time_slots ts ON s.slot_id = ts.slot_id
LEFT JOIN ticket_details td ON s.showtime_id = td.showtime_id
LEFT JOIN invoices i ON td.invoice_id = i.invoice_id AND i.status = 'Paid'
GROUP BY
    s.show_date, m.movie_id, m.title, h.hall_id, h.hall_name, h.total_rows, h.total_cols,
    ts.slot_id, ts.slot_name, ts.start_time, ts.end_time;

CREATE VIEW vw_kpi_timeslot_revenue AS
SELECT
    s.show_date,
    ts.slot_id,
    ts.slot_name,
    ts.start_time,
    ts.end_time,
    COUNT(td.seat_id) AS tickets_sold,
    SUM(i.total_amount) AS revenue
FROM showtimes s
JOIN time_slots ts ON s.slot_id = ts.slot_id
JOIN invoices i ON s.showtime_id = i.showtime_id AND i.status = 'Paid'
JOIN ticket_details td ON i.invoice_id = td.invoice_id
GROUP BY s.show_date, ts.slot_id, ts.slot_name, ts.start_time, ts.end_time;
