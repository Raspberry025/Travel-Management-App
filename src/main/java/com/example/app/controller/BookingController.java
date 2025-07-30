package com.example.app.controller;

import com.example.app.repo.BookingRepository;
import com.example.app.model.*;
import com.example.app.util.SeasonalConfig;
import com.example.app.util.BookingUtils;
import com.example.app.util.Session;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class BookingController {
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> touristColumn;
    @FXML private TableColumn<Booking, String> guideColumn;
    @FXML private TableColumn<Booking, String> attractionColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, LocalDate> dateColumn;
    @FXML private TableColumn<Booking, Double> priceColumn;
    @FXML private TableColumn<Booking, String> emergencyReportCol;

    @FXML private ComboBox<Tourist> touristCombo;
    @FXML private ComboBox<Guide> guideCombo;
    @FXML private ComboBox<Attraction> attractionCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField statusField;

    private final BookingRepository repo = BookingRepository.get();
    private FilteredList<Booking> view;

    @FXML
    public void initialize() {
        // Columns (null-safe)
        touristColumn.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getTourist() == null ? "" : d.getValue().getTourist().getName()));
        guideColumn.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getGuide() == null ? "" : d.getValue().getGuide().getName()));
        attractionColumn.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getAttraction() == null ? "" : d.getValue().getAttraction().getName()));
        statusColumn.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getStatus() == null ? "" : d.getValue().getStatus()));
        dateColumn.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getDate()));
        priceColumn.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getPrice()).asObject());
        emergencyReportCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getEmergencyReport() == null ? "" : d.getValue().getEmergencyReport()));

        // Shared lists (Dashboard should have called repo.loadAll())
        attractionCombo.setItems(FXCollections.observableArrayList(repo.getAttractions()));
        guideCombo.setItems(FXCollections.observableArrayList(repo.getGuides()));

        // Tourists: admins can see all; users see only theirs
        var myUser = Session.getUserId();
        var touristChoices = repo.getTourists().stream()
                .filter(t -> Session.isAdmin() || Objects.equals(t.getOwnerid(), myUser))
                .toList();
        touristCombo.setItems(FXCollections.observableArrayList(touristChoices));

        // Bind table to shared master list with user filter
        view = new FilteredList<>(repo.masterList());
        applyUserFilter();
        bookingTable.setItems(view);

        // Default date
        if (datePicker != null) datePicker.setValue(LocalDate.now());
    }

    private void applyUserFilter() {
        if (Session.isAdmin()) {
            view.setPredicate(b -> true);
        } else {
            String me = Session.getUserId();
            view.setPredicate(b -> Objects.equals(b.getOwnerId(), me));
        }
    }

    // === Add Booking ===
    @FXML
    public void handleAdd() {
        Attraction selectedAttraction = attractionCombo.getValue();
        LocalDate selectedDate = datePicker.getValue();
        Tourist selectedTourist = touristCombo.getValue();
        Guide selectedGuide = guideCombo.getValue();
        String status = (statusField.getText() == null || statusField.getText().isBlank())
                ? "BOOKED" : statusField.getText().trim();

        if (Session.getCurrentUser() == null) {
            showAlert(Alert.AlertType.ERROR, "Login Required", "Please log in to add a booking.");
            return;
        }
        if (selectedAttraction == null || selectedDate == null || selectedTourist == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Select attraction, date, and tourist.");
            return;
        }
        if (!Session.isAdmin() && !Objects.equals(selectedTourist.getOwnerid(), Session.getUserId())) {
            showAlert(Alert.AlertType.ERROR, "Forbidden", "You can only book with your own tourist profile.");
            return;
        }
        if (selectedDate.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Invalid Date", "You cannot make bookings for past dates.");
            return;
        }

        // Altitude notice
        if (selectedAttraction.getAltitude() > 3000) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Altitude Sickness Warning");
            alert.setHeaderText("High Altitude Destination");
            alert.setContentText("The selected attraction is above 3000 meters. Please prepare accordingly.");
            alert.showAndWait();
        }

        // Seasonal restriction
        if (!BookingUtils.isBookingAllowed(selectedAttraction, selectedDate)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Booking Not Allowed");
            alert.setHeaderText("High Altitude Booking Restriction");
            alert.setContentText("Bookings for high altitude attractions are disabled during June to August.");
            alert.showAndWait();
            return;
        }

        // Prevent duplicates for same attraction+date+owner (and active)
        boolean exists = repo.masterList().stream().anyMatch(b ->
                Objects.equals(b.getOwnerId(), Session.getUserId()) &&
                        b.getAttraction() != null &&
                        Objects.equals(b.getAttraction().getName(), selectedAttraction.getName()) &&
                        Objects.equals(b.getDate(), selectedDate) &&
                        !"CANCELLED".equalsIgnoreCase(b.getStatus())
        );
        if (exists) {
            showAlert(Alert.AlertType.INFORMATION, "Already Booked",
                    "You already have a booking for this attraction on " + selectedDate + ".");
            return;
        }

        // Pricing + discount
        double basePrice = getBasePriceForAttraction(selectedAttraction);
        double finalPrice = basePrice;
        if (SeasonalConfig.isFestival(selectedDate)) {
            finalPrice = basePrice * (1 - SeasonalConfig.FESTIVAL_DISCOUNT);
            Alert discountAlert = new Alert(Alert.AlertType.INFORMATION);
            discountAlert.setTitle("Festival Discount");
            discountAlert.setHeaderText("15% Discount Applied!");
            discountAlert.setContentText("Your booking qualifies for a 15% discount.");
            discountAlert.showAndWait();
        }

        // Create and persist booking
        Booking booking = new Booking(
                selectedTourist,
                selectedGuide,          // may be null
                selectedAttraction,
                selectedDate,
                status,
                finalPrice
        );
        // IMPORTANT: set owner for filtering to work
        booking.setOwnerId(Session.getUserId());

        try {
            repo.addAndSave(booking);
            bookingTable.getSelectionModel().select(booking);
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Booked", "Booking added.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to save booking: " + e.getMessage());
        }
    }

    // === Cancel Selected ===
    @FXML
    public void handleCancelSelected() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Select a booking to cancel.");
            return;
        }
        if (!Session.isAdmin() && !Objects.equals(selected.getOwnerId(), Session.getUserId())) {
            showAlert(Alert.AlertType.ERROR, "Forbidden", "You can only cancel your own booking.");
            return;
        }
        if ("CANCELLED".equalsIgnoreCase(selected.getStatus())) {
            showAlert(Alert.AlertType.INFORMATION, "Already Cancelled", "This booking is already cancelled.");
            return;
        }

        selected.setStatus("CANCELLED");
        try {
            repo.persist();
            bookingTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Booking cancelled.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to save: " + e.getMessage());
        }
    }

    // === Emergency report (uses repo.persist) ===
    @FXML
    private void handleEmergency() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Booking Selected", "Please select a booking to report an emergency.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Report Emergency");
        dialog.setHeaderText("Emergency Report for: " + (selected.getTourist() == null ? "" : selected.getTourist().getName()));
        dialog.setContentText("Describe the emergency:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(description -> {
            selected.reportEmergency(description);
            try {
                repo.persist();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bookingTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Emergency Logged", "Emergency reported successfully.");
        });
    }

    private void clearFields() {
        touristCombo.setValue(null);
        guideCombo.setValue(null);
        attractionCombo.setValue(null);
        if (datePicker != null) datePicker.setValue(LocalDate.now());
        statusField.clear();
    }

    private double getBasePriceForAttraction(Attraction attraction) {
        if (attraction.getAltitude() > 3000) return 5000;
        if ("Cultural".equalsIgnoreCase(attraction.getType())) return 2000;
        return 3000;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
