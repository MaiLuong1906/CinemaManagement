<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Test Upload Image</title>
</head>
<body>

<h2>Test hiển thị ảnh từ ImageServlet</h2>


<!-- test image -->
<img 
    src="<%= request.getContextPath() %>/image?name=1768558312921_test.jpg"
    alt="Test Image"
    style="width:300px; border:2px solid red;"
/>

<hr/>

<p><b>Context path:</b> <%= request.getContextPath() %></p>

</body>
</html>