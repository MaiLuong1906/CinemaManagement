<%-- Document : view_film Created on : Jan 14, 2026, 2:57:11 PM Author : nguye
--%> <%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.MovieShowtimeDTO" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>JSP Page</title>
    <!--sau 1p se load lai trang 1 lan-->
    <meta http-equiv="refresh" content="60">
    <!-- Bootstrap -->
    <link
      rel="stylesheet"
      href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link rel="stylesheet" href="../../../css/for_view_film_page.css" />
  </head>
  <body>
    <div></div>
    <div class="content_menu">
      <!--content menu-->
      <nav class="navbar bg-primary" role="navigation">
        <div class="container-fluid">
          <!-- Brand and toggle get grouped for better mobile display -->
          <div class="navbar-header">
            <button
              type="button"
              class="navbar-toggle"
              data-toggle="collapse"
              data-target="#bs-example-navbar-collapse-1"
            >
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
          </div>
          <div
            class="collapse navbar-collapse"
            id="bs-example-navbar-collapse-1"
          >
            <ul class="nav navbar-nav">
              <li>
                <div>
                  <a href="../dashboard.jsp">
                    <img
                      class="logo"
                      src="../../../images/logo.png"
                      alt="Logo rạp phim"
                    />
                  </a>
                </div>
              </li>
              <li>
                <a href="../bookings/BookTicket.jsp" class="btn-next"
                  >Gio hang</a
                >
              </li>
              <li>
                <a href="../movies/addFilm.jsp" class="btn-next"
                  >Them phim</a
                >
              </li>
              <li>
                <a href="../movies/updateFilm.jsp" class="btn-next"
                  >Update phim</a
                >
              </li>
              <li>
                <a href="<%= request.getContextPath() %>//addShowTimeServlet" class="btn-next"
                  >Lua chon khung gio</a
                >
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </div>
    <!-- phim  -->
     <!-- phim dang chieu  -->
    <%
        List<MovieShowtimeDTO> listNowShowing = (List<MovieShowtimeDTO>) request.getAttribute("listNowShowing");
        List<MovieShowtimeDTO> listCommingShowing = (List<MovieShowtimeDTO>) request.getAttribute("listCommingShowing");
        List<MovieShowtimeDTO> listImax = (List<MovieShowtimeDTO>) request.getAttribute("listImax");
    %>
    <div class="phimDangChieu">
        <div class="row">
        <div class="col-md-3">Phim dang chieu</div>
        <div class="col-md-3">Phim 1</div>
        <div class="col-md-3">Phim 2</div>
        <div class="col-md-3">Phim 3</div>
    </div>
</div>
<!-- phim chuan bi chieu  -->
    <div class="phimChuanBiChieu">
    <div class="row">
        <div class="col-md-3">Phim chuan bi chieu</div>
        <%
            if (listNowShowing != null) {
                for (MovieShowtimeDTO m : listNowShowing) {
        %>
            <div class="col-md-3">
                <img
                  src="<%= request.getContextPath() %>/image?name=<%= m.getPosterUrl() %>"
                  class="img-responsive"
                />
                <p><%= m.getMovieTitle() %></p>
            </div>
        <%
                }
            } else{
        %>
            <div class="col-md-3">Khong co phim nao</div>
        <%
            }
        %>
    </div>
    </div>
<!-- phim 3d  -->
    <div class="phimDangChieu">
    <div class="row">
        <div class="col-md-3">Phim 3D</div>
        <div class="col-md-3">Phim 1</div>
        <div class="col-md-3">Phim 2</div>
        <div class="col-md-3">Phim 3</div>
    </div>
    </div>
    </div>
  </body>
</html>
