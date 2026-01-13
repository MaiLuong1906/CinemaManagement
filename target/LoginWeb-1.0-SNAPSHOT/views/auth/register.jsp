<%-- Document : register Created on : Jan 11, 2026, 4:50:47 PM Author : LENOVO --%>

<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <%-- Trang đăng ký --%>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
              crossorigin="anonymous"><!-- comment -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
        crossorigin="anonymous"></script>
    </head>

    <body>
        <%
            String fullname = (request.getAttribute("fullname") != null)
                    ? request.getAttribute("fullname").toString() : "";
            String phone = (request.getAttribute("phone") != null)
                    ? request.getAttribute("phone").toString() : "";
            String email = (request.getAttribute("email") != null)
                    ? request.getAttribute("email").toString() : "";
            String dobStr = (request.getAttribute("dob") != null)
                    ? request.getAttribute("dob").toString() : "";
            String gender = (request.getAttribute("gender") != null)
                    ? request.getAttribute("gender").toString() : "";
            String address = (request.getAttribute("address") != null)
                    ? request.getAttribute("address").toString() : "";
        %>
        <div style="text-align: center">
            <h1>Sign-Up</h1>
        </div>
        <div class="container">
            <% if (request.getAttribute("Error") != null && !request.getAttribute("Error").toString().isEmpty()) {%>
            <div class="col-12">
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("Error")%>
                </div>
            </div>
            <% }%>
            <form class="row g-3" action="${pageContext.request.contextPath}/RegisterServlet" method="post">
                <div class="col-md-12">
                    <label for="fullname" class="form-label">Fullname</label><span class="red">*</span>
                    <input type="text" class="form-control" id="fullname" name="fullname" required="required"
                           value="<%=fullname%>">
                </div>
                <div class="col-12">
                    <label for="tel" class="form-label">Telephone Number</label><span class="red">*</span>
                    <input type="tel" class="form-control" id="tel" name="tel" required="required"
                           pattern="[0-9]{10,11}" placeholder="10-11 numbers" value="<%=phone%>">
                </div>
                <div class="col-12">
                    <label for="email" class="form-label">Email</label><span class="red">*</span>
                    <input type="email" class="form-control" id="email" name="email" required="required" value="<%=email%>">
                </div>
                <div class="col-12">
                    <label for="password" class="form-label">Password</label><span class="red">*</span>
                    <input type="password" class="form-control" id="password" required="required"
                           name="password" pattern="(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}"
                           placeholder="Min 8 chars, A-z, 0-9">
                </div>
                <div class="col-12">
                    <label for="cpassword" class="form-label">Confirm Password</label><span
                        class="red">*</span><span id="confirm" class="red"></span>
                    <input type="password" class="form-control" id="cpassword" required="required"
                           onkeyup="confirmPass()" name="cpassword">
                </div>
                <div class="col-md-12">
                    <label class="radio inline">
                        <input type="radio" name="gender" value="male" <%= gender.equals("male") ? "checked" : ""%>> Male
                    </label>

                    <label class="radio inline">
                        <input type="radio" name="gender" value="female" <%= gender.equals("female") ? "checked" : ""%>> Female
                    </label>
                </div>
                <div class="col-md-12">
                    <label for="age" class="form-label">Date of birth</label><span class="red">*</span>
                    <input type="date" class="form-control" id="age" name="dob" required="required" value="<%= dobStr%>">
                </div>
                <div class="col-md-12">
                    <label for="age" class="form-label">Address</label>
                    <input type="text" class="form-control" id="address" name="address" value="<%=address%>">
                </div>
                <div class="col-12">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="gridCheck" onchange="hidenSignUp()">
                        <label class="form-check-label" for="gridCheck">
                            I agree to <a href="" class="link-primary">Terms and Conditions</a>
                        </label>
                    </div>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-primary" style="visibility: hidden" id="signup">Sign
                        up</button>
                </div>
            </form>
        </div>
    </body>
    <script src="${pageContext.request.contextPath}/js/register.js"></script>
</html>