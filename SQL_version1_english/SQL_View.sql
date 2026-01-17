CREATE VIEW vw_movie_showtime_basic
AS
SELECT
    m.movie_id            AS movieId,
    m.title               AS movieTitle,
    h.hall_name           AS hallName,
    g.genre_name          AS genreName,
    m.age_rating          AS ageRating,
    m.description         AS description,
    s.start_time          AS startTime,
    m.duration            AS duration
FROM movies m
JOIN showtimes s        ON m.movie_id = s.movie_id
JOIN cinema_halls h     ON s.hall_id = h.hall_id
JOIN movie_genre_rel r  ON m.movie_id = r.movie_id
JOIN movie_genres g     ON r.genre_id = g.genre_id;
GO
