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
    if (mode == null)
        mode = "view";
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Cập nhật phim</title>
        <link rel="stylesheet" href="css/movie-update.css">
    </head>
    <body>
        <!--        thong bao-->
        <%
            String success = request.getParameter("success");
            String error = (String) request.getAttribute("errorMessage");
        %>
        <% if ("true".equals(success)) { %>
        <div class="msg msg-success">
            Cập nhật phim thành công!
        </div>
        <% } else if (error != null) {%>
        <div class="msg msg-error">
            <%= error%>
        </div>
        <% } %>
        <!-- Back Home -->
        <a href="${pageContext.request.contextPath}/home">
            <button type="button" class="btn-back"> Back to Home</button>
        </a>
        <h2>Cập nhật phim</h2>
        <!-- Form chọn phim -->
        <form action="UpdateMovieServlet" method="get" class="form-box">
            <label>Chọn phim:</label>
            <select name="movieId" required>
                <option value="">-- Chọn phim --</option>
                <% for (Movie m : movieList) {%>
                <option value="<%=m.getMovieId()%>"
                        <%= (selectedMovie != null && m.getMovieId() == selectedMovie.getMovieId())
                                ? "selected" : ""%>>
                    <%= m.getTitle()%>
                </option>
                <% } %>
            </select>
            <button type="submit" class="btn-edit">Xem</button>
        </form>
        <% if (selectedMovie != null) {%>
        <form action="UpdateMovieServlet" method="post" enctype="multipart/form-data" class="form-box">
            <input type="hidden" name="movieId" value="<%=selectedMovie.getMovieId()%>">
            <div class="movie-layout">
                <!-- 60% THÔNG TIN -->
                <div class="movie-info">
                    <table>
                        <tr>
                            <td>Tên phim:</td>
                            <td>
                                <input type="text" name="title"
                                       value="<%=selectedMovie.getTitle()%>"
                                       <%= mode.equals("view") ? "readonly" : ""%>>
                            </td>
                        </tr>
                        <tr>
                            <td>Thời lượng (phút):</td>
                            <td>
                                <input type="number" name="duration"
                                       value="<%=selectedMovie.getDuration()%>"
                                       <%= mode.equals("view") ? "readonly" : ""%>>
                            </td>
                        </tr>
                        <tr>
                            <td>Ngày phát hành:</td>
                            <td>
                                <input type="date" name="release_date"
                                       value="<%=selectedMovie.getReleaseDate()%>"
                                       <%= mode.equals("view") ? "readonly" : ""%>>
                            </td>
                        </tr>
                        <tr>
                            <td>Độ tuổi:</td>
                            <td>
                                <select name="age_rating" <%= mode.equals("view") ? "disabled" : ""%>>
                                    <option value="P"  <%= "P".equals(selectedMovie.getAgeRating()) ? "selected" : ""%>>
                                        P - Mọi lứa tuổi
                                    </option>
                                    <option value="T13" <%= "T13".equals(selectedMovie.getAgeRating()) ? "selected" : ""%>>
                                        T13
                                    </option>
                                    <option value="T16" <%= "T16".equals(selectedMovie.getAgeRating()) ? "selected" : ""%>>
                                        T16
                                    </option>
                                    <option value="T18" <%= "T18".equals(selectedMovie.getAgeRating()) ? "selected" : ""%>>
                                        T18
                                    </option>
                                </select>

                                <%-- đảm bảo gửi giá trị khi view --%>
                                <% if (mode.equals("view")) {%>
                                <input type="hidden" name="age_rating" value="<%= selectedMovie.getAgeRating()%>">
                                <% }%>

                            </td>
                        </tr>
                        <tr>
                            <td>Mô tả:</td>
                            <td>
                                <textarea name="description" rows="5"
                                    <%= mode.equals("view") ? "readonly" : ""%>><%=selectedMovie.getDescription() != null
                                            ? selectedMovie.getDescription().trim()
                                            : ""%></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>Thể loại:</td>
                            <td>
                                <select name="movieGenreId" <%= mode.equals("view") ? "disabled" : ""%>>
                                    <% for (MovieGenre g : genreList) {%>
                                    <option value="<%= g.getGenreId()%>"
                                            <%= g.getGenreId() == selectedGenreId ? "selected" : ""%>>
                                        <%= g.getGenreName()%>
                                    </option>
                                    <% } %>
                                </select>
                                <% if (mode.equals("view")) {%>
                                <input type="hidden" name="movieGenreId" value="<%= selectedGenreId%>">
                                <% } %>
                            </td>
                        </tr>
                        <br><small><span style="font-weight: bolder;">Lưu ý:</span> Để trống nếu không muốn sửa!</small>
                    </table>
                </div>
                <!-- 40% POSTER -->
                <div class="movie-poster">
                    <h3>Poster</h3>
                    <% if (selectedMovie.getPosterUrl() != null && !selectedMovie.getPosterUrl().isEmpty()) {%>
                    <img src="image?name=<%= selectedMovie.getPosterUrl()%>" alt="Poster">
                    <% } else { %>
                    <p>Chưa có poster</p>
                    <% } %>

                    <% if (mode.equals("edit")) { %>
                    <br>
                    <input type="file" name="poster" accept="image/*">
                    <br>
                    <% } %>
                </div>
            </div>
            <!-- BUTTON -->
            <div class="btn-group">
                <% if (mode.equals("view")) {%>
                <a href="UpdateMovieServlet?movieId=<%=selectedMovie.getMovieId()%>&mode=edit">
                    <button type="button" class="btn-edit">Edit</button>
                </a>
                <% } else {%>
                <button type="submit" class="btn-submit">Cập nhật</button>
                <a href="UpdateMovieServlet?movieId=<%=selectedMovie.getMovieId()%>&mode=view">
                    <button type="button" class="btn-cancel">Hủy</button>
                </a>
                <% } %>
            </div>
        </form>
        <% }%>
    </body>
</html>
