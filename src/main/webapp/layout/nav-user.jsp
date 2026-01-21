<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<nav class="navbar-custom">
    <div class="navbar-container">
        <!-- Logo -->
        <div class="navbar-brand">
            <i class="fas fa-film"></i>
            <span>CINEMA</span>
        </div>
        
        <!-- Navigation Links - BỎ thẻ <ul> riêng lẻ -->
        <div style="display: flex; align-items: center; gap: 0.3rem; flex: 1; justify-content: center;">
            <a href="${pageContext.request.contextPath}/home" class="nav-link">
                <i class="fas fa-home"></i> Home
            </a>
            <a href="${pageContext.request.contextPath}/booking" class="nav-link">
                <i class="fas fa-ticket-alt"></i> Booking
            </a>
            <a href="${pageContext.request.contextPath}/movies" class="nav-link">
                <i class="fas fa-film"></i> Movies
            </a>
        </div>
        
        <!-- User Section -->
        <div class="navbar-user">
            <c:choose>
                <c:when test="${sessionScope.user == null}">
                    <a href="${pageContext.request.contextPath}/views/auth/login.jsp" class="btn-login">
                        Login
                    </a>
                    <a href="${pageContext.request.contextPath}/views/auth/register.jsp" class="btn-signup">
                        Sign-up
                    </a>
                </c:when>
                <c:when test="${sessionScope.user != null && sessionScope.user.roleId == 'User'}">
                    <div class="user-info">
                        <div class="user-avatar">
                            <i class="fas fa-user-circle"></i>
                        </div>
                        <span class="user-name">Hello, ${sessionScope.user.fullName}</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/AccountServlet?action=logout" class="btn-logout">
                        Logout
                    </a>
                </c:when>
            </c:choose>
        </div>
    </div>
</nav>