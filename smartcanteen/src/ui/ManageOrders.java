
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import database.DBconnection;
import dbo.Orderdb;

public class ManageOrders extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ManageOrders() {
        setTitle("Smart Canteen - Manage Orders");
        setSize(850, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {
            "Order ID", "Student ID", "Date",
            "Total (₹)", "Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        JButton refreshButton = new JButton("Refresh");
        JButton statusButton = new JButton("Change Status");
        JButton itemsButton = new JButton("View Items");
        refreshButton.addActionListener(e -> loadOrders());

        itemsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    this, "Please select an order first."
                );
                return;
            }

            int orderId = (int) model.getValueAt(selectedRow, 0);
            showOrderItems(orderId);
        });

        statusButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    this, "Please select an order first."
                );
                return;
            }
            int orderId = (int) model.getValueAt(selectedRow, 0);

            String[] statuses = {
                "PLACED", "PREPARING", "READY",
                "COMPLETED", "CANCELLED"
            };
            String status = (String) JOptionPane.showInputDialog(
                this,
                "Select the new order status:",
                "Change Order Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                model.getValueAt(selectedRow, 4)
            );
            if (status == null) {
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Change order #" + orderId + " to " + status + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            Orderdb orderDB = new Orderdb();

            if (orderDB.updateOrderStatus(orderId, status)) {
                JOptionPane.showMessageDialog(
                    this, "Order status updated successfully."
                );
                loadOrders();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not update order status.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshButton);
        bottomPanel.add(itemsButton);
        bottomPanel.add(statusButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadOrders();
    }
    private void showOrderItems(int orderId) {
        String[] columns = {
            "Food Item", "Quantity",
            "Price Each (₹)", "Subtotal (₹)"
        };

        DefaultTableModel itemModel =
            new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(
                    int row, int column
                ) {
                    return false;
                }
            };
        String sql =
            "SELECT m.item_name, oi.quantity, oi.item_price " +
            "FROM order_items oi " +
            "LEFT JOIN menu_items m ON oi.item_id = m.item_id " +
            "WHERE oi.order_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String itemName = rs.getString("item_name");

                    if (itemName == null) {
                        itemName = "Unavailable item";
                    }

                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("item_price");
                    double subtotal = quantity * price;

                    itemModel.addRow(new Object[] {
                        itemName,
                        quantity,
                        price,
                        subtotal
                    });
                }
            }

            if (itemModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "No items found for order #" + orderId
                );
                return;
            }

            JTable itemTable = new JTable(itemModel);

            JScrollPane scrollPane = new JScrollPane(itemTable);
            scrollPane.setPreferredSize(new Dimension(550, 200));

            JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Items in Order #" + orderId,
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load order items.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        model.setRowCount(0);

        String sql =
            "SELECT order_id, user_id, order_date, " +
            "total_amount, status FROM orders " +
            "ORDER BY order_date DESC";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("status")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load orders.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
}
