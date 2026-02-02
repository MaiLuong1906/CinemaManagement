<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Chọn Ghế - Cinema</title>

            <!-- Bootstrap 5 CSS -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
            <!-- FontAwesome -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

            <!-- Custom CSS -->
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie-detail.css" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/seat-selection.css" />
        </head>

        <body>
            <!-- Header -->
            <jsp:include page="../../layout/header.jsp"></jsp:include>

            <div class="container-fluid px-4 py-5" style="max-width: 1200px;">
                <!-- Back Button -->
                <a href="javascript:history.back()"
                    class="btn btn-outline-light rounded-pill mb-4 px-4 py-2 fw-semibold hover-glow text-decoration-none d-inline-flex align-items-center gap-2">
                    <i class="fas fa-arrow-left"></i>
                    <span>Quay lại</span>
                </a>

                <div class="row g-4">
                    <!-- Left Column: Seat Map -->
                    <div class="col-lg-8">
                        <div class="glass p-4 h-100">
                            <h4 class="text-center mb-4 text-gradient fw-bold">CHỌN GHẾ NGỒI</h4>

                            <div class="screen-container">
                                <div class="screen">MÀN HÌNH</div>
                            </div>

                            <form action="hold-seats" method="post" id="seatForm">
                                <input type="hidden" name="showtimeId" value="${showtimeId}">
                                <input type="hidden" name="seatIds" id="seatIds">
                                <input type="hidden" name="totalPrice" id="totalPrice">

                                <div class="seat-map-container">
                                    <c:set var="currentRow" value="-1" />

                                    <c:forEach var="s" items="${seats}">
                                        <c:if test="${s.rowIndex != currentRow}">
                                            <c:if test="${currentRow != -1}">
                                </div> <!-- Close previous row -->
                                </c:if>
                                <div class="seat-row"> <!-- Start new row -->
                                    <c:set var="currentRow" value="${s.rowIndex}" />
                                    </c:if>

                                    <button type="button" class="seat ${s.status == 'BOOKED' ? 'booked' : ''} 
                                               ${s.seatTypeId == 1 ? 'normal' : s.seatTypeId == 2 ? 'vip' : 'couple'}"
                                        data-seat-id="${s.seatId}" data-price="${s.price}" ${s.status=='BOOKED'
                                        ? 'disabled' : '' } title="${s.seatCode} - ${s.price} VND">
                                        ${s.seatCode}
                                    </button>
                                    </c:forEach>

                                    <c:if test="${not empty seats}">
                                </div> <!-- Close last row -->
                                </c:if>
                        </div>
                        </form>

                        <!-- Legend -->
                        <div class="seat-legend mt-4">
                            <div class="legend-item">
                                <div class="legend-box" style="background: var(--seat-booked)"></div> Đã đặt
                            </div>
                            <div class="legend-item">
                                <div class="legend-box"
                                    style="background: var(--seat-available); border-top: 3px solid #fff;"></div> Thường
                            </div>
                            <div class="legend-item">
                                <div class="legend-box"
                                    style="background: rgba(255, 215, 0, 0.2); border-top: 3px solid var(--seat-vip);">
                                </div> VIP
                            </div>
                            <div class="legend-item">
                                <div class="legend-box"
                                    style="background: rgba(255, 107, 107, 0.2); border-top: 3px solid var(--seat-couple);">
                                </div> Đôi
                            </div>
                            <div class="legend-item">
                                <div class="legend-box" style="background: var(--gradient-primary)"></div> Đang chọn
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right Column: Booking Info -->
                <div class="col-lg-4">
                    <div class="glass p-4 info-panel">
                        <h4 class="text-white mb-4 fw-bold border-bottom pb-2">Thông tin đặt vé</h4>

                        <div class="mb-3">
                            <label class="text-secondary small d-block mb-1">Dach sách ghế đã chọn:</label>
                            <span id="selectedSeats" class="fs-5 text-primary fw-bold">Chưa chọn ghế nào</span>
                        </div>

                        <div class="mb-4">
                            <label class="text-secondary small d-block mb-1">Tổng tiền tạm tính:</label>
                            <div class="d-flex align-items-baseline">
                                <span id="total" class="display-6 fw-bold text-white me-2">0</span>
                                <span class="text-secondary">VND</span>
                            </div>
                        </div>

                        <button type="submit" form="seatForm"
                            class="confirm-btn btn btn-primary w-100 py-3 rounded-pill fw-bold fs-5 text-uppercase hover-lift"
                            disabled>
                            <i class="fas fa-ticket-alt me-2"></i> Xác nhận đặt vé
                        </button>

                        <div class="mt-3 text-center">
                            <small class="text-secondary fst-italic">
                                * Vui lòng kiểm tra kỹ thông tin trước khi xác nhận.
                            </small>
                        </div>
                    </div>
                </div>
            </div>
            </div>

            <!-- Footer -->
            <jsp:include page="../../layout/footer.jsp"></jsp:include>

            <!-- Bootstrap Bundle JS -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

            <!-- Custom JS -->
            <script src="${pageContext.request.contextPath}/js/seat-selection.js"></script>

        </body>

        </html>