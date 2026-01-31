<%-- 
    Document   : addproduct
    Created on : Jan 29, 2026, 10:32:15 AM
    Author     : itphu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
 <!-- Bootstrap -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap/css/bootstrap.min.css">

    <!-- CSS  -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-product.css">
<div class="modal fade" id="productModal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/admin/product">
                <div class="modal-header">
                    <h5 id="modalTitle">Thêm Sản Phẩm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="formAction" name="action" value="insert">
                    <input type="hidden" id="itemId" name="itemId">
                    <input class="form-control mb-3" id="itemName" name="itemName" placeholder="Tên sản phẩm">
                    <input class="form-control mb-3" id="price" name="price" type="number" placeholder="Giá sản phẩm">
                    <input class="form-control mb-3" id="stockQuantity" name="stockQuantity" type="number" placeholder="Số lượng">
                    <input class="form-control" id="productImgUrl" name="productImgUrl" placeholder="URL">
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button class="btn btn-add" type="submit">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>