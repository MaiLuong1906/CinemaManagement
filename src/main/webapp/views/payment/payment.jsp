<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ page import="java.util.List, model.Invoice" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
      <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

        <!DOCTYPE html>
        <html lang="vi">

        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>Thanh Toán - Cinema</title>

          <!-- Bootstrap 5 -->
          <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
          <!-- FontAwesome -->
          <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

          <!-- Custom CSS -->
          <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie-detail.css" />
          <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payment.css" />
        </head>

        <body>
          <!-- Header -->
          <jsp:include page="../../layout/header.jsp"></jsp:include>

          <div class="container py-5">
            <div class="row justify-content-center">
              <div class="col-lg-10">

                <div class="text-center mb-5">
                  <h2 class="display-5 fw-bold text-gradient mb-3">Xác Nhận Thanh Toán</h2>
                  <p class="text-secondary fs-5">Vui lòng kiểm tra lại thông tin hóa đơn trước khi thanh toán</p>
                </div>

                <div class="glass">
                  <div class="table-responsive">
                    <table class="table table-glass mb-0">
                      <thead>
                        <tr>
                          <th>Mã Hóa Đơn</th>
                          <th>Tổng Tiền</th>
                          <th>Thời Gian Tạo</th>
                          <th>Trạng Thái</th>
                          <th class="text-end">Hành Động</th>
                        </tr>
                      </thead>
                      <tbody>
                        <% List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");
                            if (invoices != null && !invoices.isEmpty()) {
                            for (Invoice inv : invoices) {
                            %>
                            <tr>
                              <td class="fw-bold">#<%= inv.getInvoiceId() %>
                              </td>
                              <td class="fs-5 fw-bold text-white">
                                <%= String.format("%,.0f", inv.getTotalAmount()) %> VND
                              </td>
                              <td class="text-secondary">
                                <%= inv.getBookingTime() %>
                              </td>
                              <td>
                                <span class="status-badge <%= " PAID".equalsIgnoreCase(inv.getStatus()) ? "status-paid"
                                  : "status-pending" %>">
                                  <%= inv.getStatus() %>
                                </span>
                              </td>
                              <td class="text-end">
                                <button onclick="pay(<%= inv.getInvoiceId() %>)" class="btn btn-pay">
                                  <i class="fas fa-credit-card me-2"></i>Thanh toán ngay
                                </button>
                              </td>
                            </tr>
                            <% } } else { %>
                              <tr>
                                <td colspan="5" class="text-center text-secondary py-4">
                                  Không có hóa đơn nào cần thanh toán.
                                </td>
                              </tr>
                              <% } %>
                      </tbody>
                    </table>
                  </div>
                </div>

                <div class="mt-4 text-center">
                  <a href="${pageContext.request.contextPath}/home"
                    class="text-decoration-none text-secondary hover-lift d-inline-block">
                    <i class="fas fa-arrow-left me-2"></i>Quay về trang chủ
                  </a>
                </div>

              </div>
            </div>
          </div>

          <!-- Footer -->
          <jsp:include page="../../layout/footer.jsp"></jsp:include>

          <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
          <script>
            function pay(invoiceId) {
              // Show loading state (optional but good UX)
              const btn = event.currentTarget;
              const originalText = btn.innerHTML;
              btn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Đang xử lý...';
              btn.disabled = true;

              fetch("${pageContext.request.contextPath}/ajaxServlet", {
                method: "POST",
                headers: {
                  "Content-Type": "application/x-www-form-urlencoded"
                },
                body: "invoiceId=" + invoiceId + "&language=vn"
              })
                .then(res => res.json())
                .then(data => {
                  if (data.code === "00") {
                    window.location.href = data.data;
                  } else {
                    alert("Lỗi khi tạo giao dịch: " + data.message);
                    btn.innerHTML = originalText;
                    btn.disabled = false;
                  }
                })
                .catch(err => {
                  console.error('Error:', err);
                  alert("Đã xảy ra lỗi kết nối.");
                  btn.innerHTML = originalText;
                  btn.disabled = false;
                });
            }
          </script>
        </body>

        </html>