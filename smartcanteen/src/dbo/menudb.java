
package dbo;
import database.DBconnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import model.menu;

public class menudb {
    public boolean addMenuItem(String name, String description,
                            double price) {
        String sql = "INSERT INTO menu_items " +
                    "(item_name, description, price, is_available) " +
                    "VALUES (?, ?, ?, TRUE)";
        try (
            Connection con = DBconnection.getConnection();
            java.sql.PreparedStatement ps =
                    con.prepareStatement(sql)
        ) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, price);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Menu item added successfully!");
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Could not add menu item.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updatePrice(int itemId, double newPrice) {

        String sql = "UPDATE menu_items SET price = ? WHERE item_id = ?";
        try (
            Connection con = DBconnection.getConnection();
            java.sql.PreparedStatement st = con.prepareStatement(sql)
        ) {
            st.setDouble(1, newPrice);
            st.setInt(2, itemId);
            int rows = st.executeUpdate();
            if (rows > 0) {
                System.out.println("Price updated successfully!");
                return true;
            } else {
                System.out.println("Menu item not found.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Could not update price.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateAvailability(int itemId, boolean available) {
        String sql = "UPDATE menu_items SET is_available = ? WHERE item_id = ?";
        try (
            Connection con = DBconnection.getConnection();
            java.sql.PreparedStatement st = con.prepareStatement(sql)
        ) {

            st.setBoolean(1, available);
            st.setInt(2, itemId);

            int rows = st.executeUpdate();

            if (rows > 0) {
                System.out.println("Availability updated successfully!");
                return true;
            } else {
                System.out.println("Menu item not found.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Could not update availability.");
            e.printStackTrace();
            return false;
        }
    }

    public menu[] getAvailableMenuItems() {
        java.util.ArrayList<menu> items =new java.util.ArrayList<>();
        String sql ="SELECT item_id, item_name, description, price, is_available " +
            "FROM menu_items WHERE is_available = TRUE";

        try (java.sql.Connection con =
                database.DBconnection.getConnection();
            java.sql.PreparedStatement ps =
                con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                menu item = new menu(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                );
                items.add(item);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Could not load menu items.");
            e.printStackTrace();
        }
        return items.toArray(new menu[0]);
    }
    public menu[] getAllMenuItems(){
        java.util.ArrayList<menu> items =
            new java.util.ArrayList<>();
        String sql ="SELECT item_id, item_name, description, price, is_available " +
            "FROM menu_items";
        try (java.sql.Connection con =
                database.DBconnection.getConnection();
            java.sql.PreparedStatement ps =
                con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                menu item = new menu(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                );
                items.add(item);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Could not load menu items.");
            e.printStackTrace();
        }
        return items.toArray(new menu[0]);
    }
}