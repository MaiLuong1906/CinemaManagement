<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!-- Admin Sidebar -->
<div class="d-flex flex-column flex-shrink-0 admin-sidebar-custom position-fixed overflow-auto"
     id="adminSidebar"
     style="width: 280px; top: 80px; left: 0; height: calc(100vh - 80px); z-index: 999; transition: transform 0.3s ease;">

    <!-- Sidebar Header -->
    <div class="p-4 border-bottom border-secondary border-opacity-25">
        <div class="d-flex align-items-center gap-3 text-white">
            <i class="fas fa-crown fs-4" style="color: #e60914;"></i>
            <span class="fw-bold fs-5">ADMIN PANEL</span>
        </div>
    </div>

    <!-- Sidebar Navigation -->
    <nav class="flex-grow-1 py-3">
        <ul class="nav nav-pills flex-column mb-auto">
            <!-- Dashboard -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/views/admin/dashboard.jsp" 
                   class="nav-link sidebar-link-custom text-white d-flex align-items-center gap-3 px-4 py-3 rounded-0">
                    <i class="fas fa-chart-line fs-5" style="width: 24px;"></i>
                    <span class="fw-semibold">Dashboard</span>
                </a>
            </li>

            <!-- Bookings -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/views/admin/bookings/list.jsp" 
                   class="nav-link sidebar-link-custom text-white d-flex align-items-center gap-3 px-4 py-3 rounded-0">
                    <i class="fas fa-ticket-alt fs-5" style="width: 24px;"></i>
                    <span class="fw-semibold">Bookings</span>
                </a>
            </li>

            <!-- Movies Dropdown -->
            <li class="nav-item">
                <a href="#moviesSubmenu" 
                   class="nav-link sidebar-link-custom dropdown-toggle-custom text-white d-flex align-items-center justify-content-between px-4 py-3 rounded-0"
                   data-bs-toggle="collapse"
                   role="button"
                   aria-expanded="false">
                    <div class="d-flex align-items-center gap-3">
                        <i class="fas fa-film fs-5" style="width: 24px;"></i>
                        <span class="fw-semibold">Movies</span>
                    </div>
                    <i class="fas fa-chevron-down small"></i>
                </a>
                <div class="collapse bg-dark bg-opacity-25" id="moviesSubmenu">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/ListMovieForAdmin" 
                               class="nav-link text-white-50 d-flex align-items-center gap-3 py-2"
                               style="padding-left: 4rem !important; font-size: 0.95rem; transition: all 0.3s ease;"
                               onmouseover="this.style.paddingLeft = '4.5rem'; this.style.color = '#e60914';"
                               onmouseout="this.style.paddingLeft = '4rem'; this.style.color = '';">
                                <i class="fas fa-calendar-alt" style="width: 20px;"></i>
                                <span>Showtime Schedule</span>
                            </a>
                        </li>
                        <!--them phim-->
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/AddMovieServlet" 
                               class="nav-link text-white-50 d-flex align-items-center gap-3 py-2"
                               style="padding-left: 4rem !important; font-size: 0.95rem; transition: all 0.3s ease;"
                               onmouseover="this.style.paddingLeft = '4.5rem'; this.style.color = '#e60914';"
                               onmouseout="this.style.paddingLeft = '4rem'; this.style.color = '';">
                                <i class="fas fa-plus-circle" style="width: 20px;"></i>
                                <span>Add Movie</span>
                            </a>
                        </li>
                        <!--them showtime-->
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/AddShowTimeServlet" 
                               class="nav-link text-white-50 d-flex align-items-center gap-3 py-2"
                               style="padding-left: 4rem !important; font-size: 0.95rem; transition: all 0.3s ease;"
                               onmouseover="this.style.paddingLeft = '4.5rem'; this.style.color = '#e60914';"
                               onmouseout="this.style.paddingLeft = '4rem'; this.style.color = '';">
                                <i class="fas fa-calendar-plus" style="width: 20px;"></i>
                                <span>Add Showtime</span>
                            </a>
                        </li>
                        <!--update phim-->
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/UpdateMovieServlet" 
                               class="nav-link text-white-50 d-flex align-items-center gap-3 py-2"
                               style="padding-left: 4rem !important; font-size: 0.95rem; transition: all 0.3s ease;"
                               onmouseover="this.style.paddingLeft = '4.5rem'; this.style.color = '#e60914';"
                               onmouseout="this.style.paddingLeft = '4rem'; this.style.color = '';">
                                <i class="fas fa-edit" style="width: 20px;"></i>
                                <span>Update Movie</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </li>

            <!-- Rooms -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/views/admin/rooms/list.jsp" 
                   class="nav-link sidebar-link-custom text-white d-flex align-items-center gap-3 px-4 py-3 rounded-0">
                    <i class="fas fa-door-open fs-5" style="width: 24px;"></i>
                    <span class="fw-semibold">Rooms</span>
                </a>
            </li>

            <!-- Users -->
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/views/admin/users/list.jsp" 
                   class="nav-link sidebar-link-custom text-white d-flex align-items-center gap-3 px-4 py-3 rounded-0">
                    <i class="fas fa-users fs-5" style="width: 24px;"></i>
                    <span class="fw-semibold">Users</span>
                </a>
            </li>
        </ul>
    </nav>

    <!-- Sidebar Footer -->
    <div class="p-4 border-top border-secondary border-opacity-25">
        <!-- Admin Info -->
        <div class="d-flex align-items-center gap-3 p-3 mb-3 rounded-3"
             style="background: rgba(230, 9, 20, 0.1); border: 1px solid rgba(230, 9, 20, 0.2);">
            <div class="admin-avatar-custom rounded-circle d-flex align-items-center justify-content-center text-white"
                 style="width: 45px; height: 45px; flex-shrink: 0;">
                <i class="fas fa-user-shield fs-5"></i>
            </div>
            <div class="flex-grow-1 overflow-hidden">
                <div class="text-white fw-semibold text-truncate">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.fullName}">
                            ${sessionScope.user.fullName}
                        </c:when>
                        <c:otherwise>
                            Admin
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="text-white-50 small">Administrator</div>
            </div>
        </div>
    </div>
