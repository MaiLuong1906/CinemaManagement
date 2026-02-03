<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Movie Showtimes</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view_admin_movie_list.css">
</head>
<body class="bg-light">
    <jsp:include page="/views/admin/component/flash-message.jsp"/>
<div class="container mt-4">
    <!-- Back home -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/views/user/home.jsp"
           class="btn btn-success">
            Trang chủ
        </a>
    </div>       
    <div class="card shadow-sm">
        <div class="card-body">
            <h4 class="mb-3 fw-bold">Movie Showtimes</h4>
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
                            <form action="${pageContext.request.contextPath}/venue?action=update-showtime"
                                method="get"
                                style="display:inline">
                              <input type="hidden" name="showtimeId" value="${m.showtimeId}">
                              <button type="submit"
                                      class="btn btn-sm btn-warning">
                                  Sửa
                              </button>
                          </form>
                            <form action="${pageContext.request.contextPath}/venue?action=delete-showtime"
                                method="post"
                                style="display:inline">
                              <input type="hidden" name="showtimeId" value="${m.showtimeId}">
                              <button type="submit"
                                      class="btn btn-sm btn-danger"
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
