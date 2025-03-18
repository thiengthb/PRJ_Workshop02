<%-- 
    Document   : categoryList
    Created on : Mar 15, 2025, 1:37:34 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Category List</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <table class="table table-striped mt-4">
            <thead class="thead-dark">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Memo</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="category" items="${categories}">
                    <tr>
                        <td>${category.typeId}</td>
                        <td>${category.categoryName}</td>
                        <td>${category.memo}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/category?action=view&typeId=${category.typeId}" class="btn btn-info btn-sm">View</a>
                            <c:if test="${sessionScope.user.roleInSystem <= 2}">
                                <a href="${pageContext.request.contextPath}/category?action=update&typeId=${category.typeId}" class="btn btn-warning btn-sm">Edit</a>
                                <form action="${pageContext.request.contextPath}/category?action=delete" method="post" style="display:inline;">
                                    <input type="hidden" name="typeId" value="${category.typeId}">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this category?')">Delete</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:if test="${sessionScope.user.roleInSystem <= 2}">
            <a href="${pageContext.request.contextPath}/category?action=create" class="btn btn-primary mt-3">Create New Category</a>
        </c:if>
    </div>
</body>
</html>
