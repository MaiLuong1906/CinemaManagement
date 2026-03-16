package dao;

import model.Account;
import model.UserProfile;
import model.MovieDetailDTO;
import model.ProductDetail;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SupportingDAOTest extends BaseDAOTest {

    @Test
    public void testAccountAndProfile() throws SQLException {
        AccountDAO accountDAO = new AccountDAO();
        UserProfileDAO profileDAO = new UserProfileDAO();

        try (Connection conn = DBConnect.getConnection()) {
            // 1. Account Insert
            int accId = accountDAO.insert(conn, "0987654321", "hash123");
            assertTrue(accId > 0);

            // 2. Profile Insert
            UserProfile profile = new UserProfile();
            profile.setUserId(accId);
            profile.setFullName("Test Support");
            profile.setEmail("support@test.com");
            profile.setGender(true);
            profile.setAddress("Test Address");
            profile.setDateOfBirth(java.time.LocalDate.of(1995, 5, 5));
            profileDAO.insert(conn, profile);

            // 3. Verify Profile
            UserProfile savedProfile = profileDAO.findByUserId(accId);
            assertNotNull(savedProfile);
            assertEquals("Test Support", savedProfile.getFullName());

            // 4. Verify Account
            Account savedAcc = accountDAO.findByPhone(conn, "0987654321");
            assertNotNull(savedAcc);
            assertEquals(accId, savedAcc.getId());

            // 5. Update Profile
            savedProfile.setFullName("Updated Support");
            profileDAO.update(savedProfile);
            assertEquals("Updated Support", profileDAO.findByUserId(accId).getFullName());
        }
    }

    @Test
    public void testMovieDetailDAO() throws SQLException {
        MovieDetailDAO dao = new MovieDetailDAO();
        
        try (Connection conn = DBConnect.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            // Setup parent data
            stmt.execute("INSERT INTO movies (movie_id, title) VALUES (100, 'Detailed Movie')");
            stmt.execute("INSERT INTO cinema_halls (hall_id, hall_name, total_rows, total_cols) VALUES (100, 'Detail Hall', 5, 5)");
            stmt.execute("INSERT INTO time_slots (slot_id, slot_name, start_time, end_time, slot_price) VALUES (100, 'Detail Slot', '10:00:00', '12:00:00', 50.0)");
            stmt.execute("INSERT INTO showtimes (showtime_id, movie_id, hall_id, show_date, slot_id) VALUES (100, 100, 100, '2026-03-25', 100)");
            
            // Verify View through DAO
            MovieDetailDTO detail = dao.getByShowtimeId(100);
            assertNotNull(detail);
            assertEquals("Detailed Movie", detail.getMovieTitle());
            assertEquals("Detail Hall", detail.getHallName());
        }
    }

    @Test
    public void testProductDetailDAO() throws SQLException {
        ProductDetailDAO dao = new ProductDetailDAO();

        try (Connection conn = DBConnect.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            // Setup Invoice and Product
            stmt.execute("INSERT INTO accounts (account_id, phone_number, password_hash) VALUES (200, '222', 'h')");
            stmt.execute("INSERT INTO user_profiles (user_id, full_name, email, gender, date_of_birth) VALUES (200, 'u', 'u@m.com', true, '2000-01-01')");
            stmt.execute("INSERT INTO movies (movie_id, title) VALUES (200, 'm')");
            stmt.execute("INSERT INTO cinema_halls (hall_id, hall_name, total_rows, total_cols) VALUES (200, 'h', 1, 1)");
            stmt.execute("INSERT INTO time_slots (slot_id, start_time, end_time, slot_price) VALUES (200, '1:0:0', '2:0:0', 1)");
            stmt.execute("INSERT INTO showtimes (showtime_id, movie_id, hall_id, show_date, slot_id) VALUES (200, 200, 200, '2026-03-25', 200)");
            stmt.execute("INSERT INTO invoices (invoice_id, user_id, showtime_id, total_amount) VALUES (200, 200, 200, 50.0)");
            stmt.execute("INSERT INTO products (item_id, item_name, price, stock_quantity) VALUES (200, 'Drink', 5.0, 100)");

            // Test Insert
            ProductDetail pd = new ProductDetail();
            pd.setInvoiceId(200);
            pd.setItemId(200);
            pd.setQuantity(2);
            dao.insert(conn, pd);

            // Verify
            List<ProductDetail> details = dao.findByInvoiceId(200);
            assertEquals(1, details.size());
            assertEquals(200, details.get(0).getItemId());
            assertEquals(2, details.get(0).getQuantity());
        }
    }
}
