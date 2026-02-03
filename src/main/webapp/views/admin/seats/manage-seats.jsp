<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản lý Ghế - ${hall.hallName}</title>

  <!-- Bootstrap 5 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Bootstrap Icons -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

  <style>
    body {
      background-color: #f8f9fa;
    }
    .main-content {
      margin-left: 280px;
      padding: 30px;
    }
    .page-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 30px;
      border-radius: 15px;
      margin-bottom: 30px;
    }
    .seat-map-container {
      background: white;
      border-radius: 15px;
      padding: 30px;
      box-shadow: 0 2px 15px rgba(0,0,0,0.1);
    }
    .screen {
      background: linear-gradient(to bottom, #1e3c72, #2a5298);
      color: white;
      padding: 15px;
      text-align: center;
      border-radius: 10px 10px 50% 50%;
      margin-bottom: 30px;
      font-weight: bold;
    }
    .seat-grid {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8px;
    }
    .seat-row {
      display: flex;
      align-items: center;
      gap: 5px;
    }
    .row-label {
      width: 40px;
      text-align: center;
      font-weight: bold;
      font-size: 1.1rem;
      color: #495057;
    }
    .seat {
      width: 45px;
      height: 45px;
      margin: 2px;
      border-radius: 8px 8px 0 0;
      border: 2px solid;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      font-size: 0.7rem;
      font-weight: bold;
      cursor: pointer;
      transition: all 0.3s;
    }
    .seat.active {
      background-color: #6c757d;
      border-color: #495057;
      color: white;
    }
    .seat.active:hover {
      transform: scale(1.1);
      box-shadow: 0 3px 10px rgba(0,0,0,0.3);
    }
    .seat.inactive {
      background-color: #dee2e6;
      border-color: #adb5bd;
      color: #6c757d;
      cursor: not-allowed;
      opacity: 0.5;
    }
    .seat.selected {
      border-color: #0d6efd;
      box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.3);
    }
    .seat.type-vip {
      background-color: #ffc107;
      border-color: #ffb300;
      color: #000;
    }
    .seat.type-couple {
      background-color: #dc3545;
      border-color: #c82333;
      color: white;
    }
    .legend {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
      justify-content: center;
      margin-top: 30px;
      padding: 20px;
      background: #f8f9fa;
      border-radius: 10px;
    }
    .legend-item {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .legend-seat {
      width: 35px;
      height: 35px;
      border-radius: 6px 6px 0 0;
      border: 2px solid;
    }
    .tool-panel {
      background: white;
      border-radius: 15px;
      padding: 20px;
      box-shadow: 0 2px 15px rgba(0,0,0,0.1);
      margin-bottom: 20px;
    }
    @media (max-width: 768px) {
      .main-content { margin-left: 0; padding: 15px; }
      .seat { width: 35px; height: 35px; font-size: 0.6rem; }
    }
  </style>
</head>
<body>
<!-- Main Content -->
<div class="main-content" style="margin-left: 0;">
  <!-- Page Header -->
  <div class="page-header">
    <div class="d-flex justify-content-between align-items-center">
      <div>
        <h2 class="mb-2">
          <i class="bi bi-layout-three-columns me-2"></i>Quản lý Ghế: ${hall.hallName}
        </h2>
        <p class="mb-0">
          Kích thước: ${hall.total_rows} hàng × ${hall.total_cols} cột
        </p>
      </div>
      <a href="${pageContext.request.contextPath}/venue?action=halls" class="btn btn-light">
        <i class="bi bi-arrow-left me-2"></i>Quay lại
      </a>
    </div>
  </div>

  <!-- Success/Error Messages -->
  <c:if test="${not empty sessionScope.successMsg}">
    <div class="alert alert-success alert-dismissible fade show">
      <i class="bi bi-check-circle me-2"></i>${sessionScope.successMsg}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="successMsg" scope="session"/>
  </c:if>

  <!-- Tool Panel -->
  <div class="tool-panel">
    <form id="seatUpdateForm" action="${pageContext.request.contextPath}/venue?action=update-seat" method="post">
      <input type="hidden" name="hallId" value="${hall.hallId}">

      <div class="row align-items-end">
        <div class="col-md-3 mb-3 mb-md-0">
          <label class="form-label">
            <i class="bi bi-pencil me-2"></i>Đổi loại ghế:
          </label>
          <select class="form-select" name="seatTypeId" id="seatTypeSelect">
            <option value="">-- Chọn loại ghế --</option>
            <c:forEach var="type" items="${seatTypes}">
              <option value="${type.seatTypeId}">
                  ${type.typeName} (+ ${type.extraFee} VNĐ)
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="col-md-3 mb-3 mb-md-0">
          <label class="form-label">
            <i class="bi bi-toggle-on me-2"></i>Đổi trạng thái:
          </label>
          <select class="form-select" name="seatStatus" id="statusSelect">
            <option value="">-- Chọn trạng thái --</option>
            <option value="1">Bật (Hoạt động)</option>
            <option value="0">Tắt (Lối đi)</option>
          </select>
        </div>

        <div class="col-md-6 text-md-end">
          <button type="submit" class="btn btn-primary" id="applyBtn" disabled>
            <i class="bi bi-check2-square me-2"></i>
            Áp dụng (<span id="selectedCount">0</span> ghế)
          </button>
          <button type="button" class="btn btn-outline-secondary" id="clearBtn">
            <i class="bi bi-x-circle me-2"></i>Bỏ chọn
          </button>
        </div>
      </div>

      <!-- Hidden inputs for selected seats -->
      <div id="selectedSeatsContainer"></div>
    </form>
  </div>

  <!-- Seat Map -->
  <div class="seat-map-container">
    <div class="screen">
      <i class="bi bi-tv me-2"></i>MÀN HÌNH
    </div>

    <div class="seat-grid">
      <c:set var="currentRow" value="-1" />

      <c:forEach var="seat" items="${seats}" varStatus="status">
      <!-- Nếu bắt đầu hàng mới -->
      <c:if test="${seat.rowIndex != currentRow}">
      <c:if test="${currentRow != -1}">
    </div> <!-- Đóng hàng cũ -->
    </c:if>
    <!-- Mở hàng mới -->
    <div class="seat-row">
      <div class="row-label">${seat.seatCode.substring(0,1)}</div>
      </c:if>

      <!-- Xác định class cho ghế -->
      <c:set var="seatClass" value="seat ${seat.active ? 'active' : 'inactive'}" />
      <c:set var="typeName" value="${seat.seatType.typeName}" />

      <c:if test="${typeName == 'VIP'}">
        <c:set var="seatClass" value="${seatClass} type-vip" />
      </c:if>
      <c:if test="${typeName == 'Couple' || typeName == 'Đôi'}">
        <c:set var="seatClass" value="${seatClass} type-couple" />
      </c:if>

      <!-- Ghế -->
      <div class="${seatClass}"
           data-seat-id="${seat.seatId}"
           data-active="${seat.active}"
           data-type="${seat.seatType.seatTypeId}"
           title="Ghế ${seat.seatCode} - ${seat.seatType.typeName}">
          ${seat.seatCode}
      </div>

      <c:set var="currentRow" value="${seat.rowIndex}" />

      <!-- Đóng hàng cuối cùng -->
      <c:if test="${status.last}">
      <div class="row-label">${seat.seatCode.substring(0,1)}</div>
    </div>
    </c:if>

    <!-- Đóng và mở hàng mới nếu hàng tiếp theo khác -->
    <c:if test="${!status.last}">
      <c:set var="nextSeat" value="${seats[status.index + 1]}" />
      <c:if test="${nextSeat.rowIndex != currentRow}">
        <div class="row-label">${seat.seatCode.substring(0,1)}</div>
      </c:if>
    </c:if>
    </c:forEach>
  </div>

  <!-- Legend -->
  <div class="legend">
    <div class="legend-item">
      <div class="legend-seat" style="background:#6c757d;border-color:#495057;"></div>
      <span>Ghế thường</span>
    </div>
    <div class="legend-item">
      <div class="legend-seat type-vip" style="background:#ffc107;border-color:#ffb300;"></div>
      <span>Ghế VIP</span>
    </div>
    <div class="legend-item">
      <div class="legend-seat type-couple" style="background:#dc3545;border-color:#c82333;"></div>
      <span>Ghế đôi</span>
    </div>
    <div class="legend-item">
      <div class="legend-seat inactive" style="background:#dee2e6;border-color:#adb5bd;"></div>
      <span>Lối đi / Tắt</span>
    </div>
  </div>
</div>
</div>

<!-- Bootstrap 5 JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Custom JS -->
<script>
  const selectedSeats = new Set();
  const seats = document.querySelectorAll('.seat.active');
  const selectedCount = document.getElementById('selectedCount');
  const applyBtn = document.getElementById('applyBtn');
  const clearBtn = document.getElementById('clearBtn');
  const container = document.getElementById('selectedSeatsContainer');
  const form = document.getElementById('seatUpdateForm');
  const seatTypeSelect = document.getElementById('seatTypeSelect');
  const statusSelect = document.getElementById('statusSelect');

  // Click chọn ghế
  seats.forEach(seat => {
    seat.addEventListener('click', function() {
      const seatId = this.dataset.seatId;

      if (selectedSeats.has(seatId)) {
        selectedSeats.delete(seatId);
        this.classList.remove('selected');
      } else {
        selectedSeats.add(seatId);
        this.classList.add('selected');
      }

      updateUI();
    });
  });

  // Bỏ chọn tất cả
  clearBtn.addEventListener('click', function() {
    selectedSeats.clear();
    seats.forEach(s => s.classList.remove('selected'));
    updateUI();
  });

  // Submit form
  form.addEventListener('submit', function(e) {
    const hasType = seatTypeSelect.value !== '';
    const hasStatus = statusSelect.value !== '';

    if (!hasType && !hasStatus) {
      e.preventDefault();
      alert('Vui lòng chọn loại ghế hoặc trạng thái cần thay đổi');
      return;
    }

    if (selectedSeats.size === 0) {
      e.preventDefault();
      alert('Vui lòng chọn ít nhất 1 ghế');
      return;
    }

    // Thêm hidden inputs cho các ghế đã chọn
    container.innerHTML = '';
    selectedSeats.forEach(id => {
      const input = document.createElement('input');
      input.type = 'hidden';
      input.name = 'seatIds';
      input.value = id;
      container.appendChild(input);
    });
  });

  // Update UI
  function updateUI() {
    selectedCount.textContent = selectedSeats.size;
    applyBtn.disabled = selectedSeats.size === 0;
  }
</script>
</body>
</html>