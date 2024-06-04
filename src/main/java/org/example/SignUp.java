package org.example;

import org.example.LoginPage;
import org.example.UserLogin;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;

public class SignUp extends JFrame implements ActionListener, FocusListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton signUpButton;
    JButton returnToLoginButton;
    JLabel statusLabel;


    public SignUp() {

        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 420);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(50, 50, 50)); // Set background color

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 100, 75, 25);
        usernameLabel.setForeground(Color.GREEN); // Set text color

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 150, 75, 25);
        passwordLabel.setForeground(Color.GREEN); // Set text color

        usernameField = new JTextField();
        usernameField.setBounds(125, 100, 200, 25);
        usernameField.addFocusListener(this); // Add focus listener

        passwordField = new JPasswordField();
        passwordField.setBounds(125, 150, 200, 25);
        passwordField.addFocusListener(this); // Add focus listener

        signUpButton = new JButton("Sign Up");
        customizeButton(signUpButton, 125, 200, 100, 25);

        returnToLoginButton = new JButton("Return to Login Page");
        customizeButton(returnToLoginButton, 125, 250, 200, 25);

        statusLabel = new JLabel("");
        statusLabel.setBounds(125, 300, 250, 35);
        statusLabel.setForeground(Color.RED);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signUpButton);
        panel.add(returnToLoginButton);
        panel.add(statusLabel);

        add(panel);
        setVisible(true);
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
        if (e.getSource() == signUpButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.length() < 4 || password.length() < 4) {
                statusLabel.setText("Username and Password should be at least 4 characters long");
            } else {
                if (userExists(username)) {
                    statusLabel.setText("Username already exists.");
                    return;
                } else {
                    UserLogin.insertLogin(username, password);
                    statusLabel.setText("Signed Up successfully.");
                    statusLabel.setForeground(Color.GREEN);
                }
            }
        }
        if (e.getSource() == returnToLoginButton) {
            dispose();
            new LoginPage();
        }
    }

    private boolean userExists(String username) {
        return UserLogin.userExists(username);
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

    @Override
    public void focusGained(FocusEvent e) {
        // Not needed, but required by the FocusListener interface
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == usernameField) {
            if (usernameField.getText().length() < 4) {
                statusLabel.setText("Username should be at least 4 characters long");
                statusLabel.setBounds(50, 300, 275, 35);
            } else {
                statusLabel.setText("");
            }
        } else if (e.getSource() == passwordField) {
            if (new String(passwordField.getPassword()).length() < 4) {
                statusLabel.setText("Password should be at least 4 characters long");
                statusLabel.setBounds(50, 300, 275, 35);
            } else {
                statusLabel.setText("");
            }
        }
    }
}
