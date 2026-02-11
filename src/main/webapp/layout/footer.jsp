<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <footer class="footer-custom mt-5">
        <div class="container-fluid px-4 py-5" style="max-width: 1400px;">
            <div class="row g-4">
                <!-- Logo & About Section -->
                <div class="col-lg-4 col-md-6">
                    <div class="d-flex align-items-center gap-2 mb-3">
                        <i class="fas fa-film fs-3"
                            style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent;"></i>
                        <h4 class="fw-bold m-0"
                            style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
                            CINEMA</h4>
                    </div>
                    <p class="text-secondary mb-4" style="line-height: 1.8; color: #8892b0;">
                        Trải nghiệm điện ảnh đẳng cấp với công nghệ hiện đại và dịch vụ tận tâm. Nơi những câu chuyện
                        được kể bằng ánh sáng.
                    </p>
                    <div class="d-flex gap-3">
                        <a href="#" class="social-icon"
                            style="width: 45px; height: 45px; border-radius: 50%; background: rgba(255,255,255,0.05); border: 2px solid rgba(255,255,255,0.1); display: flex; align-items: center; justify-content: center; transition: all 0.3s;">
                            <i class="fab fa-facebook-f text-white"></i>
                        </a>
                        <a href="#" class="social-icon"
                            style="width: 45px; height: 45px; border-radius: 50%; background: rgba(255,255,255,0.05); border: 2px solid rgba(255,255,255,0.1); display: flex; align-items: center; justify-content: center; transition: all 0.3s;">
                            <i class="fab fa-instagram text-white"></i>
                        </a>
                        <a href="#" class="social-icon"
                            style="width: 45px; height: 45px; border-radius: 50%; background: rgba(255,255,255,0.05); border: 2px solid rgba(255,255,255,0.1); display: flex; align-items: center; justify-content: center; transition: all 0.3s;">
                            <i class="fab fa-youtube text-white"></i>
                        </a>
                        <a href="#" class="social-icon"
                            style="width: 45px; height: 45px; border-radius: 50%; background: rgba(255,255,255,0.05); border: 2px solid rgba(255,255,255,0.1); display: flex; align-items: center; justify-content: center; transition: all 0.3s;">
                            <i class="fab fa-twitter text-white"></i>
                        </a>
                    </div>
                </div>

                <!-- Quick Links -->
                <div class="col-lg-2 col-md-6 col-6">
                    <h6 class="fw-bold mb-4" style="color: #a8b3ff;">🎬 Phim</h6>
                    <ul class="list-unstyled footer-links">
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/movie"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Phim Đang Chiếu
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/movie"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Phim Sắp Chiếu
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/movie"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Lịch Chiếu
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/movie"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Đánh Giá Phim
                            </a>
                        </li>
                    </ul>
                </div>

                <!-- Services -->
                <div class="col-lg-2 col-md-6 col-6">
                    <h6 class="fw-bold mb-4" style="color: #ffd699;">🎫 Dịch Vụ</h6>
                    <ul class="list-unstyled footer-links">
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/movie"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Đặt Vé Online
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/product"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Bắp Nước
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/AccountServlet"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Thành Viên
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/support/faqs"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Khuyến Mãi
                            </a>
                        </li>
                    </ul>
                </div>

                <!-- Support -->
                <div class="col-lg-2 col-md-6 col-6">
                    <h6 class="fw-bold mb-4" style="color: #a8e6cf;">💬 Hỗ Trợ</h6>
                    <ul class="list-unstyled footer-links">
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/support/contact"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Liên Hệ
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/support/faqs"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                FAQs
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/support/terms"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Điều Khoản
                            </a>
                        </li>
                        <li class="mb-3">
                            <a href="${pageContext.request.contextPath}/support/policy"
                                class="text-decoration-none d-flex align-items-center gap-2" style="color: #8892b0;">
                                <i class="fas fa-angle-right" style="font-size: 12px;"></i>
                                Chính Sách
                            </a>
                        </li>
                    </ul>
                </div>

                <!-- Contact -->
                <div class="col-lg-2 col-md-6">
                    <h6 class="fw-bold mb-4" style="color: #ff9999;">📍 Liên Hệ</h6>
                    <div class="footer-contact">
                        <div class="d-flex gap-3 mb-3">
                            <i class="fas fa-map-marker-alt" style="color: #667eea; margin-top: 4px;"></i>
                            <span class="small" style="color: #8892b0;">123 Trần Hưng Đạo, Q1, TP.HCM</span>
                        </div>
                        <div class="d-flex gap-3 mb-3">
                            <i class="fas fa-phone" style="color: #667eea; margin-top: 4px;"></i>
                            <a href="tel:19001234" class="text-decoration-none small" style="color: #8892b0;">1900
                                1234</a>
                        </div>
                        <div class="d-flex gap-3 mb-3">
                            <i class="fas fa-envelope" style="color: #667eea; margin-top: 4px;"></i>
                            <a href="mailto:info@cinema.vn" class="text-decoration-none small"
                                style="color: #8892b0;">info@cinema.vn</a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Bottom Bar -->
            <div class="mt-5 pt-4" style="border-top: 1px solid rgba(255,255,255,0.1);">
                <div class="row align-items-center">
                    <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
                        <p class="mb-0" style="color: #8892b0;">
                            <i class="far fa-copyright"></i> 2025 <span
                                style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; font-weight: 600;">CINEMA</span>.
                            All rights reserved.
                        </p>
                    </div>
                    <div class="col-md-6 text-center text-md-end">
                        <div class="d-flex gap-3 justify-content-center justify-content-md-end">
                            <a href="#" class="text-decoration-none small" style="color: #8892b0;">Privacy Policy</a>
                            <span style="color: #4a5568;">•</span>
                            <a href="#" class="text-decoration-none small" style="color: #8892b0;">Terms of Service</a>
                            <span style="color: #4a5568;">•</span>
                            <a href="#" class="text-decoration-none small" style="color: #8892b0;">Cookie Settings</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <style>
        /* Footer Styles */
        .footer-custom {
            background: linear-gradient(180deg, #0a0a0a 0%, #050505 100%);
            border-top: 1px solid rgba(102, 126, 234, 0.2);
            position: relative;
        }

        .footer-custom::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 1px;
            background: linear-gradient(90deg, transparent 0%, #667eea 50%, transparent 100%);
        }

        /* Social Icons */
        .social-icon:hover {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
            border-color: transparent !important;
            transform: translateY(-3px);
            box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
        }

        /* Footer Links */
        .footer-links a {
            transition: all 0.3s ease;
            font-size: 14px;
        }

        .footer-links a:hover {
            color: #667eea !important;
            padding-left: 5px;
        }

        .footer-links a i {
            transition: transform 0.3s ease;
        }

        .footer-links a:hover i {
            transform: translateX(3px);
        }

        /* Footer Contact */
        .footer-contact a:hover {
            color: #667eea !important;
        }

        /* Bottom Bar Links */
        .col-md-6 a:hover {
            color: #667eea !important;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .footer-custom {
                text-align: center;
            }

            .footer-links a {
                justify-content: center;
            }

            .footer-contact>div {
                justify-content: center;
            }
        }
    </style>