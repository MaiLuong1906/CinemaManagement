CREATE OR ALTER VIEW vw_movie_showtime_basic
AS
SELECT
    m.movie_id            AS movieId,
    m.title               AS movieTitle,
    m.poster_url          AS posterUrl,
    m.age_rating          AS ageRating,
    m.description         AS description,

    g.genre_name          AS genreName,

    h.hall_name           AS hallName,

    s.show_date           AS showDate,
    ts.start_time         AS slotStartTime,
    ts.end_time           AS slotEndTime,
    ts.slot_price         AS slotPrice,

    m.duration            AS duration
FROM showtimes s
         JOIN movies m            ON s.movie_id = m.movie_id
         JOIN cinema_halls h      ON s.hall_id  = h.hall_id
         JOIN time_slots ts       ON s.slot_id  = ts.slot_id
         JOIN movie_genre_rel r   ON m.movie_id = r.movie_id
         JOIN movie_genres g      ON r.genre_id = g.genre_id;
GO
