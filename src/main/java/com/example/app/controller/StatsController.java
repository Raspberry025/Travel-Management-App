package com.example.app.controller;

import com.example.app.model.Booking;
import com.example.app.model.Tourist;
import com.example.app.repo.BookingRepository;
import com.example.app.util.StatsUtils;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class StatsController {

    @FXML private PieChart touristNationalityPieChart;
    @FXML private BarChart<String, Number> popularAttractionsBarChart;
    @FXML private BarChart<String, Number> bookingsPerMonthBarChart;

    // We’ll read from the shared repository, not re-load from disk
    private final BookingRepository repo = BookingRepository.get();

    private List<Tourist> tourists;
    private List<Booking> bookings;

    @FXML
    public void initialize() {
        try {
            // Use the shared, already-loaded lists
            tourists  = repo.getTourists();
            bookings  = repo.masterList(); // ObservableList<Booking>

            loadTouristNationalityChart();
            loadPopularAttractionsChart();
            loadBookingsPerMonthChart();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to prepare data for stats: " + e.getMessage());
        }
    }

    private void loadTouristNationalityChart() {
        Map<String, Long> data = StatsUtils.countTouristsByNationality(tourists);
        touristNationalityPieChart.getData().clear();

        if (data.isEmpty()) {
            // Optional: show nothing instead of dummy slice
            return;
        }
        data.forEach((nationality, count) ->
                touristNationalityPieChart.getData().add(new PieChart.Data(nationality, count)));
    }

    private void loadPopularAttractionsChart() {
        Map<String, Long> byAttraction = StatsUtils.countBookingsByAttraction(bookings);
        popularAttractionsBarChart.getData().clear();

        if (byAttraction.isEmpty()) {
            // Defensive: nothing to plot
            return;
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        byAttraction.forEach((name, total) ->
                series.getData().add(new XYChart.Data<>(name, total)));
        popularAttractionsBarChart.getData().setAll(series);
    }

    private void loadBookingsPerMonthChart() {
        Map<String, Long> perMonth = StatsUtils.countBookingsPerMonth(bookings);
        bookingsPerMonthBarChart.getData().clear();

        if (perMonth.isEmpty()) {
            return;
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        perMonth.forEach((month, count) ->
                series.getData().add(new XYChart.Data<>(month, count)));
        bookingsPerMonthBarChart.getData().setAll(series);
    }

    @FXML
    private void exportStats() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Statistics");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;

        try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
            writer.println("Tourist Nationality,Count");
            StatsUtils.countTouristsByNationality(tourists)
                    .forEach((k, v) -> writer.println(csv(k) + "," + v));

            writer.println();
            writer.println("Popular Attractions,Count");
            StatsUtils.countBookingsByAttraction(bookings)
                    .forEach((k, v) -> writer.println(csv(k) + "," + v));

            writer.println();
            writer.println("Bookings Per Month,Count");
            StatsUtils.countBookingsPerMonth(bookings)
                    .forEach((k, v) -> writer.println(csv(k) + "," + v));

            showAlert(Alert.AlertType.INFORMATION, "Success", "Stats exported to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to export stats: " + e.getMessage());
        }
    }

    // Basic CSV escaping for commas/quotes
    private String csv(String s) {
        if (s == null) return "";
        String out = s.replace("\"", "\"\"");
        if (out.contains(",") || out.contains("\"") || out.contains("\n")) {
            out = "\"" + out + "\"";
        }
        return out;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
