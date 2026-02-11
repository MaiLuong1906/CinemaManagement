<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>FAQs - Câu Hỏi Thường Gặp</title>

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

                .accordion-item {
                    background: rgba(255, 255, 255, 0.05);
                    border: 1px solid rgba(102, 126, 234, 0.2);
                    margin-bottom: 15px;
                    border-radius: 10px !important;
                    overflow: hidden;
                }

                .accordion-button {
                    background: rgba(255, 255, 255, 0.05);
                    color: white;
                    font-weight: 600;
                    padding: 20px 25px;
                }

                .accordion-button:not(.collapsed) {
                    background: linear-gradient(135deg, rgba(102, 126, 234, 0.2) 0%, rgba(118, 75, 162, 0.2) 100%);
                    color: #667eea;
                    box-shadow: none;
                }

                .accordion-button:focus {
                    box-shadow: none;
                    border-color: rgba(102, 126, 234, 0.5);
                }

                .accordion-button::after {
                    filter: invert(1);
                }

                .accordion-body {
                    background: rgba(0, 0, 0, 0.3);
                    color: #b8c1d1;
                    padding: 20px 25px;
                    line-height: 1.8;
                }

                .faq-category {
                    color: #667eea;
                    font-weight: 600;
                    margin-bottom: 20px;
                    padding-bottom: 10px;
                    border-bottom: 2px solid rgba(102, 126, 234, 0.3);
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
                    <h1 class="display-4 fw-bold mb-3"><i class="fas fa-question-circle me-3"></i>FAQs</h1>
                    <p class="lead mb-0">Câu hỏi thường gặp - Giải đáp mọi thắc mắc của bạn</p>
                </div>
            </div>

            <!-- FAQ Content -->
            <div class="container pb-5" style="max-width: 900px;">

                <!-- Đặt Vé -->
                <h5 class="faq-category"><i class="fas fa-ticket-alt me-2"></i>Đặt Vé Online</h5>
                <div class="accordion" id="bookingFaq">
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq1">
                                Làm thế nào để đặt vé xem phim?
                            </button>
                        </h2>
                        <div id="faq1" class="accordion-collapse collapse" data-bs-parent="#bookingFaq">
                            <div class="accordion-body">
                                Bạn có thể đặt vé dễ dàng theo các bước: Chọn phim → Chọn suất chiếu → Chọn ghế → Chọn
                                combo bắp nước (nếu muốn) → Thanh toán. Sau khi thanh toán thành công, bạn sẽ nhận được
                                mã QR code qua email để check-in tại rạp.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq2">
                                Tôi có thể hủy vé đã đặt không?
                            </button>
                        </h2>
                        <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#bookingFaq">
                            <div class="accordion-body">
                                Bạn có thể hủy vé trước giờ chiếu 2 tiếng và được hoàn tiền 100%. Hủy trước 1 tiếng được
                                hoàn 50%. Sau thời gian này, vé không thể hủy hoặc hoàn tiền.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq3">
                                Phương thức thanh toán nào được chấp nhận?
                            </button>
                        </h2>
                        <div id="faq3" class="accordion-collapse collapse" data-bs-parent="#bookingFaq">
                            <div class="accordion-body">
                                Chúng tôi chấp nhận thanh toán qua: Thẻ tín dụng/ghi nợ (Visa, Mastercard, JCB), Ví điện
                                tử (MoMo, ZaloPay, VNPay), Chuyển khoản ngân hàng, và thanh toán tại quầy.
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Tài Khoản -->
                <h5 class="faq-category mt-5"><i class="fas fa-user me-2"></i>Tài Khoản</h5>
                <div class="accordion" id="accountFaq">
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq4">
                                Làm sao để đăng ký tài khoản?
                            </button>
                        </h2>
                        <div id="faq4" class="accordion-collapse collapse" data-bs-parent="#accountFaq">
                            <div class="accordion-body">
                                Click vào nút "Đăng ký" ở góc phải màn hình, điền thông tin cá nhân và xác nhận email.
                                Bạn cũng có thể đăng ký nhanh bằng tài khoản Google hoặc Facebook.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq5">
                                Quên mật khẩu thì phải làm sao?
                            </button>
                        </h2>
                        <div id="faq5" class="accordion-collapse collapse" data-bs-parent="#accountFaq">
                            <div class="accordion-body">
                                Tại trang đăng nhập, click "Quên mật khẩu", nhập email đã đăng ký. Chúng tôi sẽ gửi link
                                đặt lại mật khẩu đến email của bạn trong vòng 5 phút.
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Combo & Ưu Đãi -->
                <h5 class="faq-category mt-5"><i class="fas fa-gift me-2"></i>Combo & Ưu Đãi</h5>
                <div class="accordion" id="promoFaq">
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq6">
                                Có những combo bắp nước nào?
                            </button>
                        </h2>
                        <div id="faq6" class="accordion-collapse collapse" data-bs-parent="#promoFaq">
                            <div class="accordion-body">
                                Chúng tôi có nhiều combo đa dạng từ combo đơn đến combo gia đình. Bạn có thể xem và đặt
                                trước combo khi mua vé online để được ưu đãi 10%.
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                data-bs-target="#faq7">
                                Làm sao để nhận khuyến mãi?
                            </button>
                        </h2>
                        <div id="faq7" class="accordion-collapse collapse" data-bs-parent="#promoFaq">
                            <div class="accordion-body">
                                Đăng ký thành viên để nhận ưu đãi sinh nhật, tích điểm đổi quà. Theo dõi fanpage
                                Facebook và đăng ký nhận email để cập nhật các chương trình khuyến mãi mới nhất.
                            </div>
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