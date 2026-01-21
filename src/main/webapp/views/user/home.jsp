<%-- Document : home.jsp Created on : Jan 11, 2026, 4:45:20 PM Author : LENOVO --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <%-- Trang chủ --%>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
              crossorigin="anonymous"><!-- comment -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
        crossorigin="anonymous"></script>
        <!-- CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">

        <!-- Nếu là Admin, thêm CSS cho sidebar -->
    <c:if test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/nav-admin.css">
    </c:if>
</head>

<body>
    <!-- Header -->
    <jsp:include page="../../layout/header.jsp"></jsp:include>
    <!-- End header -->

    <!-- Body-->

    <!-- ========================================
         HERO SLIDER BANNER
         ======================================== -->
    <div class="hero-slider">
        <div class="slider-container" id="sliderContainer">
            <!-- Slide 1 -->
            <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1920');">
                <div class="slide-content">
                    <h1 class="slide-title">Avengers: Endgame</h1>
                    <div class="slide-info">
                        <span class="info-badge"><i class="fas fa-star"></i> 8.4/10</span>
                        <span class="info-badge"><i class="fas fa-clock"></i> 181 phút</span>
                        <span class="info-badge">T13</span>
                    </div>
                    <p class="slide-description">
                        Sau thảm họa Infinity War, các Avengers tập hợp lần cuối để đảo ngược 
                        hành động của Thanos và khôi phục trật tự vũ trụ.
                    </p>
                    <div class="slide-actions">
                        <a href="#" class="btn-primary">
                            <i class="fas fa-play"></i> Đặt vé ngay
                        </a>
                        <a href="#" class="btn-secondary">
                            <i class="fas fa-info-circle"></i> Chi tiết
                        </a>
                    </div>
                </div>
            </div>

            <!-- Slide 2 -->
            <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=1920');">
                <div class="slide-content">
                    <h1 class="slide-title">Spider-Man: No Way Home</h1>
                    <div class="slide-info">
                        <span class="info-badge"><i class="fas fa-star"></i> 8.7/10</span>
                        <span class="info-badge"><i class="fas fa-clock"></i> 148 phút</span>
                        <span class="info-badge">T13</span>
                    </div>
                    <p class="slide-description">
                        Peter Parker phải đối mặt với hậu quả sau khi danh tính Spider-Man bị lộ, 
                        dẫn đến những biến cố không thể tưởng tượng.
                    </p>
                    <div class="slide-actions">
                        <a href="#" class="btn-primary">
                            <i class="fas fa-play"></i> Đặt vé ngay
                        </a>
                        <a href="#" class="btn-secondary">
                            <i class="fas fa-info-circle"></i> Chi tiết
                        </a>
                    </div>
                </div>
            </div>

            <!-- Slide 3 -->
            <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=1920');">
                <div class="slide-content">
                    <h1 class="slide-title">The Batman</h1>
                    <div class="slide-info">
                        <span class="info-badge"><i class="fas fa-star"></i> 7.8/10</span>
                        <span class="info-badge"><i class="fas fa-clock"></i> 176 phút</span>
                        <span class="info-badge">T16</span>
                    </div>
                    <p class="slide-description">
                        Batman điều tra một loạt vụ án bí ẩn ở Gotham City, khám phá sự thật 
                        đen tối về thành phố và chính mình.
                    </p>
                    <div class="slide-actions">
                        <a href="#" class="btn-primary">
                            <i class="fas fa-play"></i> Đặt vé ngay
                        </a>
                        <a href="#" class="btn-secondary">
                            <i class="fas fa-info-circle"></i> Chi tiết
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Slider Controls -->
        <button class="slider-arrow prev" onclick="prevSlide()">
            <i class="fas fa-chevron-left"></i>
        </button>
        <button class="slider-arrow next" onclick="nextSlide()">
            <i class="fas fa-chevron-right"></i>
        </button>

        <div class="slider-controls" id="sliderDots"></div>
    </div>

    <!-- ========================================
         MOVIE SECTIONS
         ======================================== -->

    <!-- Phim mới nhất -->
    <div class="movie-section">
        <div class="section-header">
            <h2>🎬 Phim mới cập nhật</h2>
            <a href="${pageContext.request.contextPath}/movies" class="view-all">
            Xem tất cả <i class="fas fa-arrow-right"></i>
        </a>
    </div>

    <c:choose>
        <c:when test="${not empty latestMovies}">
            <div class="movie-slider">
                <c:forEach var="movie" items="${latestMovies}">
                    <div class="movie-card">
                        <img src="${pageContext.request.contextPath}/images/${movie.posterUrl}" 
                             alt="${movie.title}"
                             class="movie-poster">
                        <div class="movie-overlay">
                            <div class="movie-title">${movie.title}</div>
                            <div class="movie-info">
                                <span class="movie-rating"><i class="fas fa-star"></i> </span>
                                <span>${movie.duration} phút</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <i class="fas fa-film"></i>
                <h3>Chưa có phim nào</h3>
                <p>Hãy quay lại sau nhé !</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Top phim được yêu thích -->
