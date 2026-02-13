<%-- 
    Document   : admin-statistics
    Created on : Jan 26, 2026
    Author     : nguye
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Thống kê</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <!-- Custom CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin-statictis.css">
</head>

<body>
<!-- Back Home -->
        <a href="${pageContext.request.contextPath}/home" class="btn-back">
            Back to Home
        </a>

<div class="container admin-container">

    <!-- ===== TITLE ===== -->
    <div class="text-center mb-5">
        <h1 class="display-4 fw-bold text-gradient">Admin Dashboard</h1>
        <p class="text-secondary fs-5">
            Thống kê doanh thu & hiệu suất rạp chiếu phim
        </p>
    </div>

    <!-- ===== SUMMARY CARDS ===== -->
    <div class="row g-4 mb-5">

        
             <div class="col-md-3">
                 <a href="${pageContext.request.contextPath}/AdminRevenueServlet">
                 <div class="glass rounded-4 p-4 hover-lift text-center"
                      style="cursor: pointer;">
                     <i class="fas fa-coins fa-2x mb-3 icon-primary"></i>
                     <h6 class="text-secondary">Tổng doanh thu</h6>
                     <h3 class="fw-bold text-gradient">${totalIncome} ₫</h3>
                 </div>
                </a>
             </div>



        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/AdminTicketServlet">
            <div class="glass rounded-4 p-4 hover-lift text-center">
                <i class="fas fa-ticket fa-2x mb-3 icon-primary"></i>
                <h6 class="text-secondary">Vé đã bán</h6>
                <h3 class="fw-bold text-gradient">${monthlyTicketsSold}</h3>
            </div>
            </a>
        </div>

        <div class="col-md-3">
            <a href="${pageContext.request.contextPath}/AdminShowtimeServlet">
            <div class="glass rounded-4 p-4 hover-lift text-center">
                <i class="fas fa-video fa-2x mb-3 icon-primary"></i>
                <h6 class="text-secondary">Suất chiếu</h6>
                <h3 class="fw-bold">${numberOfShowtime}</h3>
            </div>
            </a>
        </div>

        <div class="col-md-3">
            <div class="glass rounded-4 p-4 hover-lift text-center">
                <i class="fas fa-calendar-day fa-2x mb-3 icon-primary"></i>
                <h6 class="text-secondary">Doanh thu hôm nay</h6>
                <h3 class="fw-bold text-gradient">${totalIncomeToday} ₫</h3>
            </div>
        </div>

    </div>

    <!-- ===== TOP MOVIES ===== -->
    <div class="glass rounded-4 p-4 mb-5">
        <h4 class="fw-bold mb-4">Phim bán chạy nhất</h4>

        <table class="table table-dark table-hover align-middle">
            <thead>
            <tr class="text-secondary">
                <th>Phim</th>
                <th>Vé bán</th>
                <th>Doanh thu</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${topMovies}" var="m">
                <tr>
                    <td>${m.title}</td>
                    <td>${m.ticketsSold}</td>
                    <td class="fw-bold text-green">
                        ${m.revenue} ₫
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    
    <div class="glass rounded-4 p-4 mb-5">
        <h4 class="fw-bold mb-4">Phim doanh thu thấp</h4>

        <table class="table table-dark table-hover align-middle">
            <thead>
            <tr class="text-secondary">
                <th>Phim</th>
                <th>Vé bán</th>
                <th>Doanh thu</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${badMovies}" var="m">
                <tr>
                    <td>${m.title}</td>
                    <td>${m.ticketsSold}</td>
                    <td class="fw-bold text-red">
                        ${m.revenue} ₫
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>

<!-- Floating Button -->
<button class="chatbot-toggle" onclick="toggleChat()">
    <i class="fas fa-robot"></i>
</button>

<!-- Chat Window -->
<div class="chatbot-window" id="chatbot">
    <div class="chatbot-header">
        AI Cinema Assistant
    </div>

    <div class="chatbot-body" id="chatBody">
        <div class="message bot-message">
            Xin chào Admin 👋<br>
            Tôi có thể phân tích doanh thu cho bạn.
        </div>
    </div>

    <div class="chatbot-footer">
        <input type="text" id="chatInput" placeholder="Hỏi về doanh thu..." />
        <button onclick="sendMessage()">
            <i class="fas fa-paper-plane"></i>
        </button>
    </div>
</div>
    <script>
    function toggleChat() {
        const chat = document.getElementById("chatbot");
        chat.style.display = chat.style.display === "flex" ? "none" : "flex";
    }

    function sendMessage() {
        const input = document.getElementById("chatInput");
        const message = input.value.trim();
        if(message === "") return;

        const chatBody = document.getElementById("chatBody");

        // User message
        const userDiv = document.createElement("div");
        userDiv.className = "message user-message";
        userDiv.innerText = message;
        chatBody.appendChild(userDiv);

        // Fake AI response (sau này thay bằng fetch API)
        const botDiv = document.createElement("div");
        botDiv.className = "message bot-message";
        botDiv.innerText = "AI đang phân tích dữ liệu...";
        chatBody.appendChild(botDiv);

        chatBody.scrollTop = chatBody.scrollHeight;
        input.value = "";
    }
    </script>

</body>
</html>
