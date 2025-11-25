package com.app.controllers;

import java.io.IOException;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.app.dao.CartDAO;
import com.app.dao.MenuItemDAO;
import com.app.dao.OrderDAO;
import com.app.dao_implementation.CartDAOImpl;
import com.app.dao_implementation.MenuItemDAOImpl;
import com.app.dao_implementation.OrderDAOImpl;

import com.app.models.Cart;
import com.app.models.CartItem;
import com.app.models.MenuItem;
import com.app.models.Order;
import com.app.models.OrderItem;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private CartDAO cartDAO = new CartDAOImpl();
    private MenuItemDAO menuItemDAO = new MenuItemDAOImpl();
    private OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Cart cart = cartDAO.getCartByUserId(userId);
        if (cart == null) {
            resp.sendRedirect("cart?action=view");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartItems(cart.getCartId());
        double total = cartDAO.getCartTotal(cart.getCartId());

        if (cartItems.isEmpty()) {
            resp.sendRedirect("cart?action=view");
            return;
        }

        // Attach cart preview for checkout page
        req.setAttribute("cartItems", cartItems);
        req.setAttribute("totalAmount", total);

        Map<Integer, MenuItem> menuMap = new HashMap<>();
        for (CartItem ci : cartItems) {
            menuMap.put(ci.getMenuItemId(), menuItemDAO.getMenuItemById(ci.getMenuItemId()));
        }
        req.setAttribute("menuItems", menuMap);

        // Correct JSP path
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/customer/checkout.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Cart cart = cartDAO.getCartByUserId(userId);
        if (cart == null) {
            resp.sendRedirect("cart?action=view");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartItems(cart.getCartId());
        if (cartItems.isEmpty()) {
            resp.sendRedirect("cart?action=view");
            return;
        }

        // ------------ READ CHECKOUT FORM VALUES ------------
        int restaurantId = Integer.parseInt(req.getParameter("restaurantId"));
        String paymentMethod = req.getParameter("payment_method");
        String deliveryInstructions = req.getParameter("instructions");

        String addressIdStr = req.getParameter("addressId");
        Integer addressId = (addressIdStr == null || addressIdStr.isEmpty()) ? null : Integer.parseInt(addressIdStr);

        double totalAmount = cartDAO.getCartTotal(cart.getCartId());

        // ------------ CREATE ORDER OBJECT ------------
        Order order = new Order();
        order.setUserId(userId);
        order.setRestaurantId(restaurantId);
        order.setAddressId(addressId);
        order.setTotalAmount(totalAmount);
        order.setStatus("placed");
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("pending");
        order.setDeliveryInstructions(deliveryInstructions);

        // Convert cart items → order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cartItems) {
            MenuItem mi = menuItemDAO.getMenuItemById(ci.getMenuItemId());

            OrderItem oi = new OrderItem();
            oi.setMenuItemId(ci.getMenuItemId());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtOrder(mi.getPrice());

            orderItems.add(oi);
        }

        int orderId = orderDAO.placeOrder(order, orderItems);

        if (orderId <= 0) {
            req.setAttribute("errorMessage", "Order could not be placed due to a system error.");
            req.getRequestDispatcher("/WEB-INF/jsp/customer/checkout.jsp").forward(req, resp);
            return;
        }

        // Clear cart now
        cartDAO.clearCart(cart.getCartId());

        // Redirect to correct JSP
        resp.sendRedirect("orderSuccess?orderId=" + orderId);
    }
}
