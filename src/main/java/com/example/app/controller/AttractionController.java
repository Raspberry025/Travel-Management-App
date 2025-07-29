package com.example.app.controller;

import com.example.app.model.Attraction;
import com.example.app.util.FileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

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

        try {
            List<Attraction> loaded = FileHandler.loadAttractions();
            attractionList.addAll(loaded);
            attractionTable.setItems(attractionList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        altitudeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 8000, 1200));
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
        saveData();
        clearFields();
    }

    @FXML
    public void handleDelete() {
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            attractionList.remove(selected);
            saveData();
        }
    }

    private void saveData() {
        try {
            FileHandler.saveAttractions(FXCollections.observableArrayList(attractionList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        typeField.clear();
        locationField.clear();
        altitudeSpinner.getValueFactory().setValue(1200);
    }
}
