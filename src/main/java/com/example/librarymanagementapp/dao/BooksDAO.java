package com.example.librarymanagementapp.dao;

import com.example.librarymanagementapp.books.Books;
import com.example.librarymanagementapp.database.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO {

    public static void addBook(Books book) throws SQLException {
        String sql = "INSERT INTO books (title, author, available) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getAvailable());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public static List<Books> getAllBooks() throws SQLException {
        List<Books> booksList = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Books book = new Books();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setAvailable(rs.getInt("available"));
                booksList.add(book);
            }
        }
        return booksList;
    }

    public static void removeBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }
}
