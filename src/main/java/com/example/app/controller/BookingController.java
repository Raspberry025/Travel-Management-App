package com.example.app.controller;

import com.example.app.model.*;
import com.example.app.util.FileHandler;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;


public class BookingController {
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> touristColumn;
    @FXML private TableColumn<Booking, String> guideColumn;
    @FXML private TableColumn<Booking, String> attractionColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, LocalDate> dateColumn;

    @FXML private ComboBox<Tourist> touristCombo;
    @FXML private ComboBox<Guide> guideCombo;
    @FXML private ComboBox<Attraction> attractionCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField statusField;

    private final ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private List<Tourist> touristList;
    private List<Guide> guideList;
    private List<Attraction> attractionList;

    @FXML
    public void initialize() {
        touristColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTourist().getName()));
        guideColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGuide().getName()));
        attractionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAttraction().getName()));
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDate()));

        try {
            touristList = FileHandler.loadTourists();
            guideList = FileHandler.loadGuides();
            attractionList = FileHandler.loadAttractions();

            touristCombo.setItems(FXCollections.observableArrayList(touristList));
            guideCombo.setItems(FXCollections.observableArrayList(guideList));
            attractionCombo.setItems(FXCollections.observableArrayList(attractionList));

            List<Booking> loaded = FileHandler.loadBookings(touristList, guideList, attractionList);
            bookingList.addAll(loaded);
            bookingTable.setItems(bookingList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAdd() {
        Booking booking = new Booking(
                touristCombo.getValue(),
                guideCombo.getValue(),
                attractionCombo.getValue(),
                datePicker.getValue(),
                statusField.getText()
        );
        bookingList.add(booking);
        saveData();
        clearFields();
    }

    private void clearFields() {
        touristCombo.setValue(null);
        guideCombo.setValue(null);
        attractionCombo.setValue(null);
        datePicker.setValue(null);
        statusField.clear();
    }
    private void saveData() {
        try {
            FileHandler.saveBookings(FXCollections.observableArrayList(bookingList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
