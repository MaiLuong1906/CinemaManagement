package service;

import dao.ProductDAO;
import model.Product;

import java.sql.SQLException;
import java.util.List;

public class CustomerProductService {
    private ProductDAO productDAO;

    public CustomerProductService() {
        this.productDAO = new ProductDAO();
    }

    public CustomerProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> findAll() throws SQLException {
        return productDAO.findAll();
    }
}