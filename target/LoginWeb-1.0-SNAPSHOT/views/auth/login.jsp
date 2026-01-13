<%-- 
    Document   : login
    Created on : Jan 11, 2026, 4:50:31 PM
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%-- Trang đăng nhập --%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
              crossorigin="anonymous"><!-- comment -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
        crossorigin="anonymous"></script>
    </head>
    <body>
        <jsp:include page="../../layout/header.jsp"></jsp:include>
            <div class="container">
                <div style="text-align: center">
                    <h1>Sign-In</h1>
                </div>

                <form class="px-4 py-3" action="${pageContext.request.contextPath}/LoginServlet" method="post">
                <% if (request.getAttribute("Error") != null && !request.getAttribute("Error").toString().isEmpty()) {%>
                <div class="col-12">
                    <div class="alert alert-danger" role="alert">
                        <%= request.getAttribute("Error")%>
                    </div>
                </div>
                <% }%>
                <div class="mb-3">
                    <label for="phoneNumber" class="form-label">Phone Number</label>
                    <input type="tel" class="form-control" id="phoneNumber" placeholder="Phone Number" name="phoneNumber">
                </div>
                <div class="mb-3">
                    <label for="exampleDropdownFormPassword1" class="form-label">Password</label>
                    <input type="password" class="form-control" id="exampleDropdownFormPassword1" placeholder="Password" name="password">
                </div>
                <div class="mb-3">
                    <div class="form-check">
                        <input type="checkbox" class="form-check-input" id="dropdownCheck">
                        <label class="form-check-label" for="dropdownCheck">
                            Remember me
                        </label>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Sign in</button>
            </form>
            <div class="dropdown-divider"></div>
            <a class="btn" href="register.jsp">New around here? Sign up</a>
        </div>
        <jsp:include page="../../layout/footer.jsp"></jsp:include>
    </body>
</html>
