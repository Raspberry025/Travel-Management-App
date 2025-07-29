package com.example.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class DashboardController {

    @FXML
    private Label welcomeText;

    @FXML
    public void initialize() {
        welcomeText.setText("Welcome to DashBoard!");
    }

    private void openWindow(String fxmlFile, String title) {
        try {
            // Note the leading slash - this makes it absolute from classpath root
            String fullPath = "/com/example/app/" + fxmlFile;
            URL fxmlUrl = getClass().getResource(fullPath);
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find resource: " + fullPath);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load window");
            alert.setContentText("Failed to load" + fxmlFile + ": " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleTourists(ActionEvent event) {
        openWindow("tourist.fxml", "Tourist Management");
    }

    @FXML
    public void handleAttractions(ActionEvent event) {
        openWindow("attraction.fxml", "Attraction Management");
    }

    @FXML
    public void handleBookings(ActionEvent event) {
        openWindow("booking.fxml", "Booking Management");
    }

    @FXML
    public void handleGuides(ActionEvent event) {
        openWindow("guide.fxml", "Guide Management");
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
