<%-- 
    Document   : productList
    Created on : Mar 15, 2025, 1:36:00 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Sản Phẩm</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .card {
            transition: transform 0.2s;
            margin-bottom: 20px;
        }
        .card:hover {
            transform: scale(1.05);
        }
        .card-header {
            background-color: #343a40;
            color: white;
        }
        .btn-group {
            display: flex;
            gap: 5px;
        }
    </style>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Danh Sách Sản Phẩm</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        
        <div class="row mt-4">
            <c:forEach var="product" items="${products}">
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            Mã Sản Phẩm: ${product.productId}
                        </div>
                        <div class="card-body">
                            <h5 class="card-title">${product.productName}</h5>
                            <p class="card-text">Giá: ${product.price} VNĐ</p>
                        </div>
                        <div class="card-footer">
                            <div class="btn-group">
                                <a href="${pageContext.request.contextPath}/product?action=view&productId=${product.productId}" class="btn btn-info btn-sm">Xem</a>
                                <c:if test="${sessionScope.user.roleInSystem}">
                                    <a href="${pageContext.request.contextPath}/product?action=update&productId=${product.productId}" class="btn btn-warning btn-sm">Sửa</a>
                                    <form action="${pageContext.request.contextPath}/product?action=delete" method="post" style="display:inline;">
                                        <input type="hidden" name="productId" value="${product.productId}">
                                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này không?')">Xóa</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <c:if test="${sessionScope.user.roleInSystem}">
            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/product?action=create" class="btn btn-primary">Tạo Sản Phẩm Mới</a>
            </div>
        </c:if>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>