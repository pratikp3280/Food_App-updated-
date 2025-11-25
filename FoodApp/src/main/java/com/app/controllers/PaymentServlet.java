package com.app.controllers;

import com.app.dao.OrderDAO;
import com.app.dao_implementation.OrderDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Ensure user logged in
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String orderIdParam = req.getParameter("orderId");
        if (orderIdParam == null) {
            resp.sendRedirect("restaurantList");
            return;
        }

        req.setAttribute("orderId", orderIdParam);

        req.getRequestDispatcher("/WEB-INF/jsp/customer/payment.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String orderIdParam = req.getParameter("orderId");
        if (orderIdParam == null) {
            resp.sendRedirect("restaurantList");
            return;
        }

        int orderId = Integer.parseInt(orderIdParam);

        // Your DB supports: pending → completed
        orderDAO.updatePaymentStatus(orderId, "completed");

        // After completing payment → restaurant accepts automatically
        orderDAO.updateOrderStatus(orderId, "accepted");

        resp.sendRedirect(req.getContextPath() + "/orderSummary?orderId=" + orderId);
    }
}
