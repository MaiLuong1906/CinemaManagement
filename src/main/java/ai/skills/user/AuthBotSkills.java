package ai.skills.user;

import dao.UserDAO;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.servlet.http.HttpSession;
import model.UserDTO;
import model.UserProfile;
import java.time.LocalDate;

/**
 * Tools for AI agent to handle Authentication (Login, Register) directly in chat.
 */
public class AuthBotSkills {

    private final HttpSession session;

    public AuthBotSkills(HttpSession session) {
        this.session = session;
    }

    @Tool("Kiểm tra trạng thái đăng nhập hiện tại")
    public String checkLoginStatus() {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user != null) {
            return String.format("Bạn đã đăng nhập với tên %s (Số điện thoại: %s).", user.getFullName(), user.getPhoneNumber());
        }
        return "Bạn hiện đang là khách (Guest) và chưa đăng nhập.";
    }

    @Tool("Hiển thị form đăng nhập cho người dùng thay vì hỏi từng thông tin. Gọi khi người dùng muốn đăng nhập (login).")
    public String requireLoginForm() {
        session.setAttribute("pendingWidget", "{\"actionType\": \"LOGIN_FORM\"}");
        return "Form đăng nhập đã được hiển thị trên màn hình. Hãy yêu cầu người dùng điền thông tin vào form thiết kế sẵn.";
    }

    @Tool("Thực hiện đăng nhập người dùng bằng số điện thoại và mật khẩu")
    public String loginUser(@P("Số điện thoại của người dùng") String phoneNumber, 
                            @P("Mật khẩu của người dùng") String password) {
        try {
            UserDTO user = UserDAO.login(phoneNumber, password);
            if (user != null) {
                if (!user.isStatus()) {
                    return "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ hỗ trợ.";
                }
                
                // [CHAT-PERSISTENCE] Identify that we are transitioning from guest to user
                UserDTO currentSessionUser = (UserDTO) session.getAttribute("user");
                if (currentSessionUser == null) {
                    session.setAttribute("preLoginUserId", 0); // Guest UID is 0
                }
                
                // Set session attributes
                session.setAttribute("user", user);
                session.setAttribute("userProfile", new UserProfile(user.getProfileId(), user.getFullName(),
                        user.getEmail(), user.isGender(), user.getAddress(), user.getDateOfBirth()));

                // Emit GEN UI Widget for successful login to trigger frontend reload/refresh
                session.setAttribute("pendingWidget", "{\"actionType\": \"LOGIN_SUCCESS\"}");

                return String.format("Chào mừng %s! Bạn đã đăng nhập thành công. Hệ thống sẽ tự động làm mới trang sau giây lát.", user.getFullName());
            }
            return "Số điện thoại hoặc mật khẩu không chính xác. Vui lòng thử lại.";
        } catch (Exception e) {
            return "Đã xảy ra lỗi khi đăng nhập: " + e.getMessage();
        }
    }

    @Tool("Thực hiện đăng ký tài khoản mới cho khách hàng")
    public String registerUser(
            @P("Họ và tên") String fullName,
            @P("Số điện thoại (10-11 số)") String phoneNumber,
            @P("Địa chỉ Email") String email,
            @P("Mật khẩu (tối thiểu 6 ký tự, gồm chữ hoa, chữ thường và số)") String password,
            @P("Giới tính (nam/nữ)") String gender,
            @P("Ngày sinh (định dạng YYYY-MM-DD)") String dob,
            @P("Địa chỉ liên lạc") String address) {
        
        try {
            if (!UserDAO.checkPhoneNumber(phoneNumber)) {
                return "Số điện thoại này đã tồn tại trên hệ thống.";
            }

            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(dob);
                if (birthDate.isAfter(LocalDate.now())) {
                    return "Ngày sinh không thể ở tương lai.";
                }
            } catch (Exception e) {
                return "Định dạng ngày sinh không hợp lệ. Vui lòng dùng YYYY-MM-DD.";
            }

            boolean genderValue = !"nữ".equalsIgnoreCase(gender);
            
            boolean success = UserDAO.register(phoneNumber, email, password, fullName, genderValue, address, birthDate);
            
            if (success) {
                return String.format(
                    "{\"actionType\": \"REGISTER_SUCCESS\", \"message\": \"Chúc mừng %s, bạn đã đăng ký tài khoản thành công! Bây giờ bạn có thể đăng nhập.\"}",
                    fullName
                );
            }
            return "Đăng ký không thành công. Vui lòng kiểm tra lại thông tin.";
        } catch (Exception e) {
            return "Lỗi đăng ký: " + e.getMessage();
        }
    }
}
