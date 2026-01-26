/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */


function updateQuantity(itemId, change, maxStock) {
    const input = document.getElementById("qty-" + itemId);
    let value = parseInt(input.value) + change;

    if (value >= 1 && value <= maxStock) {
        input.value = value;
        document.getElementById("form-" + itemId).submit();
    }
}

function checkout() {
    alert("Chức năng thanh toán đang được phát triển!");
}

// Auto hide alert
setTimeout(() => {
    document.querySelectorAll(".alert").forEach(alert => {
        new bootstrap.Alert(alert).close();
    });
}, 3000);