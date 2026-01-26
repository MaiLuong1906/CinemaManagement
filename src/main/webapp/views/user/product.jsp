<%-- 
    Document   : product
    Created on : Jan 26, 2026, 12:41:50 PM
    Author     : itphu
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Combo Bắp Nước - Rạp Chiếu Phim</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
</head>
<body>

<div class="header-section">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-6">
                <h2><i class="fas fa-popcorn me-2"></i>Combo Bắp Nước</h2>
            </div>
            <div class="col-md-6 text-end">
                <a href="${pageContext.request.contextPath}/cart" class="btn cart-btn">
                    <i class="fas fa-shopping-cart me-2"></i>Giỏ Hàng
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.size() > 0}">
                        <span class="cart-badge">${sessionScope.cart.size()}</span>
                    </c:if>
                </a>
            </div>
        </div>
    </div>
</div>

<div class="container">

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle me-2"></i>${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="section-title">
        <h3>🍿 Chọn Combo Yêu Thích 🥤</h3>
        <p>Thưởng thức phim tuyệt vời hơn với các combo bắp nước hấp dẫn</p>
    </div>

    <c:choose>
        <c:when test="${not empty products}">
            <div class="row g-4">
                <c:forEach var="product" items="${products}">
                    <div class="col-md-6 col-lg-4">
                        <div class="combo-card">
                            <div class="combo-img-wrapper">
                                <img class="combo-img"
                                     src="${product.productImgUrl != null && !product.productImgUrl.isEmpty()
                                          ? product.productImgUrl
                                          : 'https://via.placeholder.com/400x300?text=Combo+Bắp+Nước'}"
                                     onerror="this.src='https://via.placeholder.com/400x300?text=Combo+Bắp+Nước'">

                                <c:choose>
                                    <c:when test="${product.stockQuantity > 20}">
                                        <span class="stock-badge stock-available">Còn hàng</span>
                                    </c:when>
                                    <c:when test="${product.stockQuantity > 0}">
                                        <span class="stock-badge stock-low">Sắp hết</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="stock-badge stock-out">Hết hàng</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="combo-body">
                                <h5 class="combo-title">${product.itemName}</h5>

                                <div class="combo-price">
                                    <fmt:formatNumber value="${product.price}" groupingUsed="true"/>₫
                                </div>

                                <form action="${pageContext.request.contextPath}/cart" method="post">
                                    <input type="hidden" name="action" value="add">
                                    <input type="hidden" name="itemId" value="${product.itemId}">

                                    <div class="quantity-control">
                                        <button type="button" class="quantity-btn"
                                                onclick="decreaseQuantity(${product.itemId})"
                                                ${product.stockQuantity <= 0 ? 'disabled' : ''}>−</button>

                                        <input type="number"
                                               id="qty-${product.itemId}"
                                               name="quantity"
                                               value="1"
                                               min="1"
                                               max="${product.stockQuantity}"
                                               class="quantity-display"
                                               readonly>

                                        <button type="button" class="quantity-btn"
                                                onclick="increaseQuantity(${product.itemId}, ${product.stockQuantity})"
                                                ${product.stockQuantity <= 0 ? 'disabled' : ''}>+</button>
                                    </div>

                                    <button class="btn btn-add-cart btn-light"
                                            ${product.stockQuantity <= 0 ? 'disabled' : ''}>
                                        ${product.stockQuantity <= 0 ? 'Hết hàng' : 'Thêm Vào Giỏ'}
                                    </button>
                                </form>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>

        <c:otherwise>
            <div class="empty-state">
                <i class="fas fa-box-open"></i>
                <h3>Hiện Chưa Có Combo</h3>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/product.js"></script>
</body>
</html>