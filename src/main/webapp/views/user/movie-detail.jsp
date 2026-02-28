<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${movie.title} - Chi tiết phim</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie-detail.css" />
        </head>

        <body>
            <jsp:include page="../../layout/header.jsp"></jsp:include>

            <c:set var="backLink"
                value="${not empty backUrl ? backUrl : pageContext.request.contextPath.concat('/movie')}" />
            <div class="container-fluid px-4 py-5" style="max-width: 1400px;">
                <a href="${backLink}"
                    class="btn btn-outline-light rounded-pill mb-4 px-4 py-2 fw-semibold hover-glow text-decoration-none d-inline-flex align-items-center gap-2">

                    <i class="fas fa-arrow-left"></i>
                    <span>Quay lại</span>
                </a>

                <c:choose>
                    <c:when test="${not empty movie}">
                        <div class="glass rounded-4 p-4 p-lg-5 mb-5">
                            <div class="row g-4">
                                <div class="col-lg-3">
                                    <img src="${pageContext.request.contextPath}/image?name=${movie.posterUrl}"
                                        alt="${movie.title}" class="img-fluid rounded-3 hover-lift w-100"
                                        style="box-shadow: 0 10px 40px rgba(0,0,0,0.5)"
                                        onerror="this.src='https://via.placeholder.com/320x480/0a0a0a/667eea?text=No+Image'">
                                </div>

                                <div class="col-lg-9">
                                    <h1 class="display-4 fw-bold text-gradient mb-4">${movie.title}</h1>

                                    <div class="d-flex flex-wrap gap-3 mb-4">
                                        <span
                                            class="badge bg-opacity-10 bg-primary text-primary border border-primary rounded-pill px-3 py-2 fs-6">
                                            <i class="fas fa-clock me-2"></i>${movie.duration} phút
                                        </span>
                                        <span
                                            class="badge bg-opacity-10 bg-primary text-primary border border-primary rounded-pill px-3 py-2 fs-6">
                                            <i class="fas fa-calendar-alt me-2"></i>${movie.releaseDate}
                                        </span>
                                        <c:if test="${not empty movie.ageRating}">
                                            <span
                                                class="badge bg-opacity-10 bg-primary text-primary border border-primary rounded-pill px-3 py-2 fs-6">
                                                <i class="fas fa-user-shield me-2"></i>${movie.ageRating}
                                            </span>
                                        </c:if>
                                        <c:if test="${not empty movieGenres}">
                                            <c:forEach var="genre" items="${movieGenres}">
                                                <span class="badge rounded-pill px-3 py-2 fs-6"
                                                    style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #fff;">
                                                    <i class="fas fa-tag me-1"></i>${genre.genreName}
                                                </span>
                                            </c:forEach>
                                        </c:if>
                                    </div>

                                    <c:if test="${not empty movie.description}">
                                        <div
                                            class="bg-dark bg-opacity-50 border border-secondary border-opacity-25 rounded-3 p-4">
                                            <h3 class="h5 text-primary mb-3">
                                                <i class="fas fa-align-left me-2"></i>Mô tả phim
                                            </h3>
                                            <p class="text-secondary lh-lg mb-0">${movie.description}</p>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <div class="text-center mb-5">
                            <h2 class="display-5 fw-bold text-gradient mb-2">🎫 Chọn Suất Chiếu</h2>
                            <p class="text-secondary fs-5">Chọn rạp và giờ chiếu phù hợp với bạn</p>
                        </div>

                        <div class="row g-4">
                            <c:if test="${not empty showtimes}">
                                <c:set var="currentHall" value="" />

                                <c:forEach var="st" items="${showtimes}" varStatus="status">
                                    <%-- Khi gặp một phòng mới --%>
                                        <c:if test="${st.hallName ne currentHall}">
                                            <%-- Nếu không phải phần tử đầu tiên, đóng div của phòng trước đó --%>
                                                <c:if test="${not status.first}">
                        </div>
            </div>
            </div>
            </c:if>

            <%-- Bắt đầu card cho phòng mới --%>
                <div class="col-12">
                    <div class="glass rounded-4 p-4 mb-3">
                        <div class="mb-3">
                            <h4 class="fw-bold mb-2">${st.hallName}</h4>
                            <p class="text-secondary mb-0">
                                <i class="fas fa-calendar-day text-primary me-2"></i>${st.showDate}
                            </p>
                        </div>
                        <div class="row g-2">
                            <c:set var="currentHall" value="${st.hallName}" />
                            </c:if>

                            <%-- Nút giờ chiếu --%>
                                <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                                    <button class="btn btn-outline-primary text-white w-100 py-3 fw-semibold"
                                        onclick="bookTicket('${st.showtimeId}')">
                                        ${st.startTime}
                                    </button>
                                </div>

                                <%-- Nếu là phần tử cuối cùng, đóng toàn bộ tag đang mở --%>
                                    <c:if test="${status.last}">
                        </div>
                    </div>
                </div>
                </c:if>
                </c:forEach>
                </c:if>

                <c:if test="${empty showtimes}">
                    <div class="col-12 text-center py-5">
                        <p class="text-secondary">Hiện chưa có suất chiếu nào cho phim này.</p>
                    </div>
                </c:if>
                </div>
                </c:when>

                <c:otherwise>
                    <div class="glass rounded-4 p-5 text-center">
                        <i class="fas fa-film display-1 text-gradient opacity-50 mb-4"></i>
                        <h2 class="display-6 mb-3">Không tìm thấy phim</h2>
                        <p class="text-secondary fs-5 mb-4">Phim bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
                        <a href="${pageContext.request.contextPath}/movie?action=list"
                            class="btn btn-outline-light rounded-pill px-4 py-2 fw-semibold hover-glow text-decoration-none">
                            <i class="fas fa-home me-2"></i>Trang danh sách
                        </a>
                    </div>
                </c:otherwise>
                </c:choose>
                </div>

                <jsp:include page="../../layout/footer.jsp"></jsp:include>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
                <script>
                    function bookTicket(showtimeId) {
                        // Chuyển hướng đến trang đặt ghế với ID suất chiếu
                        window.location.href = `${pageContext.request.contextPath}/seat-selection?showtimeId=` + showtimeId;
                    }
                </script>
        </body>

        </html>