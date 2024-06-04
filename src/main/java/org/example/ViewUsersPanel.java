package org.example;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewUsersPanel {
    JFrame frame = new JFrame();
    JTable usersTable = new JTable();
    DefaultTableModel tableModel = new DefaultTableModel();
    JTextField deleteUserField = new JTextField();
    JButton deleteUserButton = new JButton("Delete User");
    JButton backButton = new JButton("Back");

    public ViewUsersPanel() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(null);

        tableModel.addColumn("User ID");
        tableModel.addColumn("User Type");
        usersTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBounds(10, 10, 560, 400);
        frame.add(scrollPane);

        JLabel deleteUserLabel = new JLabel("Delete User:");
        deleteUserLabel.setBounds(10, 420, 80, 25);
        deleteUserLabel.setForeground(Color.GREEN);
        frame.add(deleteUserLabel);

        deleteUserField.setBounds(100, 420, 200, 25);
        frame.add(deleteUserField);

        customizeButton(deleteUserButton,310, 420, 120, 25);
        frame.add(deleteUserButton);

        customizeButton(backButton, 10, 460, 80, 25);
        frame.add(backButton);

        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setVisible(true);

        loadUsers();

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new AdminPanel(); // Go back to Admin Panel
            }
        });
    }
    private void customizeButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setBackground(new Color(25, 25, 25));
        button.setForeground(Color.GREEN);
        button.setOpaque(false);
        button.setUI(new AdminPanel.RoundedButtonUI());
        button.setFocusable(false);
    }
    private static class RoundedButtonUI extends BasicButtonUI {
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

    private void loadUsers() {
        try (Connection conn = ConnectSQL.getConnection()) {
            String query = "SELECT * FROM users";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("users_id");
                String userType = rs.getString("user_type");
                tableModel.addRow(new Object[]{userId, userType});
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        String userId = deleteUserField.getText();

        if (userId.equals(AdminPanel.currentAdminId)) {
            JOptionPane.showMessageDialog(frame, "You cannot delete your own account!");
            return;
        }

        try (Connection conn = ConnectSQL.getConnection()) {
            String deleteScoresQuery = "DELETE FROM user_scores WHERE user_id = ?";
            PreparedStatement deleteScoresStmt = conn.prepareStatement(deleteScoresQuery);
            deleteScoresStmt.setString(1, userId);
            deleteScoresStmt.executeUpdate();


            String deleteUserQuery = "DELETE FROM users WHERE users_id = ?";
            PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserQuery);
            deleteUserStmt.setString(1, userId);
            int rowsAffected = deleteUserStmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "User deleted successfully!");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).equals(userId)) {
                        tableModel.removeRow(i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "User not found!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
