<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<nav class="navbar-custom">
    <div class="navbar-container">
        <!-- Logo -->
        <div class="navbar-brand" onclick="location.href = '${pageContext.request.contextPath}/home'">
            <i class="fas fa-film"></i>
            <span>CINEMA</span>
        </div>
        <!-- Navigation Links -->
        <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
            <li>
                <a href="${pageContext.request.contextPath}/home" class="nav-link px-2 text-white">
                    Home
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/booking" class="nav-link px-2 text-white">
                    Booking
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/movies" class="nav-link px-2 text-white">
                    Movies
                </a>
            </li>
        </ul>
        <!-- User Section -->
        <div class="navbar-user text-end">
            <c:choose>
                <c:when test="${sessionScope.user == null}">
                    <a href="${pageContext.request.contextPath}/views/auth/login.jsp" class="btn-login me-2">
                        Login
                    </a>
                    <a href="${pageContext.request.contextPath}/views/auth/register.jsp" class="btn-signup">
                        Sign-up
                    </a>
                </c:when>
                <c:when test="${sessionScope.user != null && sessionScope.user.roleId == 'User'}">
                    <span class="text-warning me-3" style="font-weight: 600;">
                        <i class="fas fa-user"></i> Hello, ${sessionScope.user.fullName}
                    </span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
                        Logout
                    </a>
                </c:when>
                <c:when test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
                    <span class="text-warning me-3" style="font-weight: 600;">
                        <i class="fas fa-crown"></i> Admin: ${sessionScope.user.fullName}
                    </span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
                        Logout
                    </a>
                </c:when>
            </c:choose>
        </div>
    </div>
</nav>