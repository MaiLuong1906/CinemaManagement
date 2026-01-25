<%-- 
    Document   : deleteShowtime
    Created on : Jan 24, 2026, 10:14:29?PM
    Author     : nguye
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="d-flex justify-content-center mt-3">
    <div style="min-width: 400px; max-width: 600px;">
        <c:if test="${not empty sessionScope.flash_success}">
            <div class="alert alert-success text-center">
                ${sessionScope.flash_success}
            </div>
            <c:remove var="flash_success" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.flash_error}">
            <div class="alert alert-danger text-center">
                ${sessionScope.flash_error}
            </div>
            <c:remove var="flash_error" scope="session"/>
        </c:if>
    </div>
</div>
