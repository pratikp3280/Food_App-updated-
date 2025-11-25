package com.app.controllers;

import com.app.dao.OrderDAO;
import com.app.dao_implementation.OrderDAOImpl;
import com.app.models.Order;
import com.app.models.OrderItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/orderHistory")
public class OrderHistoryServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // --- 1️⃣ Check user login ---
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        // --- 2️⃣ Fetch all orders placed by this user ---
        List<Order> orderList = orderDAO.getOrdersByUserId(userId);

        // --- 3️⃣ Fetch order-items for each order (optional but useful for JSP summary) ---
        Map<Integer, List<OrderItem>> orderItemsMap = new HashMap<>();

        for (Order order : orderList) {
            int orderId = order.getOrderId();
            List<OrderItem> items = orderDAO.getOrderItems(orderId);
            orderItemsMap.put(orderId, items);
        }

        // --- 4️⃣ Send data to JSP ---
        req.setAttribute("orders", orderList);
        req.setAttribute("orderItemsMap", orderItemsMap);

        req.getRequestDispatcher("/WEB-INF/jsp/customer/order_history.jsp")
                .forward(req, resp);
    }
}
