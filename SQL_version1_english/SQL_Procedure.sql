
-- check trung time (Dat dung tam de build)
CREATE OR ALTER PROCEDURE sp_InsertShowtime
    @movie_id   INT,
    @hall_id    INT,
    @start_time DATETIME,
    @base_price DECIMAL(10,2)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRAN;

        DECLARE @new_duration INT;
        DECLARE @new_end_time DATETIME;

        /* =========================
           1. LẤY DURATION PHIM MỚI
           ========================= */
        SELECT @new_duration = duration
        FROM movies
        WHERE movie_id = @movie_id;

        IF @new_duration IS NULL
        BEGIN
            THROW 50002, N'Phim không tồn tại hoặc chưa có duration', 1;
        END

        SET @new_end_time = DATEADD(MINUTE, @new_duration, @start_time);

        /* =========================
           2. CHECK TRÙNG SUẤT CHIẾU
           ========================= */
        IF EXISTS (
            SELECT 1
            FROM showtimes s
            JOIN movies m ON s.movie_id = m.movie_id
            WHERE s.hall_id = @hall_id
              AND (
                    @start_time < DATEADD(MINUTE, m.duration, s.start_time)
                AND @new_end_time > s.start_time
              )
        )
        BEGIN
            THROW 50001, N'Phòng chiếu đã có suất chiếu trong khung giờ này', 1;
        END

        /* =========================
           3. INSERT SUẤT CHIẾU
           ========================= */
        INSERT INTO showtimes (
            movie_id,
            hall_id,
            start_time,
            base_price
        )
        VALUES (
            @movie_id,
            @hall_id,
            @start_time,
            @base_price
        );

        COMMIT;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK;
        THROW;
    END CATCH
END;
