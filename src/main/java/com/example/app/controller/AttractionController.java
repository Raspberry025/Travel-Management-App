package com.example.app.controller;


import com.example.app.model.Attraction;
import com.example.app.model.Booking;
import com.example.app.model.Guide;
import com.example.app.model.Tourist;
import com.example.app.util.FileHandler;
import com.example.app.util.Session;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AttractionController {

    // ===== Existing UI =====
    @FXML private TableView<Attraction> attractionTable;
    @FXML private TableColumn<Attraction, String> nameColumn;
    @FXML private TableColumn<Attraction, String> typeColumn;
    @FXML private TableColumn<Attraction, String> locationColumn;
    @FXML private TableColumn<Attraction, Integer> altitudeColumn;

    @FXML private TextField nameField;
    @FXML private TextField typeField;
    @FXML private TextField locationField;
    @FXML private TextField altitudeField;

    // ===== New booking UI =====
    @FXML private DatePicker datePicker;
    @FXML private Button bookButton;
    @FXML private Button cancelButton;
    @FXML private Label statusLabel;

    // OPTIONAL "My bookings" mini table
    @FXML private TableView<Booking> myBookingsTable;
    @FXML private TableColumn<Booking, String> bDateCol;
    @FXML private TableColumn<Booking, String> bStatusCol;
    @FXML private TableColumn<Booking, Number> bPriceCol;

    // ===== Data =====
    private final ObservableList<Attraction> attractionList = FXCollections.observableArrayList();

    // Needed for bookings
    private final ObservableList<Tourist> tourists = FXCollections.observableArrayList();
    private final ObservableList<Guide> guides = FXCollections.observableArrayList();
    private final ObservableList<Booking> masterBookings = FXCollections.observableArrayList();
    private FilteredList<Booking> myBookingsForSelected;

    @FXML
    public void initialize() {
        // attractions table columns
        nameColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        typeColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType()));
        locationColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLocation()));
        altitudeColumn.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getAltitude()).asObject());

        // Load data
        try {
            List<Attraction> loadedA = FileHandler.loadAttractions();
            attractionList.addAll(loadedA);
            attractionTable.setItems(attractionList);

            // Load for bookings
            tourists.setAll(FileHandler.loadTourists());
            guides.setAll(FileHandler.loadGuides());
            masterBookings.setAll(FileHandler.loadBookings(tourists, guides, attractionList));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load data: " + e.getMessage());
        }

        // Spinner default
        if (altitudeField != null) {
            altitudeField.setText("1200");
        }

        // Default date
        if (datePicker != null) {
            datePicker.setValue(LocalDate.now());
        }

        // Setup filtered view of bookings
        myBookingsForSelected = new FilteredList<>(masterBookings);
        if (myBookingsTable != null) {
            myBookingsTable.setItems(myBookingsForSelected);
            // Mini table columns
            if (bDateCol != null)   bDateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate() == null ? "" : c.getValue().getDate().toString()));
            if (bStatusCol != null) bStatusCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus() == null ? "" : c.getValue().getStatus()));
            if (bPriceCol != null)  bPriceCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()));
        }

        // Refresh predicate when attraction selection changes
        attractionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldA, newA) -> refreshFilter());
        refreshFilter();
    }

    // ======= Booking filter & actions =======
    private void refreshFilter() {
        if (myBookingsForSelected == null) return;

        String myUser = Session.getUserId();
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();

        myBookingsForSelected.setPredicate(b -> {
            if (b == null || selected == null || b.getAttraction() == null) return false;
            boolean sameAttraction = Objects.equals(b.getAttraction().getName(), selected.getName());
            if (Session.isAdmin()) return sameAttraction;
            return sameAttraction && Objects.equals(b.getOwnerId(), myUser);
        });

        long count = myBookingsForSelected.stream().count();
        if (statusLabel != null) {
            statusLabel.setText((Session.isAdmin() ? "All" : "Your") + " bookings for this attraction: " + count);
        }
    }

    @FXML
    public void handleBook() {
        if (Session.getCurrentUser() == null) {
            showAlert(Alert.AlertType.ERROR, "Login Required", "Please log in to book.");
            return;
        }
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an attraction.");
            return;
        }
        LocalDate date = datePicker != null ? datePicker.getValue() : LocalDate.now();
        if (date == null) {
            showAlert(Alert.AlertType.WARNING, "No Date", "Please choose a date.");
            return;
        }

        // Find a tourist profile owned by this user (you can switch to a ComboBox if multiple)
        String me = Session.getUserId();
        Tourist myTourist = tourists.stream()
                .filter(t -> me != null && me.equals(t.getOwnerid()))
                .findFirst()
                .orElse(null);
        if (myTourist == null) {
            showAlert(Alert.AlertType.WARNING, "No Tourist Profile", "Create a Tourist profile first (linked to your account).");
            return;
        }

        // Prevent duplicates (same attraction+date+owner)
        boolean exists = masterBookings.stream().anyMatch(b ->
                Objects.equals(b.getOwnerId(), me) &&
                        b.getAttraction() != null &&
                        Objects.equals(b.getAttraction().getName(), selected.getName()) &&
                        Objects.equals(b.getDate(), date) &&
                        !"CANCELLED".equalsIgnoreCase(b.getStatus())
        );
        if (exists) {
            showAlert(Alert.AlertType.INFORMATION, "Already Booked",
                    "You already have a booking for this attraction on " + date + ".");
            return;
        }

        // Derive price from attraction if you have it; otherwise set a default
        double price = 0;
        try {
            // If your Attraction has getPrice(), use it:
            // price = selected.getPrice();
            price = price == 0 ? 1000.0 : price;
        } catch (Exception ignored) {}

        // Create and persist booking
        Booking b = new Booking(myTourist, null, selected, date, "BOOKED", price, me);

        masterBookings.add(b);
        persistBookings();
        refreshFilter();
        showAlert(Alert.AlertType.INFORMATION, "Booked",
                "Booked " + selected.getName() + " on " + date + ".");
    }

    @FXML
    public void handleCancel() {
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        LocalDate date = datePicker != null ? datePicker.getValue() : null;

        if (selected == null || date == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Selection", "Select an attraction and a date to cancel.");
            return;
        }

        String me = Session.getUserId();
        Optional<Booking> match = masterBookings.stream().filter(b -> {
            boolean sameAttraction = b.getAttraction() != null &&
                    Objects.equals(b.getAttraction().getName(), selected.getName());
            boolean sameDate = Objects.equals(b.getDate(), date);
            boolean allowed = Session.isAdmin() || Objects.equals(b.getOwnerId(), me);
            boolean active = !"CANCELLED".equalsIgnoreCase(b.getStatus());
            return sameAttraction && sameDate && allowed && active;
        }).findFirst();

        if (match.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found",
                    Session.isAdmin()
                            ? "No active booking found on that date."
                            : "You don't have an active booking for this attraction on that date.");
            return;
        }

        Booking b = match.get();
        b.setStatus("CANCELLED"); // soft-cancel to keep history
        persistBookings();
        refreshFilter();
        showAlert(Alert.AlertType.INFORMATION, "Cancelled",
                "Cancelled booking for " + selected.getName() + " on " + date + ".");
    }

    private void persistBookings() {
        try {
            FileHandler.saveBookings(new ArrayList<>(masterBookings));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to save bookings: " + e.getMessage());
        }
    }

    // ===== Existing attraction add/delete =====
    @FXML
    public void handleAdd() {
        int altitude = getAltitude();
        if (altitude < 0) {
            return; // Invalid altitude, alert already shown
        }
        Attraction a = new Attraction(
                nameField.getText(),
                typeField.getText(),
                locationField.getText(),
                altitude
        );
        attractionList.add(a);
        saveAttractions();
        clearFields();
    }

    private int getAltitude() {
        try {
            return Integer.parseInt(altitudeField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid integer altitude.");
            return -1; // or some fallback value
        }
    }

    @FXML
    public void handleDelete() {
        Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            attractionList.remove(selected);
            saveAttractions();
        }
    }

    private void saveAttractions() {
        try {
            FileHandler.saveAttractions(new ArrayList<>(attractionList));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to save attractions: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.clear();
        typeField.clear();
        locationField.clear();
        altitudeField.clear();
        }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
