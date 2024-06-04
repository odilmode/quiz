package org.example;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogin {
    public static void insertLogin(String userId, String password) {
        String query = "INSERT INTO users (users_id, users_password) VALUES (?, ?)";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            pstmt.executeUpdate();
            System.out.println("User login inserted successfully.");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static boolean userExists(String username) {
        String query = "SELECT * FROM users WHERE users_id = ?";

        try (Connection conn = ConnectSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
