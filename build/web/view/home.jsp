<%-- 
    Document   : home
    Created on : Mar 15, 2025, 3:10:06 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>

    <div class="container">
        <div class="hero mt-4">
            <h1>Chào Mừng Đến Với MyApp</h1>
            <p>
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        Vui lòng đăng nhập hoặc đăng ký để bắt đầu!
                    </c:when>
                    <c:otherwise>
                        Xin chào, <c:out value="${sessionScope.user.firstName} ${sessionScope.user.lastName}"/>! Khám phá các tính năng bên dưới.
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <!-- Feature Cards -->
        <div class="row mt-5">
            <div class="col-md-4">
                <div class="card mb-4">
                    <div class="card-body text-center">
                        <h5 class="card-title">Duyệt Sản Phẩm</h5>
                        <p class="card-text">Xem danh sách sản phẩm đa dạng của chúng tôi.</p>
                        <a href="${pageContext.request.contextPath}/product?action=list" class="btn btn-primary">Đi Đến Sản Phẩm</a>
                    </div>
                </div>
            </div>
            <%-- Show Categories card only for admin (1) or manager (2) --%>
            <c:if test="${not empty sessionScope.user && (sessionScope.user.roleInSystem == 1 || sessionScope.user.roleInSystem == 2)}">
                <div class="col-md-4">
                    <div class="card mb-4">
                        <div class="card-body text-center">
                            <h5 class="card-title">Khám Phá Danh Mục</h5>
                            <p class="card-text">Xem các danh mục sản phẩm của chúng tôi.</p>
                            <a href="${pageContext.request.contextPath}/category?action=list" class="btn btn-primary">Đi Đến Danh Mục</a>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${sessionScope.user.roleInSystem == 1}">
                <div class="col-md-4">
                    <div class="card mb-4">
                        <div class="card-body text-center">
                            <h5 class="card-title">Quản Lý Tài Khoản</h5>
                            <p class="card-text">Quản trị tài khoản người dùng.</p>
                            <a href="${pageContext.request.contextPath}/account?action=list" class="btn btn-primary">Đi Đến Tài Khoản</a>
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