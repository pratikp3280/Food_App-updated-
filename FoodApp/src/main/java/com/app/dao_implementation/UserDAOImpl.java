package com.app.dao_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.dao.UserDAO;
import com.app.models.User;
import com.app.utility.DBConnection;

public class UserDAOImpl implements UserDAO {

    // ✅ SQL Queries (corrected & cleaned)
    private static final String INSERT_USER_QUERY = 
        "INSERT INTO `users` (`name`, `username`, `password`, `email`, `phone`, `address`, `role`) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String GET_USER_QUERY = 
        "SELECT * FROM `users` WHERE `user_Id` = ?";
    
    private static final String UPDATE_USER_QUERY = 
        "UPDATE `users` SET `name` = ?, `username` = ?, `password` = ?, `email` = ?, `phone` = ?, `address` = ?, `role` = ? WHERE `user_Id` = ?";
    
    private static final String DELETE_USER_QUERY = 
        "DELETE FROM `users` WHERE `user_Id` = ?";
    
    private static final String GET_ALL_USERS_QUERY = 
        "SELECT * FROM `users`";

    
    private static final String GET_USER_BY_USERNAME_QUERY =
    	    "SELECT * FROM users WHERE username = ?";
    
    // -------------------------------------------------------------------------
    // ✅ Add new user
    // -------------------------------------------------------------------------
    @Override
    public void addUser(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT_USER_QUERY)) {
            
            // Set values in PreparedStatement
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAddress());
            pstmt.setString(7, user.getRole());
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ User inserted successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error while inserting user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // ✅ Get user by ID
    // -------------------------------------------------------------------------
    @Override
    public User getUser(int userId) {
        User user = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_USER_QUERY)) {
            
            pstmt.setInt(1, userId);
            ResultSet res = pstmt.executeQuery();

            // Always call next() before accessing ResultSet columns
            if (res.next()) {
                user = extractUser(res); // Using helper method
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching user: " + e.getMessage());
            e.printStackTrace();
        }

        return user;
    }

    // -------------------------------------------------------------------------
    // ✅ get user by username 
    // -------------------------------------------------------------------------
    
    @Override
    public User getUserByUsername(String username) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_USER_BY_USERNAME_QUERY)) {
            
        	pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractUser(rs); // reuse existing helper
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    
    // -------------------------------------------------------------------------
    // ✅ Update user details
    // -------------------------------------------------------------------------
    @Override
    public void updateUser(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE_USER_QUERY)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAddress());
            pstmt.setString(7, user.getRole());
            pstmt.setInt(8, user.getUserId()); // Make sure WHERE clause uses this

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ User updated successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error while updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // ✅ Delete user by ID
    // -------------------------------------------------------------------------
    @Override
    public void deleteUser(int userId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE_USER_QUERY)) {

            pstmt.setInt(1, userId);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ User deleted successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error while deleting user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // ✅ Get all users
    // -------------------------------------------------------------------------
    @Override
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet res = stmt.executeQuery(GET_ALL_USERS_QUERY)) {

            while (res.next()) {
                usersList.add(extractUser(res)); // Use helper for cleaner code
            }

        } catch (SQLException e) {
            System.err.println("❌ Error while fetching all users: " + e.getMessage());
            e.printStackTrace();
        }

        return usersList;
    }

    
    
    
    // -------------------------------------------------------------------------
    // ✅ Helper Method: Convert ResultSet row into User object
    // -------------------------------------------------------------------------
    private User extractUser(ResultSet res) throws SQLException {
        // Extracts user data from a single ResultSet record
        int userId = res.getInt("user_Id");
        String name = res.getString("name");
        String username = res.getString("username");
        String password = res.getString("password");
        String email = res.getString("email");
        String phone = res.getString("phone");
        String address = res.getString("address");
        String role = res.getString("role");

        // Returning constructed User object
        return new User(userId, name, username, password, email, phone, address, role, null, null);
    }

	
}
