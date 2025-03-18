<%-- 
    Document   : accountList
    Created on : Mar 15, 2025, 1:34:17 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Tài Khoản</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Danh Sách Tài Khoản</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <table class="table table-striped mt-4">
            <thead class="thead-dark">
                <tr>
                    <th>Tên Đăng Nhập</th>
                    <th>Tên</th>
                    <th>Họ</th>
                    <th>Trạng Thái</th>
                    <th>Hành Động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="account" items="${accounts}">
                    <tr>
                        <td>${account.account}</td>
                        <td>${account.firstName}</td>
                        <td>${account.lastName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${account.isUse}">
                                    <span class="badge badge-success">Kích Hoạt</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-danger">Vô Hiệu</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/account?action=view&accountId=${account.account}" class="btn btn-info btn-sm">Xem</a>
                            <c:if test="${sessionScope.user.roleInSystem == 1}">
                                <a href="${pageContext.request.contextPath}/account?action=update&accountId=${account.account}" class="btn btn-warning btn-sm">Sửa</a>
                                <form action="${pageContext.request.contextPath}/account?action=delete" method="post" style="display:inline;">
                                    <input type="hidden" name="accountId" value="${account.account}">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa tài khoản này không?')">Xóa</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/account?action=toggleActive" method="post" style="display:inline;">
                                    <input type="hidden" name="accountId" value="${account.account}">
                                    <c:choose>
                                        <c:when test="${account.isUse}">
                                            <button type="submit" class="btn btn-secondary btn-sm" onclick="return confirm('Bạn có chắc muốn vô hiệu tài khoản này không?')">Vô Hiệu</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" class="btn btn-success btn-sm" onclick="return confirm('Bạn có chắc muốn kích hoạt tài khoản này không?')">Kích Hoạt</button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:if test="${sessionScope.user.roleInSystem == 1}">
            <a href="${pageContext.request.contextPath}/account?action=create" class="btn btn-primary mt-3">Tạo Tài Khoản Mới</a>
        </c:if>
    </div>
</body>
</html>