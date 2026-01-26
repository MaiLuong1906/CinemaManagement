<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<ul class="nav col-12 col-lg-auto me-lg-auto mb-4 justify-content-center mb-md-0">
    <li><a href="${pageContext.request.contextPath}/views/admin/bookings/list.jsp" class="nav-link px-3 text-white">Bookings</a></li>

    <li class="nav-item dropdown px-3">
        <a class="nav-link dropdown-toggle text-white" href="#" data-bs-toggle="dropdown">Movies</a>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/views/admin/movies/list.jsp">List Movie</a></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/views/admin/movies/add.jsp">Add Movie</a></li>
        </ul>
    </li>

    <li><a href="${pageContext.request.contextPath}/admin/halls" class="nav-link px-3 text-white">Rooms</a></li>

    <li><a href="${pageContext.request.contextPath}/views/admin/users/list.jsp" class="nav-link px-3 text-white">Users</a></li>

    <li><a href="${pageContext.request.contextPath}/views/admin/dashboard.jsp" class="nav-link px-3 text-white">Dashboard</a></li>
</ul>

<div class="text-end">
    <span class="text-warning me-3">Admin</span>
    <a href="${pageContext.request.contextPath}/AccountServlet?action=logout" class="btn btn-outline-danger">Logout</a>
</div>
