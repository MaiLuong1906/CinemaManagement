package controller;

import java.util.List;

import dao.AccountDAO;
import dao.UserProfileDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserDTO;
import exception.ValidationException;

@WebServlet("/admin/users")
public class AdminUserController extends BaseServlet {

    @Override
    protected boolean requiresAuthentication() {
        return true;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String method = request.getMethod();

        if ("GET".equals(method)) {
            handleGetRequest(request, response);
        } else if ("POST".equals(method)) {
            handlePostRequest(request, response);
        }
    }

    private void handleGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<UserDTO> users = UserProfileDAO.getAllUsers();
        request.setAttribute("users", users);
        forward(request, response, "/views/admin/users/user-list.jsp");
    }

    private void handlePostRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String action = getStringParam(request, "action");
        int id = getIntParam(request, "id");

        AccountDAO dao = new AccountDAO();

        switch (action) {
            case "lock":
                dao.updateStatus(id, false);
                break;
            case "unlock":
                dao.updateStatus(id, true);
                break;
            case "role":
                String role = getStringParam(request, "role");
                dao.updateRole(id, role);
                break;
            default:
                throw new ValidationException("Invalid action: " + action);
        }

        redirect(response, "users");
    }
}
