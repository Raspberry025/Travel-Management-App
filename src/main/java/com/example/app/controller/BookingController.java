package com.example.app.controller;

import com.example.app.model.*;
import com.example.app.util.FileHandler;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
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

    @FXML
    public void initialize() {
        touristColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTourist().getName()));
        guideColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGuide().getName()));
        attractionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAttraction().getName()));
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDate()));

        List<Booking> loaded = FileHandler.loadBookings(
                "/com/example/app/data/bookings.txt",
                touristCombo.getItems(),
                guideCombo.getItems(),
                attractionCombo.getItems()
        );

        bookingTable.setItems(bookingList);

        touristCombo.setItems(FXCollections.observableArrayList(
                new Tourist("Alex", "USA", "111", "999")
        ));
        guideCombo.setItems(FXCollections.observableArrayList(
                new Guide("Kiran", List.of("English", "Nepali"), 5, "9800000000")
        ));
        attractionCombo.setItems(FXCollections.observableArrayList(
                new Attraction("Annapurna Circuit", "Trek", "Ghorepani", 4130)
        ));
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
        FileHandler.saveBookings(bookingList, "/com/example/app/data/bookings.txt");
    }
}
