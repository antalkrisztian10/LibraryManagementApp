package com.example.librarymanagementapp.dao;

import com.example.librarymanagementapp.database.DatabaseHelper;
import com.example.librarymanagementapp.users.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO {

    public static void addUser(Users user) throws SQLException {
        String sql = "INSERT INTO users (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.executeUpdate();
        }
    }

    public static List<Users> getAllUsers() throws SQLException {
        List<Users> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                userList.add(user);
            }
        }
        return userList;
    }

    public static void removeUser(int userId) throws SQLException {
        String remove = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(remove)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

}
