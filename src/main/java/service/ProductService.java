package service;

import dao.ProductDAO;
import model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> findAll() throws SQLException {
        return productDAO.findAll();
    }

    public void insert(Product product) throws SQLException {
        productDAO.insert(product);
    }

    public void update(Product product) throws SQLException {
        productDAO.update(product);
    }

    public void delete(int id) throws SQLException {
        productDAO.delete(id);
    }
}