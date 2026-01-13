package dao;


import model.ProductDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailDAO {

    /* =========================
       INSERT
       ========================= */
    public void insert(Connection conn, ProductDetail pd) throws SQLException {
        String sql = """
            INSERT INTO products_details (invoice_id, item_id, quantity)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pd.getInvoiceId());
            ps.setInt(2, pd.getItemId());
            ps.setInt(3, pd.getQuantity());
            ps.executeUpdate();
        }
    }

    /* =========================
       SELECT BY INVOICE
       ========================= */
    public List<ProductDetail> findByInvoiceId(int invoiceId) throws SQLException {
        List<ProductDetail> list = new ArrayList<>();

        String sql = """
            SELECT invoice_id, item_id, quantity
            FROM products_details
            WHERE invoice_id = ?
        """;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /* =========================
       UPDATE QUANTITY
       ========================= */
    public void updateQuantity(Connection conn,
                               int invoiceId,
                               int itemId,
                               int quantity) throws SQLException {

        String sql = """
            UPDATE products_details
            SET quantity = ?
            WHERE invoice_id = ? AND item_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, invoiceId);
            ps.setInt(3, itemId);
            ps.executeUpdate();
        }
    }

    /* =========================
       DELETE 1 ITEM IN INVOICE
       ========================= */
    public void delete(Connection conn,
                       int invoiceId,
                       int itemId) throws SQLException {

        String sql = """
            DELETE FROM products_details
            WHERE invoice_id = ? AND item_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            ps.setInt(2, itemId);
            ps.executeUpdate();
        }
    }

    /* =========================
       DELETE ALL BY INVOICE
       ========================= */
    public void deleteByInvoiceId(Connection conn, int invoiceId) throws SQLException {
        String sql = """
            DELETE FROM products_details
            WHERE invoice_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            ps.executeUpdate();
        }
    }

    /* =========================
       MAP ROW
       ========================= */
    private ProductDetail mapRow(ResultSet rs) throws SQLException {
        ProductDetail pd = new ProductDetail();
        pd.setInvoiceId(rs.getInt("invoice_id"));
        pd.setItemId(rs.getInt("item_id"));
        pd.setQuantity(rs.getInt("quantity"));
        return pd;
    }
}

