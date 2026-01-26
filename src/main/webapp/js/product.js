/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */


function decreaseQuantity(productId) {
    const input = document.getElementById("qty-" + productId);
    let value = parseInt(input.value);
    if (value > 1) input.value = value - 1;
}

function increaseQuantity(productId, maxStock) {
    const input = document.getElementById("qty-" + productId);
    let value = parseInt(input.value);
    if (value < maxStock) input.value = value + 1;
}

// auto hide alert
setTimeout(() => {
    document.querySelectorAll(".alert").forEach(alert => {
        new bootstrap.Alert(alert).close();
    });
}, 3000);