package com.app.dao_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.app.dao.MenuItemDAO;
import com.app.models.MenuItem;
import com.app.utility.DBConnection;

public class MenuItemDAOImpl implements MenuItemDAO {

    // ✅ SQL Queries
    private static final String INSERT_MENU_ITEM_QUERY = 
        "INSERT INTO `menu_items` (`restaurant_id`, `name`, `description`, `price`, `category`, `is_veg`, `is_available`, `image_url`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String GET_MENU_ITEM_BY_ID_QUERY = 
        "SELECT * FROM `menu_items` WHERE `menu_item_id` = ?";
    
    private static final String GET_MENU_ITEMS_BY_RESTAURANT_QUERY = 
        "SELECT * FROM `menu_items` WHERE `restaurant_id` = ? ORDER BY `category`, `name`";
    
    private static final String GET_MENU_ITEMS_BY_CATEGORY_QUERY = 
        "SELECT * FROM `menu_items` WHERE `category` = ? AND `is_available` = TRUE ORDER BY `restaurant_id`, `name`";
    
    private static final String GET_AVAILABLE_MENU_ITEMS_QUERY = 
        "SELECT * FROM `menu_items` WHERE `is_available` = TRUE ORDER BY `restaurant_id`, `category`, `name`";
    
    private static final String UPDATE_MENU_ITEM_QUERY = 
        "UPDATE `menu_items` SET `name` = ?, `description` = ?, `price` = ?, `category` = ?, `is_veg` = ?, `is_available` = ?, `image_url` = ? WHERE `menu_item_id` = ?";
    
    private static final String DELETE_MENU_ITEM_QUERY = 
        "DELETE FROM `menu_items` WHERE `menu_item_id` = ?";
    
    private static final String UPDATE_AVAILABILITY_QUERY = 
        "UPDATE `menu_items` SET `is_available` = ? WHERE `menu_item_id` = ?";

