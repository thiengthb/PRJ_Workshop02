<%-- 
    Document   : categoryDetail
    Created on : Mar 15, 2025, 1:36:33 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category Details</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Category Details</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <div class="card mt-4">
            <div class="card-body">
                <p><strong>ID:</strong> ${category.typeId}</p>
                <p><strong>Name:</strong> ${category.categoryName}</p>
                <p><strong>Memo:</strong> ${category.memo}</p>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/category?action=list" class="btn btn-primary mt-3">Back to List</a>
    </div>
</body>
</html>
