
package ui;

import javax.swing.*;
import java.awt.*;
import model.Admin;

public class AdminDashboard extends JFrame {

    private Admin admin;
    public AdminDashboard(Admin admin) {
        this.admin = admin;
        setTitle("Smart Canteen - Admin Dashboard");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JLabel welcomeLabel = new JLabel(
            "Welcome, Admin " + admin.getName() + "!",
            SwingConstants.CENTER
        );
        JButton menuButton =
            new JButton("Manage Menu");

        JButton ordersButton =
            new JButton("Manage Orders");

        JButton logoutButton =
            new JButton("Logout");

        JPanel panel = new JPanel(
            new GridLayout(4, 1, 10, 10)
        );
        panel.setBorder(
            BorderFactory.createEmptyBorder(35, 70, 35, 70)
        );
        panel.add(welcomeLabel);
        panel.add(menuButton);
        panel.add(ordersButton);
        panel.add(logoutButton);
        add(panel);
        menuButton.addActionListener(e -> {
            ManagerMenu frame = new ManagerMenu();
            frame.setVisible(true);
        });
        ordersButton.addActionListener(e -> {
            ManageOrders frame = new ManageOrders();
            frame.setVisible(true);
        });
        logoutButton.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });
    }
    
}