    // -------------------------------------------------------------------------
    // ➕ Add a new menu item (by restaurant)
    // -------------------------------------------------------------------------
    @Override
    public void addMenuItem(MenuItem menuItem) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT_MENU_ITEM_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, menuItem.getRestaurantId());
            pstmt.setString(2, menuItem.getName());
            pstmt.setString(3, menuItem.getDescription());
            pstmt.setDouble(4, menuItem.getPrice());
            pstmt.setString(5, menuItem.getCategory());
            pstmt.setBoolean(6, menuItem.isVeg());
            pstmt.setBoolean(7, menuItem.isAvailable());
            pstmt.setString(8, menuItem.getImageUrl());
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Menu item inserted successfully.");
                
                // Get generated menu_item_id
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    menuItem.setMenuItemId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error while inserting menu item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // 🔍 Get a single menu item by ID
    // -------------------------------------------------------------------------
    @Override
    public MenuItem getMenuItemById(int menuItemId) {
        MenuItem menuItem = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_MENU_ITEM_BY_ID_QUERY)) {
            
            pstmt.setInt(1, menuItemId);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                menuItem = extractMenuItemFromResultSet(res);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching menu item by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return menuItem;
    }

    // -------------------------------------------------------------------------
    // 🧾 Get all menu items for a specific restaurant
    // -------------------------------------------------------------------------
    @Override
    public List<MenuItem> getMenuItemsByRestaurantId(int restaurantId) {
        List<MenuItem> menuItemsList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_MENU_ITEMS_BY_RESTAURANT_QUERY)) {
            
            pstmt.setInt(1, restaurantId);
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                menuItemsList.add(extractMenuItemFromResultSet(res));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching menu items by restaurant ID: " + e.getMessage());
            e.printStackTrace();
        }

        return menuItemsList;
    }

    // -------------------------------------------------------------------------
    // 🏷️ Get all menu items by category
    // -------------------------------------------------------------------------
    @Override
    public List<MenuItem> getMenuItemsByCategory(String category) {
        List<MenuItem> menuItemsList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_MENU_ITEMS_BY_CATEGORY_QUERY)) {
            
            pstmt.setString(1, category);
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                menuItemsList.add(extractMenuItemFromResultSet(res));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching menu items by category: " + e.getMessage());
            e.printStackTrace();
        }

        return menuItemsList;
    }

    // -------------------------------------------------------------------------
    // ✅ Get all available menu items (for public listing)
    // -------------------------------------------------------------------------
    @Override
    public List<MenuItem> getAvailableMenuItems() {
        List<MenuItem> menuItemsList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet res = stmt.executeQuery(GET_AVAILABLE_MENU_ITEMS_QUERY)) {

            while (res.next()) {
                menuItemsList.add(extractMenuItemFromResultSet(res));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching available menu items: " + e.getMessage());
            e.printStackTrace();
        }

        return menuItemsList;
    }

    // -------------------------------------------------------------------------
    // ✏️ Update existing menu item details
    // -------------------------------------------------------------------------
    @Override
    public void updateMenuItem(MenuItem menuItem) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE_MENU_ITEM_QUERY)) {

            pstmt.setString(1, menuItem.getName());
            pstmt.setString(2, menuItem.getDescription());
            pstmt.setDouble(3, menuItem.getPrice());
            pstmt.setString(4, menuItem.getCategory());
            pstmt.setBoolean(5, menuItem.isVeg());
            pstmt.setBoolean(6, menuItem.isAvailable());
            pstmt.setString(7, menuItem.getImageUrl());
            pstmt.setInt(8, menuItem.getMenuItemId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Menu item updated successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error while updating menu item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // ❌ Delete a menu item by ID
    // -------------------------------------------------------------------------
    @Override
    public void deleteMenuItem(int menuItemId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE_MENU_ITEM_QUERY)) {

            pstmt.setInt(1, menuItemId);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Menu item deleted successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error while deleting menu item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // 🔄 Update availability status
    // -------------------------------------------------------------------------
    @Override
    public void updateAvailability(int menuItemId, boolean isAvailable) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE_AVAILABILITY_QUERY)) {

            pstmt.setBoolean(1, isAvailable);
            pstmt.setInt(2, menuItemId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                String status = isAvailable ? "available" : "unavailable";
                System.out.println("✅ Menu item marked as " + status + " successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error while updating menu item availability: " + e.getMessage());
            e.printStackTrace();
        }
    }

 // ♻️ Helper method to extract MenuItem from ResultSet
    private MenuItem extractMenuItemFromResultSet(ResultSet rs) throws SQLException {
       
    	MenuItem menuItem = new MenuItem();
    	
        menuItem.setMenuItemId(rs.getInt("menu_item_id"));
        menuItem.setRestaurantId(rs.getInt("restaurant_id"));
        menuItem.setName(rs.getString("name"));
        menuItem.setDescription(rs.getString("description"));
        menuItem.setPrice(rs.getDouble("price"));
        menuItem.setCategory(rs.getString("category"));
        menuItem.setVeg(rs.getBoolean("is_veg"));
        menuItem.setAvailable(rs.getBoolean("is_available"));
        menuItem.setImageUrl(rs.getString("image_url"));
        menuItem.setCreatedAt(rs.getTimestamp("created_at"));
        
        return menuItem;
    }
    
    
 // 🔍 Fetch multiple menu items by a list of IDs (USED FOR ORDER HISTORY)
    @Override
    public Map<Integer, MenuItem> getMenuItemsAsMap(Set<Integer> ids) {

        Map<Integer, MenuItem> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) return map;

        StringBuilder query = new StringBuilder("SELECT * FROM menu_items WHERE menu_item_id IN (");
        query.append(String.join(",", ids.stream().map(id -> "?").toArray(String[]::new)));
        query.append(")");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int index = 1;
            for (Integer id : ids) stmt.setInt(index++, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MenuItem item = extractMenuItemFromResultSet(rs);
                map.put(item.getMenuItemId(), item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }
    
    


}