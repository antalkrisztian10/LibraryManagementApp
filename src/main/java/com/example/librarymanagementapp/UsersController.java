package com.example.librarymanagementapp;

import com.example.librarymanagementapp.dao.UsersDAO;
import com.example.librarymanagementapp.users.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsersController {

    @FXML
    private TableView<Users> usersTable;

    @FXML
    private TableColumn<Users, Integer> idColumn;

    @FXML
    private TableColumn<Users, String> nameColumn;

    @FXML
    private TableColumn<Users, String> emailColumn;

    @FXML
    private TableColumn<Users, String> phoneColumn;

    @FXML
    private TextField searchField;

    @FXML
    private AnchorPane rootPane;

    private ObservableList<Users> usersList = FXCollections.observableArrayList();
    private FilteredList<Users> filteredUsers;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        filteredUsers = new FilteredList<>(usersList, p -> true);

        usersTable.setItems(filteredUsers);

        loadUsers();
    }

    private void loadUsers() {
        try {
            List<Users> allUsers = UsersDAO.getAllUsers();
            usersList.setAll(allUsers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearchUser() {
        String searchText = searchField.getText().toLowerCase();

        filteredUsers.setPredicate(user -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            String lowerCaseName = user.getName().toLowerCase();
            return lowerCaseName.contains(searchText);
        });
    }

    @FXML
    private void onAddUser() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add User");
        nameDialog.setHeaderText("Enter the new user's name:");
        nameDialog.setContentText("Name:");
        Optional<String> nameResult = nameDialog.showAndWait();
        if (nameResult.isEmpty()) {
            return;
        }
        String name = nameResult.get();

        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Add User");
        emailDialog.setHeaderText("Enter the new user's email:");
        emailDialog.setContentText("Email:");
        Optional<String> emailResult = emailDialog.showAndWait();
        if (emailResult.isEmpty()) {
            return;
        }
        String email = emailResult.get();

        TextInputDialog phoneDialog = new TextInputDialog();
        phoneDialog.setTitle("Add User");
        phoneDialog.setHeaderText("Enter the new user's phone:");
        phoneDialog.setContentText("Phone:");
        Optional<String> phoneResult = phoneDialog.showAndWait();
        if (phoneResult.isEmpty()) {
            return;
        }
        String phone = phoneResult.get();

        Users newUser = new Users(name, email, phone);
        try {
            UsersDAO.addUser(newUser);
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRefreshUsers() {
        loadUsers();
        searchField.clear();
        onSearchUser();
    }

    @FXML
    private void onRemoveUser() {
        // Remove the selected loan record
        Users selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to remove.");
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
            UsersDAO.removeUser(selected.getId());
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to remove a user.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.initOwner(rootPane.getScene().getWindow());
        alert.setTitle(title);
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