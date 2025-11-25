<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="WEB-INF/jsp/shared/head.jspf" %>
<%@ include file="WEB-INF/jsp/shared/header.jspf" %>

<!-- MAIN LANDING PAGE -->
<div class="main-content">

    <!-- HERO SECTION -->
    <section class="hero">
        <div class="container hero-content">

            <!-- LEFT TEXT -->
            <div class="hero-text">
                <h1 class="hero-title">Hungry? Order Your Favorite Food Now.</h1>
                <p class="hero-subtitle">
                    Fresh meals delivered from top restaurants near you.
                </p>

                <div class="hero-buttons">
                    <a href="UserServlet?action=loginPage" class="btn btn--primary">Start Ordering</a>
                    <a href="UserServlet?action=registerPage" class="btn btn--secondary">View Offers</a>
                </div>
            </div>

            <!-- RIGHT IMAGE -->
            <div class="hero-image">
                <img src="${pageContext.request.contextPath}/assets/images/food-hero.jpg" 
                     alt="Delicious Food Banner">
            </div>
        </div>
    </section>


    <!-- FEATURES SECTION -->
    <section class="features-section mt-3">
        <div class="container">
            <h2 class="page-title">What We Offer</h2>

            <div class="grid-4">

                <!-- Restaurants -->
                <div class="card">
                    <img class="banner-img" 
                         src="${pageContext.request.contextPath}/assets/images/feature-restaurants.jpg">
                    <h3 class="mt-2">Top Restaurants</h3>
                    <p>Find the best rated restaurants nearby.</p>
                </div>

                <!-- Dishes -->
                <div class="card">
                    <img class="banner-img" 
                         src="${pageContext.request.contextPath}/assets/images/feature-dishes.jpg">
                    <h3 class="mt-2">Popular Dishes</h3>
                    <p>Order from a wide range of trending dishes.</p>
                </div>

                <!-- Cuisines -->
                <div class="card">
                    <img class="banner-img" 
                         src="${pageContext.request.contextPath}/assets/images/feature-cuisines.jpg">
                    <h3 class="mt-2">Variety of Cuisines</h3>
                    <p>Indian, Chinese, Italian and many more.</p>
                </div>

                <!-- Offers -->
                <div class="card">
                    <img class="banner-img" 
                         src="${pageContext.request.contextPath}/assets/images/feature-offers.jpg">
                    <h3 class="mt-2">Best Offers</h3>
                    <p>Heavy discounts & exclusive daily deals.</p>
                </div>

            </div>
        </div>
    </section>

</div>

<%@ include file="WEB-INF/jsp/shared/footer.jspf" %>
