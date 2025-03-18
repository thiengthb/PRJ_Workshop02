<%-- 
    Document   : productDetail
    Created on : Mar 15, 2025, 1:34:42 AM
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
    <title>Chi Tiết Sản Phẩm</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .product-image {
            max-width: 100%;
            height: auto;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Chi Tiết Sản Phẩm</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>
        <div class="card mt-4">
            <div class="card-body">
                <img src="${pageContext.request.contextPath}/${product.productImage}" class="product-image" alt="${product.productName}">
                <p><strong>Mã Sản Phẩm:</strong> ${product.productId}</p>
                <p><strong>Tên Sản Phẩm:</strong> ${product.productName}</p>
                <p><strong>Mô Tả Ngắn:</strong> ${product.brief}</p>
                <p><strong>Ngày Đăng:</strong> <fmt:formatDate value="${product.postedDate}" pattern="dd/MM/yyyy"/></p>
                <p><strong>Loại Sản Phẩm (ID):</strong> ${product.typeId}</p>
                <p><strong>Người Đăng:</strong> ${product.account}</p>
                <p><strong>Đơn Vị:</strong> ${product.unit}</p>
                <p><strong>Giá:</strong> ${product.price} VNĐ</p>
                <p><strong>Giảm Giá:</strong> ${product.discount}%</p>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/product?action=list" class="btn btn-primary mt-3">Quay Lại Danh Sách</a>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>