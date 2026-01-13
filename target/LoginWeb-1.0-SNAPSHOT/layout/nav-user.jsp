<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

    <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
        <li><a href="${pageContext.request.contextPath}/views/user/home.jsp" class="nav-link px-2 text-white">Home</a>
        </li>
        <li><a href="${pageContext.request.contextPath}/views/user/booking.jsp"
                class="nav-link px-2 text-white">Booking</a></li>
        <li><a href="${pageContext.request.contextPath}/views/user/movies.jsp"
                class="nav-link px-2 text-white">Movies</a></li>
    </ul>

    <div class="text-end">
        <c:choose>
            <c:when test="${sessionScope.user == null}">
                <a href="${pageContext.request.contextPath}/views/auth/login.jsp"
                    class="btn btn-outline-light me-2">Login</a>
                <a href="${pageContext.request.contextPath}/views/auth/register.jsp" class="btn btn-warning">Sign-up</a>
            </c:when>
            <c:when test="${sessionScope.user != null && sessionScope.user.role == 2}" >
                <span class="text-warning me-3">Hello ${sessionScope.user.fullname}</span>
                <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn btn-outline-danger">Logout</a>
            </c:when>
        </c:choose>
    </div>