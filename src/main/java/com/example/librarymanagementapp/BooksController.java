package com.example.librarymanagementapp;

import com.example.librarymanagementapp.books.Books;
import com.example.librarymanagementapp.dao.BooksDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BooksController {

    @FXML
    private TableView<Books> booksTable;

    @FXML
    private TableColumn<Books, Integer> idColumn;

    @FXML
    private TableColumn<Books, String> titleColumn;

    @FXML
    private TableColumn<Books, String> authorColumn;

    @FXML
    private TableColumn<Books, Integer> availableColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Books> booksList = FXCollections.observableArrayList();
    private FilteredList<Books> filteredBooks;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        filteredBooks = new FilteredList<>(booksList, p -> true);

        booksTable.setItems(filteredBooks);

        loadBooks();
    }

    private void loadBooks() {
        try {
            List<Books> books = BooksDAO.getAllBooks();
            booksList.setAll(books);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearchBook() {
        String searchText = searchField.getText().toLowerCase();

        filteredBooks.setPredicate(book -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            String lowerCaseTitle = book.getTitle().toLowerCase();
            return lowerCaseTitle.contains(searchText);
        });
    }

    @FXML
    private void onAddBook() {
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add Book");
        titleDialog.setHeaderText("Enter the new book's title:");
        titleDialog.setContentText("Title:");
        Optional<String> titleResult = titleDialog.showAndWait();
        if (titleResult.isEmpty()) {
            return;
        }
        String title = titleResult.get();

        TextInputDialog authorDialog = new TextInputDialog();
        authorDialog.setTitle("Add Book");
        authorDialog.setHeaderText("Enter the new book's author:");
        authorDialog.setContentText("Author:");
        Optional<String> authorResult = authorDialog.showAndWait();
        if (authorResult.isEmpty()) {
            return;
        }
        String author = authorResult.get();

        Books newBook = new Books(title, author, 1);

        try {
            BooksDAO.addBook(newBook);
            loadBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onGetBook() {
        loadBooks();
        searchField.clear();
        onSearchBook();
    }

    @FXML
    private void onRemoveBook() {
        Books selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("No Selection", "Please select a book to remove.");
            return;
        }

        Optional<ButtonType> result = showConfirmation(
                "Remove Book",
                "Are you sure you want to remove this book?"
        );
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }
        try {
            BooksDAO.removeBook(selectedBook.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to remove the book from the database.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }

}