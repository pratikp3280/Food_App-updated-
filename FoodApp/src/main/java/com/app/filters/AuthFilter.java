package com.app.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import com.app.models.User;

/**
 * AuthFilter
 * Enforces authentication + role-based access control (RBAC)
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final String SESSION_USER = "loggedUser";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String context = request.getContextPath();
        String uri = request.getRequestURI();
        String path = uri.substring(context.length());

        // ===============================
        // ✅ PUBLIC RESOURCES
        // ===============================
        if (
            path.equals("/") ||
            path.equals("/index.jsp") ||

            // static resources
            path.startsWith("/assets/") ||

            // servlet endpoints
            path.startsWith("/user") ||

            // shared JSP fragments
            path.startsWith("/jsp/shared/") ||

            // ✅ PUBLIC AUTH PAGES (VERY IMPORTANT)
            path.equals("/jsp/login.jsp") ||
            path.equals("/jsp/register.jsp")
        ) {
            chain.doFilter(request, response);
            return;
        }

        // ===============================
        // ✅ CHECK AUTHENTICATION
        // ===============================
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(context + "/user?action=login");
            return;
        }

        String role = ((com.app.models.User) session.getAttribute("loggedUser")).getRole();

        // ===============================
        // ✅ ROLE BASED ACCESS
        // ===============================
        if (path.startsWith("/jsp/admin/") && !"admin".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (path.startsWith("/jsp/customer/") && !"customer".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (path.startsWith("/jsp/delivery/") && !"delivery".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

}
