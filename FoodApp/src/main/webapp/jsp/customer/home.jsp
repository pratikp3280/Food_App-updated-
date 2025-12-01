<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../shared/head.jspf" %>
<%@ include file="../shared/header.jspf" %>

<div class="container main-content">
  <h2 class="page-title">Welcome, ${sessionScope.loggedUser.name}</h2>
  <div class="grid-3">
    <a class="card" href="${pageContext.request.contextPath}/restaurantList">Browse Restaurants</a>
    <a class="card" href="${pageContext.request.contextPath}/cart">My Cart</a>
    <a class="card" href="${pageContext.request.contextPath}/orders">My Orders</a>
  </div>
</div>

<%@ include file="../shared/footer.jspf" %>
