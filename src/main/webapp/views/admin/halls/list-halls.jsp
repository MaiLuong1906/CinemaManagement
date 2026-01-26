<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Quản lý phòng chiếu</title>
  <style>
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    .status-on { color: green; } .status-off { color: red; }
  </style>
</head>
<body>
<h2>Danh sách phòng chiếu</h2>
<a href="${pageContext.request.contextPath}/admin/halls/add">Thêm phòng mới</a><table>
  <tr>
    <th>ID</th>
    <th>Tên phòng</th>
    <th>Quy mô (Hàng x Cột)</th>
    <th>Trạng thái</th>
    <th>Hành động</th>
  </tr>
  <c:forEach var="h" items="${halls}">
    <tr>
      <td>${h.hallId}</td>
      <td>${h.hallName}</td>
      <td>${h.total_rows} x ${h.total_cols}</td>
      <td class="${h.status ? 'status-on' : 'status-off'}">
          ${h.status ? "Đang hoạt động" : "Tạm dừng"}
      </td>
      <td>
        <a href="${pageContext.request.contextPath}/admin/halls/seats?hallId=${h.hallId}">
          Quản lý ghế
        </a>
      </td>
    </tr>
  </c:forEach>
</table>
</body>
</html>