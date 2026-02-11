<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Chính Sách Bảo Mật - Cinema</title>

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
                .page-header {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    padding: 60px 0;
                    margin-bottom: 50px;
                }

                .content-section {
                    background: rgba(255, 255, 255, 0.05);
                    border-radius: 15px;
                    padding: 40px;
                    margin-bottom: 30px;
                    border: 1px solid rgba(102, 126, 234, 0.2);
                }

                .section-title {
                    color: #667eea;
                    font-weight: 700;
                    margin-bottom: 20px;
                    padding-bottom: 10px;
                    border-bottom: 2px solid rgba(102, 126, 234, 0.3);
                }

                .content-text {
                    color: #b8c1d1;
                    line-height: 1.9;
                }

                .content-text ul {
                    padding-left: 20px;
                }

                .content-text li {
                    margin-bottom: 10px;
                }

                .highlight-box {
                    background: rgba(102, 126, 234, 0.1);
                    border-left: 4px solid #667eea;
                    padding: 15px 20px;
                    border-radius: 0 10px 10px 0;
                    margin: 20px 0;
                }

                .security-badge {
                    display: inline-flex;
                    align-items: center;
                    gap: 8px;
                    background: rgba(40, 167, 69, 0.2);
                    color: #28a745;
                    padding: 8px 15px;
                    border-radius: 20px;
                    font-size: 14px;
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
                    <h1 class="display-4 fw-bold mb-3"><i class="fas fa-shield-alt me-3"></i>Chính Sách Bảo Mật</h1>
                    <p class="lead mb-0">Cam kết bảo vệ thông tin cá nhân của khách hàng</p>
                    <div class="mt-3">
                        <span class="security-badge"><i class="fas fa-lock"></i> SSL Secured</span>
                    </div>
                </div>
            </div>

            <!-- Content -->
            <div class="container pb-5" style="max-width: 900px;">

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-database me-2"></i>1. Thông Tin Thu Thập</h4>
                    <div class="content-text">
                        <p>Chúng tôi thu thập các thông tin sau khi bạn sử dụng dịch vụ:</p>
                        <ul>
                            <li><strong>Thông tin cá nhân:</strong> Họ tên, email, số điện thoại, ngày sinh.</li>
                            <li><strong>Thông tin thanh toán:</strong> Thông tin thẻ (được mã hóa), lịch sử giao dịch.
                            </li>
                            <li><strong>Dữ liệu sử dụng:</strong> Lịch sử đặt vé, phim yêu thích, thời gian truy cập.
                            </li>
                            <li><strong>Thông tin thiết bị:</strong> IP address, loại trình duyệt, hệ điều hành.</li>
                        </ul>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-bullseye me-2"></i>2. Mục Đích Sử Dụng</h4>
                    <div class="content-text">
                        <p>Thông tin của bạn được sử dụng để:</p>
                        <ul>
                            <li>Xử lý đặt vé và giao dịch thanh toán.</li>
                            <li>Gửi thông báo về vé, suất chiếu và khuyến mãi.</li>
                            <li>Cải thiện trải nghiệm người dùng và dịch vụ.</li>
                            <li>Hỗ trợ khách hàng và giải quyết khiếu nại.</li>
                            <li>Đề xuất phim và combo phù hợp với sở thích.</li>
                        </ul>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-lock me-2"></i>3. Bảo Mật Thông Tin</h4>
                    <div class="content-text">
                        <div class="highlight-box">
                            <strong>Cam kết bảo mật:</strong> Chúng tôi sử dụng công nghệ mã hóa SSL 256-bit để bảo vệ
                            mọi thông tin truyền tải trên website.
                        </div>
                        <ul>
                            <li>Thông tin thanh toán được mã hóa theo tiêu chuẩn PCI-DSS.</li>
                            <li>Mật khẩu được hash bằng thuật toán BCrypt.</li>
                            <li>Hệ thống được giám sát 24/7 để phát hiện xâm nhập.</li>
                            <li>Backup dữ liệu định kỳ và lưu trữ tại nhiều địa điểm.</li>
                        </ul>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-share-alt me-2"></i>4. Chia Sẻ Thông Tin</h4>
                    <div class="content-text">
                        <p><strong>Chúng tôi KHÔNG bán thông tin của bạn.</strong> Thông tin chỉ được chia sẻ trong các
                            trường hợp:</p>
                        <ul>
                            <li>Với đối tác thanh toán để xử lý giao dịch (VNPay, MoMo...).</li>
                            <li>Khi có yêu cầu từ cơ quan pháp luật.</li>
                            <li>Với sự đồng ý rõ ràng của bạn.</li>
                        </ul>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-user-cog me-2"></i>5. Quyền Của Bạn</h4>
                    <div class="content-text">
                        <p>Bạn có quyền:</p>
                        <ul>
                            <li><strong>Truy cập:</strong> Xem thông tin cá nhân đã cung cấp.</li>
                            <li><strong>Chỉnh sửa:</strong> Cập nhật thông tin không còn chính xác.</li>
                            <li><strong>Xóa:</strong> Yêu cầu xóa tài khoản và dữ liệu liên quan.</li>
                            <li><strong>Hạn chế:</strong> Từ chối nhận email marketing.</li>
                            <li><strong>Xuất dữ liệu:</strong> Yêu cầu bản sao dữ liệu cá nhân.</li>
                        </ul>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-cookie-bite me-2"></i>6. Cookie</h4>
                    <div class="content-text">
                        <p>Website sử dụng cookie để:</p>
                        <ul>
                            <li>Duy trì phiên đăng nhập của bạn.</li>
                            <li>Lưu cài đặt ngôn ngữ và tùy chọn.</li>
                            <li>Phân tích lưu lượng truy cập (Google Analytics).</li>
                        </ul>
                        <p>Bạn có thể tắt cookie trong cài đặt trình duyệt, nhưng một số tính năng có thể không hoạt
                            động đúng.</p>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-envelope me-2"></i>7. Liên Hệ</h4>
                    <div class="content-text">
                        <p>Nếu có câu hỏi về chính sách bảo mật, vui lòng liên hệ:</p>
                        <ul>
                            <li><strong>Email:</strong> privacy@cinema.vn</li>
                            <li><strong>Hotline:</strong> 1900 1234 (nhánh 2)</li>
                            <li><strong>Địa chỉ:</strong> 123 Trần Hưng Đạo, Q1, TP.HCM</li>
                        </ul>
                    </div>
                </div>

                <div class="text-center mt-4">
                    <p class="text-secondary">Cập nhật lần cuối: 01/02/2026</p>
                </div>
            </div>

            <!-- Footer -->
            <jsp:include page="../../layout/footer.jsp"></jsp:include>

            <!-- Bootstrap JS -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>