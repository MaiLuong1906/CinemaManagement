<%-- 
    Document   : addproduct
    Created on : Jan 29, 2026, 10:32:15 AM
    Author     : itphu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- Bootstrap -->
<!--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">-->

<!-- CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-product.css">

<div class="modal fade" id="productModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">

            <form method="post"
                  action="${pageContext.request.contextPath}/admin/product"
                  enctype="multipart/form-data">

                <div class="modal-header">
                    <h5 class="modal-title fw-bold text-dark" id="modalTitle">Thêm Sản Phẩm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">

                    <input type="hidden" id="formAction" name="action" value="insert">
                    <input type="hidden" id="itemId" name="itemId">

                    <input class="form-control mb-3"
                           id="itemName"
                           name="itemName"
                           placeholder="Tên sản phẩm"
                           required>

                    <input class="form-control mb-3"
                           id="price"
                           name="price"
                           type="number"
                           placeholder="Giá sản phẩm"
                           min="0"
                           step="1000"
                           required>


                    <input class="form-control mb-3"
                           id="stockQuantity"
                           name="stockQuantity"
                           type="number"
                           placeholder="Số lượng"
                           min="0"
                           step="1"
                           required>

                    <!-- preview -->
                    <div class="mb-2 text-center">
                        <img id="imagePreview"
                             src="https://via.placeholder.com/200x150?text=Preview"
                             style="max-width:100%;
                             height:150px;
                             object-fit:cover;
                             border-radius:6px;">
                    </div>

                    <small class="text-muted d-block mb-2">
                        * Khi sửa sản phẩm, không chọn ảnh mới sẽ giữ ảnh cũ
                    </small>

                    <input class="form-control"
                           type="file"
                           name="image"
                           accept="image/*"
                           onchange="previewImage(this)">
                </div>

                <div class="modal-footer">
                    <button type="button"
                            class="btn btn-secondary"
                            data-bs-dismiss="modal">
                        Hủy
                    </button>
                    <button type="submit" class="btn btn-add">
                        Lưu
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>