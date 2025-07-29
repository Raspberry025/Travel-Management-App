package com.example.app.controller;


import com.example.app.model.Attraction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AttractionController {
    @FXML private TableView<Attraction> attractionTable;
    @FXML private TableColumn<Attraction, String> nameColumn;
    @FXML private TableColumn<Attraction, String> typeColumn;
    @FXML private TableColumn<Attraction, String> locationColumn;
    @FXML private TableColumn<Attraction, Integer> altitudeColumn;

    @FXML private TextField nameField;
    @FXML private TextField typeField;
    @FXML private TextField locationField;
    @FXML private Spinner<Integer> altitudeSpinner;

    private final ObservableList<Attraction> attractionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        typeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        locationColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));
        altitudeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAltitude()).asObject());

        attractionTable.setItems(attractionList);
        altitudeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 8000, 0));
    }

    @FXML
    public void handleAdd() {
        Attraction a = new Attraction(
                nameField.getText(),
                typeField.getText(),
                locationField.getText(),
                altitudeSpinner.getValue()
        );
        attractionList.add(a);
        clearFields();
    }

    private void clearFields() {
        nameField.clear();
        typeField.clear();
        locationField.clear();
        altitudeSpinner.getValueFactory().setValue(0);
    }
}
