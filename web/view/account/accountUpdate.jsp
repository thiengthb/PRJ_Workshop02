<%-- 
    Document   : accountUpdate
    Created on : Mar 15, 2025, 1:33:37 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập Nhật Vai Trò Tài Khoản</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .form-group label { font-weight: bold; }
    </style>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Cập Nhật Vai Trò Tài Khoản</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <c:if test="${sessionScope.user.roleInSystem != 1}">
            <div class="alert alert-warning mt-3">Bạn không có quyền truy cập trang này.</div>
            <% response.sendRedirect(request.getContextPath() + "/account?action=profileUpdate"); %>
        </c:if>
        <c:if test="${sessionScope.user.roleInSystem == 1}">
            <form action="${pageContext.request.contextPath}/account?action=update" method="post" class="mt-4">
                <input type="hidden" name="accountId" value="${account.account}">
                <div class="form-group">
                    <label for="account">Tên Tài Khoản:</label>
                    <input type="text" class="form-control" id="account" name="account" value="${account.account}" readonly>
                </div>
                <div class="form-group">
                    <label for="role">Vai Trò Chi Tiết:</label>
                    <input type="text" class="form-control" id="role" name="role" value="${account.role}">
                </div>
                <div class="form-group">
                    <label for="roleInSystem">Vai Trò Hệ Thống:</label>
                    <select class="form-control" id="roleInSystem" name="roleInSystem" 
                            ${account.account == sessionScope.user.account ? 'disabled' : ''}>
                        <option value="1" ${account.roleInSystem == 1 ? 'selected' : ''}>Quản Trị Viên</option>
                        <option value="2" ${account.roleInSystem == 2 ? 'selected' : ''}>Quản Lý</option>
                        <option value="3" ${account.roleInSystem == 3 ? 'selected' : ''}>Khách Hàng</option>
                    </select>
                    <c:if test="${account.account == sessionScope.user.account}">
                        <small class="form-text text-muted">Không thể thay đổi vai trò của tài khoản đang đăng nhập.</small>
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary">Cập Nhật</button>
                <a href="${pageContext.request.contextPath}/account?action=list" class="btn btn-secondary">Hủy</a>
            </form>
        </c:if>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>