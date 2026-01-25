<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>Register - Create Your Account</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
        crossorigin="anonymous"></script>     
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css"/>
    </head>

    <body>

        <%
            String fullname = request.getAttribute("fullname") != null ? request.getAttribute("fullname").toString() : "";
            String phone = request.getAttribute("phoneNumber") != null ? request.getAttribute("phoneNumber").toString() : "";
            String email = request.getAttribute("email") != null ? request.getAttribute("email").toString() : "";
            String dobStr = request.getAttribute("dob") != null ? request.getAttribute("dob").toString() : "";
            String gender = request.getAttribute("gender") != null ? request.getAttribute("gender").toString() : "";
            String address = request.getAttribute("address") != null ? request.getAttribute("address").toString() : "";
        %>

        <div class="min-vh-100 d-flex align-items-center justify-content-center p-3">
            <div class="register-card bg-white" style="max-width: 500px; width: 100%;">
                <div class="register-header text-center">
                    <h2 class="mb-2">Create Account</h2>
                </div>

                <div class="p-4 p-md-5">
                    <% if (request.getAttribute("Error") != null
                                && !request.getAttribute("Error").toString().isEmpty()) {%>
                    <div class="alert alert-danger mb-3" role="alert">
                        <%= request.getAttribute("Error")%>
                    </div>
                    <% }%>

                    <form action="${pageContext.request.contextPath}/AccountServlet" method="post">
                        <input type="hidden" name="action" value="register">
                        <div class="mb-3">
                            <label class="form-label fw-medium" for="phoneNumber">Phone Number</label>
                            <input type="tel" class="form-control" name="phoneNumber"
                                   placeholder="Enter your phone number" id="phoneNumber" required="required"
                                   value="<%= phone%>" onkeyup="validatePhone()">
                            <span id="phoneError" class="text-danger small"></span>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-medium" for="password">Password</label>
                            <input type="password" class="form-control" name="password" id="password"
                                   placeholder="Create a password" required onkeyup="validatePass()">
                            <span id="passwordError" class="text-danger small"></span>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-medium" for="cpassword">Confirm Password</label>
                            <input type="password" class="form-control" name="cpassword" id="cpassword"
                                   required onkeyup="confirmPass()" placeholder="Re-enter your password">
                            <span id="confirm" class="text-danger small"></span>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-medium" for="fullname">Full Name</label>
                            <input type="text" class="form-control" name="fullname" id="fullname"
                                   placeholder="Enter your full name" required value="<%= fullname%>"
                                   onkeyup="validateName()">
                            <span id="nameError" class="text-danger small"></span>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-medium" for="email">Email Address</label>
                            <input type="email" class="form-control" name="email" id="email"
                                   placeholder="Enter your email" required value="<%= email%>"
                                   onkeyup="validateEmail()">
                            <span id="emailError" class="text-danger small"></span>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-medium d-block">Gender</label>
                            <div class="d-flex gap-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="gender" id="male"
                                           value="male" <%=gender.equals("male") ? "checked" : ""%>>
                                    <label class="form-check-label" for="male">Male</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="gender" id="female"
                                           value="female" <%=gender.equals("female") ? "checked" : ""%>>
                                    <label class="form-check-label" for="female">Female</label>
                                </div>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-medium" for="dob">Date of Birth</label>
                            <input type="date" class="form-control" name="dob" required value="<%= dobStr%>"
                                   id="dob">
                            <span id="dobError" class="text-danger small"></span>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-medium" for="address">Address (Optional)</label>
                            <input type="text" class="form-control" name="address" id="address"
                                   placeholder="Enter your address" value="<%= address%>">
                        </div>

                        <div class="form-check d-flex justify-content-center align-items-center gap-2 my-4 terms-check">
                            <input class="form-check-input m-0" type="checkbox" id="gridCheck"
                                   onchange="hidenSignUp()">
                            <label class="form-check-label small" for="gridCheck">
                                I agree to the <a href="#" class="text-decoration-none">Terms of Service</a>
                            </label>
                        </div>

                        <button type="submit" id="signup" class="btn btn-primary w-100 register-btn"
                                style="visibility: hidden">
                            Create Account
                        </button>

                        <div class="text-center text-muted mt-3 small login-link">
                            Already have an account?
                            <a href="login.jsp" class="text-decoration-none">Login here</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/js/register.js"></script>
    </body>

</html>