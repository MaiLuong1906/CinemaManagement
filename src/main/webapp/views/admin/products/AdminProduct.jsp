<%-- 
    Document   : AdminProduct
    Created on : Jan 26, 2026, 12:46:59 PM
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
    <title>Quản Lý Combo Bắp Nước - Admin</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-product.css">
</head>
<body>

<div class="header-section">
    <div class="container">
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

    <c:choose>
        <c:when test="${not empty products}">
            <div class="row g-4">
                <c:forEach var="product" items="${products}">
                    <div class="col-md-6 col-lg-4">
                        <div class="product-card">
                            <img class="product-img"
                                 src="${product.productImgUrl != null && !product.productImgUrl.isEmpty()
                                        ? product.productImgUrl
                                        : 'https://via.placeholder.com/300x200?text=Combo'}"
                                 onerror="this.src='https://via.placeholder.com/300x200?text=Combo'">

                            <div class="product-body">
                                <h5 class="product-title">${product.itemName}</h5>

                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <div class="product-price">
                                        <fmt:formatNumber value="${product.price}" groupingUsed="true"/>₫
                                    </div>

                                    <c:choose>
                                        <c:when test="${product.stockQuantity > 20}">
                                            <span class="badge badge-stock stock-available">
                                                Còn ${product.stockQuantity}
                                            </span>
                                        </c:when>
                                        <c:when test="${product.stockQuantity > 0}">
                                            <span class="badge badge-stock stock-low">
                                                Còn ${product.stockQuantity}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-stock stock-out">Hết hàng</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="text-center">
                                    <button class="btn btn-edit btn-sm"
                                            onclick="editProduct(
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

        <c:otherwise>
            <div class="empty-state">
                <i class="fas fa-inbox"></i>
                <h3>Chưa có sản phẩm</h3>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- MODAL -->
<div class="modal fade" id="productModal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form id="productForm" method="post"
                  action="${pageContext.request.contextPath}/admin/product">
                <div class="modal-header">
                    <h5 id="modalTitle">Thêm Sản Phẩm</h5>
                    <button type="button" class="btn-close btn-close-white"
                            data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">
                    <input type="hidden" id="formAction" name="action" value="insert">
                    <input type="hidden" id="itemId" name="itemId">

                    <input class="form-control mb-3" id="itemName" name="itemName" placeholder="Tên sản phẩm">
                    <input class="form-control mb-3" id="price" name="price" type="number">
                    <input class="form-control mb-3" id="stockQuantity" name="stockQuantity" type="number">
                    <input class="form-control" id="productImgUrl" name="productImgUrl">
                </div>

                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button class="btn btn-add" type="submit">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="deleteModal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5>Xác nhận xóa</h5>
                <button type="button" class="btn-close btn-close-white"
                        data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                Xóa sản phẩm <strong id="deleteProductName"></strong>?
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <a id="confirmDeleteBtn" class="btn btn-danger">Xóa</a>
            </div>
        </div>
    </div>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/admin-product.js"></script>

</body>
</html>