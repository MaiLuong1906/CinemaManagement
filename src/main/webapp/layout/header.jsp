<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page contentType="text/html" pageEncoding="UTF-8" %>

        <head>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        </head>

        <nav class="navbar navbar-expand-lg navbar-dark navbar-custom fixed-top py-3">
            <div class="container-fluid px-4 position-relative">

                <a href="${pageContext.request.contextPath}/home"
                    class="navbar-brand navbar-brand-custom d-flex align-items-center gap-2 text-decoration-none">
                    <i class="fas fa-film fs-4"></i>
                    <span class="fw-bold d-none d-sm-inline">CINEMA</span> </a>

                <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarContent">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse justify-content-end" id="navbarContent">

                    <ul class="navbar-nav mb-2 mb-lg-0 gap-1 nav-center-absolute">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/home"
                                class="nav-link nav-link-custom text-white px-4 py-2 rounded-3 fw-semibold fs-5">
                                <i class="fas fa-home me-2"></i> Home
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/movies"
                                class="nav-link nav-link-custom text-white px-4 py-2 rounded-3 fw-semibold fs-5">
                                <i class="fas fa-film me-2"></i> Movies
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/product"
                                class="nav-link nav-link-custom text-white px-4 py-2 rounded-3 fw-semibold fs-5">
                                <i class="fas fa-shopping-bag me-2"></i> Products
                            </a>
                        </li>
                    </ul>

                    <div class="d-flex align-items-center gap-2 mt-3 mt-lg-0">
                        <c:choose>
                            <%-- User not logged in --%>
                                <c:when test="${sessionScope.user == null}">
                                    <a href="${pageContext.request.contextPath}/views/auth/login.jsp"
                                        class="btn btn-sm btn-outline-light rounded-pill px-3 py-2 fw-semibold me-2">
                                        <i class="fas fa-sign-in-alt me-1"></i> Login
                                    </a>
                                    <a href="${pageContext.request.contextPath}/views/auth/register.jsp"
                                        class="btn btn-sm btn-outline-danger rounded-pill px-3 py-2 fw-semibold">
                                        <i class="fas fa-user-plus me-1"></i> Sign-up
                                    </a>
                                </c:when>

                                <%-- Regular User logged in --%>
                                    <c:when test="${sessionScope.user != null && sessionScope.user.roleId == 'User'}">
                                        <a href="${pageContext.request.contextPath}/AccountServlet?action=profile"
                                            class="btn btn-sm btn-outline-light rounded-pill px-3 py-2 fw-semibold me-2">
                                            <i class="fas fa-user me-1"></i> ${sessionScope.user.fullName}
                                        </a>
                                        <a href="${pageContext.request.contextPath}/AccountServlet?action=logout"
                                            class="btn btn-sm btn-outline-danger rounded-pill px-3 py-2 fw-semibold">
                                            <i class="fas fa-sign-out-alt me-1"></i> Logout
                                        </a>
                                    </c:when>

                                    <%-- Admin logged in --%>
                                        <c:when
                                            test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
                                            <div
                                                class="admin-badge d-flex align-items-center gap-2 px-3 py-2 rounded-pill me-2">
                                                <div
                                                    class="d-flex align-items-center justify-content-center rounded-circle avatar-circle">
                                                    <i class="fas fa-crown text-white"></i>
                                                </div>
                                                <span class="text-white fw-semibold small">
                                                    <i class="fas fa-shield-alt me-1" style="color: #e60914;"></i>
                                                    ${sessionScope.user.fullName}
                                                </span>
                                            </div>
                                            <a href="${pageContext.request.contextPath}/AccountServlet?action=logout"
                                                class="btn btn-sm btn-outline-danger rounded-pill px-3 py-2 fw-semibold">
                                                <i class="fas fa-sign-out-alt me-1"></i> Logout
                                            </a>
                                        </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>
        </nav>