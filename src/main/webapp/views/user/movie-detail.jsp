<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${movie.title} - Chi tiết phim</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #141414;
            color: #fff;
            margin: 0;
            padding: 20px;
        }
        
        .container {
            max-width: 1000px;
            margin: 0 auto;
        }
        
        .back-btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #333;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            margin-bottom: 20px;
            transition: background-color 0.3s;
        }
        
        .back-btn:hover {
            background-color: #e50914;
        }
        
        .movie-detail {
            display: flex;
            gap: 30px;
            flex-wrap: wrap;
        }
        
        .poster {
            width: 300px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.3);
        }
        
        .info {
            flex: 1;
            min-width: 300px;
        }
        
        .info h1 {
            margin-bottom: 20px;
            font-size: 2.5em;
        }
        
        .info p {
            line-height: 1.8;
            margin-bottom: 10px;
            font-size: 1.1em;
        }
        
        .info-label {
            color: #999;
            font-weight: normal;
            display: inline-block;
            width: 150px;
        }
        
        .description {
            margin-top: 20px;
            line-height: 1.8;
            color: #ccc;
        }
    </style>
</head>
<body>
    <div class="container">
        <a href="${pageContext.request.contextPath}/movies" class="back-btn">
            <i class="fas fa-arrow-left"></i> Quay lại
        </a>
        
        <c:if test="${not empty movie}">
            <div class="movie-detail">
                <img src="${pageContext.request.contextPath}/images/${movie.posterUrl}" 
                     alt="${movie.title}" 
                     class="poster"
                     onerror="this.src='https://via.placeholder.com/300x450?text=No+Image'">
                
                <div class="info">
                    <h1>${movie.title}</h1>
                    
                    <%-- Comment rating và genre vì chưa có trong model --%>
                    <%-- <p class="rating">⭐ ${movie.rating}/10</p> --%>
                    <%-- <p><span class="info-label">Thể loại:</span> <strong>${movie.genre}</strong></p> --%>
                    
                    <p><span class="info-label">Thời lượng:</span> <strong>${movie.duration} phút</strong></p>
                    
                    <p><span class="info-label">Ngày phát hành:</span> <strong>${movie.releaseDate}</strong></p>
                    
                    <c:if test="${not empty movie.ageRating}">
                        <p><span class="info-label">Độ tuổi:</span> <strong>${movie.ageRating}</strong></p>
                    </c:if>
                    
                    <c:if test="${not empty movie.description}">
                        <div class="description">
                            <h3>Mô tả:</h3>
                            <p>${movie.description}</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:if>
        
        <c:if test="${empty movie}">
            <div class="text-center mt-5">
                <h2>Không tìm thấy phim</h2>
                <a href="${pageContext.request.contextPath}/movies" class="back-btn mt-3">
                    Quay lại danh sách
                </a>
            </div>
        </c:if>
    </div>
</body>
</html>