<div class="movie-section">
    <div class="section-header">
        <h2>⭐ Top phim được yêu thích</h2>
        <a href="${pageContext.request.contextPath}/movies" class="view-all">
            Xem tất cả <i class="fas fa-arrow-right"></i>
        </a>
    </div>

    <c:if test="${not empty topRatedMovies}">
        <div class="movie-slider">
            <c:forEach var="movie" items="${topRatedMovies}">
                <div class="movie-card" onclick="location.href = '${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}'">
                    <img src="${pageContext.request.contextPath}/images/${movie.posterUrl}" 
                         alt="${movie.title}"
                         class="movie-poster"
                         onerror="this.src='https://via.placeholder.com/220x330?text=No+Image'">
                    <div class="movie-overlay">
                        <div class="movie-title">${movie.title}</div>
                        <div class="movie-info">
                            <span class="movie-rating"><i class="fas fa-star"></i> 9.0</span>
                            <span>${movie.duration} phút</span>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>

<!-- Phim hành động -->
<div class="movie-section">
    <div class="section-header">
        <h2>💥 Phim hành động</h2>
        <a href="${pageContext.request.contextPath}/movies?genre=Action" class="view-all">
            Xem tất cả <i class="fas fa-arrow-right"></i>
        </a>
    </div>

    <c:if test="${not empty actionMovies}">
        <div class="movie-slider">
            <c:forEach var="movie" items="${actionMovies}">
                <div class="movie-card" onclick="location.href = '${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}'">
                    <img src="${pageContext.request.contextPath}/images/${movie.posterUrl}" 
                         alt="${movie.title}"
                         class="movie-poster"
                         onerror="this.src='https://via.placeholder.com/220x330?text=No+Image'">
                    <div class="movie-overlay">
                        <div class="movie-title">${movie.title}</div>
                        <div class="movie-info">
                            <span class="movie-rating"><i class="fas fa-star"></i> 8.2</span>
                            <span>${movie.duration} phút</span>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>

<!-- Phim kịch -->
<div class="movie-section">
    <div class="section-header">
        <h2>🎭 Phim kịch</h2>
        <a href="${pageContext.request.contextPath}/movies?genre=Drama" class="view-all">
            Xem tất cả <i class="fas fa-arrow-right"></i>
        </a>
    </div>

    <c:if test="${not empty dramaMovies}">
        <div class="movie-slider">
            <c:forEach var="movie" items="${dramaMovies}">
                <div class="movie-card" onclick="location.href = '${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}'">
                    <img src="${pageContext.request.contextPath}/images/${movie.posterUrl}" 
                         alt="${movie.title}"
                         class="movie-poster"
                         onerror="this.src='https://via.placeholder.com/220x330?text=No+Image'">
                    <div class="movie-overlay">
                        <div class="movie-title">${movie.title}</div>
                        <div class="movie-info">
                            <span class="movie-rating"><i class="fas fa-star"></i> 8.8</span>
                            <span>${movie.duration} phút</span>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>
<!-- End Body-->
<!-- Footer -->
<jsp:include page="../../layout/footer.jsp"></jsp:include>
    <!-- End Footer -->
    <script src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>