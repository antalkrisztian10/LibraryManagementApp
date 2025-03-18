package com.example.librarymanagementapp.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/jokim/IdeaProjects/LibraryManagementApp/library.db";

    public static Connection getConnection() throws SQLException {
        try {
            // Force load the driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }

        File dbFile = new File("library.db");

        return DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
    }

    public static void initializeDatabase() {
        String createBooksTable = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                available INTEGER DEFAULT 1
            );
        """;
        try (Connection conn = getConnection()) {
            conn.createStatement().execute(createBooksTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                phone TEXT NOT NULL
            );
        """;
        try (Connection conn = getConnection()) {
            conn.createStatement().execute(createUsersTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String createLoansTable = """
        CREATE TABLE IF NOT EXISTS loans (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            book_id INTEGER NOT NULL,
            user_id INTEGER NOT NULL,
            loan_date TEXT DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (book_id) REFERENCES books(id),
            FOREIGN KEY (user_id) REFERENCES users(id)
        );
        """;
        try (Connection conn = getConnection()) {
            conn.createStatement().execute(createLoansTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
