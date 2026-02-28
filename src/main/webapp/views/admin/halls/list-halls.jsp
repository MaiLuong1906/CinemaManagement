<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <html>

    <head>
      <title>Quản lý phòng chiếu</title>
      <!-- FontAwesome -->
      <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
      <!-- Google Fonts -->
      <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
      <!-- Admin CSS -->
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-hall.css">
    </head>

    <body>

      <div class="container" style="max-width: 1200px; margin: 0 auto; padding: 2rem;">

        <!-- Page Header -->
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
          <div>
            <a href="${pageContext.request.contextPath}/home" class="btn-primary-admin"
              style="background-color: #3182ce; color: white; padding: 0.5rem 1rem; margin-bottom: 1rem; display: inline-flex; align-items: center; text-decoration: none; border-radius: 6px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
              <i class="fas fa-arrow-left" style="margin-right: 0.5rem;"></i> Quay về trang chủ
            </a>
            <h2 style="margin: 0; color: #1a202c; font-size: 1.75rem;">Danh sách phòng chiếu</h2>
            <p style="margin: 0.5rem 0 0; color: #718096;">Quản lý sơ đồ và trạng thái phòng chiếu</p>
          </div>
          <a href="${pageContext.request.contextPath}/admin/halls?action=add" class="btn-primary-admin">
            <i class="fas fa-plus"></i> Thêm phòng mới
          </a>
        </div>

        <!-- Main Card -->
        <div class="admin-card">
          <div class="admin-card-header">
            <h5 class="admin-card-title">Tất cả phòng chiếu</h5>
            <div style="font-size: 0.9rem; color: #718096;">Tổng số: <strong>${halls.size()}</strong></div>
          </div>

          <div class="admin-card-body" style="padding: 0;">
            <table class="table-scientific">
              <thead>
                <tr>
                  <th width="80">ID</th>
                  <th>Tên phòng</th>
                  <th>Kích thước (D x R)</th>
                  <th>Tổng ghế</th>
                  <th>Trạng thái</th>
                  <th class="text-right">Hành động</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="h" items="${halls}">
                  <tr>
                    <td><span style="font-family: monospace; font-weight: bold; color: #4a5568;">#${h.hallId}</span>
                    </td>
                    <td>
                      <div style="font-weight: 600; color: #2d3748;">${h.hallName}</div>
                    </td>
                    <td>
                      <span
                        style="background: #edf2f7; padding: 2px 8px; border-radius: 4px; font-size: 0.85rem; color: #4a5568;">
                        ${h.total_rows} hàng x ${h.total_cols} cột
                      </span>
                    </td>
                    <td>
                      ${h.total_rows * h.total_cols} <span style="font-size: 0.8rem; color: #aaa;">(max)</span>
                    </td>
                    <td>
                      <span class="badge-dot ${h.status ? 'status-active' : 'status-inactive'}">
                        ${h.status ? "Hoạt động" : "Bảo trì"}
                      </span>
                    </td>
                    <td class="text-right">
                      <form action="${pageContext.request.contextPath}/admin/halls?action=toggle-status" method="post"
                        style="display: inline-block; margin: 0;">
                        <input type="hidden" name="hallId" value="${h.hallId}">
                        <input type="hidden" name="status" value="${!h.status}">
                        <button type="submit" class="action-link"
                          style="background: none; border: none; cursor: pointer; color: ${h.status ? '#f56565' : '#48bb78'};"
                          title="${h.status ? 'Tắt hoạt động' : 'Kích hoạt'}">
                          <i class="fas ${h.status ? 'fa-toggle-on' : 'fa-toggle-off'} fa-lg"></i>
                        </button>
                      </form>

                      <a href="${pageContext.request.contextPath}/admin/halls/seats?hallId=${h.hallId}"
                        class="btn-primary-admin"
                        style="padding: 0.25rem 0.75rem; font-size: 0.85rem; margin-right: 0.5rem; margin-left: 0.5rem;">
                        <i class="fas fa-chair"></i> Sơ đồ ghế
                      </a>
                      <a href="#" class="action-link" title="Chỉnh sửa"><i class="fas fa-edit"></i></a>
                      <a href="#" class="action-link" style="color: #e53e3e;" title="Xóa"><i
                          class="fas fa-trash"></i></a>
                    </td>
                  </tr>
                </c:forEach>

                <c:if test="${empty halls}">
                  <tr>
                    <td colspan="6" style="text-align: center; padding: 3rem; color: #a0aec0;">
                      <i class="fas fa-film" style="font-size: 2rem; margin-bottom: 1rem; display: block;"></i>
                      Chưa có phòng chiếu nào được tạo.
                    </td>
                  </tr>
                </c:if>
              </tbody>
            </table>
          </div>
        </div>

      </div>

    </body>

    </html>