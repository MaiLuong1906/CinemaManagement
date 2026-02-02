<%-- 
    Document   : showtime-management
    Created on : Feb 2, 2026, 9:51:41 PM
    Author     : nguye
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Showtime Statistic</title>
        <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/overal-revenue.css">

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <!-- Back -->
        <a href="${pageContext.request.contextPath}/AdminStatisticServlet" class="btn-back">
            Back
        </a>

    <h1>Thống kê doanh thu</h1>
<div class="container-fluid mt-4">
    <div class="row">

        <!-- ===== BÊN TRÁI: BIỂU ĐỒ TRÒN ===== -->
        <div class="col-md-6">
            <div class="card dashboard-card p-4">
                <h5 class="fw-bold mb-3 text-center">
                    Tỉ lệ vé bán theo khung giờ
                </h5>
                <canvas id="slotPieChart"></canvas>
            </div>
        </div>

        <!-- ===== BÊN PHẢI: KPI ===== -->
        <!--doi voi phan nay thi du lieu dang tinh ca cac ve tam tuc la status chua paid-->
        <div class="col-md-6">
            <div class="row g-4">
                <div class="col-md-12">
                    <div class="kpi-card text-center kpi-year">
                        <h6 class="text-muted">Độ phủ ghế theo khung giờ</h6>
                        <table class="table table-sm table-bordered mt-3">
                            <thead class="table-light">
                                <tr>
                                    <th>Khung giờ</th>
                                    <th>Độ phủ (%)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${list}">
                                    <tr>
                                        <td>  ${item.startTime} - ${item.endTime}</td>
                                        <td class="fw-bold text-info">
                                            ${item.fillRate}%
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </div>

    </div>
</div>
    <br>
                <!-- ===== KPI TABLE ===== -->
    <div class="card dashboard-card">
        <div class="card-header text-center fw-bold">
            KPI Khung Giờ (Tháng Hiện Tại)
        </div>

        <div class="card-body p-0">
            <table class="table table-bordered table-hover text-center align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>#</th>
                        <th>Khung giờ</th>
                        <th>Bắt đầu</th>
                        <th>Kết thúc</th>
                        <th>Vé đã bán</th>
                        <th>Doanh thu (₫)</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach items="${listKpi}" var="kpi" varStatus="i">
                        <tr>
                            <td class="fw-bold">${i.index + 1}</td>
                            <td>${kpi.slotName}</td>
                            <td>${kpi.startTime}</td>
                            <td>${kpi.endTime}</td>
                            <td class="fw-bold text-primary">${kpi.ticketsSold}</td>
                            <td class="fw-bold text-success">
                                <fmt:formatNumber value="${kpi.revenue}"
                                                  type="number"
                                                  groupingUsed="true"/> ₫
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>


<!-- ===== BIỂU ĐỒ TRÒN ===== -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    const slotLabels = [
        <c:forEach items="${ticketBySlot}" var="s" varStatus="i">
            "Slot ${s[0]}"<c:if test="${!i.last}">,</c:if>
        </c:forEach>
    ];

    const slotData = [
        <c:forEach items="${ticketBySlot}" var="s" varStatus="i">
            ${s[1]}<c:if test="${!i.last}">,</c:if>
        </c:forEach>
    ];

    const ctx = document.getElementById('slotPieChart');

    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: slotLabels,
            datasets: [{
                data: slotData
            }]
        },
        options: {
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return context.label + ': ' + context.parsed + ' vé';
                        }
                    }
                }
            }
        }
    });
</script>
</body>
</html>
