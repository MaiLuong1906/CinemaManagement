package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/support/*")
public class SupportPagesServlet extends BaseServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Default to contact page
            forward(request, response, "/views/user/contact.jsp");
            return;
        }

        switch (pathInfo) {
            case "/contact":
                forward(request, response, "/views/user/contact.jsp");
                break;
            case "/faqs":
                forward(request, response, "/views/user/faqs.jsp");
                break;
            case "/terms":
                forward(request, response, "/views/user/terms.jsp");
                break;
            case "/policy":
                forward(request, response, "/views/user/policy.jsp");
                break;
            default:
                forward(request, response, "/views/user/contact.jsp");
                break;
        }
    }
}
