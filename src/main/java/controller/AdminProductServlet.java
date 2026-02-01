package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;

import model.Product;
import service.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/product")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class AdminProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    // =====================
    // GET: list / delete
    // =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "delete":
                    deleteProduct(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // =====================
    // POST: insert / update
    // =====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "insert":
                    insertProduct(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // =====================
    // LIST → list.jsp
    // =====================
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        List<Product> products = productService.findAll();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/views/admin/products/list.jsp")
                .forward(request, response);
    }

    // =====================
    // INSERT (từ add.jsp modal)
    // =====================
    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Product p = buildProduct(request);

        if (p.getProductImgUrl() == null) {
            p.setProductImgUrl("default.jpg");
        }

        productService.insert(p);
        response.sendRedirect(request.getContextPath() + "/admin/product");
    }

    // =====================
    // UPDATE (từ add.jsp modal)
    // =====================
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int id = Integer.parseInt(request.getParameter("itemId"));
        Product p = buildProduct(request);
        p.setItemId(id);

        if (p.getProductImgUrl() == null) {
            Product old = productService.findById(id);
            p.setProductImgUrl(old.getProductImgUrl());
        }

        productService.update(p);
        response.sendRedirect(request.getContextPath() + "/admin/product");
    }

    // =====================
    // DELETE (từ delete.jsp)
    // =====================
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int id = Integer.parseInt(request.getParameter("id"));
        productService.delete(id);
        response.sendRedirect(request.getContextPath() + "/admin/product");
    }

    // =====================
    // BUILD PRODUCT
    // =====================
    private Product buildProduct(HttpServletRequest request)
            throws IOException, ServletException {

        Product p = new Product();
        p.setItemName(request.getParameter("itemName"));
        p.setPrice(new BigDecimal(request.getParameter("price")));
        p.setStockQuantity(
                Integer.parseInt(request.getParameter("stockQuantity"))
        );

        String uploadDir
                = request.getServletContext().getRealPath("/images/products");

        Part imagePart = request.getPart("image");

        if (imagePart != null && imagePart.getSize() > 0) {

            String fileName = imagePart.getSubmittedFileName();

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            imagePart.write(uploadDir + File.separator + fileName);
            p.setProductImgUrl(fileName);
        }

        return p;
    }
}
