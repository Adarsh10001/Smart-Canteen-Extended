
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.menu;
import dbo.menudb;

public class AdminMenutable extends JFrame {
    public AdminMenutable() {
        setTitle("Smart Canteen - All Menu Items");
        setSize(700, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Item ID", "Name", "Description",
            "Price (₹)", "Availability"
        };
        DefaultTableModel model =
            new DefaultTableModel(columns, 0) ;

        menudb menuDB = new menudb();
        menu[] items = menuDB.getAllMenuItems();

        for (menu item : items) {
            model.addRow(new Object[] {
                item.getItemId(),
                item.getItemName(),
                item.getDescription(),
                item.getPrice(), 
                item.isAvailable() ? "Available" : "Unavailable"
            });
        }
        JTable table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }
}
