<%-- 
    Document   : list
    Created on : Jan 11, 2026, 4:47:35 PM
    Author     : LENOVO
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Movie Showtimes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../../../css/list_movie_admin.css">
</head>

<body>
    <div class="back-home">
            <a href="${pageContext.request.contextPath}/views/user/home.jsp"
               class="btn btn-success">
                Trang chủ
            </a>
        </div>
<div class="container mt-4 movie-detail-container">

    <h2 class="movie-detail-title mb-4">Movie Showtimes</h2>

    <table class="table movie-detail-table text-center align-middle">
        <thead>
            <tr>
                <th>ID Phim</th>
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
                    <td>${m.movieId}</td>
                    <td class="movie-title">
                        ${m.movieTitle}
                    </td>
                    <td>
                        <span class="genre-badge">${m.genres}</span>
                    </td>
                    <td>${m.hallName}</td>
                    <td>
                        <div class="showtime-box">
                            <span class="slot-name">${m.slotName}</span><br>
                            ${m.startTime} - ${m.endTime}
                        </div>
                    </td>
                    <td>
                        <a href="edit?movieId=${m.movieId}" class="action-btn edit">Sửa</a>
                        <a href="delete?movieId=${m.movieId}" class="action-btn delete"
                           onclick="return confirm('Xóa phim này?')">Xóa</a>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty movieDetails}">
                <tr>
                    <td colspan="5" class="movie-detail-empty">
                        Không có lịch chiếu
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>

</div>
</body>
</html>