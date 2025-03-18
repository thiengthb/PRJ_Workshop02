<%-- 
    Document   : dashboard
    Created on : Mar 15, 2025, 3:09:57 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" >
</head>
<body>
    <!-- Check if user is admin -->
    <c:if test="${empty sessionScope.user || sessionScope.user.roleInSystem != 1}">
        <c:redirect url="/auth?action=login"/>
    </c:if>

    <div class="d-flex">
        <!-- Sidebar -->
        <div class="sidebar col-md-2">
            <h4 class="text-white text-center">Admin Panel</h4>
            <hr class="bg-white">
            <a href="${pageContext.request.contextPath}/account?action=list">Manage Accounts</a>
            <a href="${pageContext.request.contextPath}/category?action=list">Manage Categories</a>
            <a href="${pageContext.request.contextPath}/product?action=list">Manage Products</a>
            <a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a>
        </div>

        <!-- Main Content -->
        <div class="content col-md-10">
            <h2 class="mt-3">Admin Dashboard</h2>
            <c:if test="${not empty error}">
                <div class="alert alert-danger mt-3">${error}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-success mt-3">${message}</div>
            </c:if>

            <!-- Welcome Card -->
            <div class="welcome-card p-4 mt-4">
                <h3>Welcome, <c:out value="${sessionScope.user.firstName} ${sessionScope.user.lastName}"/>!</h3>
                <p>This is your admin dashboard. Use the sidebar to manage accounts, categories, and products.</p>
            </div>

            <!-- Quick Stats (Optional Placeholder) -->
            <div class="row mt-4">
                <div class="col-md-4">
                    <div class="card text-white bg-primary mb-3">
                        <div class="card-body">
                            <h5 class="card-title">Total Accounts</h5>
                            <p class="card-text">Placeholder: X accounts</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card text-white bg-success mb-3">
                        <div class="card-body">
                            <h5 class="card-title">Total Categories</h5>
                            <p class="card-text">Placeholder: Y categories</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card text-white bg-info mb-3">
                        <div class="card-body">
                            <h5 class="card-title">Total Products</h5>
                            <p class="card-text">Placeholder: Z products</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
