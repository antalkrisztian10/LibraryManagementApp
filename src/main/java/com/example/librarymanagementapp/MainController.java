package com.example.librarymanagementapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private AnchorPane bookPane;

    @FXML
    private AnchorPane usersPane;

    @FXML
    private AnchorPane loanedBooksPane;

    @FXML
    public void initialize() {
        loadBooksView();
        bookPane.setVisible(true);
        usersPane.setVisible(false);
        loanedBooksPane.setVisible(false);
    }

    @FXML
    private void onShowBooks() {
        loadBooksView();
        bookPane.setVisible(true);
        usersPane.setVisible(false);
        loanedBooksPane.setVisible(false);
    }

    @FXML
    private void onShowUsers() {
        loadUsersView();
        usersPane.setVisible(true);
        bookPane.setVisible(false);
        loanedBooksPane.setVisible(false);
    }

    @FXML
    private void onShowLoanedBooks() {
        loadLoanedBooksView();
        loanedBooksPane.setVisible(true);
        bookPane.setVisible(false);
        usersPane.setVisible(false);
    }

    private void loadBooksView() {
        try {
            // Load books-view.fxml and set it as the content of bookPane
            AnchorPane booksContent = FXMLLoader.load(getClass().getResource("books-view.fxml"));
            bookPane.getChildren().setAll(booksContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUsersView() {
        try {
            // Load users-view.fxml and set it as the content of usersPane
            AnchorPane usersContent = FXMLLoader.load(getClass().getResource("users-view.fxml"));
            usersPane.getChildren().setAll(usersContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLoanedBooksView() {
        try {
            AnchorPane loanedBooksContent = FXMLLoader.load(getClass().getResource("loanedbooks-view.fxml"));
            loanedBooksPane.getChildren().setAll(loanedBooksContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
