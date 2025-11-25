package com.app.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.app.dao.CartDAO;
import com.app.dao.MenuItemDAO;
import com.app.dao_implementation.CartDAOImpl;
import com.app.dao_implementation.MenuItemDAOImpl;
import com.app.models.Cart;
import com.app.models.CartItem;
import com.app.models.MenuItem;


@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private CartDAO cartDAO = new CartDAOImpl();
    private MenuItemDAO menuItemDAO = new MenuItemDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) action = "view";

        switch (action) {
            case "view":
                showCart(req, resp);
                break;

            case "remove":
                removeItem(req, resp);
                break;

            case "clear":
                clearCart(req, resp);
                break;

            default:
                showCart(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) action = "view";

        switch (action) {
            case "add":
                addItem(req, resp);
                break;

            case "update":
                updateItem(req, resp);
                break;

            default:
                resp.sendRedirect("cart?action=view");
        }
    }

    // =========================================================
    // VIEW CART
    // =========================================================
    private void showCart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        // Ensure cart exists
        Cart cart = cartDAO.getCartByUserId(userId);
        if (cart == null) {
            cartDAO.createCart(userId);
            cart = cartDAO.getCartByUserId(userId);
        }

        List<CartItem> cartItems = cartDAO.getCartItems(cart.getCartId());

        // Fetch MenuItem details for display (name, price, image)
        java.util.Map<Integer, MenuItem> menuMap =
                new java.util.HashMap<>();

        for (CartItem ci : cartItems) {
            MenuItem item = menuItemDAO.getMenuItemById(ci.getMenuItemId());
            if (item != null) menuMap.put(ci.getMenuItemId(), item);
        }

        double total = cartDAO.getCartTotal(cart.getCartId());

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("menuItems", menuMap);
        req.setAttribute("cartTotal", total);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/jsp/customer/cart.jsp");
        rd.forward(req, resp);
    }

    // =========================================================
    // ADD ITEM TO CART
    // =========================================================
    private void addItem(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int menuItemId = Integer.parseInt(req.getParameter("menuItemId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        // Ensure cart exists
        Cart cart = cartDAO.getCartByUserId(userId);
        if (cart == null) cartDAO.createCart(userId);

        cartDAO.addItemToCart(userId, menuItemId, quantity);

        resp.sendRedirect("cart?action=view");
    }

    // =========================================================
    // UPDATE ITEM QUANTITY
    // =========================================================
    private void updateItem(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int menuItemId = Integer.parseInt(req.getParameter("menuItemId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        Cart cart = cartDAO.getCartByUserId(userId);

        if (cart != null) {
            cartDAO.updateCartItemQuantity(cart.getCartId(), menuItemId, quantity);
        }

        resp.sendRedirect("cart?action=view");
    }

    // =========================================================
    // REMOVE ITEM FROM CART
    // =========================================================
    private void removeItem(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int menuItemId = Integer.parseInt(req.getParameter("menuItemId"));

        Cart cart = cartDAO.getCartByUserId(userId);

        if (cart != null) {
            cartDAO.removeItemFromCart(cart.getCartId(), menuItemId);
        }

        resp.sendRedirect("cart?action=view");
    }

    // =========================================================
    // CLEAR CART
    // =========================================================
    private void clearCart(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Cart cart = cartDAO.getCartByUserId(userId);

        if (cart != null) {
            cartDAO.clearCart(cart.getCartId());
        }

        resp.sendRedirect("cart?action=view");
    }
}

/*
===========================================================
 CartServlet – JSP Mappings (Customer Cart System)
===========================================================

URL MAPPINGS:
----------------------------------------------
GET  /cart?action=view
    → /WEB-INF/jsp/customer/cart.jsp

POST /cart?action=add&menuItemId={id}
    → Adds item → redirects to /cart?action=view

POST /cart?action=update&menuItemId={id}&quantity={q}
    → Updates item quantity → redirects to cart

GET  /cart?action=remove&menuItemId={id}
    → Removes item → redirects to cart

GET  /cart?action=clear
    → Clears cart → redirects to cart


REQUIRED JSP FILES:
----------------------------------------------
📁 /WEB-INF/jsp/customer/cart.jsp


DATA PASSED TO JSP:
----------------------------------------------
cartItems  → List<CartItem>
menuItems  → Map<menuItemId, MenuItem>
cartTotal  → double total price


IMPORTANT NOTES:
----------------------------------------------
✔ One active cart per user (per DB design)
✔ If cart does not exist → automatically created
✔ Uses CartDAO + MenuItemDAO
✔ Uses DB exact fields:
      carts.cart_id, user_id
      cart_items.cart_item_id, menu_item_id, quantity
✔ Requires user session attribute: session.getAttribute("userId")
===========================================================
*/
