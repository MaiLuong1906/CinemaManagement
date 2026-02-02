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
@WebServlet("/update-profile")
public class UpdateProfile extends HttpServlet {
    
    private MovieDAO movieDAO = new MovieDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserProfile user = (UserProfile) session.getAttribute("userProfile");
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String address = request.getParameter("address");
        
        String error ="";
        
        if (fullName == null || fullName.trim().isEmpty()) {
            error += "Fullname is empty<br>";
        }
        if (email == null || email.trim().isEmpty()
                || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            error += "Invalid email<br>";
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
            UserProfile userUpdate = new UserProfile(user.getUserId(),fullName, email, gender.equals("male"), address, dateOfBirth);
            udao.update(userUpdate);
            session.setAttribute("userProfile", userUpdate);
            request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
        } catch (SQLException ex) {
            
        }
    }
    
}