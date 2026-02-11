<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Liên Hệ - Cinema</title>

            <!-- Bootstrap CSS -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

            <!-- Font Awesome -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

            <!-- Custom CSS -->
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">

            <c:if test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/nav-admin.css">
            </c:if>

            <style>
                .contact-card {
                    background: rgba(255, 255, 255, 0.05);
                    border-radius: 15px;
                    padding: 30px;
                    transition: all 0.3s ease;
                    border: 1px solid rgba(102, 126, 234, 0.2);
                }

                .contact-card:hover {
                    transform: translateY(-5px);
                    box-shadow: 0 10px 30px rgba(102, 126, 234, 0.2);
                    border-color: #667eea;
                }

                .contact-icon {
                    width: 60px;
                    height: 60px;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    margin-bottom: 20px;
                }

                .page-header {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    padding: 60px 0;
                    margin-bottom: 50px;
                }

                .form-control-custom {
                    background: rgba(255, 255, 255, 0.05);
                    border: 1px solid rgba(255, 255, 255, 0.1);
                    color: white;
                    padding: 12px 15px;
                }

                .form-control-custom:focus {
                    background: rgba(255, 255, 255, 0.08);
                    border-color: #667eea;
                    color: white;
                    box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
                }

                .form-control-custom::placeholder {
                    color: #8892b0;
                }

                .btn-gradient {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    border: none;
                    color: white;
                    padding: 12px 30px;
                    border-radius: 8px;
                    font-weight: 600;
                    transition: all 0.3s ease;
                }

                .btn-gradient:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
                    color: white;
                }
            </style>
        </head>

        <body class="bg-dark text-white <c:if test='${sessionScope.user != null && sessionScope.user.roleId == "
            Admin"}'>admin-layout</c:if>"
            style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0a0a0a !important;">

            <!-- Header -->
            <jsp:include page="../../layout/header.jsp"></jsp:include>

            <!-- Admin Sidebar -->
            <c:if test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
                <jsp:include page="../../layout/nav-admin.jsp"></jsp:include>
            </c:if>

            <!-- Page Header -->
            <div class="page-header">
                <div class="container text-center">
                    <h1 class="display-4 fw-bold mb-3"><i class="fas fa-envelope me-3"></i>Liên Hệ</h1>
                    <p class="lead mb-0">Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7</p>
                </div>
            </div>

            <!-- Contact Content -->
            <div class="container pb-5" style="max-width: 1200px;">
                <div class="row g-4 mb-5">
                    <!-- Phone -->
                    <div class="col-md-4">
                        <div class="contact-card text-center h-100">
                            <div class="contact-icon mx-auto">
                                <i class="fas fa-phone-alt fs-4 text-white"></i>
                            </div>
                            <h5 class="fw-bold mb-3">Hotline</h5>
                            <p class="text-secondary mb-2">Hỗ trợ 24/7</p>
                            <a href="tel:19001234" class="fs-5 text-decoration-none" style="color: #667eea;">1900
                                1234</a>
                        </div>
                    </div>
                    <!-- Email -->
                    <div class="col-md-4">
                        <div class="contact-card text-center h-100">
                            <div class="contact-icon mx-auto">
                                <i class="fas fa-envelope fs-4 text-white"></i>
                            </div>
                            <h5 class="fw-bold mb-3">Email</h5>
                            <p class="text-secondary mb-2">Phản hồi trong 24h</p>
                            <a href="mailto:support@cinema.vn" class="fs-5 text-decoration-none"
                                style="color: #667eea;">support@cinema.vn</a>
                        </div>
                    </div>
                    <!-- Address -->
                    <div class="col-md-4">
                        <div class="contact-card text-center h-100">
                            <div class="contact-icon mx-auto">
                                <i class="fas fa-map-marker-alt fs-4 text-white"></i>
                            </div>
                            <h5 class="fw-bold mb-3">Địa Chỉ</h5>
                            <p class="text-secondary mb-2">Cinema Headquarters</p>
                            <p style="color: #667eea;">123 Trần Hưng Đạo, Q1, TP.HCM</p>
                        </div>
                    </div>
                </div>

                <!-- Contact Form -->
                <div class="row justify-content-center">
                    <div class="col-lg-8">
                        <div class="contact-card">
                            <h4 class="text-center fw-bold mb-4">Gửi Tin Nhắn Cho Chúng Tôi</h4>
                            <form>
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <input type="text" class="form-control form-control-custom"
                                            placeholder="Họ và tên">
                                    </div>
                                    <div class="col-md-6">
                                        <input type="email" class="form-control form-control-custom"
                                            placeholder="Email">
                                    </div>
                                    <div class="col-12">
                                        <input type="text" class="form-control form-control-custom"
                                            placeholder="Tiêu đề">
                                    </div>
                                    <div class="col-12">
                                        <textarea class="form-control form-control-custom" rows="5"
                                            placeholder="Nội dung tin nhắn..."></textarea>
                                    </div>
                                    <div class="col-12 text-center">
                                        <button type="submit" class="btn btn-gradient">
                                            <i class="fas fa-paper-plane me-2"></i>Gửi Tin Nhắn
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Footer -->
            <jsp:include page="../../layout/footer.jsp"></jsp:include>

            <!-- Bootstrap JS -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>