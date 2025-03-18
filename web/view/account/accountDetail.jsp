<%-- 
    Document   : accountDetail
    Created on : Mar 15, 2025, 1:33:01 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Details</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Account Details</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <div class="card mt-4">
            <div class="card-body">
                <p><strong>Username:</strong> ${account.account}</p>
                <p><strong>First Name:</strong> ${account.firstName}</p>
                <p><strong>Last Name:</strong> ${account.lastName}</p>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/account?action=list" class="btn btn-primary mt-3">Back to List</a>
    </div>
</body>
</html>
