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
        .card { transition: transform 0.2s; margin-bottom: 20px; width: 100%; height: 400px; display: flex; flex-direction: column; }
        .card:hover { transform: scale(1.05); }
        .card-header { background-color: #343a40; color: white; flex-shrink: 0; }
        .card-body { flex-grow: 1; overflow: hidden; display: flex; flex-direction: column; justify-content: space-between; }
        .card-img-top { width: 100%; height: 200px; object-fit: cover; }
        .card-footer { flex-shrink: 0; }
        .btn-group { display: flex; gap: 5px; }
        .filter-section { max-width: 800px; margin: 0 auto; }
    </style>
</head>
<body>
    <jsp:include page="/view/common/navbar.jsp"/>
    
    <div class="container">
        <h2 class="mt-5 text-center">Danh Sách Sản Phẩm</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mt-3">${error}</div>
        </c:if>

        <!-- Filter Section -->
        <div class="filter-section mt-3 mb-4">
            <form action="${pageContext.request.contextPath}/product?action=list" method="get" class="form-inline justify-content-center">
                <input type="hidden" name="action" value="list">

                <!-- Category Filter -->
                <div class="form-group mx-2">
                    <label for="typeId" class="mr-2">Danh mục:</label>
                    <select name="typeId" id="typeId" class="form-control">
                        <option value="all" ${selectedTypeId == null || selectedTypeId == 'all' ? 'selected' : ''}>Tất Cả</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.typeId}" ${selectedTypeId == category.typeId ? 'selected' : ''}>${category.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Price Range Filter -->
                <div class="form-group mx-2">
                    <label for="minPrice" class="mr-2">Giá từ:</label>
                    <input type="number" name="minPrice" id="minPrice" class="form-control" value="${param.minPrice}" placeholder="0" min="0">
                </div>
                <div class="form-group mx-2">
                    <label for="maxPrice" class="mr-2">Đến:</label>
                    <input type="number" name="maxPrice" id="maxPrice" class="form-control" value="${param.maxPrice}" placeholder="∞" min="0">
                </div>

                <!-- Discount Filter -->
                <div class="form-group mx-2">
                    <label for="discount" class="mr-2">Giảm giá:</label>
                    <select name="discount" id="discount" class="form-control">
                        <option value="all" ${param.discount == null || param.discount == 'all' ? 'selected' : ''}>Tất cả</option>
                        <option value="yes" ${param.discount == 'yes' ? 'selected' : ''}>Có giảm giá</option>
                        <option value="no" ${param.discount == 'no' ? 'selected' : ''}>Không giảm giá</option>
                    </select>
                </div>

                <!-- Sort by Price -->
                <div class="form-group mx-2">
                    <label for="sort" class="mr-2">Sắp xếp:</label>
                    <select name="sort" id="sort" class="form-control">
                        <option value="default" ${param.sort == null || param.sort == 'default' ? 'selected' : ''}>Mặc định</option>
                        <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá tăng dần</option>
                        <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá giảm dần</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary mx-2">Lọc</button>
            </form>
        </div>

        <!-- Add New Product Button -->
        <c:if test="${sessionScope.user.roleInSystem == 1 || sessionScope.user.roleInSystem == 2}">
            <div class="text-center mb-4">
                <a href="${pageContext.request.contextPath}/product?action=create" class="btn btn-primary">Tạo Sản Phẩm Mới</a>
            </div>
        </c:if>
        
        <div class="row mt-4">
            <c:forEach var="product" items="${products}">
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            Tên Sản Phẩm: ${product.productName}
                        </div>
                        <div class="card-body">
                            <img src="${pageContext.request.contextPath}/${product.productImage}" class="card-img-top" alt="${product.productName}">
                            <p class="card-text mt-2">Giá: ${product.price} VNĐ 
                                <c:if test="${product.discount > 0}"><br><small>(Giảm: ${product.discount}%)</small></c:if>
                            </p>
                        </div>
                        <div class="card-footer">
                            <div class="btn-group">
                                <a href="${pageContext.request.contextPath}/product?action=view&productId=${product.productId}" class="btn btn-info btn-sm">Xem</a>
                                <c:if test="${sessionScope.user.roleInSystem == 1 || sessionScope.user.roleInSystem == 2}">
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
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>