<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Order History</title>
    <style>

        body {
            font-family: Arial, sans-serif;
            background: #f4f4f7;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 85%;
            margin: 30px auto;
        }

        h2 {
            margin-bottom: 20px;
            color: #333;
        }

        .order-card {
            background: white;
            padding: 18px;
            margin-bottom: 20px;
            border-radius: 8px;
            border: 1px solid #ddd;
        }

        .order-header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 12px;
        }

        .order-status {
            font-weight: bold;
            color: #007b5e;
        }

        .order-items {
            margin-top: 10px;
            margin-left: 15px;
        }

        .item-row {
            margin-bottom: 6px;
            padding-bottom: 6px;
            border-bottom: 1px dotted #ccc;
        }

        .summary {
            font-weight: bold;
            margin-top: 10px;
        }

        .view-btn {
            margin-top: 15px;
            display: inline-block;
            padding: 8px 15px;
            background: #ff7b00;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        .view-btn:hover {
            background: #e46f00;
        }

    </style>
</head>

<body>

<div class="container">
    <h2>Your Order History</h2>

    <!-- If no orders -->
    <c:if test="${empty orders}">
        <p>You have not placed any orders yet.</p>
    </c:if>

    <!-- List all orders -->
    <c:forEach var="order" items="${orders}">
        <div class="order-card">

            <div class="order-header">
                <div>
                    <strong>Order #${order.orderId}</strong><br>
                    Date: ${order.createdAt}
                </div>

                <div class="order-status">
                    Status: ${order.status}
                </div>
            </div>

            <div class="order-items">
                <c:forEach var="item" items="${orderItemsMap[order.orderId]}">
                    <div class="item-row">
                        Item ID: ${item.menuItemId}<br>
                        Qty: ${item.quantity}<br>
                        Price: ₹${item.priceAtOrder}
                    </div>
                </c:forEach>
            </div>

            <div class="summary">
                Total Amount: ₹${order.totalAmount}
            </div>

            <a href="orderSummary?orderId=${order.orderId}" class="view-btn">
                View Order Details
            </a>

        </div>
    </c:forEach>

</div>

</body>
</html>
