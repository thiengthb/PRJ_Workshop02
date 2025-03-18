<%-- 
    Document   : navbar
    Created on : Mar 15, 2025, 3:40:16 AM
    Author     : trann
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/view/home.jsp">MyApp</a>
    
    <%-- Check if the current page is an auth page --%>
    <c:set var="currentPage" value="${pageContext.request.servletPath}"/>
    <c:if test="${not fn:contains(currentPage, '/view/auth/')}">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" 
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/product?action=list">Products</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/category?action=list">Categories</a>
                </li>
                <c:if test="${sessionScope.user.roleInSystem == 1}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/account?action=list">Accounts</a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav ml-auto">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=login">Login</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=register">Register</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <span class="nav-link">Welcome, <c:out value="${sessionScope.user.firstName}"/>!</span>
                        </li>
                        <c:if test="${sessionScope.user.roleInSystem}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/view/admin/dashboard.jsp">Admin Dashboard</a>
                            </li>
                        </c:if>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=logout">Logout</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </c:if>
</nav>
