<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%@page import="model.Movie"%>
<%@page import="model.MovieGenre"%>

<%
    Movie selectedMovie = (Movie) request.getAttribute("selectedMovie");
    List<Movie> movieList = (List<Movie>) request.getAttribute("movieList");
    List<MovieGenre> genreList = (List<MovieGenre>) request.getAttribute("movieGenreList");
    Integer selectedGenreId = (Integer) request.getAttribute("selectedGenreId");
    String mode = (String) request.getAttribute("mode");
    if (mode == null) mode = "view";
%>

<form action="UpdateMovieServlet" method="get">
    <label>Chọn phim</label>
    <select name="movieId">
        <option value="">-- chọn --</option>
        <% for (Movie m : movieList) { %>
            <option value="<%=m.getMovieId()%>"
                <%= (selectedMovie != null && m.getMovieId() == selectedMovie.getMovieId())
                    ? "selected" : "" %>>
                <%= m.getTitle() %>
            </option>
        <% } %>
    </select>
    <button type="submit">Xem</button>
</form>

<% if (selectedMovie != null) { %>
<hr>

<form action="UpdateMovieServlet" method="post" enctype="multipart/form-data">
    <input type="hidden" name="movieId" value="<%=selectedMovie.getMovieId()%>">

    Tên phim:
    <input type="text" name="title"
           value="<%=selectedMovie.getTitle()%>"
           <%= mode.equals("view") ? "readonly" : "" %>><br>

    Thời lượng:
    <input type="number" name="duration"
           value="<%=selectedMovie.getDuration()%>"
           <%= mode.equals("view") ? "readonly" : "" %>><br>

    Ngày phát hành:
    <input type="date" name="release_date"
           value="<%=selectedMovie.getReleaseDate()%>"
           <%= mode.equals("view") ? "readonly" : "" %>><br>

    Độ tuổi:
    <input type="text" name="age_rating"
           value="<%=selectedMovie.getAgeRating()%>"
           <%= mode.equals("view") ? "readonly" : "" %>><br>

    Mô tả:
    <textarea name="description"
        <%= mode.equals("view") ? "readonly" : "" %>>
        <%=selectedMovie.getDescription()%>
    </textarea><br>

    Thể loại:
    <select name="movieGenreId" <%= mode.equals("view") ? "disabled" : "" %>>
    <% for (MovieGenre g : genreList) { %>
        <option value="<%= g.getGenreId() %>"
            <%= g.getGenreId() == selectedGenreId ? "selected" : "" %>>
            <%= g.getGenreName() %>
        </option>
    <% } %>
</select>


    Poster:
    <input type="hidden" name="oldPoster" value="<%= selectedMovie.getPosterUrl() %>">
<input type="file" name="poster">


    <br><br>

    <% if (mode.equals("view")) { %>
        <a href="UpdateMovieServlet?movieId=<%=selectedMovie.getMovieId()%>&mode=edit">
            <button type="button">Edit</button>
        </a>
    <% } else { %>
        <button type="submit">Update</button>
    <% } %>
</form>
<% } %>
