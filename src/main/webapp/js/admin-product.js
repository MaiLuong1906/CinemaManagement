/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */


function resetForm() {
    document.getElementById("productForm").reset();
    document.getElementById("formAction").value = "insert";
    document.getElementById("itemId").value = "";
    document.getElementById("modalTitle").innerText = "Thêm Sản Phẩm";
}

function editProduct(id, name, price, img, stock) {
    document.getElementById("formAction").value = "update";
    document.getElementById("itemId").value = id;
    document.getElementById("itemName").value = name;
    document.getElementById("price").value = price;
    document.getElementById("stockQuantity").value = stock;
    document.getElementById("productImgUrl").value = img || "";
    document.getElementById("modalTitle").innerText = "Chỉnh Sửa Sản Phẩm";

    new bootstrap.Modal(document.getElementById("productModal")).show();
}

function confirmDelete(id, name) {
    document.getElementById("deleteProductName").innerText = name;
    document.getElementById("confirmDeleteBtn").href =
        contextPath + "/admin/product?action=delete&id=" + id;

    new bootstrap.Modal(document.getElementById("deleteModal")).show();
}