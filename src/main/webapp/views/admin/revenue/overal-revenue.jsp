<%-- 
    Document   : overal-revenue
    Created on : Jan 29, 2026, 10:05:07 AM
    Author     : nguye
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thống kê doanh thu</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/overal-revenue.css">

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body class="bg-light">

    <h1>Thống kê doanh thu</h1>
<div class="container-fluid mt-4">
    <div class="row">

        <!-- ===== BÊN TRÁI: BIỂU ĐỒ TRÒN ===== -->
        <div class="col-md-6">
            <div class="card dashboard-card p-4">
                <h5 class="fw-bold mb-3 text-center">
                    Tỷ lệ doanh thu
                </h5>
                <canvas id="revenuePieChart"></canvas>
            </div>
        </div>

        <!-- ===== BÊN PHẢI: KPI ===== -->
        <div class="col-md-6">
            <div class="row g-4">

                <div class="col-md-6">
                    <div class="kpi-card text-center">
                        <h6 class="text-muted">Tổng doanh thu</h6>
                        <h3 class="fw-bold text-primary">
                            ${totalRevenue} ₫
                        </h3>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="kpi-card text-center">
                        <h6 class="text-muted">Doanh thu hôm nay</h6>
                        <h3 class="fw-bold text-success">
                            ${todayRevenue} ₫
                        </h3>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="kpi-card text-center">
                        <h6 class="text-muted">Doanh thu tháng này</h6>
                        <h3 class="fw-bold text-warning">
                            ${monthRevenue} ₫
                        </h3>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="kpi-card text-center">
                        <h6 class="text-muted">Doanh thu năm nay</h6>
                        <h3 class="fw-bold text-danger">
                            ${yearRevenue} ₫
                        </h3>
                    </div>
                </div>

            </div>
        </div>

    </div>
</div>

<!-- ===== BIỂU ĐỒ TRÒN ===== -->
<script>
    const ctx = document.getElementById('revenuePieChart');

    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Nước', 'Vé'],
            datasets: [{
//                data: [
//                    ${drinkRatio},
//                    ${ticketRatio}
//                ] // co ham service moi mo cmt
                data: [60, 40]   // FAKE CỨNG
            }]
        },
        options: {
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.label + ': ' + context.parsed + '%';
                        }
                    }
                }
            }
        }
    });
</script>


</body>
</html>

