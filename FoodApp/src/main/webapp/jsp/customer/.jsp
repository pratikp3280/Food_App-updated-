<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="pageTitle" value="My Orders"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/jsp/shared/head.jspf" %>
</head>
<body>

<%@ include file="/jsp/shared/header.jspf" %>

<main class="main-content">
    <div class="container">

        <!-- PAGE HEADING -->
        <div class="mb-4">
            <h1 class="page-title">My Orders</h1>
            <p style="color: var(--medium-text);">
                View all orders you’ve placed
            </p>
        </div>

        <!-- EMPTY STATE -->
        <c:if test="${empty orders}">
            <div class="card text-center p-3">
                <h3 class="mb-2">No orders found 🍽️</h3>
                <p class="mb-3" style="color: var(--light-text);">
                    You haven’t placed any orders yet.
                </p>
                <a href="${pageContext.request.contextPath}/restaurantList"
                   class="btn btn--primary">
                    Browse Restaurants
                </a>
            </div>
        </c:if>

        <!-- ORDERS LIST -->
        <c:if test="${not empty orders}">

            <div class="mb-3">
                <strong>${fn:length(orders)}</strong> order(s)
            </div>

            <c:forEach var="order" items="${orders}">
                <div class="card mb-3">
                    <div class="card__body">

                        <!-- ORDER BASIC INFO -->
                        <div class="grid-2 mb-3">

                            <div>
                                <p class="mb-1">
                                    <strong>Order ID:</strong> #${order.orderId}
                                </p>

                                <p class="mb-1" style="font-size:0.9rem;color:var(--light-text);">
                                    <fmt:formatDate value="${order.createdAt}"
                                                    pattern="dd MMM yyyy, hh:mm a"/>
                                </p>

                                <p class="mb-1">
                                    <strong>Payment:</strong> ${order.paymentMethod}
                                </p>
                            </div>

                            <div style="text-align:right">
                                <p class="mb-1">
                                    <strong>
                                        ₹ <fmt:formatNumber value="${order.totalAmount}"
                                                           type="number"
                                                           maxFractionDigits="2"/>
                                    </strong>
                                </p>

                                <!-- STATUS -->
                                <c:choose>
                                    <c:when test="${order.status eq 'placed'}">
                                        <span style="color:var(--accent-green);font-weight:600;">
                                            Placed
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status eq 'cancelled'}">
                                        <span style="color:var(--accent-red);font-weight:600;">
                                            Cancelled
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color:var(--accent-blue);font-weight:600;">
                                            ${order.status}
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                        </div>

                        <!-- ORDER ITEMS -->
                        <div class="mb-3" style="color:var(--medium-text);font-size:0.9rem;">
                            <c:forEach var="item"
                                       items="${orderItemsMap[order.orderId]}"
                                       varStatus="loop">
                                Item #${item.menuItemId} × ${item.quantity}
                                <c:if test="${not loop.last}">, </c:if>
                            </c:forEach>
                        </div>

                        <!-- ACTION BUTTONS -->
                        <div style="display:flex;gap:12px;flex-wrap:wrap;">
                            <a href="${pageContext.request.contextPath}/orderSummary?orderId=${order.orderId}"
                               class="btn btn--secondary">
                                View Details
                            </a>

                            <c:if test="${order.status eq 'placed'}">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/orders">
                                    <input type="hidden" name="action" value="cancel">
                                    <input type="hidden" name="orderId" value="${order.orderId}">
                                    <button type="submit"
                                            class="btn btn--success"
                                            onclick="return confirm('Cancel this order?');">
                                        Cancel Order
                                    </button>
                                </form>
                            </c:if>
                        </div>

                    </div>
                </div>
            </c:forEach>

        </c:if>
    </div>
</main>

<%@ include file="/jsp/shared/footer.jspf" %>

</body>
</html>
