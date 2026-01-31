<%-- 
    Document   : ticket-management
    Created on : Jan 31, 2026, 2:49:45 AM
    Author     : nguye
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Ticket Statistics</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ticket-management.css"/>
    </head>
    <body>
        <!-- Back -->
        <a href="${pageContext.request.contextPath}/AdminStatisticServlet" class="btn-back">
            Back
        </a>
        <h1>Thống kê vé</h1>
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
                            <h2>${yearlyTicketsSold}</h2>
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
                    <h5> Vé bán theo phim</h5>
                </div>
                <div class="card-body">
                    <table class="table table-dark table-hover">
                        <thead>
                            <tr>
                                <th>Phim</th>
                                <th>Số vé</th>
                            </tr>
                        </thead>
                        <tbody id="movieTableBody">
                            <c:forEach items="${movies}" var="m">
                                <tr>
                                    <td>${m.title}</td>
                                    <td>${m.ticketsSold}</td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                    <!--phan vung-->
                    <div class="d-flex justify-content-center align-items-center gap-3 mt-3">
                        <button class="btn btn-outline-light btn-sm" onclick="prevPage('movie')">◀</button>
                        <span id="moviePageInfo">
                            ${currentPage} / ${totalPages}
                        </span>
                        <button class="btn btn-outline-light btn-sm" onclick="nextPage('movie')">▶</button>
                    </div>

                </div>
            </div>

            <!-- ================= VÉ THEO PHÒNG CHIẾU ================= -->
            <div class="card bg-secondary mb-4">
                <div class="card-header">
                    <h5> Vé theo khung giờ</h5>
                </div>
                <div class="card-body">
                    <table class="table table-dark table-striped">
                        <thead>
                            <tr>
                                <th>Khung giờ</th>
                                <th>Vé đã bán</th>
                                <th>Doanh thu</th>
                            </tr>
                        </thead>
                        <tbody id="roomTableBody">
                            <c:forEach items="${ticketSoldBySlot}" var="s">
                                <tr>
                                    <td>${s.startTime} - ${s.endTime}</td>
                                    <td>${s.ticketsSold}</td>
                                    <td class="fw-bold text-gradient">
                                        <fmt:formatNumber value="${s.slotRevenue}" type="number" groupingUsed="true"/> ₫
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
        <script>
            function nextPage() {
                let current = ${currentPage};
                let total = ${totalPages};
                if (current < total) {
                    window.location.href = "?page=" + (current + 1);
                }
            }

            function prevPage() {
                let current = ${currentPage};
                if (current > 1) {
                    window.location.href = "?page=" + (current - 1);
                }
            }
            </script>


    </body>
</html>

