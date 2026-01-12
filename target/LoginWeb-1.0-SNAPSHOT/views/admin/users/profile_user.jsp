<%-- Document : profile_user Created on : Jan 12, 2026, 1:37:42 PM Author :
nguye --%> <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>JSP Page</title>
    <!-- Bootstrap -->
    <link href="../../../css/bootstrap.min.css" rel="stylesheet" />
    <!-- duong dan cho css -->
    <link rel="stylesheet" href="../../../css/LibraryForProfileUser.css" />
  </head>
  <body>
    <div class="all_information">
      <!-- chua anh -->
      <div class="images_information">
        <img
          class="infor_img"
          src="../../../images/profile.jpg"
          alt="User's images"
        />
      </div>
      <!-- chua text -->
      <div class="text_information">
        <div><h1 class="firstHeader">Information</h1></div>
        <!-- jsp tai day -->
        <% String username = request.getParameter("username"); UserDAO userDAO =
        new UserDAO(); User user = userDAO.getUserByUsername(); %> User: <%=
        username %> <br />
        Name: <%= user.getFullname() %> <br />
        Email: <%= user.getEmail() %> <br />
        Phone: <%= user.getPhone() %> <br />
        Address: <%= user.getAddress() %> <br />
        <p>This is the profile user page for admin view.</p>
        <br />
        <!-- cac nut tien ich -->
        <div class="Update">
          <p>
            Update your information:
            <button
              type="button"
              class="btn btn-primary"
              onclick="window.location.href='updateInformation.jsp'"
            >
              Update Information
            </button>
          </p>
        </div>
        <div class="Back">
          <p>
            Back to homepage:
            <button
              type="button"
              class="btn btn-primary"
              onclick="window.location.href='homepage.jsp'"
            >
              Back to homepage
            </button>
          </p>
        </div>
      </div>
    </div>
  </body>
</html>
