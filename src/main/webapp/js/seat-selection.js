document.addEventListener('DOMContentLoaded', () => {
    const seatMap = document.querySelector('.seat-map-container');
    const selectedSeatsSpan = document.getElementById('selectedSeats');
    const totalPriceSpan = document.getElementById('total');
    const seatIdsInput = document.getElementById('seatIds');
    const totalPriceInput = document.getElementById('totalPrice');
    const confirmBtn = document.querySelector('.confirm-btn');

    let selectedSeats = [];
    let totalPrice = 0;

    // Delegate event listener to container for better performance
    seatMap.addEventListener('click', (e) => {
        if (e.target.classList.contains('seat') && !e.target.classList.contains('booked') && !e.target.disabled) {
            toggleSeat(e.target);
        }
    });

    function toggleSeat(seat) {
        const seatId = seat.dataset.seatId;
        const seatCode = seat.innerText;
        const price = parseFloat(seat.dataset.price);

        if (seat.classList.contains('selected')) {
            // Deselect
            seat.classList.remove('selected');
            selectedSeats = selectedSeats.filter(s => s.id !== seatId);
            totalPrice -= price;
        } else {
            // Select
            seat.classList.add('selected');
            selectedSeats.push({ id: seatId, code: seatCode });
            totalPrice += price;
        }

        updateUI();
    }

    function updateUI() {
        // Update total price display
        totalPriceSpan.innerText = totalPrice.toLocaleString('vi-VN');
        
        // Update selected seats text
        if (selectedSeats.length > 0) {
            selectedSeatsSpan.innerText = selectedSeats.map(s => s.code).join(', ');
            confirmBtn.disabled = false;
        } else {
            selectedSeatsSpan.innerText = 'Chưa chọn ghế nào';
            confirmBtn.disabled = true;
        }

        // Update hidden inputs
        seatIdsInput.value = selectedSeats.map(s => s.id).join(',');
        totalPriceInput.value = totalPrice;
    }
});
