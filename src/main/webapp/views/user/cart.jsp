<%-- 
    Document   : cart
    Created on : Jan 25, 2026, 6:34:00 PM
    Author     : itphu
--%>



<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giỏ Hàng - Rạp Chiếu Phim</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
</head>

<body>
<div class="header-section">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-6">
                <h2><i class="fas fa-shopping-cart me-2"></i>Giỏ Hàng</h2>
            </div>
            <div class="col-md-6 text-end">
                <!-- FIX 1 -->
                <a href="${pageContext.request.contextPath}/product" class="btn btn-back">
                    <i class="fas fa-arrow-left me-2"></i>Tiếp Tục Mua
                </a>
            </div>
        </div>
    </div>
</div>

<div class="container">

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle me-2"></i>${successMessage}
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty cartDetails}">
            <div class="row">
                <div class="col-lg-8">
                    <div class="cart-container">
                        <c:set var="total" value="0"/>
                        <c:forEach var="item" items="${cartDetails}">
                            <div class="cart-item">
                                <div class="row align-items-center">
                                    <div class="col-md-2">
                                        <img class="cart-item-img"
                                             src="${item.key.productImgUrl}"
                                             onerror="this.src='https://via.placeholder.com/120?text=Combo'">
                                    </div>

                                    <div class="col-md-4">
                                        <div class="item-name">${item.key.itemName}</div>
                                        <div class="item-price">
                                            <fmt:formatNumber value="${item.key.price}" groupingUsed="true"/>₫
                                        </div>
                                    </div>

                                    <div class="col-md-3">
                                        <!-- FIX 2 -->
                                        <form action="${pageContext.request.contextPath}/cart" method="post"
                                              id="form-${item.key.itemId}">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="itemId" value="${item.key.itemId}">

                                            <div class="quantity-control">
                                                <button type="button" class="quantity-btn"
                                                        onclick="updateQuantity(${item.key.itemId}, -1, ${item.key.stockQuantity})">
                                                    <i class="fas fa-minus"></i>
                                                </button>

                                                <input type="number" name="quantity"
                                                       id="qty-${item.key.itemId}"
                                                       value="${item.value}"
                                                       min="1" max="${item.key.stockQuantity}"
                                                       class="quantity-input"
                                                       onchange="this.form.submit()">

                                                <button type="button" class="quantity-btn"
                                                        onclick="updateQuantity(${item.key.itemId}, 1, ${item.key.stockQuantity})">
                                                    <i class="fas fa-plus"></i>
                                                </button>
                                            </div>
                                        </form>
                                    </div>

                                    <div class="col-md-3 text-end">
                                        <div class="item-price mb-3">
                                            <fmt:formatNumber value="${item.key.price * item.value}" groupingUsed="true"/>₫
                                        </div>

                                        <!-- FIX 3 -->
                                        <a href="${pageContext.request.contextPath}/cart?action=remove&itemId=${item.key.itemId}"
                                           class="btn btn-remove btn-sm"
                                           onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                                            <i class="fas fa-trash me-1"></i>Xóa
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <c:set var="total" value="${total + (item.key.price * item.value)}"/>
                        </c:forEach>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="summary-card">
                        <h3 class="summary-title">Tóm Tắt Đơn Hàng</h3>

                        <div class="summary-row">
                            <span>Tổng tiền:</span>
                            <span><fmt:formatNumber value="${total}" groupingUsed="true"/>₫</span>
                        </div>

                        <button class="btn btn-checkout" onclick="checkout()">
                            <i class="fas fa-credit-card me-2"></i>Thanh Toán
                        </button>

                        <!-- FIX 4 -->
                        <a href="${pageContext.request.contextPath}/cart?action=clear"
                           class="btn btn-clear"
                           onclick="return confirm('Xóa toàn bộ giỏ hàng?')">
                            <i class="fas fa-trash me-2"></i>Xóa Toàn Bộ
                        </a>
                    </div>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="cart-container empty-cart">
                <i class="fas fa-shopping-cart"></i>
                <h3>Giỏ Hàng Trống</h3>
                <a href="${pageContext.request.contextPath}/product" class="btn-continue">
                    Khám Phá Combo
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function updateQuantity(id, change, max) {
        const input = document.getElementById("qty-" + id);
        let value = parseInt(input.value) + change;
        if (value >= 1 && value <= max) {
            input.value = value;
            document.getElementById("form-" + id).submit();
        }
    }

    function checkout() {
        alert("Chức năng thanh toán đang phát triển!");
    }
</script>
</body>
</html>