<%-- Document : list Created on : Jan 29, 2026, 10:31:33 AM Author : itphu --%>



    <!DOCTYPE html>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                <!DOCTYPE html>
                <html lang="vi">

                <head>
                    <meta charset="UTF-8">
                    <title>Quản Lý Combo - Admin</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-product.css?v=4">
                </head>

                <body>

                    <div class="header-section">
                        <div class="container">
                            <div class="mb-3">
                                <a href="${pageContext.request.contextPath}/home" class="btn btn-success">
                                    <i class="fas fa-home me-2"></i>Trang chủ
                                </a>
                            </div>
                            <div class="row align-items-center">
                                <div class="col-md-6">
                                    <h1><i class="fas fa-user-shield me-2"></i>Quản Lý Combo - Admin</h1>
                                </div>
                                <div class="col-md-6 text-end">
                                    <button class="btn btn-add btn-light" data-bs-toggle="modal"
                                        data-bs-target="#productModal" onclick="resetForm()">
                                        <i class="fas fa-plus me-2"></i>Thêm Sản Phẩm Mới
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="container">
                        <c:choose>
                            <c:when test="${not empty products}">
                                <div class="row g-4">
                                    <c:forEach var="product" items="${products}">
                                        <div class="col-md-6 col-lg-4">
                                            <div class="product-card">
                                                <img
                                                    src="${pageContext.request.contextPath}/images/products/${product.productImgUrl}">
                                                <div class="product-body">
                                                    <h5 class="product-title">${product.itemName}</h5>

                                                    <!-- Giá -->
                                                    <div class="product-info text-center mb-1 fw-bold text-white">
                                                        <i class="fas fa-tag me-1"></i>
                                                        <fmt:formatNumber value="${product.price}"
                                                            groupingUsed="true" /> ₫
                                                    </div>

                                                    <!-- Số lượng tồn -->
                                                    <div class="product-info text-center mb-3 fw-bold text-white">
                                                        <i class="fas fa-boxes me-1"></i>
                                                        Còn lại: <strong>${product.stockQuantity}</strong>
                                                    </div>

                                                    <div class="text-center">
                                                        <button class="btn btn-edit btn-sm" onclick="editProduct(
                                                    ${product.itemId},
                                                                    '${product.itemName}',
                                                    ${product.price},
                                                                    '${product.productImgUrl}',
                                                    ${product.stockQuantity})">
                                                            <i class="fas fa-edit"></i> Sửa
                                                        </button>

                                                        <button class="btn btn-delete btn-sm"
                                                            onclick="confirmDelete(${product.itemId}, '${product.itemName}')">
                                                            <i class="fas fa-trash"></i> Xóa
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                        </c:choose>
                    </div>

                    <jsp:include page="add.jsp" />
                    <jsp:include page="delete.jsp" />

                    <script>
                        const contextPath = '${pageContext.request.contextPath}';
                    </script>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script src="${pageContext.request.contextPath}/js/admin-product.js"></script>
                </body>

                </html>