package ui;

import javax.swing.*;
import java.awt.*;
import model.PaymentMethod;
import model.UpiPayment;

public class Payment extends JFrame {
    private model.Student student;
    private int[] itemIds;
    private int[] quantities;

    public Payment(model.Student student, int[] itemIds,
                   int[] quantities, double totalAmount) {
        this.student = student;
        this.itemIds = itemIds;
        this.quantities = quantities;

        setTitle("Smart Canteen-Payment");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel titleLabel = new JLabel(
            "UPI Payment", SwingConstants.CENTER
        );

        JLabel amountLabel = new JLabel(
            String.format("Total Amount: ₹%.2f", totalAmount),
            SwingConstants.CENTER
        );

        JButton payButton = new JButton("Confirm Payment");
        JButton cancelButton = new JButton("Cancel");

        payButton.addActionListener(e -> {
            PaymentMethod payment = new UpiPayment();

            boolean confirmed = payment.processPayment(totalAmount);

            if (!confirmed) {
                return;
            }

            dbo.Orderdb orderDB = new dbo.Orderdb();
            boolean success = orderDB.placeOrder(
                student.getUserId(),
                itemIds,
                quantities
            );

            if (success) {
                JOptionPane.showMessageDialog(
                    this,
                    "Payment successful!\nYour order has been placed.",
                    "Order Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Order placement failed. No successful order was recorded.",
                    "Order Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(titleLabel);
        panel.add(amountLabel);
        panel.add(payButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }
}