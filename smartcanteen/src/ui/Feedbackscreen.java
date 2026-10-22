
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import database.DBconnection;
import dbo.Feedbackdb;
import model.Student;

public class Feedbackscreen extends JFrame {

    private Student student;
    private JComboBox<String> foodBox;
    private JComboBox<Integer> ratingBox;
    private JTextArea commentArea;
    private DefaultTableModel model;

    public Feedbackscreen(Student student) {
        this.student = student;

        setTitle("Food Feedback");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        foodBox = new JComboBox<>();
        ratingBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        commentArea = new JTextArea(3, 20);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("Food Item:"));
        form.add(foodBox);

        form.add(new JLabel("Rating:"));
        form.add(ratingBox);

        form.add(new JLabel("Comment:"));
        form.add(new JScrollPane(commentArea));

        JButton submitButton = new JButton("Submit Feedback");
        form.add(submitButton);

        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{"Student", "Rating", "Comment", "Date"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadFoodItems();

        foodBox.addActionListener(e -> loadFeedback());
        submitButton.addActionListener(e -> submitFeedback());

        if (foodBox.getItemCount() > 0) {
            loadFeedback();
        }

        setVisible(true);
    }

    private void loadFoodItems() {
        String sql = "SELECT item_name FROM menu_items ORDER BY item_name";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                foodBox.addItem(rs.getString("item_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Could not load feedback: " + e.getMessage()
            );
        }
    }

    private int getSelectedItemId() {
        String name = (String) foodBox.getSelectedItem();

        if (name == null) {
            return -1;
        }

        String sql = "SELECT item_id FROM menu_items WHERE item_name = ?";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("item_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private void loadFeedback() {
        model.setRowCount(0);

        int itemId = getSelectedItemId();

        if (itemId == -1) {
            return;
        }

        String sql =
            "SELECT u.name, f.rating, f.comments, f.feedback_date " +
            "FROM feedback f " +
            "JOIN users u ON f.user_id = u.user_id " +
            "WHERE f.item_id = ? " +
            "ORDER BY f.feedback_date DESC";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getInt("rating"),
                        rs.getString("comments"),
                        rs.getTimestamp("feedback_date")
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load feedback.");
        }
    }

    private void submitFeedback() {
        int itemId = getSelectedItemId();

        if (itemId == -1) {
            JOptionPane.showMessageDialog(this, "Select a food item.");
            return;
        }

        int rating = (Integer) ratingBox.getSelectedItem();
        String comments = commentArea.getText().trim();

        Feedbackdb feedbackDB = new Feedbackdb();

        boolean success = feedbackDB.addFeedback(
            student.getUserId(), itemId, rating, comments
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Feedback submitted!");
            commentArea.setText("");
            loadFeedback();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Feedback not submitted. Make sure you have ordered this item."
            );
        }
    }
}
