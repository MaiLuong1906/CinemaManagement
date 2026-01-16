<%-- 
    Document   : TestUpImg
    Created on : Jan 16, 2026, 4:04:28 PM
    Author     : nguye
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Test Image</title>
</head>
<body>

<h2>Test hiển thị ảnh từ server</h2>

<!-- Thay đúng tên file ảnh vừa upload -->
<img src="${pageContext.request.contextPath}/image/ForFilm/test.jpg"
     width="300"
     alt="Poster phim">

</body>
</html>
