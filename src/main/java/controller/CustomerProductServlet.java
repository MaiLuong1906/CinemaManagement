package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Product;
import service.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet("/product")
public class CustomerProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Product> products = productService.findAll();

            // DEBUG
            System.out.println("===== CustomerProductServlet =====");
            System.out.println("Số lượng sản phẩm: " + products.size());

            request.setAttribute("products", products);
            request.getRequestDispatcher("/views/user/product.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra");
            request.getRequestDispatcher("/views/user/product.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}