package ui;
import javax.swing.*;
import java.awt.*;
import dbo.menudb;

public class ManagerMenu extends JFrame {
    private menudb menuDB;

    public ManagerMenu() {
        menuDB = new menudb();
        setTitle("Smart Canteen - Manager Menu");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton viewButton = new JButton("View All Menu Items");
        JButton addButton = new JButton("Add Food Item");
        JButton priceButton = new JButton("Update Price");
        JButton availabilityButton =new JButton("Change Availability");
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        
        panel.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));
        panel.add(viewButton);
        panel.add(addButton);
        panel.add(priceButton);
        panel.add(availabilityButton);
        add(panel);
        viewButton.addActionListener(e -> {
            new AdminMenutable().setVisible(true);
        });
        addButton.addActionListener(e -> addFoodItem());
        priceButton.addActionListener(e -> updatePrice());
        availabilityButton.addActionListener(
            e -> changeAvailability()
        );
    }
    private void addFoodItem() {
        String name = JOptionPane.showInputDialog(this, "Enter food name:");
        if (name == null) return;
        name = name.trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Food name cannot be empty.");
            return;
        }
        String description = JOptionPane.showInputDialog(
            this, "Enter description:");
        if (description == null) return;
        String priceText = JOptionPane.showInputDialog(this, "Enter price:");
        if (priceText == null) return;
        try {
            double price = Double.parseDouble(priceText.trim());

            if (!Double.isFinite(price) || price <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid positive price.");
                return;
            }
            boolean success = menuDB.addMenuItem(name, description, price);

            if (success) {
                    JOptionPane.showMessageDialog(this, "Food item added successfully.");
            }
            else {
                    JOptionPane.showMessageDialog(this, "Could not add food item.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric price.");
        }
    }

    private void updatePrice() {
        String idText = JOptionPane.showInputDialog(this, "Enter food item ID:");

        if (idText == null) 
            return;
        String priceText = JOptionPane.showInputDialog(this, "Enter new price:");
        if (priceText == null) return;

        try {
            int itemId = Integer.parseInt(idText.trim());
            double price = Double.parseDouble(priceText.trim());
            if (itemId <= 0 || !Double.isFinite(price) || price <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid item ID and positive price.");
                return;
            }
            boolean success = menuDB.updatePrice(itemId, price);
            if (success) {
                JOptionPane.showMessageDialog(this, "Price updated successfully.");
            } 
            else {
                JOptionPane.showMessageDialog(this, "Could not update price. Check item ID.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric values.");
        }
    }
    private void changeAvailability() {
        String idText = JOptionPane.showInputDialog(this, "Enter food item ID:");
        if (idText == null) 
            return;
        try {
            int itemId = Integer.parseInt(idText.trim());
            if (itemId <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid item ID.");
                return;
            }
            Object[] options = {"Available", "Unavailable"};
            int choice = JOptionPane.showOptionDialog(
                this,
                "Choose availability:",
                "Change Availability",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == JOptionPane.CLOSED_OPTION) 
                return;
            boolean available;
            if (choice == 0) 
                available = true;
            else 
                available = false;
            boolean success = menuDB.updateAvailability(itemId, available);

            if (success) {
                JOptionPane.showMessageDialog(this, "Availability updated successfully.");
            }
            else {
                JOptionPane.showMessageDialog(this, "Could not update availability. Check the item ID.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric item ID.");
        }
    }
}
