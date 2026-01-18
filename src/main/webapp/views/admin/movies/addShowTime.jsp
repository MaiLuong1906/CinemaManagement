<%-- Document : addShowTime Created on : Jan 17, 2026, 8:38:04 PM Author : nguye
--%> <%@page import="java.time.LocalTime"%>
<%@page import="model.CinemaHall"%>
<%@page import="model.Movie"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>JSP Page</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/css/for_addShowTime.css">
    </head>
    <body>
        <div>
            <h2>Them lich chieu</h2>
        <form action="<%=request.getContextPath()%>/AddShowTimeServlet"
                method="post">
        <!--cac hang muc-->
       <div class="form-group">
    <label for="movieSelect">Chon phim</label>
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
    <label for="movieSelect">Chon phong chieu</label>
    <select id="movieSelect" name="hallId" class="form-control">
        <%
            List<CinemaHall> hallList = (List<CinemaHall>) request.getAttribute("hallList");
            if (movieList != null) {
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
    <!--gio chieu-->
    <div class="form-group">
    <label for="movieSelect">Chon gio chieu</label>
    <select id="movieSelect" name="hallId" class="form-control">
        <%
            List<LocalTime> listChieu = (List<LocalTime>) request.getAttribute("listChieu");
            if (movieList != null) {
                for (LocalTime gioChieu : listChieu) {
        %>
        <option value="<%= gioChieu %>">
            <%= gioChieu %> 
        </option>
        <%
                }
            }
        %>
    </select>
    </div>
    <div class="form-group">
        Chon ngay chieu : <input type="date" name="showDate">
    </div>
    <div class="form-group">
        Gia goc: <input type="number" name="basePrice" placeholder="Nhap gia">
    </div>
    <input type="submit" name="Them">
</form>
</div>
            
    <a href="${pageContext.request.contextPath}/home" class="btn btn-success">
        Quay ve trang chu
    </a>
</body>
</html>
