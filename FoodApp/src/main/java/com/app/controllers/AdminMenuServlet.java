package com.app.controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.app.dao.MenuItemDAO;
import com.app.dao_implementation.MenuItemDAOImpl;
import com.app.models.MenuItem;



@WebServlet("/admin/menu")
public class AdminMenuServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MenuItemDAO menuItemDAO = new MenuItemDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {

            case "add":  // Show Add Form
                showAddForm(request, response);
                break;

            case "edit": // Show Edit Form
                showEditForm(request, response);
                break;

            case "delete": // Delete menu item
                deleteMenuItem(request, response);
                break;

            default: // List all menu items for a restaurant
                listMenuItems(request, response);
                break;
        }
    }

    // ------------------------- LIST MENU ITEMS -------------------------
    private void listMenuItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
        List<MenuItem> menuItems = menuItemDAO.getMenuItemsByRestaurantId(restaurantId);

        request.setAttribute("restaurantId", restaurantId);
        request.setAttribute("menuItems", menuItems);

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/jsp/admin/menu/listMenuItems.jsp");
        rd.forward(request, response);
    }

    // ------------------------- SHOW ADD FORM -------------------------
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
        request.setAttribute("restaurantId", restaurantId);

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/jsp/admin/menu/addMenuItem.jsp");
        rd.forward(request, response);
    }

    // ------------------------- SHOW EDIT FORM -------------------------
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int menuItemId = Integer.parseInt(request.getParameter("id"));
        MenuItem menuItem = menuItemDAO.getMenuItemById(menuItemId);

        request.setAttribute("menuItem", menuItem);

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/jsp/admin/menu/editMenuItem.jsp");
        rd.forward(request, response);
    }

    // ------------------------- DELETE MENU ITEM -------------------------
    private void deleteMenuItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int menuItemId = Integer.parseInt(request.getParameter("id"));
        MenuItem item = menuItemDAO.getMenuItemById(menuItemId);

        int restaurantId = item.getRestaurantId();

        menuItemDAO.deleteMenuItem(menuItemId);

        response.sendRedirect(
                request.getContextPath() + "/admin/menu?action=list&restaurantId=" + restaurantId);
    }

    // ------------------------- POST (SAVE/UPDATE) -------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("save".equals(action)) {
            saveMenuItem(request, response);
        } else if ("update".equals(action)) {
            updateMenuItem(request, response);
        }
    }

    // ------------------------- SAVE NEW MENU ITEM -------------------------
    private void saveMenuItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        MenuItem menuItem = new MenuItem();

        menuItem.setRestaurantId(Integer.parseInt(request.getParameter("restaurantId")));
        menuItem.setName(request.getParameter("name"));
        menuItem.setDescription(request.getParameter("description"));
        menuItem.setPrice(Double.parseDouble(request.getParameter("price")));
        menuItem.setCategory(request.getParameter("category"));
        menuItem.setVeg("veg".equalsIgnoreCase(request.getParameter("isVeg")));
        menuItem.setAvailable("available".equalsIgnoreCase(request.getParameter("isAvailable")));
        menuItem.setImageUrl(request.getParameter("imageUrl"));

        menuItemDAO.addMenuItem(menuItem);

        response.sendRedirect(
                request.getContextPath() + "/admin/menu?action=list&restaurantId=" + menuItem.getRestaurantId());
    }

    // ------------------------- UPDATE MENU ITEM -------------------------
    private void updateMenuItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        MenuItem menuItem = new MenuItem();

        menuItem.setMenuItemId(Integer.parseInt(request.getParameter("menuItemId")));
        menuItem.setRestaurantId(Integer.parseInt(request.getParameter("restaurantId")));
        menuItem.setName(request.getParameter("name"));
        menuItem.setDescription(request.getParameter("description"));
        menuItem.setPrice(Double.parseDouble(request.getParameter("price")));
        menuItem.setCategory(request.getParameter("category"));
        menuItem.setVeg("veg".equalsIgnoreCase(request.getParameter("isVeg")));
        menuItem.setAvailable("available".equalsIgnoreCase(request.getParameter("isAvailable")));
        menuItem.setImageUrl(request.getParameter("imageUrl"));

        menuItemDAO.updateMenuItem(menuItem);

        response.sendRedirect(
                request.getContextPath() + "/admin/menu?action=list&restaurantId=" + menuItem.getRestaurantId());
    }
}


/*
===========================================================
 AdminMenuServlet – JSP Mappings (For Future Reference)
===========================================================

URL MAPPINGS:
----------------------------------------------
GET  /admin/menu?action=list&restaurantId={id}
    → /WEB-INF/jsp/admin/menu/listMenuItems.jsp

GET  /admin/menu?action=add&restaurantId={id}
    → /WEB-INF/jsp/admin/menu/addMenuItem.jsp

GET  /admin/menu?action=edit&id={menuItemId}
    → /WEB-INF/jsp/admin/menu/editMenuItem.jsp

GET  /admin/menu?action=delete&id={menuItemId}
    → (No JSP — Performs delete, then redirects to list)

POST /admin/menu?action=save
    → After saving → redirect to /admin/menu?action=list&restaurantId={id}

POST /admin/menu?action=update
    → After updating → redirect to /admin/menu?action=list&restaurantId={id}


REQUIRED JSP FILES (YOU MUST CREATE THESE):
----------------------------------------------
📁 /WEB-INF/jsp/admin/menu/listMenuItems.jsp
📁 /WEB-INF/jsp/admin/menu/addMenuItem.jsp
📁 /WEB-INF/jsp/admin/menu/editMenuItem.jsp



*/