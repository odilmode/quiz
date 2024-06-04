package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SavingScores {
    public static void insertScores(String userId, int score, int percentage) {
        String query = "INSERT INTO user_scores (user_id, score, percentage) VALUES (?, ?, ?)";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            pstmt.setInt(2, score);
            pstmt.setInt(3, percentage);

            pstmt.executeUpdate();
            System.out.println("User login inserted successfully.");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}