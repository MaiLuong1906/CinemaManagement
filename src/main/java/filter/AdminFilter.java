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
import model.UserDTO;

/**
 *
 * @author LENOVO
 */

@WebFilter(urlPatterns = {
        // Admin pages
        "/admin/*",
        "/views/admin/*",

        // Admin servlets - Statistics
        "/admin/statistic"
})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpServletResponse response = (HttpServletResponse) sr1;
        HttpSession session = request.getSession(false);

        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user != null && "Admin".equalsIgnoreCase(user.getRoleId())) {
            fc.doFilter(sr, sr1);
        } else {
            // Không phải Admin -> Báo lỗi 403 (Cấm truy cập)
            response.setStatus(403);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("<h1>403 - Access Denied</h1><p>Bạn không có quyền truy cập trang này.</p>");
        }
    }

}
