-- view cho chuc nang list phim cho admin
CREATE VIEW vw_movie_showtime_detail
AS
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
    STRING_AGG(g.genre_name, N', ') AS genres
FROM showtimes s
JOIN movies m 
    ON s.movie_id = m.movie_id
JOIN time_slots ts 
    ON s.slot_id = ts.slot_id
JOIN cinema_halls h 
    ON s.hall_id = h.hall_id
LEFT JOIN movie_genre_rel mgr 
    ON m.movie_id = mgr.movie_id
LEFT JOIN movie_genres g 
    ON mgr.genre_id = g.genre_id
GROUP BY
    s.showtime_id,
    s.show_date,
    m.movie_id,
    m.title,
    ts.slot_id,
    ts.slot_name,
    ts.start_time,
    ts.end_time,
    h.hall_id,
    h.hall_name;
GO
-- view ve da ban, phuc vu cho statistic
CREATE VIEW vw_sold_tickets
AS
SELECT
    td.invoice_id,
    td.seat_id,
    td.showtime_id,
    td.actual_price,
    i.booking_time
FROM ticket_details td
JOIN invoices i ON td.invoice_id = i.invoice_id
WHERE i.status = 'Paid';
GO

-- view phim va so ve ban duoc : phan vung theo cum 10 records va sap xep tu cao den thap (lay theo thang hien tai)
CREATE VIEW vw_movie_ticket_paging_5
AS
WITH movie_stats AS (
    SELECT
        m.movie_id,
        m.title AS movie_title,
        COUNT(td.seat_id) AS tickets_sold,
        SUM(td.actual_price) AS movie_revenue
    FROM movies m
    JOIN showtimes s
        ON m.movie_id = s.movie_id
    JOIN ticket_details td
        ON s.showtime_id = td.showtime_id
    JOIN invoices i
        ON td.invoice_id = i.invoice_id
    WHERE
        i.status = N'Paid'
        AND MONTH(i.booking_time) = MONTH(GETDATE())
        AND YEAR(i.booking_time) = YEAR(GETDATE())
    GROUP BY
        m.movie_id,
        m.title
),
ranked_movies AS (
    SELECT
        movie_id,
        movie_title,
        tickets_sold,
        movie_revenue,
        ROW_NUMBER() OVER (
            ORDER BY tickets_sold DESC, movie_revenue DESC
        ) AS row_num
    FROM movie_stats
)
SELECT
    movie_id,
    movie_title,
    tickets_sold,
    movie_revenue,
    row_num,

    -- mỗi page 5 phim
    ((row_num - 1) / 5) + 1 AS page_number
FROM ranked_movies;
GO
-- ve theo khung gio trong thang hien tai
CREATE VIEW vw_ticket_sold_by_slot_current_month
AS
SELECT
    ts.slot_id,
    ts.slot_name,
    ts.start_time,
    ts.end_time,

    COUNT(td.seat_id) AS tickets_sold,

    SUM(td.actual_price) AS slot_revenue

FROM invoices i
JOIN ticket_details td
    ON i.invoice_id = td.invoice_id
JOIN showtimes s
    ON i.showtime_id = s.showtime_id
JOIN time_slots ts
    ON s.slot_id = ts.slot_id

WHERE
    i.status = N'Paid'
    AND MONTH(i.booking_time) = MONTH(GETDATE())
    AND YEAR(i.booking_time) = YEAR(GETDATE())

GROUP BY
    ts.slot_id,
    ts.slot_name,
    ts.start_time,
    ts.end_time;
GO
-- độ phủ ghế tổng quát, từ đây có thể mở rộng để lọc theo phim, theo phòng, theo suất chiếu
CREATE OR ALTER VIEW vw_seat_coverage_detail
AS
SELECT
    -- Thời gian
    s.show_date,
    YEAR(s.show_date)  AS year,
    MONTH(s.show_date) AS month,
    DAY(s.show_date)   AS day,

    -- Phim
    m.movie_id,
    m.title AS movie_title,

    -- Phòng
    h.hall_id,
    h.hall_name,
    h.total_rows * h.total_cols AS total_seats,

    -- Khung giờ
    ts.slot_id,
    ts.slot_name,
    ts.start_time,
    ts.end_time,

    -- Số ghế đã bán
    COUNT(td.seat_id) AS seats_sold,

    -- Độ phủ ghế %
    CAST(
        COUNT(td.seat_id) * 100.0 /
        NULLIF(h.total_rows * h.total_cols, 0)
        AS DECIMAL(5,2)
    ) AS seat_coverage_percent

FROM showtimes s
JOIN movies m
    ON s.movie_id = m.movie_id
JOIN cinema_halls h
    ON s.hall_id = h.hall_id
JOIN time_slots ts
    ON s.slot_id = ts.slot_id

LEFT JOIN ticket_details td
    ON s.showtime_id = td.showtime_id
LEFT JOIN invoices i
    ON td.invoice_id = i.invoice_id
    AND i.status = N'Paid'

GROUP BY
    s.show_date,
    m.movie_id, m.title,
    h.hall_id, h.hall_name, h.total_rows, h.total_cols,
    ts.slot_id, ts.slot_name, ts.start_time, ts.end_time;
GO
