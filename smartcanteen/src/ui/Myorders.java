package ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import database.DBconnection;
import model.Student;

public class Myorders extends JFrame {
    private DefaultTableModel model;
    private Student student;

    public Myorders(Student student) {
        this.student = student;
        setTitle("Smart Canteen - My Orders");
        setSize(650, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {
            "Order ID", "Date", "Total (₹)", "Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(
            int row, int column
            )
            {
                return false;
            }
            };

        JTable table = new JTable(model);
        loadOrders();
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadOrders());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshButton); 

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);    }
            
    private void loadOrders() {
        model.setRowCount(0);
        String sql ="SELECT order_id, order_date, total_amount, status " +
            "FROM orders WHERE user_id = ? " +
            "ORDER BY order_date DESC";
        try (Connection con = DBconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, student.getUserId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[] {
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                        });
                    }
                }
            } 
            catch (SQLException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not load your orders.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
    }
}
