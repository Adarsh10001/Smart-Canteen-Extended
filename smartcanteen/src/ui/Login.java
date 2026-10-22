
package ui;
import dbo.Userdb;
import model.User;
import model.Student;
import model.Admin;
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Login() {
        setTitle("Smart Canteen - Login");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 150, 100, 150));
        emailField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
        registerButton.addActionListener(e -> {
    Register registerFrame = new Register();
    registerFrame.setVisible(true);
});
loginButton.addActionListener(e -> {
    String email = emailField.getText().trim();
    String password = new String(
        passwordField.getPassword()
    );
    if (email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this,"Please enter both email and password.");
        return;
    }
    Userdb userDB = new Userdb();
    User user = userDB.loginUser(email, password);
    if (user instanceof Student) {
        StudentDashboard dashboard =new StudentDashboard((Student) user);
        dashboard.setVisible(true);
        dispose();
    }
    else if (user instanceof Admin) {
        AdminDashboard dashboard =new AdminDashboard((Admin) user);
        dashboard.setVisible(true);
        dispose();
    }
    else {
        JOptionPane.showMessageDialog(
            this,
            "Invalid email or password.",
            "Login Failed",
            JOptionPane.ERROR_MESSAGE
        );
    }
});

    }
    
}
