<%-- 
    Document   : categoryCreate
    Created on : Mar 15, 2025, 1:37:16 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Category</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Create Category</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/category?action=create" method="post" class="mt-4">
            <div class="form-group">
                <label for="categoryName">Category Name:</label>
                <input type="text" class="form-control" id="categoryName" name="categoryName" required>
            </div>
            <div class="form-group">
                <label for="memo">Memo:</label>
                <textarea class="form-control" id="memo" name="memo"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Create</button>
            <a href="${pageContext.request.contextPath}/category?action=list" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
</body>
</html>
