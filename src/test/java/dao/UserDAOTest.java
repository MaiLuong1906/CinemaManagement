package dao;

import model.UserDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest extends BaseDAOTest {

    @Test
    public void testRegisterAndLogin() {
        String phone = "0123456789";
        String email = "test@example.com";
        String pass = "password123";
        String name = "Test User";
        LocalDate dob = LocalDate.of(1990, 1, 1);

        // 1. Register
        boolean registered = UserDAO.register(phone, email, pass, name, true, "123 Street", dob);
        assertTrue(registered, "Registration should succeed");

        // 2. Check phone number (should return false because it exists)
        boolean isUnique = UserDAO.checkPhoneNumber(phone);
        assertFalse(isUnique, "Phone number should already exist");

        // 3. Login
        UserDTO user = UserDAO.login(phone, pass);
        assertNotNull(user, "Login should succeed with correct credentials");
        assertEquals(name, user.getFullName());
        assertEquals(email, user.getEmail());

        // 4. Change Password
        String newPass = "newpassword456";
        String currentHash = util.Encoding.toSHA1(pass);
        String newHash = util.Encoding.toSHA1(newPass);
        
        boolean changed = UserDAO.changePassword(user.getAccountId(), currentHash, newHash);
        assertTrue(changed, "Password change should succeed");

        // 5. Login with new password
        UserDTO userNew = UserDAO.login(phone, newPass);
        assertNotNull(userNew, "Login should succeed with new password");
    }

    @Test
    public void testLoginFailure() {
        UserDTO user = UserDAO.login("nonexistent", "wrongpass");
        assertNull(user, "Login should fail for nonexistent user");
    }
}
