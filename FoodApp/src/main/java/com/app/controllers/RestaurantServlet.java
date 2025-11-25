package com.app.controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.app.dao.RestaurantDAO;
import com.app.dao_implementation.RestaurantDAOImpl;
import com.app.models.Restaurant;

/*
 * This RestaurantServlet controls:
 *
 * ✔ Listing all restaurants
 * ✔ Viewing a single restaurant's details
 * ✔ Redirecting/Forwarding to restaurant JSP pages
 *
 * Pattern used:
 * - doGet → page display (list, view)
 * - doPost → not used (no form submission yet)
 */

@WebServlet("/restaurant")
public class RestaurantServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // DAO object for database operations
    private RestaurantDAO restaurantDAO;

    @Override
    public void init() throws ServletException {
        // Initialize DAO once when servlet starts
        restaurantDAO = new RestaurantDAOImpl();
    }

    // ----------------------------------------------------
    // GET Requests (display pages)
    // ----------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Determine which action user wants (default = list restaurants)
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {

            case "view":    // Show a single restaurant details
                showRestaurantDetails(request, response);
                break;

            case "list":    // Show all restaurants
            default:
                listRestaurants(request, response);
                break;
        }
    }

    // ----------------------------------------------------
    // POST Requests (currently unused for restaurants)
    // ----------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // No POST-use cases yet → return error
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    // ----------------------------------------------------
    // List All Restaurants
    // URL → /restaurant?action=list
    // ----------------------------------------------------
    private void listRestaurants(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get list from database
        List<Restaurant> restaurants = restaurantDAO.getAllRestaurants();

        // Send list to JSP
        req.setAttribute("restaurants", restaurants);

        // Forward to the JSP page
        forward(req, resp, "/jsp/restaurantList.jsp");
    }

    // ----------------------------------------------------
    // View Restaurant Details
    // URL → /restaurant?action=view&id=10
    // ----------------------------------------------------
    private void showRestaurantDetails(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");

        // If ID missing → redirect back to list
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/restaurant?action=list");
            return;
        }

        int restaurantId = Integer.parseInt(idParam);

        // Fetch from DB
        Restaurant restaurant = restaurantDAO.getRestaurantById(restaurantId);

        // If not found → show error JSP
        if (restaurant == null) {
            req.setAttribute("errorMessage", "Restaurant not found!");
            forward(req, resp, "/jsp/error.jsp");
            return;
        }

        // Attach info for JSP
        req.setAttribute("restaurant", restaurant);

        // Forward to details page
        forward(req, resp, "/jsp/restaurantDetails.jsp");
    }

    // ----------------------------------------------------
    // Utility Function → forward to JSP
    // ----------------------------------------------------
    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(path);
        rd.forward(req, resp);
    }
}


/*Action	       				URL	                                   Purpose
list (default):	           /restaurant?action=list	        To Show all restaurants
view	      :            /restaurant?action=view&id=3	To Show restaurant details
 * 
 * **/
