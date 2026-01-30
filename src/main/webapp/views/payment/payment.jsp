<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, model.Invoice" %>

<h2>Danh sách hóa đơn chưa thanh toán</h2>

<table border="1" cellpadding="8">
  <tr>
    <th>Invoice ID</th>
    <th>Số tiền (VND)</th>
    <th>Ngày tạo</th>
    <th>Trạng thái</th>
    <th>Action</th>
  </tr>

  <%
    List<Invoice> invoices = (List<Invoice>) request.getAttribute("invoices");
    for (Invoice inv : invoices) {
  %>
  <tr>
    <td><%= inv.getInvoiceId() %></td>
    <td><%= inv.getTotalAmount() %></td>
    <td><%= inv.getBookingTime() %></td>
    <td><%= inv.getStatus() %></td>
    <td>
      <button onclick="pay(<%= inv.getInvoiceId() %>)">
        Thanh toán
      </button>
    </td>
  </tr>
  <%
    }
  %>
</table>

<script>
  function pay(invoiceId) {
    fetch("ajaxServlet", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: "invoiceId=" + invoiceId + "&language=vn"
    })
            .then(res => res.json())
            .then(data => {
              if (data.code === "00") {
                window.location.href = data.data;
              }
            });
  }
</script>
