<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/view/home.jsp">Ứng Dụng Của Tôi</a>
    <c:set var="currentPage" value="${pageContext.request.servletPath}"/>
    <c:if test="${not fn:contains(currentPage, '/view/auth/')}">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" 
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/product?action=list">Sản Phẩm</a>
                </li>
                <%-- Show Categories link only for admin (1) or manager (2) --%>
                <c:if test="${not empty sessionScope.user && (sessionScope.user.roleInSystem == 1 || sessionScope.user.roleInSystem == 2)}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/category?action=list">Danh Mục</a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.user.roleInSystem == 1}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/account?action=list">Tài Khoản</a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav ml-auto">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=login">Đăng Nhập</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=register">Đăng Ký</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <span class="nav-link">Chào mừng, <c:out value="${sessionScope.user.firstName}"/>!</span>
                        </li>
                        <c:if test="${sessionScope.user.roleInSystem == 1}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/view/admin/dashboard.jsp">Bảng Điều Khiển Quản Trị</a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.user.roleInSystem == 2}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/view/manager/dashboard.jsp">Bảng Điều Khiển Quản Lý</a>
                            </li>
                        </c:if>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=logout">Đăng Xuất</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </c:if>
</nav>