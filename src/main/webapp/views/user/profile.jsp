<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Profile</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css" />
            </head>

            <body>
                <!-- Header -->
                <jsp:include page="../../layout/header.jsp"></jsp:include>

                <div class="container py-5" style="max-width: 1200px;">
                    <!-- Page Header -->
                    <div class="text-center mb-5">
                        <h1 class="display-4 fw-bold text-gradient mb-2">Thông tin người dùng</h1>
                        <p class="text-secondary fs-5">Quản lý thông tin cá nhân và lịch sử đặt vé</p>
                    </div>

                    <div class="row g-4">
                        <!-- Sidebar -->
                        <div class="col-lg-3">
                            <div class="glass rounded-4 p-4 sticky-top" style="top: 100px;">
                                <!-- Avatar -->
                                <div class="text-center mb-4">
                                    <div class="position-relative d-inline-block">
                                        <div class="rounded-circle border border-primary border-3 bg-dark bg-opacity-50 overflow-hidden"
                                            style="width: 150px; height: 150px;">
                                            <!--Avatar của người dùng - Update sau-->
                                            <c:choose>
                                                <c:when test="${not empty customer.avatarUrl}">
                                                    <img src="${pageContext.request.contextPath}/image?name=${customer.avatarUrl}"
                                                        class="w-100 h-100 object-fit-cover" alt="Avatar">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="d-flex align-items-center justify-content-center h-100">
                                                        <i class="fas fa-user fa-4x text-secondary"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <label for="avatarInput"
                                            class="position-absolute bottom-0 end-0 btn btn-gradient text-white rounded-circle p-0 d-flex align-items-center justify-content-center"
                                            style="width: 40px; height: 40px; cursor: pointer;">
                                            <i class="fas fa-camera"></i>
                                        </label>
                                        <input type="file" id="avatarInput" class="d-none" accept="image/*">
                                    </div>
                                    <h5 class="mt-3 mb-1 fw-bold">${user.fullName}</h5>
                                    <p class="text-secondary small mb-0">${user.email}</p>
                                </div>

                                <!-- Navigation -->
                                <ul class="nav nav-pills flex-column gap-2" role="tablist">
                                    <li class="nav-item">
                                        <button class="nav-link active w-100 text-start rounded-3" data-bs-toggle="pill"
                                            data-bs-target="#info">
                                            <i class="fas fa-user me-2"></i>Thông tin cá nhân
                                        </button>
                                    </li>
                                    <li class="nav-item">
                                        <button class="nav-link w-100 text-start rounded-3" data-bs-toggle="pill"
                                            data-bs-target="#tickets">
                                            <i class="fas fa-ticket-alt me-2"></i>Lịch sử đặt vé
                                        </button>
                                    </li>
                                    <li class="nav-item">
                                        <button class="nav-link w-100 text-start rounded-3" data-bs-toggle="pill"
                                            data-bs-target="#password">
                                            <i class="fas fa-lock me-2"></i>Đổi mật khẩu
                                        </button>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <!-- Main Content -->
                        <div class="col-lg-9">
                            <div class="tab-content">

                                <!-- Personal Info Tab -->
                                <div class="tab-pane fade show active" id="info">
                                    <div class="glass rounded-4 p-4 p-lg-5">
                                        <h3 class="h4 fw-bold mb-4">
                                            <i class="fas fa-user-edit text-primary me-2"></i>Thông tin cá nhân
                                        </h3>
                                        <% if (request.getAttribute("error") !=null &&
                                            !request.getAttribute("error").toString().isEmpty()) {%>
                                            <div class="alert alert-danger mb-3" role="alert">
                                                <%= request.getAttribute("error")%>
                                            </div>
                                            <% }%>
                                                <% if (request.getAttribute("success") !=null &&
                                                    !request.getAttribute("success").toString().isEmpty()) {%>
                                                    <div class="alert alert-success mb-3" role="alert">
                                                        <i class="fas fa-check-circle me-2"></i>
                                                        <%= request.getAttribute("success")%>
                                                    </div>
                                                    <% }%>

                                                        <form action="${pageContext.request.contextPath}/AccountServlet"
                                                            method="POST">
                                                            <div class="row g-3">
                                                                <input type="hidden" name="action"
                                                                    value="update-profile">
                                                                <div class="col-md-6">
                                                                    <label class="form-label fw-semibold">Họ và
                                                                        tên</label>
                                                                    <input type="text" class="form-control rounded-3"
                                                                        name="fullName" value="${user.fullName}"
                                                                        required>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label fw-semibold">Email</label>
                                                                    <input type="email" class="form-control rounded-3"
                                                                        name="email" value="${user.email}" required>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label fw-semibold">Số điện
                                                                        thoại</label>
                                                                    <input type="tel" class="form-control rounded-3"
                                                                        name="phoneNumber" value="${user.phoneNumber}"
                                                                        required readonly>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label fw-semibold">Ngày
                                                                        sinh</label>
                                                                    <input type="date" class="form-control rounded-3"
                                                                        name="dob" value="${user.dateOfBirth}">
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label fw-medium d-block">Giới
                                                                        tính</label>
                                                                    <div class="d-flex gap-3">
                                                                        <div class="form-check">
                                                                            <input class="form-check-input" type="radio"
                                                                                name="gender" id="male" value="male"
                                                                                <c:if test="${user.gender}">checked
                                                                            </c:if>>
                                                                            <label class="form-check-label"
                                                                                for="male">Nam</label>
                                                                        </div>
                                                                        <div class="form-check">
                                                                            <input class="form-check-input" type="radio"
                                                                                name="gender" id="female" value="female"
                                                                                <c:if test="${!user.gender}">checked
                                                                            </c:if>>
                                                                            <label class="form-check-label"
                                                                                for="female">Nữ</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-12">
                                                                    <label class="form-label fw-semibold">Địa
                                                                        chỉ</label>
                                                                    <textarea class="form-control rounded-3"
                                                                        name="address"
                                                                        rows="3">${user.address}</textarea>
                                                                </div>
                                                            </div>

                                                            <div class="mt-4 d-flex gap-2">
                                                                <button type="submit"
                                                                    class="btn btn-gradient text-white px-4 rounded-3 fw-semibold">
                                                                    <i class="fas fa-save me-2"></i>Lưu thay đổi
                                                                </button>
                                                                <button type="reset"
                                                                    class="btn btn-outline-secondary px-4 rounded-3">
                                                                    <i class="fas fa-undo me-2"></i>Hủy
                                                                </button>
                                                            </div>
                                                        </form>
                                    </div>
                                </div>

                                <!-- Tickets History Tab -->
                                <div class="tab-pane fade" id="tickets">
                                    <div class="glass rounded-4 p-4 p-lg-5">
                                        <h3 class="h4 fw-bold mb-4">
                                            <i class="fas fa-history text-primary me-2"></i>Lịch sử đặt vé
                                        </h3>

                                        <div class="d-flex flex-column gap-3">
                                            <c:choose>
                                                <c:when test="${empty bookingHistory}">
                                                    <div class="text-center py-5">
                                                        <i class="fas fa-ticket-alt fa-3x text-secondary mb-3"></i>
                                                        <p class="text-secondary">Bạn chưa có lịch sử đặt vé nào</p>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="booking" items="${bookingHistory}">
                                                        <div
                                                            class="glass rounded-3 p-4 border-start border-primary border-4">
                                                            <div class="row align-items-center g-3">
                                                                <div class="col-md-2">
                                                                    <img src="${pageContext.request.contextPath}/image?name=${booking.posterUrl}"
                                                                        class="img-fluid rounded-2 w-100"
                                                                        alt="${booking.movieTitle}"
                                                                        onerror="this.src='https://via.placeholder.com/120x180/667eea/ffffff?text=Movie'">
                                                                </div>
                                                                <div class="col-md-7">
                                                                    <h5 class="fw-bold mb-2">${booking.movieTitle}</h5>
                                                                    <div class="text-secondary small mb-1">
                                                                        <i
                                                                            class="fas fa-calendar me-1"></i>${booking.showDate}
                                                                        - ${booking.startTime}
                                                                    </div>
                                                                    <div class="text-secondary small mb-1">
                                                                        <i
                                                                            class="fas fa-map-marker-alt me-1"></i>${booking.hallName}
                                                                    </div>
                                                                    <div class="text-secondary small">
                                                                        <i class="fas fa-couch me-1"></i>Ghế:
                                                                        ${booking.seatCodes}
                                                                    </div>
                                                                </div>
                                                                <div class="col-md-3 text-md-end">

                                                                    <c:choose>
                                                                        <c:when
                                                                            test="${booking.status.equalsIgnoreCase('paid')}">
                                                                            <span
                                                                                class="badge bg-success bg-opacity-25 text-success border border-success rounded-pill px-3 py-2 mb-2 d-inline-block">
                                                                                <i
                                                                                    class="fas fa-check-circle me-1"></i>Đã
                                                                                thanh toán
                                                                            </span>
                                                                        </c:when>
                                                                        <c:when
                                                                            test="${booking.status.equalsIgnoreCase('pending')}">
                                                                            <span
                                                                                class="badge bg-warning bg-opacity-25 text-warning border border-warning rounded-pill px-3 py-2 mb-2 d-inline-block">
                                                                                <i class="fas fa-clock me-1"></i>Chờ
                                                                                thanh toán
                                                                            </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span
                                                                                class="badge bg-danger bg-opacity-25 text-danger border border-danger rounded-pill px-3 py-2 mb-2 d-inline-block">
                                                                                <i
                                                                                    class="fas fa-times-circle me-1"></i>Đã
                                                                                hủy
                                                                            </span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <div class="h5 fw-bold text-primary mb-2">
                                                                        <fmt:formatNumber value="${booking.totalAmount}"
                                                                            type="number" groupingUsed="true" /> đ
                                                                    </div>
                                                                    <c:if test="${booking.ticketCode != null}">
                                                                        <button
                                                                            class="btn btn-sm btn-outline-light rounded-pill px-3"
                                                                            data-bs-toggle="modal"
                                                                            data-bs-target="#ticketModal"
                                                                            onclick="showTicket('${booking.ticketCode}', '${booking.movieTitle}', '${booking.showDate}', '${booking.startTime}', '${booking.endTime}', '${booking.hallName}', '${booking.seatCodes}', '${booking.totalAmount}', '${booking.status}', '${booking.invoiceId}')">
                                                                            <i class="fas fa-qrcode me-1"></i>Xem vé
                                                                        </button>
                                                                    </c:if>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>

                                <!-- Change Password Tab -->
                                <div class="tab-pane fade" id="password">
                                    <div class="glass rounded-4 p-4 p-lg-5">
                                        <h3 class="h4 fw-bold mb-4">
                                            <i class="fas fa-key text-primary me-2"></i>Đổi mật khẩu
                                        </h3>

                                        <% if (request.getAttribute("passwordError") !=null) { %>
                                            <div class="alert alert-danger mb-3" role="alert">
                                                <i class="fas fa-exclamation-circle me-2"></i>
                                                <%= request.getAttribute("passwordError") %>
                                            </div>
                                            <% } %>

                                                <% if (request.getAttribute("passwordSuccess") !=null) { %>
                                                    <div class="alert alert-success mb-3" role="alert">
                                                        <i class="fas fa-check-circle me-2"></i>
                                                        <%= request.getAttribute("passwordSuccess") %>
                                                    </div>
                                                    <% } %>

                                                        <form
                                                            action="${pageContext.request.contextPath}/AccountServlet?action=change-password"
                                                            method="post">
                                                            <div class="row g-3">
                                                                <div class="col-12">
                                                                    <label class="form-label fw-semibold">Mật khẩu hiện
                                                                        tại</label>
                                                                    <div class="input-group">
                                                                        <input type="password"
                                                                            class="form-control rounded-start-3"
                                                                            name="currentPassword" required>
                                                                        <button
                                                                            class="btn btn-outline-secondary rounded-end-3"
                                                                            type="button"
                                                                            onclick="togglePassword(this)">
                                                                            <i class="fas fa-eye"></i>
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                                <div class="col-12">
                                                                    <label class="form-label fw-semibold">Mật khẩu
                                                                        mới</label>
                                                                    <div class="input-group">
                                                                        <input type="password"
                                                                            class="form-control rounded-start-3"
                                                                            name="newPassword" required>
                                                                        <button
                                                                            class="btn btn-outline-secondary rounded-end-3"
                                                                            type="button"
                                                                            onclick="togglePassword(this)">
                                                                            <i class="fas fa-eye"></i>
                                                                        </button>
                                                                    </div>
                                                                    <small class="text-secondary">Minimum 6 characters
                                                                        with
                                                                        uppercase, lowercase, and number</small>
                                                                </div>
                                                                <div class="col-12">
                                                                    <label class="form-label fw-semibold">Xác nhận mật
                                                                        khẩu
                                                                        mới</label>
                                                                    <div class="input-group">
                                                                        <input type="password"
                                                                            class="form-control rounded-start-3"
                                                                            name="confirmPassword" required>
                                                                        <button
                                                                            class="btn btn-outline-secondary rounded-end-3"
                                                                            type="button"
                                                                            onclick="togglePassword(this)">
                                                                            <i class="fas fa-eye"></i>
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <div class="mt-4">
                                                                <button type="submit"
                                                                    class="btn btn-gradient text-white px-4 rounded-3 fw-semibold">
                                                                    <i class="fas fa-lock me-2"></i>Đổi mật khẩu
                                                                </button>
                                                            </div>
                                                        </form>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

                <!-- Ticket Detail Modal -->
                <div class="modal fade" id="ticketModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered" style="max-width: 420px;">
                        <div class="modal-content border-0 rounded-4 overflow-hidden" style="background: #1a1a2e;">
                            <!-- Ticket Header -->
                            <div class="text-center py-4 px-4"
                                style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                                <h4 class="fw-bold text-white mb-1"><i class="fas fa-ticket-alt me-2"></i>VÉ XEM PHIM
                                </h4>
                                <small class="text-white-50">Cinema Management System</small>
                            </div>

                            <!-- Dashed separator -->
                            <div style="border-top: 2px dashed rgba(255,255,255,0.15); margin: 0 20px;"></div>

                            <!-- Ticket Body -->
                            <div class="modal-body p-4">
                                <h5 class="fw-bold text-white text-center mb-3" id="tkMovieTitle"></h5>

                                <div class="row g-3 mb-3">
                                    <div class="col-6">
                                        <div class="text-secondary small mb-1"><i class="fas fa-calendar me-1"></i>Ngày
                                            chiếu</div>
                                        <div class="text-white fw-semibold" id="tkShowDate"></div>
                                    </div>
                                    <div class="col-6">
                                        <div class="text-secondary small mb-1"><i class="fas fa-clock me-1"></i>Giờ
                                            chiếu</div>
                                        <div class="text-white fw-semibold" id="tkTime"></div>
                                    </div>
                                    <div class="col-6">
                                        <div class="text-secondary small mb-1"><i
                                                class="fas fa-door-open me-1"></i>Phòng chiếu</div>
                                        <div class="text-white fw-semibold" id="tkHall"></div>
                                    </div>
                                    <div class="col-6">
                                        <div class="text-secondary small mb-1"><i class="fas fa-couch me-1"></i>Ghế
                                        </div>
                                        <div class="text-white fw-semibold" id="tkSeats"></div>
                                    </div>
                                </div>

                                <div style="border-top: 1px solid rgba(255,255,255,0.1); margin: 16px 0;"></div>

                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <span class="text-secondary">Tổng tiền</span>
                                    <span class="h5 fw-bold mb-0" style="color: #667eea;" id="tkAmount"></span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <span class="text-secondary">Trạng thái</span>
                                    <span id="tkStatus"></span>
                                </div>

                                <div style="border-top: 2px dashed rgba(255,255,255,0.15); margin: 16px 0;"></div>

                                <!-- QR Code -->
                                <div class="text-center">
                                    <div class="bg-white rounded-3 d-inline-block p-3 mb-2">
                                        <img id="tkQrCode" width="160" height="160" alt="QR Code">
                                    </div>
                                    <div class="text-white fw-bold" id="tkCode"></div>
                                    <div class="text-secondary small">Đưa mã này cho nhân viên để nhận vé</div>
                                </div>
                            </div>

                            <!-- Footer -->
                            <div class="modal-footer border-0 justify-content-center pb-4 pt-0">
                                <button type="button" class="btn btn-outline-light rounded-pill px-4"
                                    data-bs-dismiss="modal">
                                    <i class="fas fa-times me-2"></i>Đóng
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Footer -->
                <jsp:include page="../../layout/footer.jsp"></jsp:include>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
                <script src="${pageContext.request.contextPath}/js/tab-activation.js"></script>
                <script>
                    function showTicket(code, movie, date, start, end, hall, seats, amount, status, invoiceId) {
                        document.getElementById('tkMovieTitle').textContent = movie;
                        document.getElementById('tkShowDate').textContent = date;
                        document.getElementById('tkTime').textContent = start + ' - ' + end;
                        document.getElementById('tkHall').textContent = hall;
                        document.getElementById('tkSeats').textContent = seats;
                        document.getElementById('tkCode').textContent = code;

                        // Format amount
                        const formatted = Number(amount).toLocaleString('vi-VN');
                        document.getElementById('tkAmount').textContent = formatted + ' đ';

                        // Status badge
                        let statusHtml = '';
                        const s = status.toLowerCase();
                        if (s === 'paid') {
                            statusHtml = '<span class="badge bg-success bg-opacity-25 text-success border border-success rounded-pill px-3 py-2"><i class="fas fa-check-circle me-1"></i>Đã thanh toán</span>';
                        } else if (s === 'pending') {
                            statusHtml = '<span class="badge bg-warning bg-opacity-25 text-warning border border-warning rounded-pill px-3 py-2"><i class="fas fa-clock me-1"></i>Chờ thanh toán</span>';
                        } else {
                            statusHtml = '<span class="badge bg-danger bg-opacity-25 text-danger border border-danger rounded-pill px-3 py-2"><i class="fas fa-times-circle me-1"></i>Đã hủy</span>';
                        }
                        document.getElementById('tkStatus').innerHTML = statusHtml;

                        // QR Code using free API
                        const qrData = 'TICKET:' + code + '|INVOICE:' + invoiceId;
                        document.getElementById('tkQrCode').src = 'https://api.qrserver.com/v1/create-qr-code/?size=160x160&data=' + encodeURIComponent(qrData);
                    }
                </script>
                <script>
                    function togglePassword(button) {
                        const input = button.previousElementSibling;
                        const icon = button.querySelector('i');

                        if (input.type === 'password') {
                            input.type = 'text';
                            icon.classList.remove('fa-eye');
                            icon.classList.add('fa-eye-slash');
                        } else {
                            input.type = 'password';
                            icon.classList.remove('fa-eye-slash');
                            icon.classList.add('fa-eye');
                        }
                    }

                    document.getElementById('avatarInput').addEventListener('change', function (e) {
                        const file = e.target.files[0];
                        if (file) {
                            const reader = new FileReader();
                            reader.onload = function (event) {
                                const avatarDiv = document.querySelector('.rounded-circle');
                                avatarDiv.innerHTML = `<img src="${event.target.result}" class="w-100 h-100 object-fit-cover" alt="Avatar">`;
                            }
                            reader.readAsDataURL(file);
                        }
                    });

                    // Auto-activate tab based on URL parameter or server attribute
                    const urlParams = new URLSearchParams(window.location.search);
                    const tabParam = urlParams.get('tab');
                    const serverActiveTab = '<%= request.getAttribute("activeTab") != null ? request.getAttribute("activeTab") : "" %>';

                    // Determine which tab to activate
                    let activeTabId = null;
                    if (tabParam) {
                        // URL parameter takes priority
                        activeTabId = tabParam;
                    } else if (serverActiveTab === 'password') {
                        // Server-side attribute (for password change)
                        activeTabId = 'password';
                    }

                    // Activate the target tab if specified
                    if (activeTabId) {
                        const targetTab = document.querySelector(`button[data-bs-target="#${activeTabId}"]`);
                        const targetTabPane = document.getElementById(activeTabId);
                        const infoTab = document.querySelector('button[data-bs-target="#info"]');
                        const infoTabPane = document.getElementById('info');

                        if (targetTab && targetTabPane) {
                            // Deactivate info tab
                            infoTab.classList.remove('active');
                            infoTabPane.classList.remove('show', 'active');

                            // Activate target tab
                            targetTab.classList.add('active');
                            targetTabPane.classList.add('show', 'active');
                        }
                    }
                </script>

            </body>

            </html>