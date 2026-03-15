<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Movie Showtimes</title>

            <!-- Bootstrap -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view_admin_movie_list.css">
        </head>

        <body class="bg-light">
            <jsp:include page="/views/admin/component/flash-message.jsp" />
            <div class="container mt-4">
                <!-- Back home -->
                <div class="mb-3">
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-success">
                        Trang chủ
                    </a>
                </div>
                <div class="card shadow-sm">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h4 class="fw-bold m-0">Movie Showtimes</h4>
                            <form action="${pageContext.request.contextPath}/movie" method="get" class="d-flex gap-2">
                                <input type="hidden" name="action" value="admin-list">
                                <input type="text" name="search" class="form-control form-control-sm"
                                    placeholder="Tìm tên phim..." value="${searchKeyword}" style="width: 200px;">
                                <button type="submit" class="btn btn-sm btn-primary">Tìm</button>
                                <c:if test="${not empty searchKeyword}">
                                    <a href="${pageContext.request.contextPath}/movie?action=admin-list"
                                        class="btn btn-sm btn-secondary">Xóa</a>
                                </c:if>
                            </form>
                        </div>
                        <table class="table table-bordered table-hover align-middle text-center"
                            style="table-layout: fixed; width: 100%;">
                            <thead class="table-dark">
                                <tr>
                                    <th>ID Suất chiếu</th>
                                    <th>Tên phim</th>
                                    <th>Thể loại</th>
                                    <th>Phòng</th>
                                    <th>Khung giờ</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${movieDetails}">
                                    <tr>
                                        <td>${m.showtimeId}</td>
                                        <td class="fw-semibold text-truncate">
                                            ${m.movieTitle}
                                        </td>
                                        <td>
                                            <span class="badge bg-secondary">
                                                ${m.genres}
                                            </span>
                                        </td>
                                        <td>${m.hallName}</td>
                                        <td>
                                            <div class="fw-semibold small">${m.slotName}</div>
                                            <div class="small">
                                                ${m.showDate}
                                            </div>
                                            <div class="small text-muted">
                                                ${m.startTime} - ${m.endTime}
                                            </div>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/showtime?action=update&showtimeId=${m.showtimeId}"
                                                class="btn btn-sm btn-warning">
                                                Sửa
                                            </a>
                                            <form action="${pageContext.request.contextPath}/showtime?action=delete"
                                                method="post" style="display:inline">
                                                <input type="hidden" name="showtimeId" value="${m.showtimeId}">
                                                <button type="submit" class="btn btn-sm btn-danger"
                                                    onclick="return confirm('Xóa suất chiếu này?')">
                                                    Xóa
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty movieDetails}">
                                    <tr>
                                        <td colspan="6" class="text-center text-muted">
                                            Không có lịch chiếu
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