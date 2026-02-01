<%-- Document : product Created on : Jan 26, 2026, 12:41:50 PM Author : itphu --%>

    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                <!DOCTYPE html>
                <html lang="vi">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Combo Bắp Nước - Rạp Chiếu Phim</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
                </head>

                <body>
                    <!-- Header -->
                    <div class="header-section">
                        <div class="container-fluid px-4">
                            <div class="row align-items-center">
                                <div class="col-md-6">
                                    <h2><i class="fas fa-popcorn me-2"></i>Combo Bắp Nước</h2>
                                </div>
                                <div class="col-md-6 text-end">
                                    <span class="text-white">
                                        <i class="fas fa-shopping-cart me-2"></i>
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.cart}">
                                                ${sessionScope.cart.size()} sản phẩm
                                            </c:when>
                                            <c:otherwise>Giỏ hàng trống</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid px-4">
                        <!-- Messages -->
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

                        <div class="row">
                            <!-- Products Section (Left) -->
                            <div class="col-lg-8">
                                <div class="section-title">
                                    <h3>🍿 Chọn Combo Yêu Thích 🥤</h3>
                                    <p>Thưởng thức phim tuyệt vời hơn với các combo bắp nước hấp dẫn</p>
                                </div>

                                <c:choose>
                                    <c:when test="${not empty products}">
                                        <div class="row g-3">
                                            <c:forEach var="product" items="${products}">
                                                <div class="col-md-6 col-xl-4">
                                                    <div class="combo-card">
                                                        <div class="combo-img-wrapper">
                                                            <img class="combo-img"
                                                                src="${pageContext.request.contextPath}/images/products/${product.productImgUrl}">
                                                            <c:choose>
                                                                <c:when test="${product.stockQuantity > 20}">
                                                                    <span class="stock-badge stock-available">Còn
                                                                        hàng</span>
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
                                                                <fmt:formatNumber value="${product.price}"
                                                                    groupingUsed="true" />₫
                                                            </div>
                                                            <form action="${pageContext.request.contextPath}/cart"
                                                                method="post">
                                                                <input type="hidden" name="action" value="add">
                                                                <input type="hidden" name="itemId"
                                                                    value="${product.itemId}">
                                                                <input type="hidden" name="redirectTo" value="product">
                                                                <div class="quantity-control">
                                                                    <button type="button" class="quantity-btn"
                                                                        onclick="decreaseQuantity(${product.itemId})"
                                                                        ${product.stockQuantity <=0 ? 'disabled' : ''
                                                                        }>−</button>
                                                                    <input type="number" id="qty-${product.itemId}"
                                                                        name="quantity" value="1" min="1"
                                                                        max="${product.stockQuantity}"
                                                                        class="quantity-display" readonly>
                                                                    <button type="button" class="quantity-btn"
                                                                        onclick="increaseQuantity(${product.itemId}, ${product.stockQuantity})"
                                                                        ${product.stockQuantity <=0 ? 'disabled' : ''
                                                                        }>+</button>
                                                                </div>
                                                                <button class="btn btn-add-cart" ${product.stockQuantity
                                                                    <=0 ? 'disabled' : '' }>
                                                                    <i
                                                                        class="fas fa-cart-plus me-2"></i>${product.stockQuantity
                                                                    <= 0 ? 'Hết hàng' : 'Thêm Vào Giỏ' } </button>
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

                            <!-- Cart Sidebar (Right) -->
                            <div class="col-lg-4">
                                <div class="cart-sidebar">
                                    <h4 class="cart-sidebar-title">
                                        <i class="fas fa-shopping-cart me-2"></i>Giỏ Hàng
                                    </h4>

                                    <c:choose>
                                        <c:when test="${not empty cartDetails}">
                                            <div class="cart-items-container">
                                                <c:set var="total" value="0" />
                                                <c:forEach var="item" items="${cartDetails}">
                                                    <div class="cart-item-row">
                                                        <div class="item-name">${item.key.itemName}</div>
                                                        <div class="item-qty">
                                                            <form action="${pageContext.request.contextPath}/cart"
                                                                method="post" id="sidebar-form-${item.key.itemId}">
                                                                <input type="hidden" name="action" value="update">
                                                                <input type="hidden" name="itemId"
                                                                    value="${item.key.itemId}">
                                                                <input type="hidden" name="redirectTo" value="product">
                                                                <button type="button" class="qty-btn-sm"
                                                                    onclick="updateSidebarQty(${item.key.itemId}, -1, ${item.key.stockQuantity})">−</button>
                                                                <span class="qty-value"
                                                                    id="sidebar-qty-${item.key.itemId}">${item.value}</span>
                                                                <input type="hidden" name="quantity"
                                                                    id="sidebar-qty-input-${item.key.itemId}"
                                                                    value="${item.value}">
                                                                <button type="button" class="qty-btn-sm"
                                                                    onclick="updateSidebarQty(${item.key.itemId}, 1, ${item.key.stockQuantity})">+</button>
                                                            </form>
                                                        </div>
                                                        <div class="item-subtotal">
                                                            <fmt:formatNumber value="${item.key.price * item.value}"
                                                                groupingUsed="true" />₫
                                                        </div>
                                                        <a href="${pageContext.request.contextPath}/cart?action=remove&itemId=${item.key.itemId}&redirectTo=product"
                                                            class="item-remove" title="Xóa">
                                                            <i class="fas fa-times"></i>
                                                        </a>
                                                    </div>
                                                    <c:set var="total"
                                                        value="${total + (item.key.price * item.value)}" />
                                                </c:forEach>
                                            </div>

                                            <!-- Summary Section -->
                                            <div class="cart-summary">
                                                <div class="summary-row">
                                                    <span>Tổng cộng:</span>
                                                    <span class="summary-total">
                                                        <fmt:formatNumber value="${total}" groupingUsed="true" />₫
                                                    </span>
                                                </div>
                                                <button class="btn btn-checkout" onclick="checkout()">
                                                    <i class="fas fa-credit-card me-2"></i>Thanh Toán
                                                </button>
                                                <a href="${pageContext.request.contextPath}/cart?action=clear&redirectTo=product"
                                                    class="btn btn-clear"
                                                    onclick="return confirm('Xóa toàn bộ giỏ hàng?')">
                                                    <i class="fas fa-trash me-2"></i>Xóa Toàn Bộ
                                                </a>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="cart-empty">
                                                <i class="fas fa-shopping-basket"></i>
                                                <p>Giỏ hàng trống</p>
                                                <small>Hãy thêm combo yêu thích!</small>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script src="${pageContext.request.contextPath}/js/product.js"></script>
                    <script>
                        function updateSidebarQty(id, change, max) {
                            const qtySpan = document.getElementById("sidebar-qty-" + id);
                            const qtyInput = document.getElementById("sidebar-qty-input-" + id);
                            let value = parseInt(qtySpan.innerText) + change;
                            if (value >= 1 && value <= max) {
                                qtySpan.innerText = value;
                                qtyInput.value = value;
                                document.getElementById("sidebar-form-" + id).submit();
                            }
                        }

                        function checkout() {
                            alert("Chức năng thanh toán đang phát triển!");
                        }
                    </script>
                </body>

                </html>