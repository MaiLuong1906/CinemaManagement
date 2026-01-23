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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie-detail.css"/>
</head>

<body>
    <!-- Header -->
    <jsp:include page="../../layout/header.jsp"></jsp:include>

    <div class="container-fluid px-4 py-5" style="max-width: 1400px;">
        <!-- Back Button -->
        <a href="${pageContext.request.contextPath}/movies" 
           class="btn btn-outline-light rounded-pill mb-4 px-4 py-2 fw-semibold hover-glow text-decoration-none d-inline-flex align-items-center gap-2">
            <i class="fas fa-arrow-left"></i>
            <span>Quay lại danh sách phim</span>
        </a>

        <c:if test="${not empty movie}">
            <!-- Movie Hero Section -->
            <div class="glass rounded-4 p-4 p-lg-5 mb-4">
                <div class="row g-4">
                    <!-- Poster -->
                    <div class="col-lg-3">
                        <img src="${pageContext.request.contextPath}/image?name=${movie.posterUrl}"
                             alt="${movie.title}" 
                             class="img-fluid rounded-3 hover-lift w-100"
                             style="box-shadow: 0 10px 40px rgba(0,0,0,0.5)"
                             onerror="this.src='https://via.placeholder.com/320x480/0a0a0a/667eea?text=No+Image'">
                    </div>

                    <!-- Movie Info -->
                    <div class="col-lg-9">
                        <h1 class="display-4 fw-bold text-gradient mb-4">${movie.title}</h1>

                        <!-- Meta Tags -->
                        <div class="d-flex flex-wrap gap-3 mb-4">
                            <span class="badge bg-opacity-10 bg-primary text-primary border border-primary rounded-pill px-3 py-2 fs-6">
                                <i class="fas fa-clock me-2"></i>${movie.duration} phút
                            </span>
                            <span class="badge bg-opacity-10 bg-primary text-primary border border-primary rounded-pill px-3 py-2 fs-6">
                                <i class="fas fa-calendar-alt me-2"></i>${movie.releaseDate}
                            </span>
                            <c:if test="${not empty movie.ageRating}">
                                <span class="badge bg-opacity-10 bg-primary text-primary border border-primary rounded-pill px-3 py-2 fs-6">
                                    <i class="fas fa-user-shield me-2"></i>${movie.ageRating}
                                </span>
                            </c:if>
                        </div>

                        <!-- Description -->
                        <c:if test="${not empty movie.description}">
                            <div class="bg-dark bg-opacity-50 border border-secondary border-opacity-25 rounded-3 p-4 mt-4">
                                <h3 class="h5 text-primary mb-3">
                                    <i class="fas fa-align-left me-2"></i>Mô tả phim
                                </h3>
                                <p class="text-secondary lh-lg mb-0">${movie.description}</p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- Showtimes Section -->
            <div class="text-center mb-5">
                <h2 class="display-5 fw-bold text-gradient mb-2">🎫 Chọn Suất Chiếu</h2>
                <p class="text-secondary fs-5">Chọn rạp và giờ chiếu phù hợp với bạn</p>
            </div>

            <!-- Theater Cards -->
            <div class="row g-4">
                <!-- Theater 1 -->
                <div class="col-12">
                    <div class="glass rounded-4 p-4 hover-lift">
                        <div class="mb-3">
                            <h4 class="fw-bold mb-2">CGV Vincom Center</h4>
                            <p class="text-secondary mb-0">
                                <i class="fas fa-map-marker-alt text-primary me-2"></i>72 Lê Thánh Tôn, Quận 1, TP.HCM
                            </p>
                        </div>
                        <div class="row g-2">
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('CGV Vincom Center', '10:00')">10:00</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('CGV Vincom Center', '13:30')">13:30</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('CGV Vincom Center', '16:00')">16:00</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('CGV Vincom Center', '19:00')">19:00</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('CGV Vincom Center', '21:30')">21:30</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Theater 2 -->
                <div class="col-12">
                    <div class="glass rounded-4 p-4 hover-lift">
                        <div class="mb-3">
                            <h4 class="fw-bold mb-2">Lotte Cinema Diamond Plaza</h4>
                            <p class="text-secondary mb-0">
                                <i class="fas fa-map-marker-alt text-primary me-2"></i>34 Lê Duẩn, Quận 1, TP.HCM
                            </p>
                        </div>
                        <div class="row g-2">
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Lotte Cinema Diamond Plaza', '09:30')">09:30</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Lotte Cinema Diamond Plaza', '12:00')">12:00</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Lotte Cinema Diamond Plaza', '14:30')">14:30</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Lotte Cinema Diamond Plaza', '17:00')">17:00</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Lotte Cinema Diamond Plaza', '20:00')">20:00</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Lotte Cinema Diamond Plaza', '22:30')">22:30</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Theater 3 -->
                <div class="col-12">
                    <div class="glass rounded-4 p-4 hover-lift">
                        <div class="mb-3">
                            <h4 class="fw-bold mb-2">Galaxy Nguyễn Du</h4>
                            <p class="text-secondary mb-0">
                                <i class="fas fa-map-marker-alt text-primary me-2"></i>116 Nguyễn Du, Quận 1, TP.HCM
                            </p>
                        </div>
                        <div class="row g-2">
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Galaxy Nguyễn Du', '11:00')">11:00</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Galaxy Nguyễn Du', '14:00')">14:00</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Galaxy Nguyễn Du', '17:30')">17:30</button>
                            </div>
                            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                <button class="btn showtime-btn text-white w-100 py-3 fw-semibold" onclick="bookTicket('Galaxy Nguyễn Du', '20:30')">20:30</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Empty State -->
        <c:if test="${empty movie}">
            <div class="glass rounded-4 p-5 text-center">
                <i class="fas fa-film display-1 text-gradient opacity-50 mb-4"></i>
                <h2 class="display-6 mb-3">Không tìm thấy phim</h2>
                <p class="text-secondary fs-5 mb-4">Phim bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
                <a href="${pageContext.request.contextPath}/movies" 
                   class="btn btn-outline-light rounded-pill px-4 py-2 fw-semibold hover-glow text-decoration-none d-inline-flex align-items-center gap-2">
                    <i class="fas fa-home"></i>
                    <span>Về trang danh sách phim</span>
                </a>
            </div>
        </c:if>
    </div>

    <!-- Footer -->
    <jsp:include page="../../layout/footer.jsp"></jsp:include>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function bookTicket(theater, time) {
            alert(`Đặt vé:\nRạp: ${theater}\nSuất chiếu: ${time}\n\nChức năng đang phát triển...`);
        }
    </script>
</body>

</html>