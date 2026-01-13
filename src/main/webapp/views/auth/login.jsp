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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
    </head>
    <body class="text-center login-bg">
        <div class="d-flex align-items-center justify-content-center vh-100">
            <div style="width: 100%; max-width: 380px;">
                <div class="container">
                   

                    <form action="${pageContext.request.contextPath}/LoginServlet" method="post" class="text-center" style=" padding: 0 50px">

                        <!-- Logo (tuỳ chọn) -->
                        <img class="mb-4" src="${pageContext.request.contextPath}/image/logoCC.png" alt="" width="200" height="200">

                        <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

                        <% if (request.getAttribute("Error") != null && !request.getAttribute("Error").toString().isEmpty()) {%>
                        <div class="alert alert-danger" role="alert">
                            <%= request.getAttribute("Error")%>
                        </div>
                        <% }%>

                        <div class="form-floating mb-3">
                            <input type="tel"
                                   class="form-control"
                                   id="floatingPhone"
                                   placeholder="Phone number"
                                   name="phoneNumber"
                                   required>
                            <label for="floatingPhone">Phone number</label>
                        </div>

                        <div class="form-floating mb-3">
                            <input type="password"
                                   class="form-control"
                                   id="floatingPassword"
                                   placeholder="Password"
                                   name="password"
                                   required>
                            <label for="floatingPassword">Password</label>
                        </div>

                        <div class="form-check text-start my-3">
                            <input class="form-check-input" type="checkbox" value="remember-me" id="checkRemember">
                            <label class="form-check-label" for="checkRemember">
                                Remember me
                            </label>
                        </div>

                        <button class="w-100 btn btn-lg btn-primary" type="submit">
                            Sign in
                        </button>

                        <p class="mt-5 mb-3 text-muted">&copy; 2025</p>

                    </form>

                    <div class="dropdown-divider"></div>
                    <a class="btn" href="register.jsp">New around here? Register</a>
                </div>
            </div>
        </div>
    </body>
</html>
