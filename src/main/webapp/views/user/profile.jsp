<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

                                                    <form action="${pageContext.request.contextPath}/update-profile"
                                                        method="POST">
                                                        <div class="row g-3">
                                                            <div class="col-md-6">
                                                                <label class="form-label fw-semibold">Họ và tên</label>
                                                                <input type="text" class="form-control rounded-3"
                                                                    name="fullName" value="${user.fullName}" required>
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
                                                                <label class="form-label fw-semibold">Ngày sinh</label>
                                                                <input type="date" class="form-control rounded-3"
                                                                    name="dob" value="${user.dateOfBirth}">
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label class="form-label fw-medium d-block">Giới
                                                                    tính</label>
                                                                <div class="d-flex gap-3">
                                                                    <div class="form-check">
                                                                        <input class="form-check-input" type="radio"
                                                                            name="gender" id="male" value="male" <c:if
                                                                            test="${user.gender}">checked</c:if>>
                                                                        <label class="form-check-label"
                                                                            for="male">Nam</label>
                                                                    </div>
                                                                    <div class="form-check">
                                                                        <input class="form-check-input" type="radio"
                                                                            name="gender" id="female" value="female"
                                                                            <c:if test="${!user.gender}">checked</c:if>>
                                                                        <label class="form-check-label"
                                                                            for="female">Nữ</label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="col-12">
                                                                <label class="form-label fw-semibold">Địa chỉ</label>
                                                                <textarea class="form-control rounded-3" name="address"
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
                                        <!-- Ticket 1 -->
                                        <div class="glass rounded-3 p-4 border-start border-primary border-4">
                                            <div class="row align-items-center g-3">
                                                <div class="col-md-2">
                                                    <img src="https://via.placeholder.com/120x180/667eea/ffffff?text=Movie"
                                                        class="img-fluid rounded-2 w-100" alt="Movie">
                                                </div>
                                                <div class="col-md-7">
                                                    <h5 class="fw-bold mb-2">Avatar: The Way of Water</h5>
                                                    <div class="text-secondary small mb-1">
                                                        <i class="fas fa-calendar me-1"></i>25/01/2026 - 19:00
                                                    </div>
                                                    <div class="text-secondary small mb-1">
                                                        <i class="fas fa-map-marker-alt me-1"></i>CGV Vincom Center
                                                    </div>
                                                    <div class="text-secondary small">
                                                        <i class="fas fa-couch me-1"></i>Ghế: A1, A2
                                                    </div>
                                                </div>
                                                <div class="col-md-3 text-md-end">
                                                    <span
                                                        class="badge bg-success bg-opacity-25 text-success border border-success rounded-pill px-3 py-2 mb-2 d-inline-block">
                                                        <i class="fas fa-check-circle me-1"></i>Đã thanh toán
                                                    </span>
                                                    <div class="h5 fw-bold text-primary mb-2">200.000 đ</div>
                                                    <button class="btn btn-sm btn-outline-light rounded-pill px-3">
                                                        <i class="fas fa-qrcode me-1"></i>Xem vé
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Change Password Tab -->
                            <div class="tab-pane fade" id="password">
                                <div class="glass rounded-4 p-4 p-lg-5">
                                    <h3 class="h4 fw-bold mb-4">
                                        <i class="fas fa-key text-primary me-2"></i>Đổi mật khẩu
                                    </h3>

                                    <form action="${pageContext.request.contextPath}/customer/change-password"
                                        method="post">
                                        <div class="row g-3">
                                            <div class="col-12">
                                                <label class="form-label fw-semibold">Mật khẩu hiện tại</label>
                                                <div class="input-group">
                                                    <input type="password" class="form-control rounded-start-3"
                                                        name="currentPassword" required>
                                                    <button class="btn btn-outline-secondary rounded-end-3"
                                                        type="button" onclick="togglePassword(this)">
                                                        <i class="fas fa-eye"></i>
                                                    </button>
                                                </div>
                                            </div>
                                            <div class="col-12">
                                                <label class="form-label fw-semibold">Mật khẩu mới</label>
                                                <div class="input-group">
                                                    <input type="password" class="form-control rounded-start-3"
                                                        name="newPassword" required>
                                                    <button class="btn btn-outline-secondary rounded-end-3"
                                                        type="button" onclick="togglePassword(this)">
                                                        <i class="fas fa-eye"></i>
                                                    </button>
                                                </div>
                                                <small class="text-secondary">Mật khẩu phải có ít nhất 8 ký tự</small>
                                            </div>
                                            <div class="col-12">
                                                <label class="form-label fw-semibold">Xác nhận mật khẩu mới</label>
                                                <div class="input-group">
                                                    <input type="password" class="form-control rounded-start-3"
                                                        name="confirmPassword" required>
                                                    <button class="btn btn-outline-secondary rounded-end-3"
                                                        type="button" onclick="togglePassword(this)">
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

            <!-- Footer -->
            <jsp:include page="../../layout/footer.jsp"></jsp:include>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
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
            </script>
        </body>

        </html>