<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!-- Admin Sidebar -->
<aside class="admin-sidebar">
    <div class="sidebar-header">
        <div class="admin-logo">
            <i class="fas fa-crown"></i>
            <span>ADMIN PANEL</span>
        </div>
    </div>
    
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/views/admin/dashboard.jsp" class="sidebar-link">
            <i class="fas fa-chart-line"></i>
            <span>Dashboard</span>
        </a>
        
        <a href="${pageContext.request.contextPath}/views/admin/bookings/list.jsp" class="sidebar-link">
            <i class="fas fa-ticket-alt"></i>
            <span>Bookings</span>
        </a>
        
        <div class="sidebar-dropdown">
            <a href="javascript:void(0)" class="sidebar-link" onclick="toggleDropdown(event)">
                <i class="fas fa-film"></i>
                <span>Movies</span>
                <i class="fas fa-chevron-down dropdown-icon"></i>
            </a>
            <div class="dropdown-content">
                <a href="${pageContext.request.contextPath}/views/admin/movies/list.jsp">
                    <i class="fas fa-list"></i> List Movies
                </a>
                <a href="${pageContext.request.contextPath}/views/admin/movies/add.jsp">
                    <i class="fas fa-plus-circle"></i> Add Movie
                </a>
            </div>
        </div>
        
        <a href="${pageContext.request.contextPath}/views/admin/rooms/list.jsp" class="sidebar-link">
            <i class="fas fa-door-open"></i>
            <span>Rooms</span>
        </a>
        
        <a href="${pageContext.request.contextPath}/views/admin/users/list.jsp" class="sidebar-link">
            <i class="fas fa-users"></i>
            <span>Users</span>
        </a>
    </nav>
    
    <div class="sidebar-footer">
        <div class="admin-info">
            <div class="admin-avatar">
                <i class="fas fa-user-shield"></i>
            </div>
            <div class="admin-details">
                <span class="admin-name">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.fullName}">
                            ${sessionScope.user.fullName}
                        </c:when>
                        <c:otherwise>
                            Admin
                        </c:otherwise>
                    </c:choose>
                </span>
                <span class="admin-role">Administrator</span>
            </div>
        </div>
    </div>
</aside>

<!-- Sidebar Toggle Button (for mobile) -->
<button class="sidebar-toggle" onclick="toggleSidebar()">
    <i class="fas fa-bars"></i>
</button>

<script>
function toggleDropdown(event) {
    event.preventDefault();
    const dropdown = event.currentTarget.parentElement;
    dropdown.classList.toggle('active');
}

function toggleSidebar() {
    const sidebar = document.querySelector('.admin-sidebar');
    const content = document.querySelector('.page-content');
    sidebar.classList.toggle('active');
    if(content) content.classList.toggle('shifted');
}
</script>