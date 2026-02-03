<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <html>

    <head>
      <title>Thêm phòng chiếu mới</title>
      <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-hall.css">
    </head>

    <body>

      <div class="container" style="max-width: 1200px; margin: 0 auto; padding: 2rem;">

        <!-- Breadcrumb -->
        <div style="margin-bottom: 2rem; font-size: 0.9rem; color: #718096;">
          <a href="${pageContext.request.contextPath}/venue?action=halls" class="action-link"
            style="padding-left: 0;">&larr; Quay lại danh sách</a>
        </div>

        <div style="max-width: 600px; margin: 0 auto;">

          <div class="admin-card">
            <div class="admin-card-header">
              <h5 class="admin-card-title">Thiết lập phòng chiếu mới</h5>
            </div>

            <div class="admin-card-body">
              <form action="${pageContext.request.contextPath}/venue?action=add-hall" method="post">

                <div class="form-group">
                  <label class="form-label">Tên phòng chiếu</label>
                  <input type="text" name="hallName" class="form-control-admin" placeholder="Ví dụ: Phòng IMAX 01"
                    required autofocus>
                  <div class="form-text">Tên hiển thị cho khách hàng khi đặt vé.</div>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
                  <div class="form-group">
                    <label class="form-label">Số hàng ghế (Rows)</label>
                    <input type="number" name="rows" class="form-control-admin" min="1" max="20" value="10" required>
                    <div class="form-text">Tối đa 20 hàng.</div>
                  </div>

                  <div class="form-group">
                    <label class="form-label">Số cột (Columns)</label>
                    <input type="number" name="cols" class="form-control-admin" min="1" max="30" value="12" required>
                    <div class="form-text">Tối đa 30 cột.</div>
                  </div>
                </div>

                <div class="form-group">
                  <label class="form-label">Loại ghế mặc định</label>
                  <select name="defaultSeatType" class="form-control-admin">
                    <c:forEach var="st" items="${seatTypes}">
                      <option value="${st.seatTypeId}">${st.typeName}</option>
                    </c:forEach>
                  </select>
                  <div class="form-text">Toàn bộ ghế sẽ được khởi tạo với loại này. Bạn có thể chỉnh sửa từng ghế sau.
                  </div>
                </div>

                <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid #e2e8f0; text-align: right;">
                  <a href="${pageContext.request.contextPath}/venue?action=halls" class="action-link"
                    style="margin-right: 1rem;">Hủy bỏ</a>
                  <button type="submit" class="btn-primary-admin" style="padding: 0.6rem 1.5rem;">
                    <i class="fas fa-magic"></i> Tạo phòng & Sinh ghế
                  </button>
                </div>

              </form>
            </div>
          </div>

          <div style="text-align: center; color: #a0aec0; font-size: 0.85rem;">
            <i class="fas fa-info-circle"></i> Hệ thống sẽ tự động tạo ma trận ghế `(Rows x Cols)` với trạng thái "Hoạt
            động".
          </div>

        </div>
      </div>

    </body>

    </html>