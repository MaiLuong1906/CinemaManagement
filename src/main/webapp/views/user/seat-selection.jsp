<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Seat Selection</title>

        <style>
            body {
                font-family: Arial;
            }

            .screen {
                text-align: center;
                margin: 20px auto;
                font-weight: bold;
                padding: 10px;
                background: #ccc;
                width: 300px;
            }

            .seat-map {
                display: flex;
                flex-direction: column;
                gap: 10px;
                align-items: center;
                margin-top: 30px;
            }

            .seat-row {
                display: flex;
                gap: 8px;
            }

            .seat {
                width: 50px;
                height: 45px;
                border-radius: 5px;
                cursor: pointer;
                border: none;
                color: white;
                font-weight: bold;
            }

            /* Seat types */
            .normal {
                background-color: #4CAF50;
            }   /* thường */
            .vip    {
                background-color: #FF9800;
            }   /* VIP */
            .couple {
                background-color: #E91E63;
            }   /* đôi */

            .selected {
                background-color: #2196F3 !important;
            }

            .booked {
                background-color: #BDBDBD !important;
                cursor: not-allowed;
            }

            .info {
                margin-top: 20px;
                text-align: center;
            }

            .confirm-btn {
                margin-top: 15px;
                padding: 10px 20px;
                font-size: 16px;
            }
        </style>
    </head>

    <body>

        <h2 align="center">Choose Your Seats</h2>

        <div class="screen">SCREEN</div>

        <form action="create-invoice" method="post" id="seatForm">

            <input type="hidden" name="showtimeId" value="${showtimeId}">
            <input type="hidden" name="seatIds" id="seatIds">
            <input type="hidden" name="totalPrice" id="totalPrice">

            <div class="seat-map">

                <!-- GROUP THEO ROW -->
                <c:set var="currentRow" value="-1"/>

                <c:forEach var="s" items="${seats}">
                    <c:if test="${s.rowIndex != currentRow}">
                        <c:if test="${currentRow != -1}">
                        </div>
                    </c:if>
                    <div class="seat-row">
                        <c:set var="currentRow" value="${s.rowIndex}"/>
                    </c:if>

                    <button
                        type="button"
                        class="seat
                        ${s.status == 'BOOKED' ? 'booked' :
                          s.seatTypeId == 1 ? 'normal' :
                          s.seatTypeId == 2 ? 'vip' : 'couple'}"
                        data-seat-id="${s.seatId}"
                        data-price="${s.price}"
                        <c:if test="${s.status == 'BOOKED'}">disabled</c:if>>
                        ${s.seatCode}
                    </button>



                </c:forEach>

            </div>

        </div>

        <div class="info">
            <p>Selected Seats: <span id="selectedSeats">None</span></p>
            <p>Total Price: <span id="total">0</span> VND</p>

            <button type="submit" class="confirm-btn">Confirm</button>
        </div>

    </form>

    <script>
        let selected = [];
        let total = 0;

        document.querySelectorAll(".seat:not(.booked)").forEach(btn => {

            btn.addEventListener("click", () => {
                const seatId = btn.dataset.seatId;
                const price = parseFloat(btn.dataset.price);

                if (btn.classList.contains("selected")) {
                    btn.classList.remove("selected");
                    selected = selected.filter(id => id !== seatId);
                    total -= price;
                } else {
                    btn.classList.add("selected");
                    selected.push(seatId);
                    total += price;
                }

                document.getElementById("selectedSeats").innerText =
                        selected.length > 0 ? selected.join(", ") : "None";

                document.getElementById("total").innerText = total.toLocaleString();
                document.getElementById("seatIds").value = selected.join(",");
                document.getElementById("totalPrice").value = total;
            });

        });
    </script>

</body>
</html>