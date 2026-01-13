<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <header class="p-3 bg-dark text-white">
            <div class="container">
                <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">

                    <!-- Logo -->
                    <a href="${pageContext.request.contextPath}/home"
                        class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                        <img src="${pageContext.request.contextPath}/image/logo.png" alt="Logo" width="40" height="32"
                            class="me-2" />
                    </a>

                    <c:choose>

                        <c:when test="${sessionScope.user != null && sessionScope.user.roleId == 'Admin'}">
                            <jsp:include page="/layout/nav-admin.jsp" />
                        </c:when>

                        <c:otherwise>
                            <jsp:include page="/layout/nav-user.jsp" />
                        </c:otherwise>

                    </c:choose>

                </div>
            </div>
        </header>