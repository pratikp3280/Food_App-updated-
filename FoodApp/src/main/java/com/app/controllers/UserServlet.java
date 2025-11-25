package com.app.controllers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.app.dao.UserDAO;
import com.app.dao_implementation.UserDAOImpl;
import com.app.models.User;
import org.mindrot.jbcrypt.BCrypt;

// This servlet handles all user-related actions 
/*This UserServlet controls:

✔ Registration 
✔ Login
✔ Logout
✔ Showing profile
✔ Updating profile
✔ Redirecting to JSP pages
 * */
@WebServlet("/user")  // Maps this servlet to /user URL
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

 // DAO object for interacting with the database
    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
       
    	// Initialize DAO object. Called once when servlet loads. 
    	userDao = new UserDAOImpl();
    }

    // ----------------------------------------------------
    // GET Requests
    // ----------------------------------------------------
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	 // Determine what action is requested (default = login)
        String action = request.getParameter("action");
        if (action == null) action = "login";

        switch (action) {
            case "register":          // Shows registration page
                forward(request, response, "/jsp/register.jsp");
                break;

            case "login":             // Show login page
                forward(request, response, "/jsp/login.jsp");
                break;

            case "logout":            // Perform logout
                doLogout(request, response);
                break;

            case "profile":           // Show user profile
                showProfile(request, response);
                break;

            default:           // Unknown action → go to login page
                response.sendRedirect(request.getContextPath() + "/user?action=login");
        }
    }

    // ----------------------------------------------------
    // POST Requests
    // ----------------------------------------------------
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	// Read which action was submitted from the form
        String action = request.getParameter("action");
        
     // If no action provided → bad request
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (action) {
            case "register":       // Process registration form
                handleRegister(request, response);
                break;

            case "login":          // Process login form
                handleLogin(request, response);
                break;

            case "profileUpdate":  // Process profile update
                handleProfileUpdate(request, response);
                break;

            default:             
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // ----------------------------------------------------
    // Registration
    // ----------------------------------------------------
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	// Collect form inputs
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String plainPassword = req.getParameter("password");

       
        
     // NEW: role taken from form
        String role = req.getParameter("role");
        if (role == null || role.isEmpty()) role = "customer";
        
        // Validate required fields
        if (username == null || username.isEmpty() || plainPassword == null || plainPassword.isEmpty()) {
            req.setAttribute("error", "Username and password are required.");
            forward(req, resp, "/jsp/register.jsp");
            return;
        }

        // Check if username exists
        if (userDao.getUserByUsername(username) != null) {
            req.setAttribute("error", "Username already exists.");
            forward(req, resp, "/jsp/register.jsp");
            return;
        }
        
     // Hash password before saving (BCrypt is secure)
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));

     // Create user model and populate fields
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(role);

     // Insert new user into database
        userDao.addUser(user);

     // Show success message and redirect to login page
        req.setAttribute("message", "Registration successful. Please login.");
        forward(req, resp, "/jsp/login.jsp");
    }

    // ----------------------------------------------------
    // Login  + RBAC
    // ----------------------------------------------------
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String passwordAttempt = req.getParameter("password");
   
     // Basic validation
        if (username == null || passwordAttempt == null) {
            req.setAttribute("error", "Username and password required.");
            forward(req, resp, "/jsp/login.jsp");
            return;
        }

     // Retrieve user from DB
        User user = userDao.getUserByUsername(username);

     // Check if user exists AND password matches
        if (user == null || !BCrypt.checkpw(passwordAttempt, user.getPassword())) {
            req.setAttribute("error", "Invalid username or password.");
            forward(req, resp, "/jsp/login.jsp");
            return;
        }

     // Create session and store logged-in user
        HttpSession session = req.getSession();
        session.setAttribute("loggedUser", user);
        session.setMaxInactiveInterval(30 * 60);
        
        // NEW — store role in session
        session.setAttribute("role", user.getRole());
        
        // NEW — redirect based on role
        String role = user.getRole();

        if ("admin".equalsIgnoreCase(role)) {
            resp.sendRedirect(req.getContextPath() + "/jsp/admin/dashboard.jsp");
        } else if ("delivery".equalsIgnoreCase(role)) {
            resp.sendRedirect(req.getContextPath() + "/jsp/delivery/dashboard.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/jsp/customer/home.jsp");
        }
    }
    
    // ----------------------------------------------------
    // Logout
    // ----------------------------------------------------
    private void doLogout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
    	// Get current session (don't create new one)
        HttpSession session = req.getSession(false);
        
     // Remove user session if exists
        if (session != null) session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/user?action=login");
    }

    // ----------------------------------------------------
    // Profile
    // ----------------------------------------------------
    private void showProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        
     // If session invalid or user not logged in → redirect to login
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

     // Forward user to profile JSP page
        forward(req, resp, "/jsp/profile.jsp");
    }

    // ----------------------------------------------------
    // Profile Update
    // ----------------------------------------------------
    private void handleProfileUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        
     // Ensure user is logged in
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

     // Get current logged-in user object
        User logged = (User) session.getAttribute("loggedUser");

     // Update profile fields
        logged.setName(req.getParameter("name"));
        logged.setEmail(req.getParameter("email"));
        logged.setPhone(req.getParameter("phone"));
        logged.setAddress(req.getParameter("address"));

     // Handle password update only if provided
        String newPassword = req.getParameter("newPassword");
        if (newPassword != null && !newPassword.isEmpty()) {
            logged.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
        }

     // Save updated user to DB
        userDao.updateUser(logged);

     // Update session with new data
        session.setAttribute("loggedUser", logged);
        
        // Show success message
        req.setAttribute("message", "Profile updated successfully.");

     // Stay on profile page
        forward(req, resp, "/jsp/profile.jsp");
    }
    
    
    // ----------------------------------------------------
    // Role Checker (Optional but useful)
    // ----------------------------------------------------
    @SuppressWarnings("unused")
	private boolean hasRole(HttpServletRequest req, String requiredRole) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;

        String role = (String) session.getAttribute("role");
        if (role == null) return false;

        return role.equalsIgnoreCase(requiredRole);
    }

    // ----------------------------------------------------
    // Utility Forward Request to JSP
    // ----------------------------------------------------
    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        
    // Use RequestDispatcher to forward request internally (URL doesn't change)
    	RequestDispatcher rd = req.getRequestDispatcher(path);
        rd.forward(req, resp);
    }
}



/*How requests flow
🟦 GET Requests (doGet)

Used for displaying pages:

Action	Page/Logic
register	/jsp/register.jsp
login	/jsp/login.jsp
logout	Calls doLogout()
profile	Requires user login → forwards to /jsp/profile.jsp


🟧 POST Requests (doPost)

Used when forms submit data:

Action	Method	Purpose
register	handleRegister()	Creates new account
login	handleLogin()	Authenticates user
profileUpdate	handleProfileUpdate()	Updates user info


BCrypt Password Hashing: Passwords are always stored in hashed form (secure).
hashpw() → convert plain text to hash
checkpw() → verify login password
This prevents storing real passwords in the database.


Sessions
When the user logs in:
                      session.setAttribute("loggedUser", user);
This keeps the user logged in until:
*They logout
*Session expires (30 min)
*
*
*Forwarding vs Redirect
* forward() → stays inside servlet, URL unchanged
  sendRedirect() → instructs browser to load a different URL
 * 
 * */
