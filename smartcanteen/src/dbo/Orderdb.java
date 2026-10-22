package dbo;
import database.DBconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Orderdb {
    public int createOrder(Connection con, int userId)
            throws SQLException {
        String sql =
            "INSERT INTO orders (user_id, total_amount, status) " +
            "VALUES (?, 0.00, 'PLACED')";

        try (PreparedStatement ps = con.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
    
            ps.setInt(1, userId);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Order ID could not be generated.");
    }
    public boolean addOrderItem(Connection con, int orderId,
                                int itemId, int quantity)
            throws SQLException {
        if (quantity <= 0) {
            throw new SQLException(
                "Quantity must be greater than zero."
            );
        }
        String sql ="INSERT INTO order_items " +
            "(order_id, item_id, quantity, item_price) " +
            "SELECT ?, item_id, ?, price " +
            "FROM menu_items " +
            "WHERE item_id = ? AND is_available = TRUE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, quantity);
            ps.setInt(3, itemId);
            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new SQLException(
                    "Food item not found or unavailable: " + itemId
                );
            }
            return true;
        }
    }
    public boolean updateOrderTotal(Connection con, int orderId)
            throws SQLException {

        String sql ="UPDATE orders SET total_amount = (" +
            "SELECT COALESCE(SUM(quantity * item_price), 0) " +
            "FROM order_items WHERE order_id = ?) " +
            "WHERE order_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, orderId);
            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new SQLException(
                    "Could not update total for order: " + orderId
                );
            }
            System.out.println("yaay");
            return true;
        }
    }
    public boolean placeOrder(int userId, int[] itemIds, int[] quantities) {
        if (itemIds == null || quantities == null ||
            itemIds.length == 0 ||
            itemIds.length != quantities.length) {
            System.out.println("Invalid order details.");
            return false;
        }
        try (Connection con = DBconnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int orderId = createOrder(con, userId);

                for (int i = 0; i < itemIds.length; i++) {
                    addOrderItem(
                        con, orderId, itemIds[i], quantities[i]
                    );
                }
                updateOrderTotal(con, orderId);
                con.commit();
                System.out.println("Order placed successfully!");
                System.out.println("Order ID: " + orderId);
                return true;
            } catch (SQLException e) {
                con.rollback();
                System.out.println(
                    "Order failed. All changes were rolled back."
                );
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
            return false;
        }
    }
      
    public boolean updateOrderStatus(int orderId, String status) {
            String sql ="UPDATE orders SET status = ? WHERE order_id = ?";
            try (Connection con = DBconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, status);
                ps.setInt(2, orderId);
                return ps.executeUpdate() == 1;
            } 
            catch (SQLException e) {
                System.out.println("Could not update order status.");
                e.printStackTrace();
                return false;
            }
        }

}


