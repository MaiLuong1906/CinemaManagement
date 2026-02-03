/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDTO;
import model.UserProfile;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Account management servlet (login, register, logout)
 * Extends BaseServlet for automatic UTF-8 encoding and error handling
 */
@WebServlet("/AccountServlet")
public class AccountServlet extends BaseServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String action = getStringParam(request, "action", "register");

        switch (action) {
            case "login":
                login(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            case "register":
            default:
                register(request, response);
                break;
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");

        UserDTO user = UserDAO.login(phoneNumber, password);

        if (user != null) {
            // --- TRƯỜNG HỢP THÀNH CÔNG ---
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userProfile", new UserProfile(user.getProfileId(), user.getFullName(),
                    user.getEmail(), user.isGender(), user.getAddress(), user.getDateOfBirth()));

            // SỬ DỤNG REDIRECT: Trình duyệt sẽ tải trang mới, URL sẽ đổi
            // request.getContextPath() để lấy tên project (ví dụ: /cinema)
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            // --- TRƯỜNG HỢP THẤT BẠI ---
            request.setAttribute("Error", "Phone Number or password are incorrect !");

            // SỬ DỤNG FORWARD: Để giữ lại thông báo lỗi (request attribute)
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 1. Hủy session hiện tại (Xóa hết thông tin user đã đăng nhập)
        session.invalidate();

        // 2. SỬ DỤNG REDIRECT: Chuyển hướng về trang chủ (hoặc trang login)
        // URL trên trình duyệt sẽ thay đổi về .../home.jsp thay vì .../AccountServlet
        response.sendRedirect(request.getContextPath() + "/home");
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String cpassword = request.getParameter("cpassword");

        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String address = request.getParameter("address");

        request.setAttribute("fullname", fullname);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("email", email);
        request.setAttribute("dob", dob);
        request.setAttribute("gender", gender);
        request.setAttribute("address", address);

        String error = "";

        // Validate
        if (!UserDAO.checkPhoneNumber(phoneNumber)) {
            error += "Phone number already exists<br>";
        }
        if (fullname == null || fullname.trim().isEmpty()) {
            error += "Fullname is empty<br>";
        }
        if (phoneNumber == null || !phoneNumber.matches("[0-9]{10,11}")) {
            error += "Invalid phone number<br>";
        }
        if (email == null || email.trim().isEmpty()
                || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            error += "Invalid email<br>";
        }
        if (password == null || password.trim().isEmpty()
                || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,}$")) {
            error += "Minimum 6 characters with at least one uppercase, one lowercase, and one number.<br>";
        }
        if (!password.equals(cpassword)) {
            error += "Password and confirm password do not match<br>";
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
            request.setAttribute("Error", error);
            request.getRequestDispatcher("views/auth/register.jsp").forward(request, response);
            return;
        }

        // Convert gender
        boolean genderValue = "female".equals(gender);

        boolean success = UserDAO.register(
                phoneNumber,
                email,
                password,
                fullname,
                genderValue,
                address,
                dateOfBirth);

        if (success) {
            response.sendRedirect("views/auth/login.jsp");
        } else {
            request.setAttribute("Error", "Register failed. Please try again.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
        }
    }
}