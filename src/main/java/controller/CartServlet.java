package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.CartService;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() {
        cartService = new CartService();
    }

    // =====================
    // GET
    // =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "view";

        try {
            switch (action) {
                case "remove":
                    remove(request, response);
                    break;
                case "clear":
                    clear(request, response);
                    break;
                default:
                    view(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/views/user/cart.jsp")
                   .forward(request, response);
        }
    }

    // =====================
    // POST
    // =====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    add(request, response);
                    break;
                case "update":
                    update(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/cart");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    // =====================
    // ACTIONS
    // =====================
    private void add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        int qty = Integer.parseInt(request.getParameter("quantity"));

        cartService.addToCart(request.getSession(), itemId, qty);
        request.getSession().setAttribute("successMessage", "Đã thêm vào giỏ hàng");
        response.sendRedirect(request.getContextPath() + "/product");
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        int qty = Integer.parseInt(request.getParameter("quantity"));

        cartService.updateCart(request.getSession(), itemId, qty);
        request.getSession().setAttribute("successMessage", "Đã cập nhật giỏ hàng");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int itemId = Integer.parseInt(request.getParameter("itemId"));

        cartService.removeFromCart(request.getSession(), itemId);
        request.getSession().setAttribute("successMessage", "Đã xóa sản phẩm");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void clear(HttpServletRequest request, HttpServletResponse response) throws Exception {
        cartService.clearCart(request.getSession());
        request.getSession().setAttribute("successMessage", "Đã a toàn bộ giỏ hàng\")xóa toàn bộ giỏ hàng");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void view(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute(
            "cartDetails",
            cartService.getCartDetails(request.getSession())
        );
        request.getRequestDispatcher("/views/user/cart.jsp")
               .forward(request, response);
    }
}