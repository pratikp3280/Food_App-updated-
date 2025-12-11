<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Order Successful" />

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/jsp/shared/head.jspf" %>
</head>

<body>

<%@ include file="/jsp/shared/header.jspf" %>

<main class="main-content">
    <div class="container">

        <!-- ================= SUCCESS HERO ================= -->
        <div class="card text-center mb-5" style="padding:40px">

            <i class="fas fa-check-circle"
               style="font-size:64px;color:var(--accent-green);margin-bottom:16px"></i>

            <h1 class="mb-2">Order Placed Successfully!</h1>

            <p style="color:var(--medium-text);font-size:1.05rem">
                Your delicious food is being prepared 🧑‍🍳
            </p>

            <p class="mt-2"
               style="color:var(--light-text);font-size:0.95rem">
                Estimated delivery in <strong>30–40 mins</strong>
            </p>
        </div>

        <!-- ================= ORDER SNAPSHOT ================= -->
        <div class="card mb-5">
            <div class="card__body">

                <h3 class="mb-3">Order Summary</h3>

                <div class="grid-2">

                    <div>
                        <p class="mb-1">
                            <strong>Order ID:</strong>
                            #${param.orderId}
                        </p>

                        <p class="mb-1">
                            <strong>Status:</strong>
                            <span style="color:var(--accent-green)">Placed</span>
                        </p>
                    </div>

                    <div>
                        <p class="mb-1">
                            <strong>Payment:</strong>
                            Cash on Delivery
                        </p>

                        <p class="mb-1">
                            <strong>Total Amount:</strong>
                            ₹${requestScope.totalAmount != null
                                ? requestScope.totalAmount
                                : "—"}
                        </p>
                    </div>

                </div>

            </div>
        </div>

        <!-- ================= WHAT HAPPENS NEXT ================= -->
        <div class="card mb-5">
            <div class="card__body">

                <h3 class="mb-4">What happens next?</h3>

                <div style="display:flex;justify-content:space-between;
                            text-align:center;font-size:0.95rem">

                    <div style="flex:1;color:var(--accent-green)">
                        ✅<br>
                        Order Placed
                    </div>

                    <div style="flex:1;color:var(--light-text)">
                        🍳<br>
                        Preparing
                    </div>

                    <div style="flex:1;color:var(--light-text)">
                        🚴<br>
                        Out for Delivery
                    </div>

                    <div style="flex:1;color:var(--light-text)">
                        📦<br>
                        Delivered
                    </div>

                </div>

            </div>
        </div>

        <!-- ================= PRIMARY CTA ================= -->
        <div class="mb-4">
            <a href="${pageContext.request.contextPath}/orders"
               class="btn btn--primary"
               style="width:100%;padding:14px;font-size:1.05rem">
                Track Order
            </a>
        </div>

        <!-- ================= SECONDARY CTAs ================= -->
        <div class="grid-2 mb-5">

            <a href="${pageContext.request.contextPath}/restaurant?action=list"
               class="btn btn--secondary">
                Browse Restaurants
            </a>

            <a href="${pageContext.request.contextPath}/orderHistory"
               class="btn btn--secondary">
                View My Orders
            </a>

        </div>

        <!-- ================= SUPPORT NOTE ================= -->
        <p class="text-center"
           style="color:var(--light-text);font-size:0.9rem">
            Need help? Our support team is available 24×7.
        </p>

    </div>
</main>

<%@ include file="/jsp/shared/footer.jspf" %>

</body>
</html>
