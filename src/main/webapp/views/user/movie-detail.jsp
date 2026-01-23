<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${movie.title} - Chi tiết phim</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
            <style>
                * {
                    margin: 0;
                    padding: 0;
                    box-sizing: border-box;
                }

                body {
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
                    color: #fff;
                    min-height: 100vh;
                    position: relative;
                    overflow-x: hidden;
                }

                /* Animated background */
                body::before {
                    content: '';
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background:
                        radial-gradient(circle at 20% 50%, rgba(229, 9, 20, 0.1) 0%, transparent 50%),
                        radial-gradient(circle at 80% 80%, rgba(138, 43, 226, 0.1) 0%, transparent 50%),
                        radial-gradient(circle at 40% 20%, rgba(30, 144, 255, 0.1) 0%, transparent 50%);
                    animation: gradient-shift 15s ease infinite;
                    z-index: -1;
                }

                @keyframes gradient-shift {

                    0%,
                    100% {
                        opacity: 1;
                    }

                    50% {
                        opacity: 0.8;
                    }
                }

                .container {
                    max-width: 1200px;
                    margin: 0 auto;
                    padding: 30px 20px;
                }

                /* Back button with smooth hover */
                .back-btn {
                    display: inline-flex;
                    align-items: center;
                    gap: 10px;
                    padding: 12px 24px;
                    background: rgba(255, 255, 255, 0.1);
                    backdrop-filter: blur(10px);
                    border: 1px solid rgba(255, 255, 255, 0.2);
                    color: #fff;
                    text-decoration: none;
                    border-radius: 50px;
                    font-weight: 600;
                    margin-bottom: 30px;
                    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                }

                .back-btn:hover {
                    background: linear-gradient(135deg, #e50914, #ff6b6b);
                    transform: translateX(-5px);
                    box-shadow: 0 6px 25px rgba(229, 9, 20, 0.4);
                    color: #fff;
                    border-color: transparent;
                }

                .back-btn i {
                    transition: transform 0.3s ease;
                }

                .back-btn:hover i {
                    transform: translateX(-3px);
                }

                /* Movie detail card with glass effect */
                .movie-detail {
                    background: rgba(255, 255, 255, 0.05);
                    backdrop-filter: blur(20px);
                    border: 1px solid rgba(255, 255, 255, 0.1);
                    border-radius: 24px;
                    padding: 40px;
                    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
                    display: flex;
                    gap: 40px;
                    flex-wrap: wrap;
                    animation: fadeInUp 0.6s ease;
                }

                @keyframes fadeInUp {
                    from {
                        opacity: 0;
                        transform: translateY(30px);
                    }

                    to {
                        opacity: 1;
                        transform: translateY(0);
                    }
                }

                /* Poster with 3D effect */
                .poster-container {
                    position: relative;
                    flex-shrink: 0;
                }

                .poster {
                    width: 320px;
                    height: auto;
                    border-radius: 16px;
                    box-shadow:
                        0 20px 40px rgba(0, 0, 0, 0.5),
                        0 0 0 1px rgba(255, 255, 255, 0.1);
                    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
                    display: block;
                }

                .poster-container:hover .poster {
                    transform: translateY(-10px) scale(1.02);
                    box-shadow:
                        0 30px 60px rgba(0, 0, 0, 0.6),
                        0 0 0 1px rgba(255, 255, 255, 0.2);
                }

                /* Glowing effect on poster hover */
                .poster-container::after {
                    content: '';
                    position: absolute;
                    top: -2px;
                    left: -2px;
                    right: -2px;
                    bottom: -2px;
                    background: linear-gradient(45deg, #e50914, #ff6b6b, #8b2bda, #1e90ff);
                    border-radius: 16px;
                    opacity: 0;
                    z-index: -1;
                    transition: opacity 0.4s ease;
                    filter: blur(20px);
                }

                .poster-container:hover::after {
                    opacity: 0.6;
                }

                /* Info section */
                .info {
                    flex: 1;
                    min-width: 320px;
                }

                .info h1 {
                    font-size: 3em;
                    margin-bottom: 15px;
                    background: linear-gradient(135deg, #fff, #e0e0e0);
                    -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent;
                    background-clip: text;
                    font-weight: 800;
                    line-height: 1.2;
                    animation: fadeIn 0.8s ease 0.2s both;
                }

                @keyframes fadeIn {
                    from {
                        opacity: 0;
                    }

                    to {
                        opacity: 1;
                    }
                }

                /* Info items with icons */
                .info-item {
                    display: flex;
                    align-items: center;
                    gap: 15px;
                    margin-bottom: 18px;
                    padding: 12px;
                    background: rgba(255, 255, 255, 0.05);
                    border-radius: 12px;
                    border-left: 3px solid #e50914;
                    transition: all 0.3s ease;
                }

                .info-item:hover {
                    background: rgba(255, 255, 255, 0.1);
                    transform: translateX(5px);
                    border-left-color: #ff6b6b;
                }

                .info-icon {
                    width: 40px;
                    height: 40px;
                    background: linear-gradient(135deg, #e50914, #ff6b6b);
                    border-radius: 10px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 1.2em;
                    box-shadow: 0 4px 15px rgba(229, 9, 20, 0.3);
                }

                .info-label {
                    color: #999;
                    font-weight: 600;
                    font-size: 0.9em;
                    text-transform: uppercase;
                    letter-spacing: 1px;
                    min-width: 120px;
                }

                .info-value {
                    color: #fff;
                    font-size: 1.1em;
                    font-weight: 600;
                }

                /* Description section */
                .description {
                    margin-top: 30px;
                    padding: 25px;
                    background: rgba(0, 0, 0, 0.3);
                    border-radius: 16px;
                    border: 1px solid rgba(255, 255, 255, 0.1);
                    line-height: 1.8;
                }

                .description h3 {
                    font-size: 1.5em;
                    margin-bottom: 15px;
                    color: #e50914;
                    display: flex;
                    align-items: center;
                    gap: 10px;
                }

                .description h3::before {
                    content: '';
                    width: 4px;
                    height: 24px;
                    background: linear-gradient(180deg, #e50914, #ff6b6b);
                    border-radius: 2px;
                }

                .description p {
                    color: #ccc;
                    font-size: 1.05em;
                    line-height: 1.9;
                }

                /* Empty state */
                .empty-state {
                    text-align: center;
                    padding: 80px 20px;
                    animation: fadeIn 0.6s ease;
                }

                .empty-state i {
                    font-size: 5em;
                    color: #e50914;
                    margin-bottom: 20px;
                    opacity: 0.5;
                }

                .empty-state h2 {
                    font-size: 2em;
                    margin-bottom: 20px;
                }

                /* Responsive design */
                @media (max-width: 768px) {
                    .container {
                        padding: 20px 15px;
                    }

                    .movie-detail {
                        padding: 25px;
                        gap: 25px;
                    }

                    .poster {
                        width: 100%;
                        max-width: 320px;
                        margin: 0 auto;
                    }

                    .info h1 {
                        font-size: 2em;
                    }

                    .info-item {
                        flex-direction: column;
                        align-items: flex-start;
                        gap: 8px;
                    }

                    .info-label {
                        min-width: auto;
                    }
                }

                /* Loading animation */
                @keyframes pulse {

                    0%,
                    100% {
                        opacity: 1;
                    }

                    50% {
                        opacity: 0.5;
                    }
                }
            </style>
        </head>

        <body>
            <div class="container">
                <a href="${pageContext.request.contextPath}/movies" class="back-btn">
                    <i class="fas fa-arrow-left"></i>
                    <span>Quay lại</span>
                </a>

                <c:if test="${not empty movie}">
                    <div class="movie-detail">
                        <div class="poster-container">
                            <img src="${pageContext.request.contextPath}/image?name=${movie.posterUrl}"
                                alt="${movie.title}" class="poster"
                                onerror="this.src='https://via.placeholder.com/320x480/1a1a1a/e50914?text=No+Image'">
                        </div>
                        <div class="info">
                            <h1>${movie.title}</h1>

                            <%-- Rating section (commented out) --%>
                                <%-- <div class="info-item">
                                    <div class="info-icon">
                                        <i class="fas fa-star"></i>
                                    </div>
                                    <div>
                                        <div class="info-label">Đánh giá</div>
                                        <div class="info-value">${movie.rating}/10</div>
                                    </div>
                        </div>
                        --%>

                        <%-- Genre section (commented out) --%>
                            <%-- <div class="info-item">
                                <div class="info-icon">
                                    <i class="fas fa-film"></i>
                                </div>
                                <div>
                                    <div class="info-label">Thể loại</div>
                                    <div class="info-value">${movie.genre}</div>
                                </div>
                    </div>
                    --%>

                    <div class="info-item">
                        <div class="info-icon">
                            <i class="fas fa-clock"></i>
                        </div>
                        <div>
                            <div class="info-label">Thời lượng</div>
                            <div class="info-value">${movie.duration} phút</div>
                        </div>
                    </div>

                    <div class="info-item">
                        <div class="info-icon">
                            <i class="fas fa-calendar-alt"></i>
                        </div>
                        <div>
                            <div class="info-label">Phát hành</div>
                            <div class="info-value">${movie.releaseDate}</div>
                        </div>
                    </div>

                    <c:if test="${not empty movie.ageRating}">
                        <div class="info-item">
                            <div class="info-icon">
                                <i class="fas fa-user-shield"></i>
                            </div>
                            <div>
                                <div class="info-label">Độ tuổi</div>
                                <div class="info-value">${movie.ageRating}</div>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty movie.description}">
                        <div class="description">
                            <h3>Mô tả phim</h3>
                            <p>${movie.description}</p>
                        </div>
                    </c:if>
            </div>
            </div>
            </c:if>

            <c:if test="${empty movie}">
                <div class="movie-detail">
                    <div class="empty-state">
                        <i class="fas fa-film"></i>
                        <h2>Không tìm thấy phim</h2>
                        <p style="color: #999; margin-bottom: 25px;">
                            Phim bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.
                        </p>
                        <a href="${pageContext.request.contextPath}/movies" class="back-btn">
                            <i class="fas fa-home"></i>
                            <span>Về trang chủ</span>
                        </a>
                    </div>
                </div>
            </c:if>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>