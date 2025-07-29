package com.example.app.controller;

import com.example.app.model.Tourist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

        touristTable.setItems(touristList);
    }

    @FXML
    public void handleAdd() {
        Tourist t = new Tourist(
                nameField.getText(),
                nationalityField.getText(),
                contactField.getText(),
                EmergencyContactField.getText()
        );
        touristList.add(t);
        clearFields();
    }

    @FXML
    public void handleDelete() {
        Tourist selected = touristTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            touristList.remove(selected);
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
        }
    }

    private void clearFields() {
        nameField.clear();
        nationalityField.clear();
        contactField.clear();
        EmergencyContactField.clear();
    }
}
