package com.app.controllers;

import com.app.dao.AddressDAO;
import com.app.dao_implementation.AddressDAOImpl;
import com.app.models.Address;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/addresses")
public class AddressServlet extends HttpServlet {

    private final AddressDAO addressDAO = new AddressDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1️⃣ Check Login
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        // 2️⃣ Read Action
        String action = req.getParameter("action");
        String addressIdParam = req.getParameter("addressId");

        // 3️⃣ Load Specific Address for Editing
        if ("edit".equals(action) && addressIdParam != null) {
            int addressId = Integer.parseInt(addressIdParam);
            Address address = addressDAO.getAddressById(addressId);
            req.setAttribute("editAddress", address);
        }

        // 4️⃣ Load All User Addresses
        List<Address> addresses = addressDAO.getAddressesByUserId(userId);
        req.setAttribute("addresses", addresses);

        // 5️⃣ Forward to JSP
        req.getRequestDispatcher("/WEB-INF/jsp/customer/addresses.jsp")
                .forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1️⃣ Check Login
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");

        // 2️⃣ DELETE Address
        if ("delete".equals(action)) {
            int addressId = Integer.parseInt(req.getParameter("addressId"));
            addressDAO.deleteAddress(addressId);
            resp.sendRedirect("addresses");
            return;
        }

        // 3️⃣ READ Common Fields (Add / Update)
        String street = req.getParameter("street");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String zip = req.getParameter("zip");
        String landmark = req.getParameter("landmark");

        // 4️⃣ UPDATE Address
        if ("update".equals(action)) {
            int addressId = Integer.parseInt(req.getParameter("addressId"));

            Address address = new Address();
            address.setAddressId(addressId);
            address.setUserId(userId);
            address.setStreet(street);
            address.setCity(city);
            address.setState(state);
            address.setZip(zip);
            address.setLandmark(landmark);

            addressDAO.updateAddress(address);
            resp.sendRedirect("addresses");
            return;
        }

        // 5️⃣ ADD New Address
        if ("add".equals(action)) {

            Address address = new Address();
            address.setUserId(userId);
            address.setStreet(street);
            address.setCity(city);
            address.setState(state);
            address.setZip(zip);
            address.setLandmark(landmark);

            addressDAO.addAddress(address);
            resp.sendRedirect("addresses");
        }

    }
}
