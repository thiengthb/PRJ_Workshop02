<%-- 
    Document   : home
    Created on : Mar 15, 2025, 3:10:06 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css" >
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>

    <div class="container">
        <div class="hero mt-4">
            <h1>Welcome to MyApp</h1>
            <p>
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        Please login or register to get started!
                    </c:when>
                    <c:otherwise>
                        Hello, <c:out value="${sessionScope.user.firstName} ${sessionScope.user.lastName}"/>! Explore our features below.
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <!-- Feature Cards -->
        <div class="row mt-5">
            <div class="col-md-4">
                <div class="card mb-4">
                    <div class="card-body text-center">
                        <h5 class="card-title">Browse Products</h5>
                        <p class="card-text">Check out our wide range of products.</p>
                        <a href="${pageContext.request.contextPath}/product?action=list" class="btn btn-primary">Go to Products</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card mb-4">
                    <div class="card-body text-center">
                        <h5 class="card-title">Explore Categories</h5>
                        <p class="card-text">View our product categories.</p>
                        <a href="${pageContext.request.contextPath}/category?action=list" class="btn btn-primary">Go to Categories</a>
                    </div>
                </div>
            </div>
            <c:if test="${sessionScope.user.roleInSystem == 1}">
                <div class="col-md-4">
                    <div class="card mb-4">
                        <div class="card-body text-center">
                            <h5 class="card-title">Manage Accounts</h5>
                            <p class="card-text">Administer user accounts.</p>
                            <a href="${pageContext.request.contextPath}/account?action=list" class="btn btn-primary">Go to Accounts</a>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Bootstrap JS and jQuery -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
