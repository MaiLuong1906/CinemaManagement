package service;

import dao.ProductDAO;
import model.Product;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private ProductDAO productDAO;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartService cartService;

    private Map<Integer, Integer> cartMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartMap = new HashMap<>();
        when(session.getAttribute("cart")).thenReturn(cartMap);
    }

    @Test
    void testAddToCart_Success() throws SQLException {
        Product product = new Product();
        product.setItemId(1);
        product.setStockQuantity(10);
        when(productDAO.findById(1)).thenReturn(product);

        cartService.addToCart(session, 1, 2);

        assertEquals(2, cartMap.get(1));
    }

    @Test
    void testAddToCart_InsufficientStock() throws SQLException {
        Product product = new Product();
        product.setItemId(1);
        product.setStockQuantity(5);
        when(productDAO.findById(1)).thenReturn(product);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addToCart(session, 1, 6);
        });

        assertTrue(exception.getMessage().contains("Số lượng vượt quá tồn kho"));
    }

    @Test
    void testUpdateCart_Success() throws SQLException {
        Product product = new Product();
        product.setItemId(1);
        product.setStockQuantity(10);
        when(productDAO.findById(1)).thenReturn(product);
        cartMap.put(1, 2);

        cartService.updateCart(session, 1, 5);

        assertEquals(5, cartMap.get(1));
    }

    @Test
    void testRemoveFromCart() {
        cartMap.put(1, 2);
        cartService.removeFromCart(session, 1);
        assertFalse(cartMap.containsKey(1));
    }

    @Test
    void testClearCart() {
        cartService.clearCart(session);
        verify(session).removeAttribute("cart");
    }
}
