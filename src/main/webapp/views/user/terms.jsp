<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Điều Khoản Sử Dụng - Cinema</title>

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
                    <h1 class="display-4 fw-bold mb-3"><i class="fas fa-file-contract me-3"></i>Điều Khoản Sử Dụng</h1>
                    <p class="lead mb-0">Các điều khoản và điều kiện khi sử dụng dịch vụ của Cinema</p>
                </div>
            </div>

            <!-- Content -->
            <div class="container pb-5" style="max-width: 900px;">

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-info-circle me-2"></i>1. Giới Thiệu</h4>
                    <div class="content-text">
                        <p>Chào mừng bạn đến với Cinema. Khi truy cập và sử dụng website của chúng tôi, bạn đồng ý tuân
                            thủ các điều khoản và điều kiện được nêu dưới đây.</p>
                        <div class="highlight-box">
                            <strong>Lưu ý quan trọng:</strong> Vui lòng đọc kỹ các điều khoản này trước khi sử dụng dịch
                            vụ của chúng tôi.
                        </div>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-user-check me-2"></i>2. Điều Kiện Sử Dụng</h4>
                    <div class="content-text">
                        <ul>
                            <li>Bạn phải từ đủ 13 tuổi trở lên để tạo tài khoản.</li>
                            <li>Thông tin đăng ký phải chính xác và đầy đủ.</li>
                            <li>Bạn chịu trách nhiệm bảo mật tài khoản của mình.</li>
                            <li>Không được sử dụng dịch vụ cho mục đích bất hợp pháp.</li>
                            <li>Tuân thủ quy định về độ tuổi xem phim theo phân loại.</li>
                        </ul>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-ticket-alt me-2"></i>3. Quy Định Đặt Vé</h4>
                    <div class="content-text">
                        <ul>
                            <li>Vé đã mua không được hoàn trả, trừ trường hợp suất chiếu bị hủy.</li>
                            <li>Hủy vé trước 2 tiếng: hoàn 100%. Trước 1 tiếng: hoàn 50%.</li>
                            <li>Vui lòng đến trước giờ chiếu 15-20 phút để check-in.</li>
                            <li>Xuất trình mã QR hoặc email xác nhận khi vào rạp.</li>
                            <li>Mỗi tài khoản tối đa đặt 10 vé/suất chiếu.</li>
                        </ul>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-ban me-2"></i>4. Hành Vi Bị Cấm</h4>
                    <div class="content-text">
                        <ul>
                            <li>Quay phim, chụp ảnh trong rạp chiếu.</li>
                            <li>Mang thức ăn, đồ uống từ bên ngoài vào rạp.</li>
                            <li>Gây ồn ào, ảnh hưởng đến khán giả khác.</li>
                            <li>Sử dụng vé giả hoặc vé của người khác.</li>
                            <li>Hacking, phá hoại hệ thống website.</li>
                        </ul>
                    </div>
                </div>

                <div class="content-section">
                    <h4 class="section-title"><i class="fas fa-gavel me-2"></i>5. Quyền Hạn</h4>
                    <div class="content-text">
                        <p>Cinema có quyền:</p>
                        <ul>
                            <li>Thay đổi lịch chiếu, thời gian chiếu mà không cần báo trước.</li>
                            <li>Từ chối phục vụ nếu khách hàng vi phạm điều khoản.</li>
                            <li>Cập nhật điều khoản sử dụng khi cần thiết.</li>
                            <li>Đình chỉ hoặc xóa tài khoản vi phạm.</li>
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