package dbo;
import database.DBconnection;
import model.User;
import model.Student;
import model.Admin;
import java.sql.*;

public class Userdb {
    public boolean registerUser(User user) {
        if (!"STUDENT".equals(user.getRole())) {
            System.out.println("Only student registration is allowed.");
            return false;
        }
        String sql ="INSERT INTO users (name, email, password, role) " +
            "VALUES (?, ?, ?, ?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            int rows = ps.executeUpdate();
            if (rows==1){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(
                "Registration failed: this email may already exist."
            );
            return false;
        } catch (SQLException e) {
            System.out.println("Could not register user.");
            e.printStackTrace();
            return false;
        }
    }
public User loginUser(String email, String password) {
    String sql ="SELECT user_id, name, email, password, role " +
        "FROM users WHERE email = ? AND password = ?";
    try (Connection con = DBconnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, email);
        ps.setString(2, password);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                String userPassword = rs.getString("password");
                String role = rs.getString("role");
                if ("STUDENT".equals(role)) {
                    return new Student(userId, name, userEmail, userPassword);
                } else if ("ADMIN".equals(role)) {
                    return new Admin(userId, name, userEmail, userPassword);
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Login failed due to a database error.");
        e.printStackTrace();
    }
    return null;
}


}
