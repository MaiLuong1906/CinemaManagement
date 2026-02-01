/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */


function resetForm() {
    const form = document.querySelector("#productModal form");
    form.reset();

    document.getElementById("formAction").value = "insert";
    document.getElementById("itemId").value = "";
    document.getElementById("modalTitle").innerText = "Thêm Sản Phẩm";

    // reset preview
    const preview = document.getElementById("imagePreview");
    if (preview) {
        preview.src = "https://via.placeholder.com/300x200?text=Preview";
    }
}

function editProduct(id, name, price, imgFileName, quantity) {
    document.getElementById("modalTitle").innerText = "Sửa Sản Phẩm";
    document.getElementById("formAction").value = "update";
    document.getElementById("itemId").value = id;
    document.getElementById("itemName").value = name;
    document.getElementById("price").value = price;
    document.getElementById("stockQuantity").value = quantity;

    // HIỆN ẢNH CŨ (đúng ImageServlet)
    const preview = document.getElementById("imagePreview");
    if (preview && imgFileName) {
        preview.src = contextPath + "/image?path=" + imgFileName;
    }

    new bootstrap.Modal(document.getElementById("productModal")).show();
}

function confirmDelete(id, name) {
    document.getElementById("deleteProductName").innerText = name;
    document.getElementById("confirmDeleteBtn").href =
        contextPath + "/admin/product?action=delete&id=" + id;

    new bootstrap.Modal(document.getElementById("deleteModal")).show();
}

function previewImage(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("imagePreview").src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
}
