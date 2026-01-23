<%-- Document : addFilm Created on : Jan 16, 2026, 12:53:32 PM Author : nguye
--%> <%@page import="model.MovieGenre"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Them phim</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/for_addFilm.css">

    </head>
    <body>
        <div class="back-home">
            <a href="${pageContext.request.contextPath}/views/user/home.jsp"
               class="btn btn-success">
                Trang chủ
            </a>
        </div>

        <!--tra ket qua-->
        <%
        HttpSession sessionObj = request.getSession(false); // li do false la neu da co session thi lay khong thi tra null, khong tao moi
        if (sessionObj != null) {
            String message = (String) sessionObj.getAttribute("message");
            Boolean success = (Boolean) sessionObj.getAttribute("success");
            String messageDb = (String) sessionObj.getAttribute("dbError");
            if (message != null) {
        %>  
        <div class="message-box
            <%= success != null && success ? "success" : "error" %>">
           <%= message %>
           <%= messageDb != null ? "  " + messageDb : "" %>
       </div>

        <%
            // FLASH MESSAGE → xoa sau khi hien thi hoac f5
            sessionObj.removeAttribute("message");
            sessionObj.removeAttribute("success");
            sessionObj.removeAttribute("dbError");
                }
            }
        %>
        <!--cac hang muc-->
        <div class="container">
            <h2 class="text-center">Thêm phim mới</h2>
            <hr />

            <!-- Form add movie -->
            <form
                action="<%=request.getContextPath()%>/AddMovieServlet"
                method="post"
                enctype="multipart/form-data"
                >
                <!-- khi co file thi phai co enctype="multipart/form-data"de quy dinh cach du lieu duoc dong goi va gui di -->
                <!-- Ten phim -->
                <div class="form-group">
                    <label>Tên phim</label>
                    <input type="text" name="title" class="form-control" required />
                </div>

                <!-- Poster phim -->
                <div class="form-group">
                    <label>Poster phim</label>
                    <input
                        type="file"
                        name="poster"
                        class="form-control"
                        accept="image/*"
                        required
                        />
                </div>
                <!--the loai-->
                <div class="form-group">
                    <label>Chọn thể loại</label>
                    <select " name="movieGenreId" class="form-control">
                        <%
                            List<MovieGenre> movieGenreList = (List<MovieGenre>) request.getAttribute("movieGenreList");
                            if (movieGenreList != null) {
                                for (MovieGenre movieGenre : movieGenreList) {
                        %>
                        <option value="<%= movieGenre.getGenreId() %>">
                            <%= movieGenre.getGenreId() %> - <%= movieGenre.getGenreName() %>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                <!-- Thoi luong -->
                <div class="form-group">
                    <label>Thời lượng (min)</label>
                    <input type="number" name="duration" class="form-control" required />
                </div>

                <!-- Ngay khoi chieu -->
                <div class="form-group">
                    <label>Ngày khởi chiếu</label>
                    <input
                        type="date"
                        name="release_date"
                        class="form-control"
                        required
                        />
                </div>

                <!-- Do tuoi -->
                <div class="form-group">
                    <label>Giới hạn tuổi</label>
                    <select name="age_rating" class="form-control">
                        <option value="P">P - Mọi lứa tuổi</option>
                        <option value="T13">T13</option>
                        <option value="T16">T16</option>
                        <option value="T18">T18</option>
                    </select>
                </div>

                <!-- Mo ta -->
                <div class="form-group">
                    <label>Mô tả phim</label>
                    <textarea name="description" class="form-control" rows="4"></textarea>
                </div>

                <!-- Button -->
                <div class="form-group text-center">
                    <button type="submit" class="btn btn-success">Thêm phim</button>

                    <a
                        href="<%=request.getContextPath()%>/home"
                        class="btn btn-secondary"
                        >
                        Hủy
                    </a>
                </div>
            </form>
        </div>


    </body>
</html>
