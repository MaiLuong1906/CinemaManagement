<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Thêm phòng chiếu mới</h2>
<form action="${pageContext.request.contextPath}/admin/halls/add" method="post">

  Tên phòng: <input type="text" name="hallName" required><br>
  Số hàng: <input type="number" name="rows" min="1" max="20" required><br>
  Số cột: <input type="number" name="cols" min="1" max="30" required><br>
  Loại ghế mặc định:
  <select name="defaultSeatType">
    <c:forEach var="st" items="${seatTypes}">
      <option value="${st.seatTypeId}">${st.typeName}</option>
    </c:forEach>
  </select><br>
  <button type="submit">Tạo phòng và tự động sinh ghế</button>
</form>
</body>
</html>