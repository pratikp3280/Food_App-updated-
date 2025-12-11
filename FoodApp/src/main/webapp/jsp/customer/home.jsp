<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Customer Home"/>

<%@ include file="../shared/head.jspf" %>
<%@ include file="../shared/header.jspf" %>

<main class="main-content">
  <div class="container">

    <h2 class="page-title mb-4">
      Welcome, ${sessionScope.loggedUser.name} 👋
    </h2>

    <div class="grid-3">

      <a class="card" href="${pageContext.request.contextPath}/restaurant?action=list">
        <h3>🍽 Browse Restaurants</h3>
        <p class="card__text">Discover restaurants near you</p>
      </a>

      <a class="card" href="${pageContext.request.contextPath}/cart">
        <h3>🛒 My Cart</h3>
        <p class="card__text">View items added to cart</p>
      </a>

      <a class="card" href="${pageContext.request.contextPath}/orderHistory">
        <h3>📦 My Orders</h3>
        <p class="card__text">Track your order history</p>
      </a>

      <a class="card" href="${pageContext.request.contextPath}/jsp/customer/profile.jsp">
        <h3>👤 My Profile</h3>
        <p class="card__text">Manage your account</p>
      </a>

      <a class="card" href="${pageContext.request.contextPath}/jsp/customer/addresses.jsp">
        <h3>📍 My Addresses</h3>
        <p class="card__text">Manage delivery locations</p>
      </a>

    </div>

  </div>
</main>

<%@ include file="../shared/footer.jspf" %>
