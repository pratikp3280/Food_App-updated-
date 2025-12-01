<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="shared/head.jspf" %>
<%@ include file="shared/header.jspf" %>

<div class="container main-content">
  <div class="card">
    <h2 class="page-title">My Profile</h2>

    <form method="post" action="${pageContext.request.contextPath}/user">
      <input type="hidden" name="action" value="profileUpdate"/>

      <label>Name</label>
      <input class="form-control" name="name" value="${sessionScope.loggedUser.name}"/>

      <label>Email</label>
      <input class="form-control" name="email" value="${sessionScope.loggedUser.email}"/>

      <label>Phone</label>
      <input class="form-control" name="phone" value="${sessionScope.loggedUser.phone}"/>

      <label>Address</label>
      <input class="form-control" name="address" value="${sessionScope.loggedUser.address}"/>

      <label>New Password</label>
      <input class="form-control" type="password" name="newPassword"/>

      <button class="btn btn--primary mt-3">Update Profile</button>
    </form>
  </div>
</div>

<%@ include file="shared/footer.jspf" %>
