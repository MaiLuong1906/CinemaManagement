<%-- 
    Document   : ticket-management
    Created on : Jan 31, 2026, 2:49:45 AM
    Author     : nguye
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Ticket Statistics</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
        <!-- Back -->
        <a href="${pageContext.request.contextPath}/AdminStatisticServlet" class="btn-back">
            Back
        </a>
<div class="container mt-4">

    <!-- ================= KPI CARDS ================= -->
    <div class="row g-3 mb-4">

        <div class="col-md-3">
            <div class="card bg-primary text-center">
                <div class="card-body">
                    <h6>Vé hôm nay</h6>
                    <h2>${dailyTicketsSold}</h2>
                </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card bg-success text-center">
                <div class="card-body">
                    <h6>Vé tháng này</h6>
                    <h2>${monthlyTicketsSold}</h2>
                </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card bg-warning text-dark text-center">
                <div class="card-body">
                    <h6>Vé năm nay</h6>
                    <h2>${ticketsThisYear}</h2>
                </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card bg-info text-dark text-center">
                <div class="card-body">
                    <h6>Tỷ lệ lấp đầy</h6>
                    <h2>${avgFillRate}%</h2>
                </div>
            </div>
        </div>

    </div>

    <!-- ================= VÉ THEO PHIM ================= -->
    <div class="card bg-secondary mb-4">
        <div class="card-header">
            <h5>🎬 Vé bán theo phim</h5>
        </div>
        <div class="card-body">
            <table class="table table-dark table-hover">
                <thead>
                <tr>
                    <th>Phim</th>
                    <th>Số vé</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${ticketsByMovie}" var="m">
                    <tr>
                        <td>${m.movieTitle}</td>
                        <td>${m.totalTickets}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- ================= VÉ THEO SUẤT CHIẾU ================= -->
    <div class="card bg-secondary mb-4">
        <div class="card-header">
            <h5>🕒 Vé theo suất chiếu</h5>
        </div>
        <div class="card-body">
            <table class="table table-dark table-striped">
                <thead>
                <tr>
                    <th>Phim</th>
                    <th>Ngày</th>
                    <th>Suất</th>
                    <th>Vé đã bán</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${ticketsByShowtime}" var="s">
                    <tr>
                        <td>${s.movieTitle}</td>
                        <td>${s.showDate}</td>
                        <td>${s.slotName}</td>
                        <td>${s.totalTickets}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

</body>
</html>

