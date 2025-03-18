<%-- 
    Document   : error
    Created on : Mar 15, 2025, 1:32:17 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    <div class="container">
        <h2 class="mt-5 text-center">Error</h2>
        <div class="alert alert-danger mt-3">
            <c:choose>
                <c:when test="${not empty error}">
                    ${error}
                </c:when>
                <c:otherwise>
                    An unexpected error occurred. Please try again later.
                </c:otherwise>
            </c:choose>
        </div>
        <a href="/view/home.jsp" class="btn btn-primary d-block mx-auto">Back to Home</a>
    </div>
</body>
</html>
