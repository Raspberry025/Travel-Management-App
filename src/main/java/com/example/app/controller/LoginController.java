package com.example.app.controller;

import com.example.app.model.User;
import com.example.app.util.FileHandler;
import com.example.app.util.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

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
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter username and password.");
            return;
        }

        String hashedInput = PasswordUtils.hashPassword(password);

        User user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPasswordHash().equals(hashedInput))
                .findFirst()
                .orElse(null);

        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            return;
        }

        // Login success: open appropriate dashboard
        openDashboard(user);
    }

    @FXML
    private void openSignupPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/app/signup.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open signup page.");
        }
    }


    private void openDashboard(User user) {
        try {
            Locale locale = new Locale("en");
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app/dashboard.fxml"), bundle);
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUser(user);  // Pass user to dashboard controller

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle((user.getisAdmin() ? "Admin" : "User") + " Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open dashboard.");
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
