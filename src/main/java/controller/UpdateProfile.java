package controller;

import dao.MovieDAO;
import dao.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import model.UserProfile;
import model.UserDTO;

@WebServlet("/update-profile")
public class UpdateProfile extends HttpServlet {

    private MovieDAO movieDAO = new MovieDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String address = request.getParameter("address");

        String error = "";

        if (fullName == null || fullName.trim().isEmpty()) {
            error += "Fullname is empty<br>";
        }
        if (email == null || email.trim().isEmpty()
                || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            error += "Invalid email<br>";
        }

        if (gender == null || gender.trim().isEmpty()) {
            error += "Gender is required<br>";
        }

        LocalDate dateOfBirth = null;
        if (dob == null || dob.trim().isEmpty()) {
            error += "Date of birth is required<br>";
        } else {
            try {
                dateOfBirth = LocalDate.parse(dob);
                if (dateOfBirth.isAfter(LocalDate.now())) {
                    error += "Date of birth cannot be in the future<br>";
                }
            } catch (Exception e) {
                error += "Invalid date format for date of birth<br>";
            }
        }

        if (!error.isEmpty()) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("views/user/profile.jsp").forward(request, response);
            return;
        }

        UserProfileDAO udao = new UserProfileDAO();

        try {
            UserProfile userProfileUpdate = new UserProfile(user.getProfileId(), fullName, email, gender.equals("male"),
                    address, dateOfBirth);

            int rowsAffected = udao.update(userProfileUpdate);

            if (rowsAffected == 0) {
                request.setAttribute("error",
                        "Không tìm thấy thông tin người dùng trong database. Vui lòng đăng nhập lại.");
                request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
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
            request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
        } catch (SQLException ex) {
            request.setAttribute("error", "Lỗi cập nhật thông tin: " + ex.getMessage());
            request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);

        }
    }

}