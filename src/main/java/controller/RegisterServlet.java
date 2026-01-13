/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 *
 * @author LENOVO
 */
public class RegisterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$")) {
            error += "Invalid password<br>";
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
                dateOfBirth
        );

        if (success) {
            response.sendRedirect("views/auth/login.jsp"); 
        } else {
            request.setAttribute("Error", "Register failed. Please try again.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
