package controller.payment;

import config.VNPAYConfig;
import controller.BaseServlet;
import dao.InvoiceDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import exception.ValidationException;
import exception.BusinessException;

@WebServlet("/payment-return")
public class PaymentReturnServlet extends BaseServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Process VNPAY parameters
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

        // Verify signature
        String signValue = VNPAYConfig.hashAllFields(fields);

        if (!signValue.equals(vnp_SecureHash)) {
            throw new ValidationException("Invalid payment signature");
        }

        String responseCode = getStringParam(request, "vnp_ResponseCode");

        if ("00".equals(responseCode)) {
            // Payment success
            try {
                String vnp_TxnRef = getStringParam(request, "vnp_TxnRef");
                String[] parts = vnp_TxnRef.split("_");
                int invoiceId = Integer.parseInt(parts[0]);

                InvoiceDAO invoiceDAO = new InvoiceDAO();
                invoiceDAO.updateStatus(invoiceId, "PAID");

                request.setAttribute("message", "Thanh toán thành công cho hóa đơn #" + invoiceId);
            } catch (Exception e) {
                log("Payment status update failed", e);
                throw new BusinessException("Payment successful but status update failed: " + e.getMessage());
            }
            forward(request, response, "/views/payment/bookingsuccess.jsp");
        } else {
            // Payment failed
            request.setAttribute("message", "Thanh toán thất bại! Mã lỗi: " + responseCode);
            forward(request, response, "/views/payment/bookingsuccess.jsp");
        }
    }
}
