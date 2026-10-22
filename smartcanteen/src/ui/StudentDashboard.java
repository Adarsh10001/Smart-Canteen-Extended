package ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Student;
import dbo.menudb;
import model.menu;
public class StudentDashboard extends JFrame {
    private Student student;
    public StudentDashboard(Student student) {
        this.student = student;
        setTitle("Smart Canteen - Student Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JLabel welcomeLabel = new JLabel(
            "Welcome, " + student.getName(),
            SwingConstants.CENTER
        );
        JButton menuButton = new JButton("View Menu");
        JButton orderButton = new JButton("Place Order");
        JButton logoutButton = new JButton("Logout");
        JButton myOrdersButton = new JButton("View My Orders");
        JButton feedbackButton = new JButton("Food Feedback");

        JPanel panel = new JPanel(new GridLayout(10, 1, 10, 10));
        panel.setBorder(
            BorderFactory.createEmptyBorder(40, 80, 40, 80)
        );        
        panel.add(welcomeLabel);
        panel.add(menuButton);
        panel.add(orderButton);
        panel.add(myOrdersButton);
        panel.add(logoutButton);
        panel.add(feedbackButton);
        add(panel);
         menuButton.addActionListener(e -> {
            menudb menuDB = new menudb();
            menu[] items = menuDB.getAvailableMenuItems();

            String[] columns = {"Item Name", "Price"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            for (menu item : items) {
                model.addRow(new Object[]{
                    item.getItemName(),
                    item.getPrice()
                });
            }
            JTable menuTable = new JTable(model);
            menuTable.setEnabled(false);

            JOptionPane.showMessageDialog(
                this,
                new JScrollPane(menuTable),
                "Available Food Items",
                JOptionPane.PLAIN_MESSAGE
            );
        });
        orderButton.addActionListener(e -> {
            Orderscreen orderFrame = new Orderscreen(student);
            orderFrame.setVisible(true);
            });
        logoutButton.addActionListener(e -> {
                new Login().setVisible(true);
                dispose();
            });
        myOrdersButton.addActionListener(e -> {
            Myorders frame = new Myorders(student);
            frame.setVisible(true);
        });
        feedbackButton.addActionListener(e -> {
            new Feedbackscreen(student);
            });

        }
}
