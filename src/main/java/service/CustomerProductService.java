package service;

import dao.ProductDAO;
import model.Product;

import java.sql.SQLException;
import java.util.List;

public class CustomerProductService {

    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> findAll() throws SQLException {
        return productDAO.findAll();
    }
}