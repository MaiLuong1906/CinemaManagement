<%-- Document : movies Created on : Jan 11, 2026, 4:45:29 PM Author : LENOVO --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <%-- Trang danh sách phim --%>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
              crossorigin="anonymous"><!-- comment -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
        crossorigin="anonymous"></script>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: Arial, sans-serif;
                background-color: #141414;
                color: #fff;
                padding: 20px;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
            }

            h1 {
                margin-bottom: 30px;
                font-size: 2.5em;
            }

            .search-bar {
                margin-bottom: 30px;
            }

            .search-bar input {
                padding: 10px 15px;
                width: 300px;
                border: none;
                border-radius: 5px;
            }

            .search-bar button {
                padding: 10px 20px;
                background-color: #e50914;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }

            .genre-filter {
                margin-bottom: 20px;
            }

            .genre-filter a {
                display: inline-block;
                padding: 8px 15px;
                margin-right: 10px;
                background-color: #333;
                color: #fff;
                text-decoration: none;
                border-radius: 5px;
            }

            .genre-filter a:hover {
                background-color: #e50914;
            }

            .movie-count {
                margin-bottom: 20px;
                color: #999;
            }

            .movie-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
                gap: 20px;
            }

            .movie-card {
                background-color: #222;
                border-radius: 8px;
                overflow: hidden;
                transition: transform 0.3s;
                cursor: pointer;
            }

            .movie-card:hover {
                transform: scale(1.05);
            }

            .movie-poster {
                width: 100%;
                height: 300px;
                object-fit: cover;
            }

            .movie-info {
                padding: 15px;
            }

            .movie-title {
                font-size: 1.1em;
                font-weight: bold;
                margin-bottom: 8px;
            }

            .movie-genre {
                color: #999;
                font-size: 0.9em;
                margin-bottom: 5px;
            }

            .movie-rating {
                color: #ffd700;
                font-weight: bold;
            }

            .movie-duration {
                color: #999;
                font-size: 0.85em;
            }

            .no-movies {
                text-align: center;
                padding: 50px;
                color: #999;
            }
        </style>
    </head>

    <body>
        <div class="container">
            <h1>${pageTitle}</h1>

            <!-- Thanh tìm kiếm -->
            <div class="search-bar">
                <form action="MovieListServlet" method="get">
                    <input type="text" name="search" placeholder="Tìm kiếm phim..." 
                           value="${param.search}">
                    <button type="submit">Tìm kiếm</button>
                </form>
            </div>

            <!-- Bộ lọc thể loại -->
            <div class="genre-filter">
                <a href="${pageContext.request.contextPath}/movies">Tất cả</a>
                <a href="${pageContext.request.contextPath}/movies?genre=Action">Action</a>
                <a href="${pageContext.request.contextPath}/movies?genre=Drama">Drama</a>
                <a href="${pageContext.request.contextPath}/movies?genre=Sci-Fi">Sci-Fi</a>
                <a href="${pageContext.request.contextPath}/movies?genre=Thriller">Thriller</a>
                <a href="${pageContext.request.contextPath}/movies?genre=Adventure">Adventure</a>
            </div>

            <!-- Số lượng phim -->
            <p class="movie-count">Tìm thấy ${totalMovies} phim</p>

            <!-- Danh sách phim -->
            <c:choose>
                <c:when test="${not empty movies}">
                    <div class="movie-grid">
                        <c:forEach var="movie" items="${movies}">
                            <div class="movie-card" onclick="location.href = 'movie-detail?id=${movie.movieId}'">
<<<<<<< HEAD
                                <img src="${pageContext.request.contextPath}/images/teaser-poster/${movie.posterUrl}" 
=======
                                <img src="${pageContext.request.contextPath}/image?name=${movie.posterUrl}" 
>>>>>>> origin
                                     alt="${movie.title}" 
                                     class="movie-poster"
                                     onerror="this.src='https://via.placeholder.com/200x300?text=No+Image'">
                                <div class="movie-info">
                                    <div class="movie-title">${movie.title}</div>
                                    <%-- <div class="movie-genre">${movie.genre}</div> --%>
                                    <%-- <div class="movie-rating">⭐ ${movie.rating}/10</div> --%>
                                    <div class="movie-duration">
                                        ${movie.releaseDate.year} • ${movie.duration} phút
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="no-movies">
                        <h2>Không tìm thấy phim nào</h2>
                        <p>Vui lòng thử tìm kiếm với từ khóa khác</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </body>

</html>