package com.example.librarymanagementapp.dao;

import com.example.librarymanagementapp.database.DatabaseHelper;
import com.example.librarymanagementapp.loans.LoanRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // Retrieve all loan records using a join on loans, books, and users
    public static List<LoanRecord> getAllLoans() throws SQLException {
        List<LoanRecord> list = new ArrayList<>();
        String sql = """
            SELECT l.id, l.book_id, b.title AS bookName, l.user_id, u.name AS userName
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN users u ON l.user_id = u.id;
        """;
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LoanRecord lr = new LoanRecord();
                lr.setId(rs.getInt("id"));
                lr.setBookId(rs.getInt("book_id"));
                lr.setBookName(rs.getString("bookName"));
                lr.setUserId(rs.getInt("user_id"));
                lr.setUserName(rs.getString("userName"));
                list.add(lr);
            }
        }
        return list;
    }

    // Add a new loan record and update the book's availability
    public static void addLoan(int bookId, int userId) throws SQLException {
        // Begin a transaction
        Connection conn = DatabaseHelper.getConnection();
        try {
            conn.setAutoCommit(false);

            // Insert into loans table
            String insertLoan = "INSERT INTO loans (book_id, user_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertLoan)) {
                pstmt.setInt(1, bookId);
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
            }

            // Update the book's available column to 0
            String updateBook = "UPDATE books SET available = 0 WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateBook)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    // Remove a loan record and update the book's availability back to 1
    public static void removeLoan(int loanId) throws SQLException {
        // Begin a transaction
        Connection conn = DatabaseHelper.getConnection();
        try {
            conn.setAutoCommit(false);

            // First, find the bookId for the given loanId
            int bookId = -1;
            String select = "SELECT book_id FROM loans WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(select)) {
                pstmt.setInt(1, loanId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    bookId = rs.getInt("book_id");
                }
            }

            if (bookId != -1) {
                // Delete the loan record
                String deleteLoan = "DELETE FROM loans WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteLoan)) {
                    pstmt.setInt(1, loanId);
                    pstmt.executeUpdate();
                }

                // Update the book's available column to 1
                String updateBook = "UPDATE books SET available = 1 WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateBook)) {
                    pstmt.setInt(1, bookId);
                    pstmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}
