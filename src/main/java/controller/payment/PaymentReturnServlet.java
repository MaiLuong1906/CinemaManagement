package controller.payment;

import config.VNPAYConfig;
import dao.InvoiceDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/payment-return")
public class PaymentReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Process VNPAY parameters
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String paramName = params.nextElement();
                String fieldName = URLEncoder.encode(paramName, StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(paramName),
                        StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            // 2. Verify Signature
            String signValue = VNPAYConfig.hashAllFields(fields);

            // vnp_TxnRef contains our Invoice ID if we set it, BUT ajaxServlet set it to a
            // random number
            // However, vnp_OrderInfo typically contains "Thanh toan don hang:..."
            // OR we can pass invoiceId through vnp_TxnRef if we want.
            // Let's look at ajaxServlet again. It sets: vnp_TxnRef =
            // VNPAYConfig.getRandomNumber(8);
            // This is a PROBLEM. We can't map back to the invoice easily unless we save the
            // txRef to the invoice.
            // OR we can parse it from vnp_OrderInfo if we embedded it there.
            // Better approach: Update ajaxServlet to put InvoiceId in vnp_OrderInfo or
            // match TxnRef.
            // For now, let's assume we can't easily get ID if random.
            // WAIT! ajaxServlet set vnp_OrderInfo = "Thanh toan don hang:" + vnp_TxnRef;
            // It did NOT put invoiceId in params sent to VNPAY useful for return.

            // I need to fix ajaxServlet to put invoiceId in vnp_TxnRef or OrderInfo
            // properly.
            // But let's finish the skeleton here.

            if (signValue.equals(vnp_SecureHash)) {

                String responseCode = request.getParameter("vnp_ResponseCode");
                String transactionStatus = request.getParameter("vnp_TransactionStatus"); // Not always present in
                                                                                          // return URL depending on
                                                                                          // version

                if ("00".equals(responseCode)) {
                    // PAYMENT SUCCESS
                    try {
                        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
                        String[] parts = vnp_TxnRef.split("_");
                        int invoiceId = Integer.parseInt(parts[0]);

                        InvoiceDAO invoiceDAO = new InvoiceDAO();
                        invoiceDAO.updateStatus(invoiceId, "PAID");

                        request.setAttribute("message", "Thanh toán thành công cho hóa đơn #" + invoiceId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute("message",
                                "Thanh toán thành công nhưng lỗi cập nhật trạng thái: " + e.getMessage());
                    }
                    request.getRequestDispatcher("/views/payment/bookingsuccess.jsp").forward(request, response);
                } else {
                    // PAYMENT FAILED
                    request.setAttribute("message", "Thanh toán thất bại! Mã lỗi: " + responseCode);
                    request.getRequestDispatcher("/views/payment/bookingsuccess.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("message", "Chữ ký không hợp lệ!");
                request.getRequestDispatcher("/views/payment/bookingsuccess.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home?error=exception");
        }
    }
}