</div>

<!-- Sidebar Toggle Button (Desktop & Mobile) -->
<button class="btn btn-danger position-fixed shadow-lg rounded-3 d-flex align-items-center justify-content-center"
        style="top: 90px; left: 1rem; width: 50px; height: 50px; z-index: 1000; transition: left 0.3s ease;"
        onclick="toggleAdminSidebar()"
        id="sidebarToggleBtn">
    <i class="fas fa-bars fs-5" id="toggleIcon"></i>
</button>

<!-- Overlay for mobile -->
<div class="position-fixed top-0 start-0 w-100 h-100 bg-dark d-none"
     style="opacity: 0.7; z-index: 998;"
     id="sidebarOverlay"
     onclick="toggleAdminSidebar()">
</div>

<script>
// Sidebar state management
let sidebarCollapsed = false;

function toggleAdminSidebar() {
    const sidebar = document.getElementById('adminSidebar');
    const toggleBtn = document.getElementById('sidebarToggleBtn');
    const toggleIcon = document.getElementById('toggleIcon');
    const overlay = document.getElementById('sidebarOverlay');
    const body = document.body;

    sidebarCollapsed = !sidebarCollapsed;

    if (window.innerWidth >= 992) {
        // Desktop behavior
        if (sidebarCollapsed) {
            sidebar.style.transform = 'translateX(-280px)';
            toggleBtn.style.left = '1rem';
            toggleIcon.className = 'fas fa-bars fs-5';
            body.style.paddingLeft = '0';
        } else {
            sidebar.style.transform = 'translateX(0)';
            toggleBtn.style.left = '300px';
            toggleIcon.className = 'fas fa-times fs-5';
            body.style.paddingLeft = '280px';
        }
    } else {
        // Mobile behavior
        if (sidebarCollapsed) {
            sidebar.style.transform = 'translateX(-280px)';
            overlay.classList.add('d-none');
            toggleIcon.className = 'fas fa-bars fs-5';
        } else {
            sidebar.style.transform = 'translateX(0)';
            overlay.classList.remove('d-none');
            toggleIcon.className = 'fas fa-times fs-5';
        }
    }
}

// Initialize on page load
window.addEventListener('load', function() {
    const sidebar = document.getElementById('adminSidebar');
    const toggleBtn = document.getElementById('sidebarToggleBtn');
    const body = document.body;

    if (window.innerWidth >= 992) {
        // Desktop - sidebar visible by default
        sidebar.style.transform = 'translateX(0)';
        toggleBtn.style.left = '300px';
        document.getElementById('toggleIcon').className = 'fas fa-times fs-5';
        body.style.paddingLeft = '280px';
        sidebarCollapsed = false;
    } else {
        // Mobile - sidebar hidden by default
        sidebar.style.transform = 'translateX(-280px)';
        toggleBtn.style.left = '1rem';
        sidebarCollapsed = true;
    }
});

// Handle window resize
window.addEventListener('resize', function() {
    const sidebar = document.getElementById('adminSidebar');
    const toggleBtn = document.getElementById('sidebarToggleBtn');
    const overlay = document.getElementById('sidebarOverlay');
    const body = document.body;

    if (window.innerWidth >= 992) {
        // Switch to desktop mode
        overlay.classList.add('d-none');
        if (!sidebarCollapsed) {
            sidebar.style.transform = 'translateX(0)';
            toggleBtn.style.left = '300px';
            body.style.paddingLeft = '280px';
        }
    } else {
        // Switch to mobile mode
        toggleBtn.style.left = '1rem';
        body.style.paddingLeft = '0';
        if (sidebarCollapsed) {
            sidebar.style.transform = 'translateX(-280px)';
        }
    }
});
</script>