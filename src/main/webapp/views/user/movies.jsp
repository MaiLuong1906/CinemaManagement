<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>${pageTitle} - Cinema</title>

                <!-- Bootstrap CSS -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

                <!-- Font Awesome -->
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

                <!-- Custom CSS -->

                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie.css" />
                <c:if test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/nav-admin.css">
                </c:if>
            </head>

            <body class="bg-dark text-white <c:if test='${sessionScope.user != null && sessionScope.user.roleId == "
                Admin"}'>admin-layout</c:if>"
                style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0a0a0a
                !important;">

                <!-- Header -->
                <jsp:include page="../../layout/header.jsp"></jsp:include>

                <!-- Admin Sidebar (Only for Admin) -->
                <c:if test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
                    <jsp:include page="../../layout/nav-admin.jsp"></jsp:include>
                </c:if>

                <!-- Main Content -->
                <div class="container-fluid px-4 py-5" style="max-width: 1400px;">

                    <!-- Page Header -->
                    <div class="text-center mb-5">
                        <h1 class="display-4 fw-bold mb-2"
                            style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
                            🎬 ${pageTitle}
                        </h1>
                        <p class="text-secondary fs-5">Khám phá thế giới điện ảnh tuyệt vời</p>
                    </div>

                    <!-- Search Bar -->
                    <div class="row justify-content-center mb-5">
                        <div class="col-lg-8 col-md-10">
                            <form action="movie" method="get">
                                <input type="hidden" name="action" value="list">
                                <div class="search-wrapper position-relative">
                                    <i class="fas fa-search position-absolute text-secondary"
                                        style="left: 20px; top: 50%; transform: translateY(-50%); font-size: 18px;"></i>
                                    <input type="text" name="search" class="form-control form-control-lg search-input"
                                        placeholder="Tìm kiếm phim yêu thích của bạn..." value="${param.search}"
                                        style="padding-left: 55px; background: rgba(255,255,255,0.05); border: 2px solid rgba(255,255,255,0.1); color: #fff; border-radius: 50px;">
                                    <button class="btn btn-search position-absolute" type="submit"
                                        style="right: 5px; top: 50%; transform: translateY(-50%); background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border: none; border-radius: 50px; padding: 10px 30px; color: #fff; font-weight: 600;">
                                        Tìm kiếm
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Genre Filter Form - Cập nhật để giữ lại search keyword -->
                    <div class="mb-5 p-4 rounded-3 border"
                        style="background: rgba(255,255,255,0.03); border-color: rgba(255,255,255,0.1) !important;">
                        <form action="movie" method="get" id="genreForm">
                            <!-- Hidden input để giữ lại search keyword khi filter genre -->
                            <c:if test="${not empty param.search}">
                                <input type="hidden" name="search" value="${param.search}">
                            </c:if>

                            <div class="d-flex align-items-center justify-content-between flex-wrap gap-4">
                                <div class="d-flex align-items-center gap-4 flex-grow-1 flex-wrap">
                                    <label class="fw-bold text-white mb-0 fs-4">
                                        <i class="fas fa-filter me-2" style="color: #667eea;"></i>Thể loại:
                                    </label>
                                    <div class="d-flex flex-wrap gap-3">
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Action" id="genreAction"
                                                onchange="this.form.submit()">
                                            <label for="genreAction"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                💥 Action
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Animation" id="genreAnimation"
                                                onchange="this.form.submit()">
                                            <label for="genreAnimation"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                Animation
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Fantacy" id="genreFantacy"
                                                onchange="this.form.submit()">
                                            <label for="genreFantacy"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                Fantacy
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Drama" id="genreDrama"
                                                onchange="this.form.submit()">
                                            <label for="genreDrama"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                🎭 Drama
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Sci-Fi" id="genreSciFi"
                                                onchange="this.form.submit()">
                                            <label for="genreSciFi"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                🚀 Sci-Fi
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Thriller" id="genreThriller"
                                                onchange="this.form.submit()">
                                            <label for="genreThriller"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                😱 Thriller
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Adventure" id="genreAdventure"
                                                onchange="this.form.submit()">
                                            <label for="genreAdventure"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                🗺️ Adventure
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Comedy" id="genreComedy"
                                                onchange="this.form.submit()">
                                            <label for="genreComedy"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                😂 Comedy
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Horror" id="genreHorror"
                                                onchange="this.form.submit()">
                                            <label for="genreHorror"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                👻 Horror
                                            </label>
                                        </div>
                                        <div class="genre-chip">
                                            <input type="checkbox" name="genres" value="Romance" id="genreRomance"
                                                onchange="this.form.submit()">
                                            <label for="genreRomance"
                                                class="badge rounded-pill px-4 py-3 m-0 fs-6 fw-semibold border border-2 text-white"
                                                style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.2) !important; min-width: 120px;">
                                                ❤️ Romance
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <a href="${pageContext.request.contextPath}/movie"
                                    class="btn btn-outline-light rounded-pill px-4 py-3 fs-6 fw-semibold border-2">
                                    <i class="fas fa-redo-alt me-2"></i>Xóa bộ lọc
                                </a>
                            </div>
                        </form>
                    </div>

                    <!-- Results Info -->
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2 class="fw-bold fs-4 m-0">
                            <span class="badge"
                                style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 12px 24px; border-radius: 50px;">
                                Tìm thấy ${totalMovies} phim
                            </span>
                        </h2>
                    </div>

                    <!-- Movie Grid -->
                    <c:choose>
                        <c:when test="${not empty movies}">
                            <div class="row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-5 row-cols-xl-6 g-4">
                                <c:forEach var="movie" items="${movies}">
                                    <div class="col">
                                        <div class="movie-card"
                                            onclick="location.href = 'movie?action=detail&movieId=${movie.movieId}'">
                                            <img src="${pageContext.request.contextPath}/image?name=${movie.posterUrl}"
                                                alt="${movie.title}" class="movie-poster"
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
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5">
                                <div class="mb-4">
                                    <i class="fas fa-film" style="font-size: 80px; opacity: 0.3; color: #667eea;"></i>
                                </div>
                                <h2 class="text-white mb-3 fw-bold">Không tìm thấy phim nào</h2>
                                <p class="text-secondary mb-4">Vui lòng thử tìm kiếm với từ khóa khác hoặc chọn thể loại
                                    khác</p>
                                <a href="${pageContext.request.contextPath}/movie"
                                    class="btn btn-lg rounded-3 px-4 text-white fw-bold"
                                    style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                                    <i class="fas fa-arrow-left me-2"></i>Xem tất cả phim
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Footer -->
                <jsp:include page="../../layout/footer.jsp"></jsp:include>

                <!-- Bootstrap JS -->
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

                <script src="${pageContext.request.contextPath}/js/movies.js"></script>
            </body>

            </html>