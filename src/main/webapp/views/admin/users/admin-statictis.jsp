<%-- Document : admin-statistics Created on : Jan 26, 2026 Author : nguye --%>

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
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

                <!-- Custom CSS -->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-statictis.css">
                <!-- Chart.js -->
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                            <a href="${pageContext.request.contextPath}/admin/statistic?action=revenue">
                                <div class="glass rounded-4 p-4 hover-lift text-center" style="cursor: pointer;">
                                    <i class="fas fa-coins fa-2x mb-3 icon-primary"></i>
                                    <h6 class="text-secondary">Tổng doanh thu</h6>
                                    <h3 class="fw-bold text-gradient">${totalIncome} ₫</h3>
                                </div>
                            </a>
                        </div>



                        <div class="col-md-3">
                            <a href="${pageContext.request.contextPath}/admin/statistic?action=tickets">
                                <div class="glass rounded-4 p-4 hover-lift text-center">
                                    <i class="fas fa-ticket fa-2x mb-3 icon-primary"></i>
                                    <h6 class="text-secondary">Vé đã bán</h6>
                                    <h3 class="fw-bold text-gradient">${monthlyTicketsSold}</h3>
                                </div>
                            </a>
                        </div>

                        <div class="col-md-3">
                            <a href="${pageContext.request.contextPath}/showtime?action=admin-manage">
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

                    <div class="row g-4 mb-5">
                        <div class="col-md-6">
                            <div class="glass rounded-4 p-4">
                                <h4 class="fw-bold mb-4">Dự báo Doanh thu (7 ngày tới)</h4>
                                <div style="height: 350px;">
                                    <canvas id="revChart"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="glass rounded-4 p-4">
                                <h4 class="fw-bold mb-4">Dự báo Số vé (7 ngày tới)</h4>
                                <div style="height: 350px;">
                                    <canvas id="tixChart"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 mt-4">
                            <div class="glass rounded-4 p-4 border border-info border-opacity-25 shadow-lg">
                                <h5 class="fw-bold mb-3 text-info">
                                    <i class="fas fa-magic me-2"></i>
                                    Phân tích Thông minh từ AI
                                </h5>
                                <div class="text-white fs-5 border-start border-3 border-info ps-3 py-2"
                                    style="background: rgba(0, 210, 255, 0.05); border-radius: 0 10px 10px 0;">
                                    <c:choose>
                                        <c:when test="${not empty forecastAnalysis}">
                                            ${forecastAnalysis}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-secondary italic fs-6">Đang phân tích dữ liệu thị
                                                trường...</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="glass rounded-4 p-3">
                                <p class="text-secondary small mb-0">
                                    <i class="fas fa-info-circle me-1"></i>
                                    Dữ liệu thực tế (14 ngày qua) và dự báo (7 ngày tới) được phân tích bởi AI.
                                </p>
                            </div>
                        </div>
                    </div>

                </div>

                <script>
                    const currencyFormatter = new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND',
                        maximumFractionDigits: 0
                    });

                    document.addEventListener('DOMContentLoaded', function () {
                        const labels = [];
                        const actualRevData = [];
                        const forecastRevData = [];
                        const actualTixData = [];
                        const forecastTixData = [];

                        <c:forEach items="${forecastData}" var="d" varStatus="status">
                            labels.push('${d.date}');
                            <c:choose>
                                <c:when test="${d.future}">
                                    actualRevData.push(null);
                                    forecastRevData.push(${d.forecastRevenue});
                                    actualTixData.push(null);
                                    forecastTixData.push(${d.forecastTickets});
                                </c:when>
                                <c:otherwise>
                                    actualRevData.push(${d.actualRevenue});
                                    // Connect the forecast line starting from the last actual point
                                    // If this is the point just before future data starts (or the last point in list)
                                    // we can push it to forecast data as well.
                                    forecastRevData.push(${status.last || forecastData[status.index + 1].future ? d.actualRevenue : 'null'});
                                    actualTixData.push(${d.actualTickets});
                                    forecastTixData.push(${status.last || forecastData[status.index + 1].future ? d.actualTickets : 'null'});
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        // Revenue Chart
                        new Chart(document.getElementById('revChart'), {
                            type: 'line',
                            data: {
                                labels: labels,
                                datasets: [
                                    {
                                        label: 'Doanh thu Thực tế',
                                        data: actualRevData,
                                        borderColor: '#00d2ff',
                                        backgroundColor: 'rgba(0, 210, 255, 0.1)',
                                        fill: true,
                                        tension: 0.4
                                    },
                                    {
                                        label: 'Doanh thu Dự báo',
                                        data: forecastRevData,
                                        borderColor: '#ff9a9e',
                                        borderDash: [5, 5],
                                        fill: false,
                                        tension: 0.4
                                    }
                                ]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: { labels: { color: '#94a3b8' } },
                                    tooltip: {
                                        mode: 'index',
                                        intersect: false,
                                        callbacks: {
                                            label: function (context) {
                                                let label = context.dataset.label || '';
                                                if (label) label += ': ';
                                                if (context.parsed.y !== null) {
                                                    label += currencyFormatter.format(context.parsed.y);
                                                }
                                                return label;
                                            }
                                        }
                                    }
                                },
                                scales: {
                                    x: { ticks: { color: '#94a3b8' }, grid: { color: 'rgba(255,255,255,0.05)' } },
                                    y: {
                                        ticks: {
                                            color: '#94a3b8',
                                            callback: function (value) {
                                                if (value >= 1000000) return (value / 1000000).toFixed(1) + 'M';
                                                if (value >= 1000) return (value / 1000).toFixed(0) + 'K';
                                                return value;
                                            }
                                        },
                                        grid: { color: 'rgba(255,255,255,0.05)' }
                                    }
                                }
                            }
                        });

                        // Tickets Chart
                        new Chart(document.getElementById('tixChart'), {
                            type: 'line',
                            data: {
                                labels: labels,
                                datasets: [
                                    {
                                        label: 'Số vé Thực tế',
                                        data: actualTixData,
                                        borderColor: '#34d399',
                                        backgroundColor: 'rgba(52, 211, 153, 0.1)',
                                        fill: true,
                                        tension: 0.4
                                    },
                                    {
                                        label: 'Số vé Dự báo',
                                        data: forecastTixData,
                                        borderColor: '#fbbf24',
                                        borderDash: [5, 5],
                                        fill: false,
                                        tension: 0.4
                                    }
                                ]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: { labels: { color: '#94a3b8' } },
                                    tooltip: {
                                        mode: 'index',
                                        intersect: false,
                                        callbacks: {
                                            label: function (context) {
                                                let label = context.dataset.label || '';
                                                if (label) label += ': ';
                                                label += context.parsed.y + ' vé';
                                                return label;
                                            }
                                        }
                                    }
                                },
                                scales: {
                                    x: { ticks: { color: '#94a3b8' }, grid: { color: 'rgba(255,255,255,0.05)' } },
                                    y: { ticks: { color: '#94a3b8' }, grid: { color: 'rgba(255,255,255,0.05)' }, min: 0 }
                                }
                            }
                        });
                    });
                </script>

                <!-- Floating Button -->
                <button class="chatbot-toggle" id="chatToggleBtn" onclick="toggleChat()" title="Mở CineBot">
                    <i class="fas fa-robot"></i>
                </button>

                <!-- Chat Window -->
                <div class="chatbot-window" id="chatbot">
                    <div class="chatbot-header">
                        <span><i class="fas fa-robot me-2"></i>CineBot — Chuyên gia Phân tích</span>
                        <div class="chatbot-header-actions">
                            <button class="btn-icon" onclick="resetChat()" title="Xoá lịch sử">
                                <i class="fas fa-rotate-right"></i>
                            </button>
                            <button class="btn-icon" onclick="toggleChat()" title="Đóng">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                    </div>

                    <div class="chatbot-body" id="chatBody">
                        <div class="message bot-message">
                            <strong>Xin chào Admin! 🎬</strong><br>
                            Tôi là <strong>CineBot</strong>, có thể phân tích:<br>
                            • Doanh thu (ngày / tháng / năm)<br>
                            • Phim bán chạy và dự đoán xu hướng<br>
                            • Khung giờ vàng và hiệu suất suất chiếu<br>
                            • Độ phủ ghế và lưu ý tối ưu<br><br>
                            Bạn muốn biết gì? 🚀
                        </div>
                    </div>

                    <!-- Quick-prompt chips -->
                    <div class="chatbot-chips" id="quickChips">
                        <button class="chip" onclick="sendQuick('Doanh thu tháng này là bao nhiêu?')">&#128181; Doanh
                            thu</button>
                        <button class="chip" onclick="sendQuick('Phim nào bán vé chạy nhất?')">&#127918; Top
                            phim</button>
                        <button class="chip" onclick="sendQuick('Khung giờ nào hiệu quả nhất tháng này?')">&#9200; Khung
                            giờ vàng</button>
                        <button class="chip" onclick="sendQuick('Phân tích độ phủ ghế và gợi ý cải thiện?')">&#128186;
                            Độ phủ ghế</button>
                    </div>

                    <div class="chatbot-footer">
                        <input type="text" id="chatInput" placeholder="Hỏi CineBot..." autocomplete="off" />
                        <button id="sendBtn" onclick="sendMessage()">
                            <i class="fas fa-paper-plane"></i>
                        </button>
                    </div>
                </div>

                <!-- marked.js for markdown rendering -->
                <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
                <script>
                    marked.setOptions({ breaks: true, gfm: true });

                    function toggleChat() {
                        const chat = document.getElementById("chatbot");
                        const btn = document.getElementById("chatToggleBtn");
                        const isVisible = chat.style.display === "flex";
                        chat.style.display = isVisible ? "none" : "flex";
                        btn.innerHTML = isVisible
                            ? '<i class="fas fa-robot"></i>'
                            : '<i class="fas fa-times"></i>';
                    }

                    function sendQuick(text) {
                        document.getElementById("chatInput").value = text;
                        // hide chips after first use
                        document.getElementById("quickChips").style.display = "none";
                        sendMessage();
                    }

                    function appendMessage(html, cls) {
                        const chatBody = document.getElementById("chatBody");
                        const div = document.createElement("div");
                        div.className = "message " + cls;
                        div.innerHTML = html;
                        chatBody.appendChild(div);
                        chatBody.scrollTop = chatBody.scrollHeight;
                        return div;
                    }

                    function showTyping() {
                        return appendMessage(
                            '<span class="typing-dot"></span><span class="typing-dot"></span><span class="typing-dot"></span>',
                            'bot-message typing-indicator'
                        );
                    }

                    function sendMessage() {
                        const input = document.getElementById("chatInput");
                        const sendBtn = document.getElementById("sendBtn");
                        const message = input.value.trim();
                        if (!message) return;

                        // Hide chips on first send
                        document.getElementById("quickChips").style.display = "none";

                        appendMessage(escapeHtml(message), "user-message");
                        input.value = "";
                        sendBtn.disabled = true;
                        input.disabled = true;

                        const typingEl = showTyping();

                        fetch("${pageContext.request.contextPath}/ChatServlet", {
                            method: "POST",
                            headers: { "Content-Type": "application/x-www-form-urlencoded" },
                            body: "message=" + encodeURIComponent(message)
                        })
                            .then(res => res.json())
                            .then(data => {
                                typingEl.remove();
                                const replyHtml = data.success
                                    ? marked.parse(data.reply || "")
                                    : "<em>❌ " + escapeHtml(data.error || "Lỗi không xác định") + "</em>";
                                appendMessage(replyHtml, "bot-message");
                            })
                            .catch(() => {
                                typingEl.remove();
                                appendMessage("<em>⚠️ Mất kết nối, vui lòng thử lại.</em>", "bot-message");
                            })
                            .finally(() => {
                                sendBtn.disabled = false;
                                input.disabled = false;
                                input.focus();
                            });
                    }

                    function resetChat() {
                        fetch("${pageContext.request.contextPath}/ChatServlet/reset", { method: "POST" })
                            .then(() => {
                                const chatBody = document.getElementById("chatBody");
                                chatBody.innerHTML = '';
                                appendMessage(
                                    '<strong>Bộ nhớ đã được xóa! 🔄</strong><br>Hãy đặt câu hỏi mới nhé.',
                                    'bot-message'
                                );
                                document.getElementById("quickChips").style.display = "flex";
                            });
                    }

                    function escapeHtml(text) {
                        const d = document.createElement('div');
                        d.appendChild(document.createTextNode(text));
                        return d.innerHTML;
                    }

                    document.getElementById("chatInput").addEventListener("keydown", function (e) {
                        if (e.key === "Enter") { e.preventDefault(); sendMessage(); }
                    });
                </script>

            </body>

            </html>