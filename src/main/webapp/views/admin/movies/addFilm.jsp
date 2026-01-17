<%-- Document : addFilm Created on : Jan 16, 2026, 12:53:32 PM Author : nguye
--%> <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Them phim</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/for_addFilm.css">

    </head>
    <body>
        <!--xuat thong bao them js an sau 2s-->
        <%
            String message = (String) request.getAttribute("message");
            Boolean success = (Boolean) request.getAttribute("success");
            if (message != null && success != null) {
        %>
        <% if (message != null) {%>
        <div id="notifyBox"
             style="
             position: fixed;
             top: 20px;
             right: 20px;
             padding: 10px 16px;
             border-radius: 6px;
             color: white;
             font-size: 14px;
             background-color: <%= success ? "#2ecc71" : "#e74c3c"%>;
             z-index: 9999;
             ">
            <h2 style="margin:0; color:white;"><%= message%></h2>
        </div>
        <% } %>
        <script>
            setTimeout(() => {
                const box = document.getElementById("notifyBox");
                if (box)
                    box.style.display = "none";
            }, 2000);
        </script>
        <%
            }
        %>

        <!--cac hang muc-->
        <div class="container">
            <h2 class="text-center">Them phim moi</h2>
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
                    <label>Ten phim</label>
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

                <!-- Thoi luong -->
                <div class="form-group">
                    <label>Thoi luong (min)</label>
                    <input type="number" name="duration" class="form-control" required />
                </div>

                <!-- Ngay khoi chieu -->
                <div class="form-group">
                    <label>Ngay khoi chieu</label>
                    <input
                        type="date"
                        name="release_date"
                        class="form-control"
                        required
                        />
                </div>

                <!-- Do tuoi -->
                <div class="form-group">
                    <label>Gioi han tuoi</label>
                    <select name="age_rating" class="form-control">
                        <option value="P">P - Moi lua tuoi</option>
                        <option value="T13">T13</option>
                        <option value="T16">T16</option>
                        <option value="T18">T18</option>
                    </select>
                </div>

                <!-- Mo ta -->
                <div class="form-group">
                    <label>Mo ta phim</label>
                    <textarea name="description" class="form-control" rows="4"></textarea>
                </div>

                <!-- Button -->
                <div class="form-group text-center">
                    <button type="submit" class="btn btn-success">Them phim</button>

                    <a
                        href="<%=request.getContextPath()%>/home"
                        class="btn btn-secondary"
                        >
                        Huy
                    </a>
                </div>
            </form>
        </div>
        <a href="${pageContext.request.contextPath}/home"
           class="btn btn-success">
            Quay ve trang chu
        </a>


    </body>
</html>
