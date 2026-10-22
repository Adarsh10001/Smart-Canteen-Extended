package model;

import javax.swing.JOptionPane;

public class UpiPayment implements PaymentMethod {

    @Override
    public boolean processPayment(double amount) {
        int choice = JOptionPane.showConfirmDialog(
            null,
            "Pay ₹" + amount + " using UPI?",
            "UPI Payment",
            JOptionPane.YES_NO_OPTION
        );

        return choice == JOptionPane.YES_OPTION;
    }
}