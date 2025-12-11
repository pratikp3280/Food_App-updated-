package com.app.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.app.models.MenuItem;

/*It will handle CRUD operations for menu items
 * plus custom queries like fetching all menu items for a specific restaurant,
 * searching by category,
 * filtering by availability
 * 
 * 
 * */

public interface MenuItemDAO {

    // ➕ Add a new menu item (by restaurant)
    void addMenuItem(MenuItem menuItem);

    // 🔍 Get a single menu item by ID
    MenuItem getMenuItemById(int menuItemId);

    // 🧾 Get all menu items for a specific restaurant
    List<MenuItem> getMenuItemsByRestaurantId(int restaurantId);

    // 🏷️ Get all menu items by category (e.g., "Biryani", "Desserts")
    List<MenuItem> getMenuItemsByCategory(String category);

    // ✅ Get all available menu items (for public listing)
    List<MenuItem> getAvailableMenuItems();

    // ✏️ Update existing menu item details
    void updateMenuItem(MenuItem menuItem);

    // ❌ Delete a menu item by ID
    void deleteMenuItem(int menuItemId);
    
    // if restaurant temporarily runs out of a dish (like “Chicken Biryani”) and needs to mark it unavailable without deleting it.
    void updateAvailability(int menuItemId, boolean isAvailable);
    
    Map<Integer, MenuItem> getMenuItemsAsMap(Set<Integer> ids);


}
