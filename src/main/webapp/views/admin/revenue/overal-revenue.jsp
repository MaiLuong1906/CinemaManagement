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

<body>
        <!-- Back -->
        <a href="${pageContext.request.contextPath}/views/admin/users/admin-statictis.jsp" class="btn-back">
            Back
        </a>

    <h1>Thống kê doanh thu</h1>
<div class="container-fluid mt-4">
    <div class="row">

        <!-- ===== BÊN TRÁI: BIỂU ĐỒ TRÒN ===== -->
        <div class="col-md-6">
            <div class="card dashboard-card p-4">
                <h5 class="fw-bold mb-3 text-center">
                    Tỷ lệ doanh thu theo tháng
                </h5>
                <canvas id="revenuePieChart"></canvas>
            </div>
        </div>

        <!-- ===== BÊN PHẢI: KPI ===== -->
        <!--doi voi phan nay thi du lieu dang tinh ca cac ve tam tuc la status chua paid-->
        <div class="col-md-6">
            <div class="row g-4">

                <div class="col-md-6">
                    <div class="kpi-card text-center">
                        <h6 class="text-muted">Tổng doanh thu tháng</h6>
                        <h3 class="fw-bold text-primary">
                            ${monthlyRevenue} ₫
                        </h3>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="kpi-card text-center">
                        <h6 class="text-muted">Doanh thu hôm nay</h6>
                        <h3 class="fw-bold text-success">
                            ${dailyRevenue} ₫
                        </h3>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="kpi-card text-center">
                        <h6 class="text-muted">Doanh thu từ vé tháng này</h6>
                        <h3 class="fw-bold text-warning">
                            ${ticketRevenue} ₫
                        </h3>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="kpi-card text-center">
                        <h6 class="text-muted">Doanh thu từ product tháng này</h6>
                        <h3 class="fw-bold text-danger">
                            ${productRevenue} ₫
                        </h3>
                    </div>
                </div>
                <div class="col-md-12">
                    <div class="kpi-card text-center kpi-year">
                        <h6 class="text-muted">Tổng doanh thu năm</h6>
                        <h2 class="fw-bold text-info">
                            ${yearlyRevenue} ₫
                        </h2>
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
                data: [
                    ${percentProduct},
                    ${percentTicket}
                ]
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

