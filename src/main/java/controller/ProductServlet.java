package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import model.Product;
import service.ProductService;
import service.CartService;
import dao.SeatDAO;
import model.SeatSelectionDTO;

@WebServlet(urlPatterns = {"/admin/product", "/product"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class ProductServlet extends BaseServlet {

    private ProductService productService;
    private CartService cartService;
    private SeatDAO seatDAO;

    @Override
    public void init() {
        productService = new ProductService();
        cartService = new CartService();
        seatDAO = new SeatDAO();
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getServletPath();
        String action = getStringParam(request, "action", "list");

        if ("/admin/product".equals(path)) {
            handleAdminRequest(request, response, action);
        } else if ("/product".equals(path)) {
            handleCustomerRequest(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleAdminRequest(HttpServletRequest request, HttpServletResponse response, String action) throws Exception {
        switch (action) {
            case "insert":
                insertProduct(request, response);
                break;
            case "update":
                updateProduct(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            case "list":
            default:
                listAdminProducts(request, response);
                break;
        }
    }

    private void handleCustomerRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Product> products = productService.findAll();
        request.setAttribute("products", products);
        
        // Load cart details for sidebar
        request.setAttribute("cartDetails", cartService.getCartDetails(request.getSession()));

        // Re-hydrate Seat Details from Session IDs
        HttpSession session = request.getSession();
        String seatIdsStr = (String) session.getAttribute("BOOKING_SEAT_IDS");
        String showtimeIdStr = (String) session.getAttribute("BOOKING_SHOWTIME_ID");

        if (seatIdsStr != null && showtimeIdStr != null) {
            try {
                int showtimeId = Integer.parseInt(showtimeIdStr);
                List<SeatSelectionDTO> allSeats = seatDAO.getSeatsByShowtime(showtimeId);
                List<SeatSelectionDTO> selectedSeats = new java.util.ArrayList<>();
                String[] ids = seatIdsStr.split(",");
                for (String id : ids) {
                    try {
                        int seatId = Integer.parseInt(id.trim());
                        for (SeatSelectionDTO s : allSeats) {
                            if (s.getSeatId() == seatId) {
                                selectedSeats.add(s);
                                break;
                            }
                        }
                    } catch (NumberFormatException ignored) {}
                }
                session.setAttribute("cartSeats", selectedSeats);
            } catch (Exception e) {
                log("Error rehydrating seats in CustomerProductServlet", e);
            }
        }

        forward(request, response, "/views/user/product.jsp");
    }

    private void listAdminProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Product> products = productService.findAll();
        request.setAttribute("products", products);
        forward(request, response, "/views/admin/products/list.jsp");
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Product p = buildProduct(request);
        if (p.getProductImgUrl() == null) {
            p.setProductImgUrl("default.jpg");
        }
        productService.insert(p);
        redirect(response, request.getContextPath() + "/admin/product");
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = getIntParam(request, "itemId");
        Product p = buildProduct(request);
        p.setItemId(id);

        if (p.getProductImgUrl() == null) {
            Product old = productService.findById(id);
            p.setProductImgUrl(old.getProductImgUrl());
        }

        productService.update(p);
        redirect(response, request.getContextPath() + "/admin/product");
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = getIntParam(request, "id");
        productService.delete(id);
        redirect(response, request.getContextPath() + "/admin/product");
    }

    private Product buildProduct(HttpServletRequest request) throws IOException, ServletException {
        Product p = new Product();
        p.setItemName(request.getParameter("itemName"));
        p.setPrice(new BigDecimal(request.getParameter("price")));
        p.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));

        String uploadDir = request.getServletContext().getRealPath("/images/products");
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
