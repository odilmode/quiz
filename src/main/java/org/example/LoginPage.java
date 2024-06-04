package org.example;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage implements ActionListener {
    JFrame frame = new JFrame();
    JLabel textLabel = new JLabel();
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JButton signUpButton = new JButton("Sign UP");
    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();
    JLabel userIDLabel = new JLabel("User ID");
    JLabel userPasswordLabel = new JLabel("Password");
    JLabel messageLabel = new JLabel();

    public LoginPage() {
        textLabel.setForeground(Color.GREEN);
        textLabel.setBounds(200, 75, 350, 35);
        textLabel.setText("Welcome to Quiz Program!");
        userIDLabel.setBounds(125, 150, 75, 25);
        userIDLabel.setForeground(Color.GREEN);
        userPasswordLabel.setBounds(125, 200, 75, 25);
        userPasswordLabel.setForeground(Color.GREEN);

        messageLabel.setBounds(125, 350, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 25));

        userIDField.setBounds(220, 150, 200, 25);
        userPasswordField.setBounds(220, 200, 200, 25);

        customizeButton(loginButton, 215, 250, 100, 25);
        customizeButton(resetButton, 317, 250, 100, 25);
        customizeButton(signUpButton, 317, 300, 100, 25);

        frame.add(textLabel);
        frame.add(userIDLabel);
        frame.add(userIDField);
        frame.add(userPasswordLabel);
        frame.add(userPasswordField);
        frame.add(messageLabel);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(signUpButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(620, 620);
        frame.getContentPane().setBackground(new Color(50, 50, 50)); // Set background color of the content pane
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void customizeButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setBackground(new Color(25, 25, 25));
        button.setForeground(Color.GREEN);
        button.setOpaque(false);
        button.setUI(new RoundedButtonUI());
        button.setFocusable(false);
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userIDField.setText("");
            userPasswordField.setText("");
            messageLabel.setText(""); // Clear any existing messages
        }
        if (e.getSource() == loginButton) {
            String userID = userIDField.getText();
            String password = String.valueOf(userPasswordField.getPassword());
            boolean userExists = UserLogin.userExists(userID);
            boolean validPassword = authenticateUser(userID, password);

            if (!userExists) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid Username");
            } else if (!validPassword) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid Password");
            } else {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Login Successful");
                frame.dispose();
                if (isAdmin(userID)) {
                    new AdminPanel(); // Open admin panel
                } else {
                    new Quiz(userID); // Open quiz for regular users
                }
            }
        }
        if (e.getSource() == signUpButton) {
            frame.dispose();
            new SignUp();
        }
    }

    private boolean authenticateUser(String userID, String password) {
        String query = "SELECT * FROM users WHERE users_id = ? AND users_password = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userID);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If a row is found, user is authenticated
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean isAdmin(String userID) {
        String query = "SELECT user_type FROM users WHERE users_id = ?";
        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_type").equals("admin");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    static class RoundedButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            paintBackground(g, button, button.getModel().isPressed() ? 2 : 0);
            super.paint(g, c);
        }

        private void paintBackground(Graphics g, JComponent c, int yOffset) {
            Dimension size = c.getSize();
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getBackground().darker());
            g2.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 25, 25);
            g2.setColor(c.getBackground());
            g2.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 4, 25, 25);
        }
    }
}
