package com.app.controllers;

import com.app.dao.RestaurantDAO;
import com.app.dao_implementation.RestaurantDAOImpl;
import com.app.models.Restaurant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/restaurants")
public class AdminRestaurantServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RestaurantDAO restaurantDAO;

    @Override
    public void init() throws ServletException {
        restaurantDAO = new RestaurantDAOImpl();
    }

    // 🔐 Helper: Check admin role
    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && "admin".equals(session.getAttribute("role"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/unauthorized.jsp");
            return;
        }

        String action = req.getParameter("action");

        if (action == null) action = "list";

        switch (action) {

            case "add":
                req.getRequestDispatcher("/WEB-INF/jsp/admin/addRestaurant.jsp").forward(req, resp);
                break;

            case "edit":
                int id = Integer.parseInt(req.getParameter("id"));
                Restaurant r = restaurantDAO.getRestaurantById(id);

                req.setAttribute("restaurant", r);
                req.getRequestDispatcher("/WEB-INF/jsp/admin/editRestaurant.jsp").forward(req, resp);
                break;

            case "delete":
                int delId = Integer.parseInt(req.getParameter("id"));
                restaurantDAO.deleteRestaurant(delId);
                resp.sendRedirect(req.getContextPath() + "/admin/restaurants");
                break;

            default: // list
                List<Restaurant> list = restaurantDAO.getAllRestaurants();
                req.setAttribute("restaurants", list);
                req.getRequestDispatcher("/WEB-INF/jsp/admin/listRestaurants.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/unauthorized.jsp");
            return;
        }

        String action = req.getParameter("action");

        if ("save".equals(action)) {
            Restaurant r = new Restaurant();
            r.setName(req.getParameter("name"));
            r.setDescription(req.getParameter("description"));
            r.setCuisineType(req.getParameter("cuisineType"));
            r.setAddress(req.getParameter("address"));
            r.setCity(req.getParameter("city"));
            r.setState(req.getParameter("state"));
            r.setZip(req.getParameter("zip"));
            r.setPhone(req.getParameter("phone"));
            r.setEmail(req.getParameter("email"));
            r.setRating(Double.parseDouble(req.getParameter("rating")));
            r.setDeliveryTime(Integer.parseInt(req.getParameter("deliveryTime")));
            r.setImageUrl(req.getParameter("imageUrl"));
            r.setActive(Boolean.parseBoolean(req.getParameter("isActive")));

            restaurantDAO.addRestaurant(r);
            resp.sendRedirect(req.getContextPath() + "/admin/restaurants");
        }

        else if ("update".equals(action)) {
            Restaurant r = new Restaurant();
            r.setRestaurantId(Integer.parseInt(req.getParameter("restaurantId")));
            r.setName(req.getParameter("name"));
            r.setDescription(req.getParameter("description"));
            r.setCuisineType(req.getParameter("cuisineType"));
            r.setAddress(req.getParameter("address"));
            r.setCity(req.getParameter("city"));
            r.setState(req.getParameter("state"));
            r.setZip(req.getParameter("zip"));
            r.setPhone(req.getParameter("phone"));
            r.setEmail(req.getParameter("email"));
            r.setRating(Double.parseDouble(req.getParameter("rating")));
            r.setDeliveryTime(Integer.parseInt(req.getParameter("deliveryTime")));
            r.setImageUrl(req.getParameter("imageUrl"));
            r.setActive(Boolean.parseBoolean(req.getParameter("isActive")));

            restaurantDAO.updateRestaurant(r);
            resp.sendRedirect(req.getContextPath() + "/admin/restaurants");
        }
    }
}

/*
===========================================================
 AdminRestaurantServlet – JSP Mappings (For Future Reference)
===========================================================

URL MAPPINGS:
----------------------------------------------
GET  /admin/restaurants?action=list
    → /WEB-INF/jsp/admin/listRestaurants.jsp

GET  /admin/restaurants?action=add
    → /WEB-INF/jsp/admin/addRestaurant.jsp

GET  /admin/restaurants?action=edit&id={id}
    → /WEB-INF/jsp/admin/editRestaurant.jsp

GET  /admin/restaurants?action=delete&id={id}
    → (No JSP — Performs delete, then redirects to list)

POST /admin/restaurants?action=save
    → After saving → redirect to /admin/restaurants

POST /admin/restaurants?action=update
    → After updating → redirect to /admin/restaurants


REQUIRED JSP FILES (YOU MUST CREATE THESE):
----------------------------------------------
📁 /WEB-INF/jsp/admin/listRestaurants.jsp
📁 /WEB-INF/jsp/admin/addRestaurant.jsp
📁 /WEB-INF/jsp/admin/editRestaurant.jsp


*/
