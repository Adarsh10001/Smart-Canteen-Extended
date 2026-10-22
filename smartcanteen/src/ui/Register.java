
package ui;

import javax.swing.*;
import java.awt.*;
import dbo.Userdb;
import model.Student;

public class Register extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public Register() {
        setTitle("Smart Canteen - Student Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        registerButton = new JButton("Register");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(registerButton);
        add(panel);
        registerButton.addActionListener(e -> registerStudent());
    }
    private void registerStudent() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(
            passwordField.getPassword()
        );
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please fill in all fields."
            );
            return;
        }
        Student student = new Student(
            0, name, email, password
        );
        Userdb userDB = new Userdb();
        if (userDB.registerUser(student)) {
            JOptionPane.showMessageDialog(
                this, "Registration successful! You can now log in."
            );
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Registration failed. Check whether this email already exists.",
                "Registration Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
