package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz implements ActionListener {
    List<Question> questionList = new ArrayList<>();
    String guess;
    String answer;
    int index;
    int correct_guesses = 0;
    int total_questions;
    int result;
    int seconds = 10;
    private String username;

    JFrame frame = new JFrame();
    JTextField textfield = new JTextField();
    JTextArea textarea = new JTextArea();
    JButton buttonA = new JButton();
    JButton buttonB = new JButton();
    JButton buttonC = new JButton();
    JButton buttonD = new JButton();
    JLabel answer_labelA = new JLabel();
    JLabel answer_labelB = new JLabel();
    JLabel answer_labelC = new JLabel();
    JLabel answer_labelD = new JLabel();
    JLabel time_label = new JLabel();
    JLabel seconds_left = new JLabel();
    JTextField number_right = new JTextField();
    JTextField percent_right = new JTextField();

    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            seconds--;
            seconds_left.setText(String.valueOf(seconds));
            if (seconds <= 0) {
                displayAnswer();
            }
        }
    });

    public Quiz(String username) {
        this.username = username;
        loadQuestionsFromDatabase();
        Collections.shuffle(questionList);
        total_questions = questionList.size();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 850);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(null);
        frame.setResizable(false);

        textfield.setBounds(0, 0, 850, 50);
        textfield.setBackground(new Color(25, 25, 25));
        textfield.setForeground(new Color(25, 255, 0));
        textfield.setFont(new Font("Arial", Font.PLAIN, 30));
        textfield.setBorder(BorderFactory.createBevelBorder(1));
        textfield.setHorizontalAlignment(JTextField.CENTER);
        textfield.setEditable(false);

        textarea.setBounds(0, 50, 850, 100);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setBackground(new Color(25, 25, 25));
        textarea.setForeground(new Color(25, 255, 0));
        textarea.setFont(new Font("Arial", Font.PLAIN, 30));
        textarea.setBorder(BorderFactory.createBevelBorder(1));
        textarea.setEditable(false);

        buttonA.setBounds(0, 150, 100, 100);
        buttonA.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonA.setFocusable(false);
        buttonA.addActionListener(this);
        buttonA.setText("A");

        buttonB.setBounds(0, 250, 100, 100);
        buttonB.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonB.setFocusable(false);
        buttonB.addActionListener(this);
        buttonB.setText("B");

        buttonC.setBounds(0, 350, 100, 100);
        buttonC.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonC.setFocusable(false);
        buttonC.addActionListener(this);
        buttonC.setText("C");

        buttonD.setBounds(0, 450, 100, 100);
        buttonD.setFont(new Font("MV Boli", Font.BOLD, 35));
        buttonD.setFocusable(false);
        buttonD.addActionListener(this);
        buttonD.setText("D");

        answer_labelA.setBounds(125, 150, 750, 100);
        answer_labelA.setBackground(new Color(50, 50, 50));
        answer_labelA.setForeground(new Color(25, 255, 0));
        answer_labelA.setFont(new Font("MV Boli", Font.BOLD, 35));

        answer_labelB.setBounds(125, 250, 750, 100);
        answer_labelB.setBackground(new Color(50, 50, 50));
        answer_labelB.setForeground(new Color(25, 255, 0));
        answer_labelB.setFont(new Font("MV Boli", Font.BOLD, 35));

        answer_labelC.setBounds(125, 350, 750, 100);
        answer_labelC.setBackground(new Color(50, 50, 50));
        answer_labelC.setForeground(new Color(25, 255, 0));
        answer_labelC.setFont(new Font("MV Boli", Font.BOLD, 35));

        answer_labelD.setBounds(125, 450, 750, 100);
        answer_labelD.setBackground(new Color(50, 50, 50));
        answer_labelD.setForeground(new Color(25, 255, 0));
        answer_labelD.setFont(new Font("MV Boli", Font.BOLD, 35));

        seconds_left.setBounds(635, 610, 100, 100);
        seconds_left.setBackground(new Color(25, 25, 25));
        seconds_left.setForeground(new Color(255, 0, 0));
        seconds_left.setFont(new Font("MV Boli", Font.BOLD, 60));
        seconds_left.setBorder(BorderFactory.createBevelBorder(1));
        seconds_left.setOpaque(true);
        seconds_left.setHorizontalAlignment(JTextField.CENTER);
        seconds_left.setText(String.valueOf(seconds));

        time_label.setBounds(635, 575, 100, 25);
        time_label.setBackground(new Color(50, 50, 50));
        time_label.setForeground(new Color(255, 0, 0));
        time_label.setFont(new Font("MV Boli", Font.PLAIN, 16));
        time_label.setHorizontalAlignment(JTextField.CENTER);
        time_label.setText("timer:");

        number_right.setBounds(325, 355, 200, 100);
        number_right.setBackground(new Color(25, 25, 25));
        number_right.setForeground(new Color(25, 255, 0));
        number_right.setFont(new Font("MV Boli", Font.BOLD, 50));
        number_right.setBorder(BorderFactory.createBevelBorder(1));
        number_right.setHorizontalAlignment(JTextField.CENTER);
        number_right.setEditable(false);

        percent_right.setBounds(325, 455, 200, 100);
        percent_right.setBackground(new Color(25, 25, 25));
        percent_right.setForeground(new Color(25, 255, 0));
        percent_right.setFont(new Font("MV Boli", Font.BOLD, 50));
        percent_right.setBorder(BorderFactory.createBevelBorder(1));
        percent_right.setHorizontalAlignment(JTextField.CENTER);
        percent_right.setEditable(false);

        frame.add(time_label);
        frame.add(seconds_left);
        frame.add(answer_labelA);
        frame.add(answer_labelB);
        frame.add(answer_labelC);
        frame.add(answer_labelD);
        frame.add(buttonA);
        frame.add(buttonB);
        frame.add(buttonC);
        frame.add(buttonD);
        frame.add(textarea);
        frame.add(textfield);
        frame.setVisible(true);

        nextQuestion();
    }

    private void loadQuestionsFromDatabase() {
        String query = "SELECT * FROM questions";

        try (Connection conn = ConnectSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String questionText = rs.getString("question");
                String option1 = rs.getString("option1");
                String option2 = rs.getString("option2");
                String option3 = rs.getString("option3");
                String option4 = rs.getString("option4");
                String answer = rs.getString("answer");

                Question question = new Question(questionText, option1, option2, option3, option4, answer);
                questionList.add(question);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void nextQuestion() {
        if (index >= total_questions) {
            results();
        } else {
            Question question = questionList.get(index);
            textfield.setText("Question: " + (index + 1));
            textarea.setText(question.getQuestionText());
            List<String> options = new ArrayList<>();
            options.add(question.getOption1());
            options.add(question.getOption2());
            options.add(question.getOption3());
            options.add(question.getOption4());
            Collections.shuffle(options);
            answer_labelA.setText(options.get(0));
            answer_labelB.setText(options.get(1));
            answer_labelC.setText(options.get(2));
            answer_labelD.setText(options.get(3));
            answer = question.getAnswer(); // Set the correct answer
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);

        if (e.getSource() == buttonA) {
            guess = answer_labelA.getText();
        }
        if (e.getSource() == buttonB) {
            guess = answer_labelB.getText();
        }
        if (e.getSource() == buttonC) {
            guess = answer_labelC.getText();
        }
        if (e.getSource() == buttonD) {
            guess = answer_labelD.getText();
        }

        if (guess.equals(answer)) {
            correct_guesses++;
        }

        displayAnswer();
    }

    public void displayAnswer() {
        timer.stop();

        // Mark answers with appropriate colors
        if (answer_labelA.getText().equals(answer)) {
            answer_labelA.setForeground(new Color(25, 255, 0)); // Green for correct answer
        } else {
            answer_labelA.setForeground(new Color(255, 0, 0)); // Red for incorrect answer
        }

        if (answer_labelB.getText().equals(answer)) {// Green for correct answer
            answer_labelB.setForeground(new Color(25, 255, 0)); // Green for correct answer
        } else {
            answer_labelB.setForeground(new Color(255, 0, 0)); // Red for incorrect answer
        }

        if (answer_labelC.getText().equals(answer)) {
            answer_labelC.setForeground(new Color(25, 255, 0)); // Green for correct answer
        } else {
            answer_labelC.setForeground(new Color(255, 0, 0));// Red for incorrect answer
        }

        if (answer_labelD.getText().equals(answer)) {
            answer_labelD.setForeground(new Color(25, 255, 0)); // Green for correct answer
        } else {
            answer_labelD.setForeground(new Color(255, 0, 0)); // Red for incorrect answer
        }

        Timer pause = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset colors
                buttonA.setBackground(null);
                buttonB.setBackground(null);
                buttonC.setBackground(null);
                buttonD.setBackground(null);
                answer_labelA.setForeground(new Color(25, 255, 0));
                answer_labelB.setForeground(new Color(25, 255, 0));
                answer_labelC.setForeground(new Color(25, 255, 0));
                answer_labelD.setForeground(new Color(25, 255, 0));

                answer = "";
                seconds = 10;
                seconds_left.setText(String.valueOf(seconds));
                buttonA.setEnabled(true);
                buttonB.setEnabled(true);
                buttonC.setEnabled(true);
                buttonD.setEnabled(true);
                index++;
                nextQuestion();
            }
        });
        pause.setRepeats(false);
        pause.start();
    }

    public void results() {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);

        result = (int) ((correct_guesses / (double) total_questions) * 100);

        textfield.setText("Results!");
        textarea.setText("");
        answer_labelA.setText("");
        answer_labelB.setText("");
        answer_labelC.setText("");
        answer_labelD.setText("");

        number_right.setText("(" + correct_guesses + "/" + total_questions + ")");
        percent_right.setText(result + "%");

        JButton viewScoresButton = new JButton();

        viewScoresButton.setText("View Scores");
        viewScoresButton.setBounds(325, 650, 200, 50);
        viewScoresButton.setBackground(Color.BLACK);
        viewScoresButton.setForeground(Color.GREEN);
        viewScoresButton.setOpaque(true);
        viewScoresButton.setContentAreaFilled(true);
        viewScoresButton.setBorderPainted(false);
        viewScoresButton.setFont(new Font("Arial", Font.BOLD, 20));
        viewScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ViewScoresPage when the button is clicked
                new ViewScoresPage(username);
            }
        });

        frame.remove(time_label);
        frame.remove(seconds_left);
        frame.add(viewScoresButton);
        frame.remove(buttonA);
        frame.remove(buttonB);
        frame.remove(buttonC);
        frame.remove(buttonD);
        frame.add(answer_labelA);
        frame.add(answer_labelB);
        frame.add(answer_labelC);
        frame.add(answer_labelD);

        updateScoreAndPercentage(correct_guesses, result, username);
        SavingScores.insertScores(username, correct_guesses, result);

        frame.add(number_right);
        frame.add(percent_right);
        frame.revalidate();
        frame.repaint();
    }

    private void updateScoreAndPercentage(int correct_guesses, int result, String username) {
        String query = "UPDATE users SET users_score = ?, users_percentage = ? WHERE users_id = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, correct_guesses);
            pstmt.setDouble(2, result);
            pstmt.setString(3, username);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User's score and percentage updated successfully.");
            } else {
                System.out.println("No rows affected. User may not exist.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
