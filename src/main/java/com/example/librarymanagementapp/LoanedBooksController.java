package com.example.librarymanagementapp;

import com.example.librarymanagementapp.dao.LoanDAO;
import com.example.librarymanagementapp.loans.LoanRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoanedBooksController {

    @FXML
    private TableView<LoanRecord> loanTable;

    @FXML
    private TableColumn<LoanRecord, Integer> idColumn;

    @FXML
    private TableColumn<LoanRecord, Integer> bookIdColumn;

    @FXML
    private TableColumn<LoanRecord, String> bookNameColumn;

    @FXML
    private TableColumn<LoanRecord, Integer> userIdColumn;

    @FXML
    private TableColumn<LoanRecord, String> userNameColumn;

    @FXML
    private TextField searchField;

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        loadLoanRecords();
    }

    private void loadLoanRecords() {
        try {
            List<LoanRecord> loans = LoanDAO.getAllLoans();
            loanTable.getItems().setAll(loans);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load loan records.");
        }
    }

    @FXML
    private void onAddLoan() {
        // For simplicity, ask the user for a book ID and user ID.
        // In a real application, you might display a custom dialog with ComboBoxes.
        TextInputDialog bookIdDialog = new TextInputDialog();
        bookIdDialog.setTitle("Add Loan");
        bookIdDialog.setHeaderText("Enter Book ID:");
        Optional<String> bookIdResult = bookIdDialog.showAndWait();
        if (bookIdResult.isEmpty()) return;
        int bookId = Integer.parseInt(bookIdResult.get());

        TextInputDialog userIdDialog = new TextInputDialog();
        userIdDialog.setTitle("Add Loan");
        userIdDialog.setHeaderText("Enter User ID:");
        Optional<String> userIdResult = userIdDialog.showAndWait();
        if (userIdResult.isEmpty()) return;
        int userId = Integer.parseInt(userIdResult.get());

        try {
            LoanDAO.addLoan(bookId, userId);
            loadLoanRecords();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add loan record.");
        }
    }

    @FXML
    private void onRemoveLoan() {
        // Remove the selected loan record
        LoanRecord selected = loanTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a loan record to remove.");
            return;
        }
        try {
            LoanDAO.removeLoan(selected.getId());
            loadLoanRecords();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove loan record.");
        }
    }

    @FXML
    private void onSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadLoanRecords();
            return;
        }
        try {
            List<LoanRecord> loans = LoanDAO.getAllLoans();
            loans.removeIf(lr -> !(lr.getBookName().toLowerCase().contains(keyword.toLowerCase())
                    || lr.getUserName().toLowerCase().contains(keyword.toLowerCase())));
            loanTable.getItems().setAll(loans);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to search loan records.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.initOwner(rootPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
