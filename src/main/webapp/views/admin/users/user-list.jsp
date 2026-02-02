<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <html>

        <head>
            <title>Quản lý người dùng</title>
            <!-- FontAwesome -->
            <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
            <!-- Google Fonts -->
            <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap"
                rel="stylesheet">
            <!-- Admin CSS -->
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-hall.css">
        </head>

        <body>

            <div class="container" style="max-width: 1200px; margin: 0 auto; padding: 2rem;">

                <!-- Page Header -->
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <div>
                        <a href="${pageContext.request.contextPath}/home" class="btn-primary-admin"
                            style="background-color: #3182ce; color: white; padding: 0.5rem 1rem; margin-bottom: 1rem; display: inline-flex; align-items: center; text-decoration: none; border-radius: 6px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                            <i class="fas fa-arrow-left" style="margin-right: 0.5rem;"></i> Quay về trang chủ
                        </a>
                        <h2 style="margin: 0; color: #1a202c; font-size: 1.75rem;">Quản lý người dùng</h2>
                        <p style="margin: 0.5rem 0 0; color: #718096;">Danh sách và phân quyền tài khoản</p>
                    </div>
                </div>

                <!-- Main Card -->
                <div class="admin-card">
                    <div class="admin-card-header">
                        <h5 class="admin-card-title">Danh sách tài khoản</h5>
                        <div style="font-size: 0.9rem; color: #718096;">Tổng số: <strong>${users.size()}</strong></div>
                    </div>

                    <div class="admin-card-body" style="padding: 0;">
                        <table class="table-scientific">
                            <thead>
                                <tr>
                                    <th width="80">ID</th>
                                    <th>Họ và tên</th>
                                    <th>Thông tin liên hệ</th>
                                    <th>Vai trò (Role)</th>
                                    <th>Trạng thái</th>
                                    <th class="text-right">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="u" items="${users}">
                                    <tr>
                                        <td><span
                                                style="font-family: monospace; font-weight: bold; color: #4a5568;">#${u.accountId}</span>
                                        </td>
                                        <td>
                                            <div style="font-weight: 600; color: #2d3748;">${u.fullName}</div>
                                        </td>
                                        <td>
                                            <div style="font-size: 0.9rem;">
                                                <i class="fas fa-envelope"
                                                    style="color: #cbd5e0; margin-right: 5px;"></i> ${u.email} <br>
                                                <i class="fas fa-phone" style="color: #cbd5e0; margin-right: 5px;"></i>
                                                ${u.phoneNumber}
                                            </div>
                                        </td>

                                        <!-- ROLE -->
                                        <td>
                                            <form action="${pageContext.request.contextPath}/admin/users" method="post"
                                                style="margin:0;">
                                                <input type="hidden" name="action" value="role" />
                                                <input type="hidden" name="id" value="${u.accountId}" />

                                                <select name="role" onchange="this.form.submit()"
                                                    class="form-control-admin"
                                                    style="padding: 0.25rem 0.5rem; font-size: 0.85rem; width: auto; display: inline-block;">
                                                    <option value="User" ${u.roleId=='User' ? 'selected' : '' }>Khách
                                                        hàng</option>
                                                    <option value="Staff" ${u.roleId=='Staff' ? 'selected' : '' }>Nhân
                                                        viên</option>
                                                    <option value="Admin" ${u.roleId=='Admin' ? 'selected' : '' }>Quản
                                                        trị viên</option>
                                                </select>
                                            </form>
                                        </td>

                                        <!-- STATUS -->
                                        <td>
                                            <span class="badge-dot ${u.status ? 'status-active' : 'status-inactive'}">
                                                ${u.status ? "Hoạt động" : "Đã khóa"}
                                            </span>
                                        </td>

                                        <!-- ACTION -->
                                        <td class="text-right">
                                            <c:choose>
                                                <c:when test="${u.status}">
                                                    <!-- Lock Button -->
                                                    <form action="${pageContext.request.contextPath}/admin/users"
                                                        method="post" style="display:inline-block; margin:0;">
                                                        <input type="hidden" name="action" value="lock" />
                                                        <input type="hidden" name="id" value="${u.accountId}" />
                                                        <button type="submit" class="action-link"
                                                            style="background: none; border: none; cursor: pointer; color: #f56565;"
                                                            title="Khóa tài khoản">
                                                            <i class="fas fa-unlock"></i> <i class="fas fa-arrow-right"
                                                                style="font-size: 0.7rem;"></i> <i
                                                                class="fas fa-lock"></i>
                                                        </button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <!-- Unlock Button -->
                                                    <form action="${pageContext.request.contextPath}/admin/users"
                                                        method="post" style="display:inline-block; margin:0;">
                                                        <input type="hidden" name="action" value="unlock" />
                                                        <input type="hidden" name="id" value="${u.accountId}" />
                                                        <button type="submit" class="action-link"
                                                            style="background: none; border: none; cursor: pointer; color: #48bb78;"
                                                            title="Mở khóa tài khoản">
                                                            <i class="fas fa-lock-open"></i>
                                                        </button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty users}">
                                    <tr>
                                        <td colspan="6" style="text-align: center; padding: 3rem; color: #a0aec0;">
                                            <i class="fas fa-users"
                                                style="font-size: 2rem; margin-bottom: 1rem; display: block;"></i>
                                            Chưa có người dùng nào trong hệ thống.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>

        </body>

        </html>