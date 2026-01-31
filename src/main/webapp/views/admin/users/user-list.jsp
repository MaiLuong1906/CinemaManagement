<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Admin - User Management</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        .locked {
            color: red;
            font-weight: bold;
        }
        .active {
            color: green;
            font-weight: bold;
        }
    </style>
</head>

<body>

<h2>USER MANAGEMENT</h2>

<table>
    <tr>
        <th>ID</th>
        <th>Phone</th>
        <th>Full Name</th>
        <th>Email</th>
        <th>Role</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>

    <c:forEach var="u" items="${users}">
        <tr>
            <td>${u.accountId}</td>
            <td>${u.phoneNumber}</td>
            <td>${u.fullName}</td>
            <td>${u.email}</td>

            <!-- ROLE -->
            <td>
                <form action="users" method="post">
                    <input type="hidden" name="action" value="role"/>
                    <input type="hidden" name="id" value="${u.accountId}"/>

                    <select name="role" onchange="this.form.submit()">
                        <option value="User" ${u.roleId == 'User' ? 'selected' : ''}>Customer</option>
                        <option value="Staff" ${u.roleId == 'Staff' ? 'selected' : ''}>Staff</option>
                        <option value="Admin" ${u.roleId == 'Admin' ? 'selected' : ''}>Admin</option>
                    </select>
                </form>
            </td>

            <!-- STATUS -->
            <td>
                <c:choose>
                    <c:when test="${u.status}">
                        <span class="active">ACTIVE</span>
                    </c:when>
                    <c:otherwise>
                        <span class="locked">LOCKED</span>
                    </c:otherwise>
                </c:choose>
            </td>

            <!-- ACTION -->
            <td>
                <c:choose>
                    <c:when test="${u.status}">
                        <form action="users" method="post" style="display:inline">
                            <input type="hidden" name="action" value="lock"/>
                            <input type="hidden" name="id" value="${u.accountId}"/>
                            <button type="submit">Lock</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="users" method="post" style="display:inline">
                            <input type="hidden" name="action" value="unlock"/>
                            <input type="hidden" name="id" value="${u.accountId}"/>
                            <button type="submit">Unlock</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>

</table>

</body>
</html>
