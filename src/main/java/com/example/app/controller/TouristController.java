package com.example.app.controller;

import com.example.app.util.FileHandler;
import com.example.app.model.Tourist;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TouristController {

    @FXML private TableView<Tourist> touristTable;
    @FXML private TableColumn<Tourist, String> nameColumn;
    @FXML private TableColumn<Tourist, String> nationalityColumn;
    @FXML private TableColumn<Tourist, String> contactColumn;
    @FXML private TableColumn<Tourist, String> EmergencyContactColumn;

    @FXML private TextField nameField;
    @FXML private TextField nationalityField;
    @FXML private TextField contactField;
    @FXML private TextField EmergencyContactField;

    private final ObservableList<Tourist> touristList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        nationalityColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNationality()));
        contactColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getContact()));
        EmergencyContactColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmergencyContact()));

        loadTouristData();
        touristTable.setItems(touristList);
    }

    private void loadTouristData() {
        try {
            List<Tourist> loadedTourists = FileHandler.loadTourists();
            updateTouristList(loadedTourists);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load tourist data: " + e.getMessage());
        }
    }

    private void updateTouristList(List<Tourist> loadedTourists) {
        Platform.runLater(() -> {
            touristList.clear();
            touristList.addAll(loadedTourists);
        });
    }

    @FXML
    public void handleAdd() {
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Name field cannot be empty.");
            return;
        }

        Tourist t = new Tourist(
                nameField.getText(),
                nationalityField.getText(),
                contactField.getText(),
                EmergencyContactField.getText()
        );

        touristList.add(t);
        saveData();
        clearFields();
    }

    @FXML
    public void handleDelete() {
        Tourist selected = touristTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            touristList.remove(selected);
            saveData();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid tourist.");
        }
    }

    @FXML
    public void handleUpdate() {
        Tourist selected = touristTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setName(nameField.getText());
            selected.setNationality(nationalityField.getText());
            selected.setContact(contactField.getText());
            selected.setEmergencyContact(EmergencyContactField.getText());
            touristTable.refresh();
            clearFields();
            saveData();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid tourist to update.");
        }
    }

    private void clearFields() {
        nameField.clear();
        nationalityField.clear();
        contactField.clear();
        EmergencyContactField.clear();
    }

    private void saveData() {
        try {
            FileHandler.saveTourists(new ArrayList<>(touristList));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to save tourist data: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
