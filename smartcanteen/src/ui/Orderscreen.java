package ui;
import javax.swing.*;
import java.awt.*;
import model.Student;
import model.menu;
import dbo.menudb;
import dbo.Orderdb;

public class Orderscreen extends JFrame {
    private Student student;
    private menu[] items;
    private JSpinner[] quantities;

    public Orderscreen(Student student)
     {
        this.student = student;
        setTitle("Smart Canteen - Place Order");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        menudb menuDB = new menudb();
        items = menuDB.getAvailableMenuItems();
        quantities = new JSpinner[items.length];

        JPanel panel = new JPanel(new GridLayout(
            items.length + 1, 3, 10, 10
        ));

        panel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        panel.add(new JLabel("Food Item"));
        panel.add(new JLabel("Price"));
        panel.add(new JLabel("Quantity"));

        for (int i = 0; i < items.length; i++) {
            panel.add(new JLabel(items[i].getItemName()));

            panel.add(new JLabel(
                "₹" + items[i].getPrice()
            ));

            quantities[i] = new JSpinner(
                new SpinnerNumberModel(0, 0, 100, 1)
            );

            panel.add(quantities[i]);
        }

        JButton placeOrderButton =
            new JButton("Place Order");

        placeOrderButton.addActionListener(
            e -> placeOrder()
        );

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(placeOrderButton, BorderLayout.SOUTH);
    }

    private void placeOrder() {
        int count = 0;

        for (int i = 0; i < quantities.length; i++) {
            if ((int) quantities[i].getValue() > 0) {
                count++;
            }
        }

        if (count == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Please select at least one food item."
            );
            return;
        }
        int[] itemIds = new int[count];
        int[] itemQuantities = new int[count];

        int index = 0;

        for (int i = 0; i < quantities.length; i++) {
            int quantity = (int) quantities[i].getValue();

            if (quantity > 0) {
                itemIds[index] = items[i].getItemId();
                itemQuantities[index] = quantity;
                index++;
            }
        }
        double totalAmount = 0.0;
        for (int i = 0; i < itemIds.length; i++) {
            for (int j = 0; j < items.length; j++) {
                if (itemIds[i] == items[j].getItemId()) {
                    totalAmount +=items[j].getPrice() * itemQuantities[i];
                    break;
                }
            }
        }
        new Payment(student,itemIds,itemQuantities,totalAmount);

    }
}
