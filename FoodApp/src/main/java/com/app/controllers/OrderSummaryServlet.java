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

@WebServlet("/orderSummary")
public class OrderSummaryServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String orderIdParam = req.getParameter("orderId");
        if (orderIdParam == null) {
            resp.sendRedirect("orders");
            return;
        }

        int orderId = Integer.parseInt(orderIdParam);

        Order order = orderDAO.getOrderById(orderId);
        List<OrderItem> items = orderDAO.getOrderItems(orderId);

        req.setAttribute("order", order);
        req.setAttribute("items", items);

        req.getRequestDispatcher("/WEB-INF/jsp/customer/order_summary.jsp")
                .forward(req, resp);
    }
}
