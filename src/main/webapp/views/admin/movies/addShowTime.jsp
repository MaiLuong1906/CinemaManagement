<%-- Document : addShowTime Created on : Jan 17, 2026, 8:38:04 PM Author : nguye
--%> <%@page import="model.TimeSlot"%>
<%@page import="java.time.LocalTime"%>
<%@page import="model.CinemaHall"%>
<%@page import="model.Movie"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addShowTime.css">
    </head>
    <body>
        <div>
            <h2>Thêm lịch chiếu</h2>
        <!--tra ket qua-->
        <%
        HttpSession sessionObj = request.getSession(false); // li do false la neu da co session thi lay khong thi tra null, khong tao moi
        if (sessionObj != null) {
            String message = (String) sessionObj.getAttribute("message");
            Boolean success = (Boolean) sessionObj.getAttribute("success");
            String messageDb = (String) sessionObj.getAttribute("dbError");
            if (message != null) {
        %>  
        <div style="
            padding:10px;
            margin-bottom:15px;
            border-radius:5px;
            color:white;
            background-color:<%= success != null && success ? "#28a745" : "#dc3545" %>;">
            <%= message %> : 
            <%= messageDb %>
        </div>
        <%
            // FLASH MESSAGE → xoa sau khi hien thi hoac f5
            sessionObj.removeAttribute("message");
            sessionObj.removeAttribute("success");
            sessionObj.removeAttribute("dbError");
                }
            }
        %>

        
        <!--input dau vao-->
        <form action="<%=request.getContextPath()%>/AddShowTimeServlet"
                method="post">
        <!--cac hang muc-->
       <div class="form-group">
    <label for="movieSelect">Chọn phim</label>
    <select id="movieSelect" name="movieId" class="form-control">
        <%
            List<Movie> movieList = (List<Movie>) request.getAttribute("movieList");
            if (movieList != null) {
                for (Movie m : movieList) {
        %>
        <option value="<%= m.getMovieId() %>">
            <%= m.getMovieId() %> - <%= m.getTitle() %>
        </option>
        <%
                }
            }
        %>
        </select>
    </div>
    <!--cac thuoc tinh con lai--> 
    <!--phong chieu-->
    <div class="form-group">
    <label for="movieSelect">Chọn phòng chiếu:</label>
    <select id="movieSelect" name="hallId" class="form-control">
        <%
            List<CinemaHall> hallList = (List<CinemaHall>) request.getAttribute("hallList");
            if (hallList != null) {
                for (CinemaHall m : hallList) {
        %>
        <option value="<%= m.getHallId() %>">
            <%= m.getHallId() %> - <%= m.getHallName() %>
        </option>
        <%
                }
            }
        %>
    </select>
    </div>
    <div class="form-group">
    <label for="movieSelect">Chọn giờ chiếu:</label>
    <select id="movieSelect" name="timeSlotId" class="form-control">
        <%
            List<TimeSlot> listChieu = (List<TimeSlot>) request.getAttribute("listChieu");
            if (listChieu != null) {
                for (TimeSlot gioChieu : listChieu) {
        %>
        <option value="<%= gioChieu.getSlotId() %>">
            <%= gioChieu.getSlotId() %> - <%= gioChieu.getSlotName()%> - <%= gioChieu.getStartHour()%> 
        </option>
        <%
                }
            }
        %>
    </select>
    </div>
    <div class="form-group">
        Chọn ngày chiếu: <input type="date" name="showDate">
    </div>
    <button type="submit" name="Thêm">
</form>
</div>            
    <a href="${pageContext.request.contextPath}/home" class="btn btn-success">
        Quay lại trang chủ
    </a>
</body>
</html>
