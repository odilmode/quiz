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

public class AdminPanel {
    JFrame frame = new JFrame();
    JTextField questionField = new JTextField();
    JTextField[] optionFields = new JTextField[4];
    JTextField answerField = new JTextField();
    JButton addButton = new JButton("Add Question");
    JButton quitButton = new JButton("Quit");
    JButton deleteButton = new JButton("Delete Question");
    JButton viewButton = new JButton("View All Questions");
    JButton viewUsersButton = new JButton("View All Users");
    JButton backButton = new JButton("Back to Login Page");

    public static String currentAdminId = "admin";

    public AdminPanel() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(null);

        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setBounds(10, 10, 80, 25);
        questionLabel.setForeground(Color.GREEN);
        frame.add(questionLabel);

        questionField.setBounds(100, 10, 430, 25);
        frame.add(questionField);

        for (int i = 0; i < 4; i++) {
            JLabel optionLabel = new JLabel("Option " + (i + 1) + ":");
            optionLabel.setBounds(10, 50 + (i * 40), 80, 25);
            optionLabel.setForeground(Color.GREEN);
            frame.add(optionLabel);

            optionFields[i] = new JTextField();
            optionFields[i].setBounds(100, 50 + (i * 40), 430, 25);
            frame.add(optionFields[i]);
        }

        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setBounds(10, 210, 80, 25);
        answerLabel.setForeground(Color.GREEN);
        frame.add(answerLabel);

        answerField.setBounds(100, 210, 430, 25);
        frame.add(answerField);

        customizeButton(addButton, 10, 250, 150, 25);
        frame.add(addButton);

        customizeButton(deleteButton, 170, 250, 150, 25);
        frame.add(deleteButton);

        customizeButton(viewButton, 330, 250, 200, 25);
        frame.add(viewButton);

        customizeButton(viewUsersButton, 10, 290, 150, 25);
        frame.add(viewUsersButton);

        customizeButton(quitButton, 10, 410, 150, 25);
        frame.add(quitButton);

        customizeButton(backButton, 10, 450, 150, 25);
        frame.add(backButton);

        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setVisible(true);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteQuestion();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewQuestions();
            }
        });

        viewUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new ViewUsersPanel();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginPage();
            }
        });
    }

    private void customizeButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setBackground(new Color(25, 25, 25));
        button.setForeground(Color.GREEN);
        button.setOpaque(false);
        button.setUI(new RoundedButtonUI());
        button.setFocusable(false);
    }

    private void addQuestion() {
        String question = questionField.getText();
        String[] options = new String[4];
        for (int i = 0; i < 4; i++) {
            if (optionFields[i].getText().equals("")) {
                JOptionPane.showMessageDialog(frame, "No option selected");
            } else {
                options[i] = optionFields[i].getText();
            }
        }
        String answer = answerField.getText();
        if (answer.equals("")) {
            JOptionPane.showMessageDialog(frame, "No answer selected");
        }

        try (Connection conn = ConnectSQL.getConnection()) {
            String query = "INSERT INTO questions (question, option1, option2, option3, option4, answer) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, question);
            pstmt.setString(2, options[0]);
            pstmt.setString(3, options[1]);
            pstmt.setString(4, options[2]);
            pstmt.setString(5, options[3]);
            pstmt.setString(6, answer);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Question added successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteQuestion() {
        String question = questionField.getText();
        if (question.equals("")) {
            JOptionPane.showMessageDialog(frame, "No question selected");
        }

        try (Connection conn = ConnectSQL.getConnection()) {
            String query = "DELETE FROM questions WHERE question = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, question);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Question deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Question not found!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void viewQuestions() {
        try (Connection conn = ConnectSQL.getConnection()) {
            String query = "SELECT * FROM questions";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            StringBuilder questionsList = new StringBuilder();
            while (rs.next()) {
                questionsList.append("Question: ").append(rs.getString("question")).append("\n")
                        .append("Option 1: ").append(rs.getString("option1")).append("\n")
                        .append("Option 2: ").append(rs.getString("option2")).append("\n")
                        .append("Option 3: ").append(rs.getString("option3")).append("\n")
                        .append("Option 4: ").append(rs.getString("option4")).append("\n")
                        .append("Answer: ").append(rs.getString("answer")).append("\n\n");
            }
            JOptionPane.showMessageDialog(frame, questionsList.toString());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
