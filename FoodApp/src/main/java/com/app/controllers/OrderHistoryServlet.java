package com.app.controllers;

import com.app.dao.OrderDAO;
import com.app.dao_implementation.OrderDAOImpl;
import com.app.dao.MenuItemDAO;
import com.app.dao_implementation.MenuItemDAOImpl;
import com.app.dao.RestaurantDAO;
import com.app.dao_implementation.RestaurantDAOImpl;
import com.app.models.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/orderHistory")
public class OrderHistoryServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final MenuItemDAO menuItemDAO = new MenuItemDAOImpl();
    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl(); // New DAO

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null) {
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

        int userId = loggedUser.getUserId();

        // 1️⃣ Fetch all orders for this user
        List<Order> orders = orderDAO.getOrdersByUserId(userId);
        req.setAttribute("orders", orders);

        // 2️⃣ Prepare orderItems mapping and collect menuIds
        Map<Integer, List<OrderItem>> orderItemsMap = new HashMap<>();
        Set<Integer> menuIds = new HashSet<>();
        Set<Integer> restaurantIds = new HashSet<>();

        for (Order o : orders) {
            // Collect restaurant IDs for map
            restaurantIds.add(o.getRestaurantId());

            // Fetch order items
            List<OrderItem> items = orderDAO.getOrderItems(o.getOrderId());
            orderItemsMap.put(o.getOrderId(), items);

            for (OrderItem it : items) {
                menuIds.add(it.getMenuItemId());
            }
        }
        req.setAttribute("orderItemsMap", orderItemsMap);

        // 3️⃣ Prepare MenuItem map for JSP (name, price, image)
        Map<Integer, MenuItem> menuItemsMap = menuItemDAO.getMenuItemsAsMap(menuIds);
        req.setAttribute("menuItemsMap", menuItemsMap);

        // 4️⃣ Prepare Restaurant map for JSP (restaurantId → restaurantName)
        Map<Integer, String> restaurantsMap = new HashMap<>();
        for (Integer rid : restaurantIds) {
            Restaurant r = restaurantDAO.getRestaurantById(rid);
            if (r != null) {
                restaurantsMap.put(rid, r.getName());
            }
        }
        req.setAttribute("restaurantsMap", restaurantsMap);

        // 5️⃣ Forward to JSP
        req.getRequestDispatcher("/jsp/customer/orderHistory.jsp").forward(req, resp);
    }
}
