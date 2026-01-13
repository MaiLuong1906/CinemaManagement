<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous"><!-- comment -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>


        <!-- Register CSS -->
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

        <div class="register-bg">
            <div class="register-mask d-flex align-items-center justify-content-center">

                <div class="container">

                    <div class="row justify-content-center">
                        <div class="col-12 col-md-9 col-lg-7 col-xl-6">

                            <div class="card register-card">
                                <div class="card-body p-5">
                                    <% if (request.getAttribute("Error") != null && !request.getAttribute("Error").toString().isEmpty()) {%>
                                    <div class="col-12">
                                        <div class="alert alert-danger register-error" role="alert">
                                            <%= request.getAttribute("Error")%>
                                        </div>
                                    </div>
                                    <% }%>

                                    <h1 class="text-uppercase text-center mb-4 register-title">Register</h1>

                                    <h4 class="text-uppercase text-center mb-4 register-title">Account Information</h4>

                                    <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">

                                        <div class="mb-3">
                                            <label class="form-label">Phone number <span class="required">*</span></label>
                                            <input type="tel" class="form-control form-control-lg"
                                                   name="phoneNumber" required="required" pattern="[0-9]{10,11}"
                                                   value="<%= phone%>">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Password <span class="required">*</span></label>
                                            <input type="password" class="form-control form-control-lg"
                                                   id="password"
                                                   name="password" required="required"
                                                   pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                Confirm Password <span class="required">*</span>
                                                <span id="confirm" class="text-danger"></span>
                                            </label>
                                            <input type="password" class="form-control form-control-lg"
                                                   id="cpassword"
                                                   name="cpassword"
                                                   required="required"
                                                   onkeyup="confirmPass()">
                                        </div>

                                        <h4 class="text-uppercase text-center mb-4 register-title">Personal Information</h4>

                                        <div class="mb-3">
                                            <label class="form-label">Full name <span class="required">*</span></label>
                                            <input type="text" class="form-control form-control-lg"
                                                   name="fullname" required="required" value="<%= fullname%>">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Email <span class="required">*</span></label>
                                            <input type="email" class="form-control form-control-lg"
                                                   name="email" required="required" value="<%= email%>">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label d-block">Gender</label>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender" value="male"
                                                       <%= gender.equals("male") ? "checked" : ""%>>
                                                <label class="form-check-label">Male</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="gender" value="female"
                                                       <%= gender.equals("female") ? "checked" : ""%>>
                                                <label class="form-check-label">Female</label>
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Date of birth <span class="required">*</span></label>
                                            <input type="date" class="form-control form-control-lg"
                                                   name="dob" required="required" value="<%= dobStr%>">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Address</label>
                                            <input type="text" class="form-control form-control-lg"
                                                   name="address" value="<%= address%>">
                                        </div>

                                        <div class="form-check d-flex justify-content-center mb-4">
                                            <input class="form-check-input me-2" type="checkbox" id="gridCheck" onchange="hidenSignUp()">
                                            <label class="form-check-label">
                                                I agree to <a href="#">Terms of service</a>
                                            </label>
                                        </div>

                                        <div class="d-flex justify-content-center">
                                            <button type="submit" id="signup"
                                                    class="btn btn-success btn-lg register-btn"
                                                    style="visibility: hidden">
                                                Register
                                            </button>
                                        </div>

                                        <p class="text-center text-muted mt-4 mb-0">
                                            Have already an account?
                                            <a href="login.jsp" class="fw-bold">Login here</a>
                                        </p>

                                    </form>

                                </div>
                            </div>

                        </div>
                    </div>
                </div>

            </div>
        </div>

        <script src="${pageContext.request.contextPath}/js/register.js"></script>

    </body>
</html>
