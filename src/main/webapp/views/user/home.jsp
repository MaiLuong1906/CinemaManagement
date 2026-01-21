<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Movie Booking - Trang chủ</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

        <!-- Custom CSS (minimal) -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">

        <c:if test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/nav-admin.css">
        </c:if>
    </head>

    <body class="bg-dark text-white <c:if test='${sessionScope.user != null && sessionScope.user.roleId == "Admin"}'>admin-layout</c:if>" 
          style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0a0a0a !important;">

            <!-- Header -->
        <jsp:include page="../../layout/header.jsp"></jsp:include>

            <!-- Admin Sidebar (Only for Admin) -->
        <c:if test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
            <jsp:include page="../../layout/nav-admin.jsp"></jsp:include>
        </c:if>

        <!-- ========================================
             HERO SLIDER BANNER
             ======================================== -->
        <div class="hero-slider">
            <div class="slider-container" id="sliderContainer">
                <c:if test="${not empty topRatedMovies}">
                    <c:forEach var="movie" items="${topRatedMovies}">
                        <div class="slide" style="background-image: url('${pageContext.request.contextPath}/images/quad-poster/${movie.posterUrl}');"
                             onclick="location.href = '${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}'">
                            <div class="slide-content">
                                <h1 class="display-3 fw-bold mb-3" style="text-shadow: 2px 2px 4px rgba(0,0,0,0.5);">
                                    ${movie.title}
                                </h1>
                                <div class="d-flex gap-3 mb-4">
                                    <span class="badge info-badge rounded-pill px-3 py-2">
                                        <i class="fas fa-star"></i> 8.4/10
                                    </span>
                                    <span class="badge info-badge rounded-pill px-3 py-2">
                                        <i class="fas fa-clock"></i> ${movie.duration} phút
                                    </span>
                                    <span class="badge info-badge rounded-pill px-3 py-2">T13</span>
                                </div>
                                <p class="fs-5 mb-4" style="line-height: 1.6; text-shadow: 1px 1px 2px rgba(0,0,0,0.5);">
                                    ${movie.description}
                                </p>
                                <div class="d-flex gap-3">
                                    <a href="#" class="btn btn-gradient-primary btn-lg rounded-3 px-4 text-white fw-bold">
                                        <i class="fas fa-play me-2"></i> Đặt vé ngay
                                    </a>
                                    <a href="#" class="btn btn-glass btn-lg rounded-3 px-4 text-white fw-bold">
                                        <i class="fas fa-info-circle me-2"></i> Chi tiết
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>




            </div>

            <!-- Slider Controls -->
            <button class="slider-arrow prev" onclick="prevSlide()">
                <i class="fas fa-chevron-left fs-5"></i>
            </button>
            <button class="slider-arrow next" onclick="nextSlide()">
                <i class="fas fa-chevron-right fs-5"></i>
            </button>
            <div class="slider-controls d-flex gap-2" id="sliderDots"></div>
        </div>

        <!-- ========================================
             MOVIE SECTIONS
             ======================================== -->

        <!-- Phim mới nhất -->
        <section class="container-fluid px-4 py-5" style="max-width: 1400px;">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold fs-2 m-0">🎬 Phim mới cập nhật</h2>
                <a href="${pageContext.request.contextPath}/movies" 
                   class="text-decoration-none fw-semibold d-flex align-items-center gap-2"
                   style="color: #667eea;">
                    Xem tất cả <i class="fas fa-arrow-right"></i>
                </a>
            </div>

            <c:choose>
                <c:when test="${not empty latestMovies}">
                    <div class="d-flex gap-3 overflow-x-auto pb-3" style="scroll-behavior: smooth;">
                        <c:forEach var="movie" items="${latestMovies}">
                            <div class="movie-card" onclick="location.href = '${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}'">
                                <img src="${pageContext.request.contextPath}/images/teaser-poster/${movie.posterUrl}" 
                                     alt="${movie.title}"
                                     class="movie-poster"
                                     onerror="this.src='https://via.placeholder.com/220x330?text=No+Image'">
                                <div class="movie-overlay">
                                    <div class="fw-bold fs-6 mb-2">${movie.title}</div>
                                    <div class="d-flex gap-2 text-secondary small">
                                        <span style="color: #ffd700;">
                                            <i class="fas fa-star"></i> 8.0
                                        </span>
                                        <span>${movie.duration} phút</span>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-5" style="color: #666;">
                        <i class="fas fa-film mb-3" style="font-size: 80px; opacity: 0.3;"></i>
                        <h3>Chưa có phim nào</h3>
                        <p>Hãy quay lại sau nhé!</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

        <!-- Top phim được yêu thích -->
        <section class="container-fluid px-4 py-5" style="max-width: 1400px;">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold fs-2 m-0">⭐ Top phim được yêu thích</h2>
                <a href="${pageContext.request.contextPath}/movies" 
                   class="text-decoration-none fw-semibold d-flex align-items-center gap-2"
                   style="color: #667eea;">
                    Xem tất cả <i class="fas fa-arrow-right"></i>
                </a>
            </div>

            <c:if test="${not empty topRatedMovies}">
                <div class="d-flex gap-3 overflow-x-auto pb-3" style="scroll-behavior: smooth;">
                    <c:choose>
                        <c:when test="${not empty topRatedMovies}">
                            <div class="d-flex gap-3 overflow-x-auto pb-3" style="scroll-behavior: smooth;">
                                <c:forEach var="movie" items="${topRatedMovies}">
                                    <div class="movie-card" onclick="location.href = '${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}'">
                                        <img src="${pageContext.request.contextPath}/images/teaser-poster/${movie.posterUrl}" 
                                             alt="${movie.title}"
                                             class="movie-poster"
                                             onerror="this.src='https://via.placeholder.com/220x330?text=No+Image'">
                                        <div class="movie-overlay">
                                            <div class="fw-bold fs-6 mb-2">${movie.title}</div>
                                            <div class="d-flex gap-2 text-secondary small">
                                                <span style="color: #ffd700;">
                                                    <i class="fas fa-star"></i> 8.0
                                                </span>
                                                <span>${movie.duration} phút</span>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5" style="color: #666;">
                                <i class="fas fa-film mb-3" style="font-size: 80px; opacity: 0.3;"></i>
                                <h3>Chưa có phim nào</h3>
                                <p>Hãy quay lại sau nhé!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </section>

        <!-- Phim hành động -->
        <section class="container-fluid px-4 py-5" style="max-width: 1400px;">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold fs-2 m-0">💥 Phim hành động</h2>
                <a href="${pageContext.request.contextPath}/movies?genre=Action" 
                   class="text-decoration-none fw-semibold d-flex align-items-center gap-2"
                   style="color: #667eea;">
                    Xem tất cả <i class="fas fa-arrow-right"></i>
                </a>
            </div>

            <c:if test="${not empty actionMovies}">
                <div class="d-flex gap-3 overflow-x-auto pb-3" style="scroll-behavior: smooth;">
                    <c:choose>
                        <c:when test="${not empty actionMovies}">
                            <div class="d-flex gap-3 overflow-x-auto pb-3" style="scroll-behavior: smooth;">
                                <c:forEach var="movie" items="${actionMovies}">
                                    <div class="movie-card" onclick="location.href = '${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}'">
                                        <img src="${pageContext.request.contextPath}/images/teaser-poster/${movie.posterUrl}" 
                                             alt="${movie.title}"
                                             class="movie-poster"
                                             onerror="this.src='https://via.placeholder.com/220x330?text=No+Image'">
                                        <div class="movie-overlay">
                                            <div class="fw-bold fs-6 mb-2">${movie.title}</div>
                                            <div class="d-flex gap-2 text-secondary small">
                                                <span style="color: #ffd700;">
                                                    <i class="fas fa-star"></i> 8.0
                                                </span>
                                                <span>${movie.duration} phút</span>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5" style="color: #666;">
                                <i class="fas fa-film mb-3" style="font-size: 80px; opacity: 0.3;"></i>
                                <h3>Chưa có phim nào</h3>
                                <p>Hãy quay lại sau nhé!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </section>

        <!-- Phim kịch -->
        <section class="container-fluid px-4 py-5" style="max-width: 1400px;">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold fs-2 m-0">🎭 Phim kịch</h2>
                <a href="${pageContext.request.contextPath}/movies?genre=Drama" 
                   class="text-decoration-none fw-semibold d-flex align-items-center gap-2"
                   style="color: #667eea;">
                    Xem tất cả <i class="fas fa-arrow-right"></i>
                </a>
            </div>

            <c:if test="${not empty dramaMovies}">
                <div class="d-flex gap-3 overflow-x-auto pb-3" style="scroll-behavior: smooth;">
                    <c:choose>
                        <c:when test="${not empty dramaMovies}">
                            <div class="d-flex gap-3 overflow-x-auto pb-3" style="scroll-behavior: smooth;">
                                <c:forEach var="movie" items="${dramaMovies}">
                                    <div class="movie-card" onclick="location.href = '${pageContext.request.contextPath}/movie-detail?id=${movie.movieId}'">
                                        <img src="${pageContext.request.contextPath}/images/teaser-poster/${movie.posterUrl}" 
                                             alt="${movie.title}"
                                             class="movie-poster"
                                             onerror="this.src='https://via.placeholder.com/220x330?text=No+Image'">
                                        <div class="movie-overlay">
                                            <div class="fw-bold fs-6 mb-2">${movie.title}</div>
                                            <div class="d-flex gap-2 text-secondary small">
                                                <span style="color: #ffd700;">
                                                    <i class="fas fa-star"></i> 8.0
                                                </span>
                                                <span>${movie.duration} phút</span>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5" style="color: #666;">
                                <i class="fas fa-film mb-3" style="font-size: 80px; opacity: 0.3;"></i>
                                <h3>Chưa có phim nào</h3>
                                <p>Hãy quay lại sau nhé!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </section>

        <!-- Footer -->
        <jsp:include page="../../layout/footer.jsp"></jsp:include>

            <!-- Bootstrap JS -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

            <!-- Custom JS -->
            <script src="${pageContext.request.contextPath}/js/home.js"></script>
    </body>
</html>