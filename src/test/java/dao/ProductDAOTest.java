package dao;

import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDAOTest extends BaseDAOTest {

    private ProductDAO productDAO;

    @BeforeEach
    public void setup() throws Exception {
        productDAO = new ProductDAO();
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM products_details");
            stmt.execute("DELETE FROM products");
            
            stmt.execute("INSERT INTO products (item_id, item_name, price, stock_quantity, img_user_url) " +
                         "VALUES (1, 'Popcorn', 50000.00, 100, 'popcorn.jpg')");
            stmt.execute("INSERT INTO products (item_id, item_name, price, stock_quantity, img_user_url) " +
                         "VALUES (2, 'Cola', 30000.00, 200, 'cola.jpg')");

            stmt.execute("ALTER TABLE products ALTER COLUMN item_id RESTART WITH 10");
        }
    }

    @Test
    public void testFindById() throws Exception {
        Product p = productDAO.findById(1);
        assertNotNull(p);
        assertEquals("Popcorn", p.getItemName());
        assertEquals(0, new BigDecimal("50000.00").compareTo(p.getPrice()));
        
        assertNull(productDAO.findById(999));
    }

    @Test
    public void testFindAll() throws Exception {
        List<Product> list = productDAO.findAll();
        assertEquals(2, list.size());
    }
    
    @Test
    public void testInsertUpdateDelete() throws Exception {
        // Test Insert
        Product p = new Product();
        p.setItemName("Combo 1");
        p.setPrice(new BigDecimal("100000.00"));
        p.setStockQuantity(50);
        p.setProductImgUrl("combo1.jpg");
        
        productDAO.insert(p);
        
        List<Product> all = productDAO.findAll();
        assertEquals(3, all.size());
        Product inserted = all.get(2);
        assertEquals("Combo 1", inserted.getItemName());
        
        // Test Update
        inserted.setStockQuantity(60);
        productDAO.update(inserted);
        
        Product updated = productDAO.findById(inserted.getItemId());
        assertEquals(60, updated.getStockQuantity());
        
        // Test Delete
        productDAO.delete(updated.getItemId());
        assertNull(productDAO.findById(updated.getItemId()));
    }
    
    @Test
    public void testUpdateStock() throws Exception {
        try (Connection conn = DBConnect.getConnection()) {
            productDAO.updateStock(conn, 1, -10);
        }
        
        Product updated = productDAO.findById(1);
        assertEquals(90, updated.getStockQuantity()); // 100 - 10
        
        try (Connection conn = DBConnect.getConnection()) {
            productDAO.updateStock(conn, 1, 20);
        }
        
        Product updated2 = productDAO.findById(1);
        assertEquals(110, updated2.getStockQuantity()); // 90 + 20
    }
}
