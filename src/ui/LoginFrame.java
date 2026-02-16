package src.ui;

import src.db.DatabaseConnection;
import src.model.Cart;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public LoginFrame() {

        setTitle("Login - E-Commerce");
        setSize(300, 180);
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Username:"));
        txtUser = new JTextField();
        add(txtUser);

        add(new JLabel("Password:"));
        txtPass = new JPasswordField();
        add(txtPass);

        btnLogin = new JButton("Login");
        add(new JLabel());
        add(btnLogin);

        btnLogin.addActionListener(e -> login());
        getRootPane().setDefaultButton(btnLogin);
        setVisible(true);
    }

    private void login() {
        String username = txtUser.getText();
        String password = new String(txtPass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Cannot connect to database!");
                return;
            }

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?"
            );

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new ProductFrame(); // Must exist in ui package
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
