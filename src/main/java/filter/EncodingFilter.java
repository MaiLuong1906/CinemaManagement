package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class EncodingFilter implements Filter {
    
    private static final String ENCODING = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // 1. Set UTF-8 cho dữ liệu gửi lên (Request)
        request.setCharacterEncoding(ENCODING);
        
        // 2. Set UTF-8 cho dữ liệu trả về (Response)
        response.setCharacterEncoding(ENCODING);
//        response.setContentType("text/html; charset=" + ENCODING);
        
        // 3. Cho phép đi tiếp
        chain.doFilter(request, response);
    }
}