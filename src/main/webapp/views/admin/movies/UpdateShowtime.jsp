<%-- 
    Document   : UpdateShowtime
    Created on : Jan 25, 2026, 3:11:51 AM
    Author     : nguye
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cập nhật suất chiếu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/update-showtime-list.css">
</head>
<body class="bg-light">

<div class="container mt-4">
    <h3 class="mb-3 fw-bold">Cập nhật suất chiếu</h3>

    <form action="${pageContext.request.contextPath}/UpdateShowtimeServlet"
          method="post"
          class="card p-4 shadow-sm">

        <!--id showtime co san khi chuyen tiep-->
        <input type="hidden" name="showtimeId" value="${showtime.showtimeId}">

        <!-- Phim -->
        <div class="mb-3">
            <label class="form-label">Phim</label>
            <select name="movieId" class="form-select" required>
                <c:forEach var="m" items="${movieList}">
                    <option value="${m.movieId}"
                        ${m.movieId == showtime.movieId ? "selected" : ""}>
                        ${m.title}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Phong chieu -->
        <div class="mb-3">
            <label class="form-label">Phòng chiếu</label>
            <select name="hallId" class="form-select" required>
                <c:forEach var="h" items="${hallList}">
                    <option value="${h.hallId}"
                        ${h.hallId == showtime.hallId ? "selected" : ""}>
                        ${h.hallName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Ngay chieu -->
        <div class="mb-3">
            <label class="form-label">Ngày chiếu</label>
            <input type="date"
                   name="showDate"
                   value="${showtime.showDate}"
                   class="form-control"
                   required>
        </div>
                   
        <!-- Khung gio -->
        <div class="mb-3">
            <label class="form-label">Khung giờ</label>
            <select name="slotId" class="form-select" required>
                <c:forEach var="s" items="${slotList}">
                    <option value="${s.slotId}"
                        ${s.slotId == showtime.slotId ? "selected" : ""}>
                        ${s.slotName} (${s.startHour} - ${s.endHour})
                    </option>
                </c:forEach>
            </select>
        </div>

        

        <!-- BUTTON -->
        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-warning">
                Cập nhật
            </button>
            <a href="${pageContext.request.contextPath}/ListMovieForAdmin"
               class="btn btn-secondary">
                Hủy
            </a>
        </div>
    </form>
</div>
</body>
</html>
