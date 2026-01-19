-- Check trùng suất chiếu theo NGÀY + PHÒNG + SLOT
CREATE OR ALTER PROCEDURE sp_InsertShowtime
    @movie_id   INT,
    @hall_id    INT,
    @show_date  DATE,
    @slot_id    INT
    AS
BEGIN
    SET NOCOUNT ON;

BEGIN TRY
BEGIN TRAN;

        /* =========================
           1. CHECK PHIM TỒN TẠI
           ========================= */
        IF NOT EXISTS (
            SELECT 1 FROM movies WHERE movie_id = @movie_id
        )
BEGIN
            THROW 50001, N'Phim không tồn tại', 1;
END

        /* =========================
           2. CHECK PHÒNG TỒN TẠI
           ========================= */
        IF NOT EXISTS (
            SELECT 1 FROM cinema_halls WHERE hall_id = @hall_id
        )
BEGIN
            THROW 50002, N'Phòng chiếu không tồn tại', 1;
END

        /* =========================
           3. CHECK SLOT TỒN TẠI
           ========================= */
        IF NOT EXISTS (
            SELECT 1 FROM time_slots WHERE slot_id = @slot_id
        )
BEGIN
            THROW 50003, N'Khung giờ chiếu không tồn tại', 1;
END

        /* =========================
           4. CHECK TRÙNG SUẤT CHIẾU
           ========================= */
        IF EXISTS (
            SELECT 1
            FROM showtimes
            WHERE hall_id   = @hall_id
              AND show_date = @show_date
              AND slot_id   = @slot_id
        )
BEGIN
            THROW 50004, N'Phòng đã có suất chiếu ở khung giờ này', 1;
END

        /* =========================
           5. INSERT SHOWTIME
           ========================= */
INSERT INTO showtimes (
    movie_id,
    hall_id,
    show_date,
    slot_id
)
VALUES (
           @movie_id,
           @hall_id,
           @show_date,
           @slot_id
       );

COMMIT;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0
            ROLLBACK;
        THROW;
END CATCH
END;
