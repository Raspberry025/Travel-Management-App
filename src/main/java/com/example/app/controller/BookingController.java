package com.example.app.controller;

import com.example.app.model.*;
import com.example.app.util.FileHandler;
import com.example.app.util.SeasonalConfig;
import com.example.app.util.BookingUtils;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
        Attraction selectedAttraction = attractionCombo.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedAttraction == null || selectedDate == null || touristCombo.getValue() == null || guideCombo.getValue() == null || statusField.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Missing Fields", "Please fill all fields before booking.");
            return;
        }

        if (selectedDate.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Invalid Date", "You cannot make bookings for past dates.");
            return;
        }


        //Checking for booking restrictions
        if(!BookingUtils.isBookingAllowed(selectedAttraction, selectedDate)) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Booking is not allowed");
            alert.setHeaderText("High Altitude Booking Restriction");
            alert.setContentText("Bookings for high altitude attractions are disabled during June to August");
            alert.showAndWait();
            return;
        }
        double basePrice = getBasePriceForAttraction(selectedAttraction);
        double finalPrice = basePrice;

        // Check festival discount
        if (SeasonalConfig.isFestival(selectedDate)) {
            finalPrice = basePrice * (1 - SeasonalConfig.FESTIVAL_DISCOUNT);
            Alert discountAlert = new Alert(AlertType.INFORMATION);
            discountAlert.setTitle("Festival Discount");
            discountAlert.setHeaderText("15% Discount Applied!");
            discountAlert.setContentText("Your booking qualifies for a 15% discount.");
            discountAlert.showAndWait();
        }
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

    private double getBasePriceForAttraction(Attraction attraction) {
        // Replace this with actual price logic if your Attraction model has pricing
        return 1000.0;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

