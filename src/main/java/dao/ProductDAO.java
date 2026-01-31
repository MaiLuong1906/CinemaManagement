package dao;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final String INSERT_SQL =
            "INSERT INTO products (item_name, price, stock_quantity) VALUES (?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE products SET item_name = ?, price = ?, stock_quantity = ? WHERE item_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM products WHERE item_id = ?";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM products WHERE item_id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM products";

    private static final String UPDATE_STOCK_SQL =
            "UPDATE products SET stock_quantity = stock_quantity + ? WHERE item_id = ?";
    private static final String DELETE_PRODUCT_DETAILS_SQL =
        "DELETE FROM products_details WHERE item_id = ?";

    // =========================
    // 1. Thêm sản phẩm (Admin)
    // =========================
    public void insert(Product p) throws SQLException {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, p.getItemName());
            ps.setBigDecimal(2, p.getPrice());
            ps.setInt(3, p.getStockQuantity());
            ps.executeUpdate();
        }
    }

    // =========================
    // 2. Cập nhật sản phẩm
    // =========================
    public void update(Product p) throws SQLException {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, p.getItemName());
            ps.setBigDecimal(2, p.getPrice());
            ps.setInt(3, p.getStockQuantity());
            ps.setInt(4, p.getItemId());
            ps.executeUpdate();
        }
    }

    // =========================
    // 3. Xóa sản phẩm
    // =========================
   public void delete(int itemId) throws SQLException {

    Connection conn = null;
    PreparedStatement psDetail = null;
    PreparedStatement psProduct = null;

    try {
        conn = DBConnect.getConnection();
        conn.setAutoCommit(false); // BẮT ĐẦU TRANSACTION

        // 1. Xóa bảng con trước
        psDetail = conn.prepareStatement(DELETE_PRODUCT_DETAILS_SQL);
        psDetail.setInt(1, itemId);
        psDetail.executeUpdate();

        // 2. Xóa bảng products
        psProduct = conn.prepareStatement(DELETE_SQL);
        psProduct.setInt(1, itemId);
        psProduct.executeUpdate();

        conn.commit(); // OK → LƯU

    } catch (SQLException e) {
        if (conn != null) conn.rollback(); // LỖI → QUAY LẠI
        throw e;
    } finally {
        if (psDetail != null) psDetail.close();
        if (psProduct != null) psProduct.close();
        if (conn != null) conn.close();
    }
}


    // =========================
    // 4. Tìm theo ID
    // =========================
    public Product findById(int itemId) throws SQLException {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // =========================
    // 5. Lấy tất cả sản phẩm
    // =========================
    public List<Product> findAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // =========================
    // 6. Cập nhật tồn kho (+ / -)
    // =========================
    public void updateStock(Connection conn, int itemId, int quantity) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_STOCK_SQL)) {
            ps.setInt(1, quantity); // âm = trừ kho, dương = cộng kho
            ps.setInt(2, itemId);
            ps.executeUpdate();
        }
    }

    // =========================
    // mapRow
    // =========================
    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setItemId(rs.getInt("item_id"));
        p.setItemName(rs.getString("item_name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        return p;
    }
}

