# Library Management App

A simple **desktop application** for managing library books, users, and loan records. This project demonstrates modern **JavaFX** development, **SQLite** database integration, and fundamental Java concepts (OOP, JDBC, MVC).

---

## Features

1. **Books Management**  
   - Add new books (title, author, availability).  
   - View all books in a table.  
   - Search or filter books by title or author.  

2. **Users Management**  
   - Add new users (name, email, phone).  
   - View all users in a table.  
   - Search or filter users by name.  

3. **Loaned Books Management**  
   - Create a loan record linking a book to a user.  
   - Remove a loan record, marking the book as available again.  
   - View all active loans in a table (book name, user name, etc.).  
   - Simple search functionality to filter loan records.

4. **Database**  
   - Uses **SQLite** to store data in a local `.db` file.  
   - Auto-creates tables if they do not exist on startup.
  ---

## Tech Stack

- **Language**: Java 17  
- **GUI**: JavaFX 17 (FXML-based views)  
- **Database**: SQLite (via `org.xerial:sqlite-jdbc`)  
- **Build Tool**: Maven  

---  
