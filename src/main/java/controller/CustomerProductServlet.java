package controller;

import java.io.IOException;
import java.util.List;
import model.Product;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/product")
public class CustomerProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listProducts(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tất cả sản phẩm
            List<Product> products = productDAO.findAll();
            
            // DEBUG: In ra console
            System.out.println("===== CustomerProductServlet =====");
            System.out.println("Số lượng sản phẩm: " + (products != null ? products.size() : "null"));
            if (products != null && !products.isEmpty()) {
                for (Product p : products) {
                    System.out.println("- " + p.getItemName() + " | Giá: " + p.getPrice() + " | Tồn: " + p.getStockQuantity());
                }
            } else {
                System.out.println("Danh sách sản phẩm RỖNG hoặc NULL!");
            }
            System.out.println("==================================");
            
            request.setAttribute("products", products);
            request.getRequestDispatcher("/views/user/product.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("LỖI trong CustomerProductServlet: " + e.getMessage());
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("views/user/product.jsp").forward(request, response);
        }
    }
}