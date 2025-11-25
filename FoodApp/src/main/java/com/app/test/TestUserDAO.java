package com.app.test;

import com.app.models.User;
import com.app.dao.UserDAO;
import com.app.dao_implementation.UserDAOImpl;

public class TestUserDAO {

    public static void main(String[] args) {
        //  Create DAO object
        UserDAO userDAO = new UserDAOImpl();

        //  Call method and get user
        User u = userDAO.getUser(1);

        // Step 3: Print the result
        if (u != null) {
            System.out.println("User found: " + u.getName() + " | Email: " + u.getEmail() + " | Phone: "  + u.getPhone());
        } else {
            System.out.println("User not found!");
        }
    }
}
