<%-- 
    Document   : deleteproduct
    Created on : Jan 29, 2026, 10:32:33 AM
    Author     : itphu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
  <!-- Bootstrap -->
<!--    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">-->

    <!-- CSS  -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-product.css">
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5>Xác nhận xóa</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
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
