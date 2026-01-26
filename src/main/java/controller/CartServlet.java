package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.Product;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                showCart(request, response);
            } else {
                switch (action) {
                    case "view":
                        showCart(request, response);
                        break;
                    case "remove":
                        removeFromCart(request, response);
                        break;
                    case "clear":
                        clearCart(request, response);
                        break;
                    default:
                        showCart(request, response);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/views/user/cart.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                showCart(request, response);
            } else {
                switch (action) {
                    case "add":
                        addToCart(request, response);
                        break;
                    case "update":
                        updateCart(request, response);
                        break;
                    default:
                        showCart(request, response);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/views/user/cart.jsp").forward(request, response);
        }
    }

    // Thêm sản phẩm vào giỏ hàng
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            HttpSession session = request.getSession();
            
            // Lấy giỏ hàng từ session (Map<itemId, quantity>)
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            
            if (cart == null) {
                cart = new HashMap<>();
            }
            
            // Kiểm tra tồn kho
            Product product = productDAO.findById(itemId);
            if (product == null) {
                session.setAttribute("errorMessage", "Sản phẩm không tồn tại!");
                response.sendRedirect(request.getContextPath() + "/product");
                return;
            }
            
            int currentQty = cart.getOrDefault(itemId, 0);
            int newQty = currentQty + quantity;
            
            if (newQty > product.getStockQuantity()) {
                session.setAttribute("errorMessage", "Số lượng vượt quá tồn kho! Chỉ còn " + product.getStockQuantity() + " sản phẩm.");
                response.sendRedirect(request.getContextPath() + "/product");
                return;
            }
            
            cart.put(itemId, newQty);
            session.setAttribute("cart", cart);
            
            // DEBUG
            System.out.println("===== Thêm vào giỏ =====");
            System.out.println("Item ID: " + itemId);
            System.out.println("Số lượng thêm: " + quantity);
            System.out.println("Tổng trong giỏ: " + newQty);
            System.out.println("========================");
            
            session.setAttribute("successMessage", "Đã thêm " + product.getItemName() + " vào giỏ hàng!");
            response.sendRedirect(request.getContextPath() + "/product");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // Hiển thị giỏ hàng
    private void showCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            
            // DEBUG
            System.out.println("===== Xem giỏ hàng =====");
            System.out.println("Cart null? " + (cart == null));
            if (cart != null) {
                System.out.println("Số mặt hàng: " + cart.size());
            }
            
            if (cart != null && !cart.isEmpty()) {
                // Lấy thông tin chi tiết sản phẩm
                Map<Product, Integer> cartDetails = new HashMap<>();
                
                for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                    Product product = productDAO.findById(entry.getKey());
                    if (product != null) {
                        cartDetails.put(product, entry.getValue());
                        System.out.println("- " + product.getItemName() + " x" + entry.getValue());
                    }
                }
                
                request.setAttribute("cartDetails", cartDetails);
            }
            System.out.println("========================");
            
            request.getRequestDispatcher("/views/user/cart.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // Cập nhật số lượng trong giỏ
    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            HttpSession session = request.getSession();
            
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            
            if (cart != null) {
                // Kiểm tra tồn kho
                Product product = productDAO.findById(itemId);
                if (product != null && quantity <= product.getStockQuantity() && quantity > 0) {
                    cart.put(itemId, quantity);
                    session.setAttribute("cart", cart);
                    session.setAttribute("successMessage", "Đã cập nhật giỏ hàng!");
                    
                    // DEBUG
                    System.out.println("===== Cập nhật giỏ =====");
                    System.out.println("Item ID: " + itemId);
                    System.out.println("Số lượng mới: " + quantity);
                    System.out.println("========================");
                } else {
                    session.setAttribute("errorMessage", "Số lượng không hợp lệ hoặc vượt quá tồn kho!");
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // Xóa sản phẩm khỏi giỏ
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            
            HttpSession session = request.getSession();
            
            @SuppressWarnings("unchecked")
            Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
            
            if (cart != null) {
                cart.remove(itemId);
                session.setAttribute("cart", cart);
                session.setAttribute("successMessage", "Đã xóa sản phẩm khỏi giỏ hàng!");
                
                // DEBUG
                System.out.println("===== Xóa khỏi giỏ =====");
                System.out.println("Đã xóa Item ID: " + itemId);
                System.out.println("Còn lại: " + cart.size() + " mặt hàng");
                System.out.println("========================");
            }
            
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // Xóa toàn bộ giỏ hàng
    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("cart");
        session.setAttribute("successMessage", "Đã xóa toàn bộ giỏ hàng!");
        
        // DEBUG
        System.out.println("===== Xóa toàn bộ giỏ =====");
        System.out.println("Giỏ hàng đã được làm trống");
        System.out.println("===========================");
        
        response.sendRedirect(request.getContextPath() + " /cart");
    }
}