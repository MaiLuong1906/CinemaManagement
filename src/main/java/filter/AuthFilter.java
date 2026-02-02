/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author LENOVO
 */

@WebFilter(urlPatterns = {
        // User servlets - Booking and payment
        "/seat-selection",
        "/hold-seats",
        "/confirm-booking",
        "/payment1",
        "/payment-return",

        // User servlets - Profile and cart
        "/update-profile",
        "/cart",
        "/ajaxServlet",

        // User pages - JSP
        "/views/user/booking.jsp",
        "/views/user/my-bookings.jsp",
        "/views/user/profile.jsp",
        "/views/user/payment.jsp",
        "/views/user/seat-selection.jsp"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpServletResponse response = (HttpServletResponse) sr1;

        HttpSession session = request.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (isLoggedIn) {
            fc.doFilter(sr, sr1);
        } else {
            String requestURI = request.getRequestURI();
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
        }
    }

}
