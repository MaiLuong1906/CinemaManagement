<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Kết Quả Thanh Toán - Cinema</title>

                <!-- Bootstrap 5 -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <!-- FontAwesome -->
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

                <!-- Custom CSS -->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie-detail.css" />
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bookingsuccess.css" />
            </head>

            <body>
                <!-- Header -->
                <jsp:include page="../../layout/header.jsp"></jsp:include>

                <div class="container d-flex align-items-center justify-content-center" style="min-height: 80vh;">

                    <div class="glass-panel">
                        <!-- Icon Logic -->
                        <c:choose>
                            <c:when test="${fn:containsIgnoreCase(message, 'thành công')}">
                                <div class="checkmark-circle">
                                    <div class="background"></div>
                                    <i class="fas fa-check check-icon" style="position:relative; z-index:2;"></i>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="checkmark-circle error-circle">
                                    <div class="background"></div>
                                    <i class="fas fa-times check-icon" style="position:relative; z-index:2;"></i>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <!-- Title -->
                        <h2 class="status-title">
                            <c:choose>
                                <c:when test="${fn:containsIgnoreCase(message, 'thành công')}">
                                    Thanh Toán Thành Công!
                                </c:when>
                                <c:otherwise>
                                    Thanh Toán Thất Bại!
                                </c:otherwise>
                            </c:choose>
                        </h2>

                        <!-- Message -->
                        <p class="message-text">
                            ${message}
                            <br>
                            <span class="small text-secondary mt-2 d-block">Mã giao dịch: #${param.vnp_TxnRef}</span>
                        </p>

                        <!-- Actions -->
                        <a href="${pageContext.request.contextPath}/home" class="home-btn me-3">
                            <i class="fas fa-home me-2"></i>Về Trang Chủ
                        </a>

                        <c:if test="${fn:containsIgnoreCase(message, 'thành công')}">
                            <a href="${pageContext.request.contextPath}/profile" class="home-btn"
                                style="background: var(--gradient-primary); border:none;">
                                <i class="fas fa-ticket-alt me-2"></i>Vé Của Tôi
                            </a>
                        </c:if>
                    </div>

                </div>

                <!-- Footer -->
                <jsp:include page="../../layout/footer.jsp"></jsp:include>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>