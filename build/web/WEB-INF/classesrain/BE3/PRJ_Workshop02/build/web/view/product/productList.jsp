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
        .filter-section { max-width: 100%; margin: 20px 0; display: flex; justify-content: space-between; align-items: center; }
        .filter-left { display: flex; align-items: center; gap: 10px; }
        .filter-right { display: flex; align-items: center; gap: 10px; }
        .form-group { margin-bottom: 0; }
        .form-control-sm { width: 80px; } /* Giảm kích thước min/max */
        .select-sm { width: 120px; } /* Giảm kích thước dropdown */
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
        <div class="filter-section">
            <!-- Left Side: Price Range, Discount, Sort -->
            <form action="${pageContext.request.contextPath}/product?action=list" method="get" class="filter-left">
                <input type="hidden" name="action" value="list">

                <!-- Price Range Filter -->
                <div class="form-group">
                    <label for="minPrice" class="mr-1">Giá từ:</label>
                    <input type="number" name="minPrice" id="minPrice" class="form-control form-control-sm" value="${param.minPrice}" placeholder="0" min="0">
                </div>
                <div class="form-group">
                    <label for="maxPrice" class="mr-1">Đến:</label>
                    <input type="number" name="maxPrice" id="maxPrice" class="form-control form-control-sm" value="${param.maxPrice}" placeholder="∞" min="0">
                </div>

                <!-- Discount Filter -->
                <div class="form-group">
                    <label for="discount" class="mr-1">Giảm giá:</label>
                    <select name="discount" id="discount" class="form-control select-sm">
                        <option value="all" ${param.discount == null || param.discount == 'all' ? 'selected' : ''}>Tất cả</option>
                        <option value="yes" ${param.discount == 'yes' ? 'selected' : ''}>Có</option>
                        <option value="no" ${param.discount == 'no' ? 'selected' : ''}>Không</option>
                    </select>
                </div>

                <!-- Sort by Price -->
                <div class="form-group">
                    <label for="sort" class="mr-1">Sắp xếp:</label>
                    <select name="sort" id="sort" class="form-control select-sm">
                        <option value="default" ${param.sort == null || param.sort == 'default' ? 'selected' : ''}>Mặc định</option>
                        <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá tăng</option>
                        <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá giảm</option>
                    </select>
                </div>
            </form>

            <!-- Right Side: Category and Filter Button -->
            <div class="filter-right">
                <div class="form-group">
                    <label for="typeId" class="mr-1">Danh mục:</label>
                    <select name="typeId" id="typeId" class="form-control select-sm" form="filterForm">
                        <option value="all" <c:if test="${selectedTypeId == null || selectedTypeId == 'all'}">selected</c:if>>Tất Cả</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.typeId}" <c:if test="${selectedTypeId != null && selectedTypeId != 'all' && selectedTypeId == category.typeId}">selected</c:if>>${category.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
                        <button type="submit" class="btn btn-primary btn-md" form="filterForm" style="margin-top: 30px">Lọc</button>
            </div>
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

    <!-- Hidden form to handle filter submission -->
    <form id="filterForm" action="${pageContext.request.contextPath}/product?action=list" method="get" style="display: none;">
        <input type="hidden" name="action" value="list">
        <input type="hidden" name="minPrice" value="${param.minPrice}">
        <input type="hidden" name="maxPrice" value="${param.maxPrice}">
        <input type="hidden" name="discount" value="${param.discount}">
        <input type="hidden" name="sort" value="${param.sort}">
        <input type="hidden" name="typeId" id="hiddenTypeId">
    </form>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        // Sync typeId value to hidden form before submission
        document.querySelector('.btn-primary.btn-sm').addEventListener('click', function() {
            const typeId = document.querySelector('#typeId').value;
            document.querySelector('#hiddenTypeId').value = typeId;
            document.querySelector('#filterForm').submit();
        });
    </script>
</body>
</html>