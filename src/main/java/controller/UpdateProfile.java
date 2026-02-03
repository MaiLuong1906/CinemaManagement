package controller;

import dao.UserProfileDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserProfile;
import model.UserDTO;
import exception.ValidationException;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * User profile update servlet
 * Extends BaseServlet for parameter extraction and error handling
 */
@WebServlet("/update-profile")
public class UpdateProfile extends BaseServlet {

    @Override
    protected boolean requiresAuthentication() {
        return true;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        String fullName = getStringParam(request, "fullName");
        String email = getStringParam(request, "email");
        String gender = getStringParam(request, "gender");
        LocalDate dateOfBirth = getDateParam(request, "dob");
        String address = getStringParam(request, "address", "");

        // Validate
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new ValidationException("Invalid email");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }

        UserProfileDAO udao = new UserProfileDAO();

        UserProfile userProfileUpdate = new UserProfile(user.getProfileId(), fullName, email, gender.equals("male"),
                address, dateOfBirth);

        int rowsAffected = udao.update(userProfileUpdate);

        if (rowsAffected == 0) {
            request.setAttribute("error",
                    "Không tìm thấy thông tin người dùng trong database. Vui lòng đăng nhập lại.");
            forward(request, response, "/views/user/profile.jsp");
            return;
        }

        // Update UserDTO object with new information
        user.setFullName(fullName);
        user.setEmail(email);
        user.setGender(gender.equals("male"));
        user.setAddress(address);
        user.setDateOfBirth(dateOfBirth);

        // Update both session attributes
        session.setAttribute("user", user);
        session.setAttribute("userProfile", userProfileUpdate);

        request.setAttribute("success", "Cập nhật thông tin thành công!");
        forward(request, response, "/views/user/profile.jsp");
    }

}