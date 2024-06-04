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

public class ViewScoresPage {
    private JFrame frame;
    private JTable scoresTable;
    private DefaultTableModel tableModel;
    private JButton retakeQuizButton;
    private JButton quitButton;
    private JButton cleanAllScoreButton;
    private String username;

    public ViewScoresPage(String username) {
        this.username = username;

        frame = new JFrame("User Scores");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(null);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Score");
        tableModel.addColumn("Percentage");
        tableModel.addColumn("Attempt Time");

        scoresTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(scoresTable);
        scrollPane.setBounds(10, 10, 560, 400);
        frame.add(scrollPane);

        cleanAllScoreButton = new JButton("Clean All Scores");
        customizeButton(cleanAllScoreButton, 10, 420, 150, 25);
        frame.add(cleanAllScoreButton);
        cleanAllScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteScores();
            }
        });

        retakeQuizButton = new JButton("Retake Quiz");
        customizeButton(retakeQuizButton, 170, 420, 150, 25);
        frame.add(retakeQuizButton);
        retakeQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Quiz(username);
            }
        });

        quitButton = new JButton("Quit");
        customizeButton(quitButton, 330, 420, 100, 25);
        frame.add(quitButton);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });

        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setVisible(true);

        loadUserScores();
    }

    private void customizeButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setBackground(new Color(25, 25, 25));
        button.setForeground(Color.GREEN);
        button.setOpaque(false);
        button.setUI(new RoundedButtonUI());
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

    private void loadUserScores() {
        String query = "SELECT score, percentage, attempt_time FROM user_scores WHERE user_id = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int score = rs.getInt("score");
                int percentage = rs.getInt("percentage");
                String attemptTime = rs.getString("attempt_time");

                tableModel.addRow(new Object[]{score, percentage + "%", attemptTime});
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteScores() {
        try (Connection conn = ConnectSQL.getConnection()) {
            String deleteScoresQuery = "DELETE FROM user_scores WHERE user_id = ?";
            PreparedStatement deleteScoresStmt = conn.prepareStatement(deleteScoresQuery);
            deleteScoresStmt.setString(1, username);
            deleteScoresStmt.executeUpdate();
            // Clear table model
            tableModel.setRowCount(0);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
