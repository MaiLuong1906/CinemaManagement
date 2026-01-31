package service;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//Bazero
public class CartService {

    private final ProductDAO productDAO = new ProductDAO();

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> getCart(HttpSession session) {
        Map<Integer, Integer> cart =
                (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // =====================
    // ADD
    // =====================
    public void addToCart(HttpSession session, int itemId, int quantity) throws SQLException {
        Map<Integer, Integer> cart = getCart(session);

        Product product = productDAO.findById(itemId);
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }

        int currentQty = cart.getOrDefault(itemId, 0);
        int newQty = currentQty + quantity;

        if (newQty > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                "Số lượng vượt quá tồn kho! Chỉ còn " + product.getStockQuantity()
            );
        }

        cart.put(itemId, newQty);
    }

    // =====================
    // UPDATE
    // =====================
    public void updateCart(HttpSession session, int itemId, int quantity) throws SQLException {
        Map<Integer, Integer> cart = getCart(session);

        Product product = productDAO.findById(itemId);
        if (product == null || quantity <= 0 || quantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Số lượng không hợp lệ");
        }

        cart.put(itemId, quantity);
    }

    // =====================
    // REMOVE
    // =====================
    public void removeFromCart(HttpSession session, int itemId) {
        Map<Integer, Integer> cart = getCart(session);
        cart.remove(itemId);
    }

    // =====================
    // CLEAR
    // =====================
    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    // =====================
    // VIEW
    // =====================
    public Map<Product, Integer> getCartDetails(HttpSession session) throws SQLException {
        Map<Integer, Integer> cart =
                (Map<Integer, Integer>) session.getAttribute("cart");

        Map<Product, Integer> details = new HashMap<>();
        if (cart == null) return details;

        for (Map.Entry<Integer, Integer> e : cart.entrySet()) {
            Product p = productDAO.findById(e.getKey());
            if (p != null) {
                details.put(p, e.getValue());
            }
        }
        return details;
    }
}