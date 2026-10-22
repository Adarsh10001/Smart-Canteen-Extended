package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    private static final String URL ="jdbc:mysql://localhost:3306/smart_canteen";
    private static final String USER = "root";
    private static final String PASSWORD = "500653";
    private static final DBconnection instance =new DBconnection();
    private DBconnection() {
    }
    public static DBconnection getInstance() {
        return instance;
    }
    public static Connection getConnection()
            throws SQLException {
        return DriverManager.getConnection(
            URL, USER, PASSWORD
        );
    }
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println(
                    "Database connected successfully!"
                );
            }
        } catch (SQLException e) {
            System.err.println(
                "Database connection failed!"
            );
            e.printStackTrace();
        }
    }
}