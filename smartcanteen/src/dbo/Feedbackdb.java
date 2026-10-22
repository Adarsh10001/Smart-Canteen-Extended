package dbo;
import java.sql.*;
import database.DBconnection;

public class Feedbackdb {

    public boolean addFeedback(
        int userId, int itemId, int rating, String comments
    ) {
        if (rating < 1 || rating > 5) {
            return false;
        }

        String sql =
            "INSERT INTO feedback " +
            "(user_id, item_id, rating, comments) " +
            "SELECT ?, ?, ?, ? WHERE EXISTS (" +
            "SELECT 1 FROM orders o " +
            "JOIN order_items oi ON o.order_id = oi.order_id " +
            "WHERE o.user_id = ? AND oi.item_id = ?)";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, itemId);
            ps.setInt(3, rating);
            ps.setString(4, comments);
            ps.setInt(5, userId);
            ps.setInt(6, itemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}