<%@page import="dao.UserProfileDAO"%> <%@page import="model.UserProfile"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>User Profile</title>

    <!-- Bootstrap -->
    <link href="../../../css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="../../../css/LibraryForProfileUser.css" />
  </head>

  <body>
    <div class="all_information">
      <!-- Image -->
      <div class="images_information">
        <img
          class="infor_img"
          src="../../../images/logo.png"
          alt="User's images"
        />
      </div>

      <!-- Text -->
      <div class="text_information">
        <h1 class="firstHeader">Information</h1>

        <% // DEMO: hard-code ID 
        int id = 1; 
        // int id =Integer.parseInt(request.getParameter("id")); them dong nay neu da co userId phia truoc 
        UserProfileDAO profileDAO = new UserProfileDAO();
        UserProfile user = profileDAO.findByUserId(id); %> <% if (user != null)
        { %> ID: <%= user.getUserId()%> <br />
        Name: <%= user.getFullName() %> <br />
        Birthday: <%= user.getDateOfBirth()%> <br />
        Address: <%= user.getAddress() %> <br />
        <% } else { %>
        <p>User not found!</p>
        <% } %>
        <br />
        <!-- Buttons -->
        <div class="Update">
          <p>
            Update your information:
            <button
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
