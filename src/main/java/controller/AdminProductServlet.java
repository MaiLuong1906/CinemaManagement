package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import model.Product;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/product")
public class AdminProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                action = "list";
            }
            
            switch (action) {
                case "list":
                    listProducts(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/views/admin/products/AdminProduct.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                action = "list";
            }
            
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
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/views/admin/products/AdminProduct.jsp").forward(request, response);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Product> products = productDAO.findAll();
            
            // DEBUG
            System.out.println("===== AdminProductServlet =====");
            System.out.println("Số lượng sản phẩm: " + (products != null ? products.size() : "null"));
            
            request.setAttribute("products", products);
            request.getRequestDispatcher("/views/admin/products/AdminProduct.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "add");
        request.getRequestDispatcher("/views/admin/products/AdminProduct.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("id"));
            Product product = productDAO.findById(itemId);
            request.setAttribute("product", product);
            request.setAttribute("mode", "edit");
            request.getRequestDispatcher("/views/admin/products/AdminProduct.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String itemName = request.getParameter("itemName");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("stockQuantity");
            String imgUrl = request.getParameter("productImgUrl");
            
            BigDecimal price = new BigDecimal(priceStr);
            int stockQuantity = Integer.parseInt(quantityStr);
            
            Product product = new Product();
            product.setItemName(itemName);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setProductImgUrl(imgUrl);
            
            productDAO.insert(product);
            request.setAttribute("successMessage", "Thêm sản phẩm thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/product?action=list");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            String itemName = request.getParameter("itemName");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("stockQuantity");
            String imgUrl = request.getParameter("productImgUrl");
            
            BigDecimal price = new BigDecimal(priceStr);
            int stockQuantity = Integer.parseInt(quantityStr);
            
            Product product = new Product();
            product.setItemId(itemId);
            product.setItemName(itemName);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setProductImgUrl(imgUrl);
            
            productDAO.update(product);
            request.setAttribute("successMessage", "Cập nhật sản phẩm thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/product?action=list");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("id"));
            productDAO.delete(itemId);
            request.setAttribute("successMessage", "Xóa sản phẩm thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/product?action=list");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}