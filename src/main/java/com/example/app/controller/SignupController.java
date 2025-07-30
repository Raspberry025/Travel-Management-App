package com.example.app.controller;

import com.example.app.model.User;
import com.example.app.util.FileHandler;
import com.example.app.util.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;

public class SignupController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private List<User> users;

    @FXML
    public void initialize() {
        try {
            users = FileHandler.loadUsers();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load users data.");
        }
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Passwords do not match.");
            return;
        }

        // Check if username already exists
        boolean usernameExists = users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
        if (usernameExists) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Username already taken.");
            return;
        }

        // Hash password and create new user (default role = "user", isAdmin = false)
        String hashedPassword = PasswordUtils.hashPassword(password);
        User newUser = new User(username, hashedPassword, "user");

        users.add(newUser);

        try {
            FileHandler.saveUsers(users);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully. You can now log in.");
            goToLogin();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save new user.");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/app/Login.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open login page.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
