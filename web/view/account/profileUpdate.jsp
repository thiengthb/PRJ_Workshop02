<%-- 
    Document   : profileUpdate
    Created on : Mar 19, 2025
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập Nhật Hồ Sơ</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .form-group label { font-weight: bold; }
    </style>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Cập Nhật Hồ Sơ</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <c:if test="${empty sessionScope.user}">
            <div class="alert alert-warning mt-3">Vui lòng đăng nhập để cập nhật hồ sơ.</div>
            <% response.sendRedirect(request.getContextPath() + "/auth?action=login"); %>
        </c:if>
        <c:if test="${not empty sessionScope.user}">
            <form action="${pageContext.request.contextPath}/account?action=profileUpdate" method="post" class="mt-4">
                <input type="hidden" name="accountId" value="${sessionScope.user.account}">
                <div class="form-group">
                    <label for="account">Tên Tài Khoản:</label>
                    <input type="text" class="form-control" id="account" name="account" value="${sessionScope.user.account}" readonly>
                </div>
                <div class="form-group">
                    <label for="firstName">Họ:</label>
                    <input type="text" class="form-control" id="firstName" name="firstName" value="${sessionScope.user.firstName}" required>
                </div>
                <div class="form-group">
                    <label for="lastName">Tên:</label>
                    <input type="text" class="form-control" id="lastName" name="lastName" value="${sessionScope.user.lastName}" required>
                </div>
                <div class="form-group">
                    <label for="password">Mật Khẩu Mới (để trống nếu không thay đổi):</label>
                    <input type="password" class="form-control" id="password" name="password">
                </div>
                <div class="form-group">
                    <label for="birthday">Ngày Sinh:</label>
                    <input type="date" class="form-control" id="birthday" name="birthday" 
                           value='<fmt:formatDate value="${sessionScope.user.birthday}" pattern="yyyy-MM-dd"/>' required>
                </div>
                <div class="form-group">
                    <label>Giới Tính:</label>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="gender" id="male" value="true" 
                               ${sessionScope.user.gender ? 'checked' : ''}>
                        <label class="form-check-label" for="male">Nam</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="gender" id="female" value="false" 
                               ${!sessionScope.user.gender ? 'checked' : ''}>
                        <label class="form-check-label" for="female">Nữ</label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="phone">Số Điện Thoại:</label>
                    <input type="text" class="form-control" id="phone" name="phone" value="${sessionScope.user.phone}">
                </div>
                <button type="submit" class="btn btn-primary">Cập Nhật</button>
                <a href="${pageContext.request.contextPath}/view/home.jsp" class="btn btn-secondary">Hủy</a>
            </form>
        </c:if>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>