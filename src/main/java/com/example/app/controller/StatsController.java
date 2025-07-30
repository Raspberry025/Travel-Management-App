package com.example.app.controller;

import com.example.app.model.*;
import com.example.app.util.FileHandler;
import com.example.app.util.StatsUtils;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class StatsController {

    @FXML private PieChart touristNationalityPieChart;
    @FXML private BarChart<String, Number> popularAttractionsBarChart;
    @FXML private BarChart<String, Number> bookingsPerMonthBarChart;

    private List<Tourist> tourists;
    private List<Attraction> attractions;
    private List<Booking> bookings;
    private List<Guide> guides;

    @FXML
    public void initialize() {
        try {
            tourists = FileHandler.loadTourists();
            guides = FileHandler.loadGuides();
            attractions = FileHandler.loadAttractions();
            bookings = FileHandler.loadBookings(tourists, guides, attractions);

            loadTouristNationalityChart();
            loadPopularAttractionsChart();
            loadBookingsPerMonthChart();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data for stats.");
        }
    }

    private void loadTouristNationalityChart() {
        Map<String, Long> data = StatsUtils.countTouristsByNationality(tourists);
        touristNationalityPieChart.getData().clear();

        data.forEach((nationality, count) -> {
            touristNationalityPieChart.getData().add(new PieChart.Data(nationality, count));
        });
    }

    private void loadPopularAttractionsChart() {
        Map<String, Long> data = StatsUtils.countBookingsByAttraction(bookings);
        popularAttractionsBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        data.forEach((attraction, count) -> series.getData().add(new XYChart.Data<>(attraction, count)));
        popularAttractionsBarChart.getData().add(series);
    }

    private void loadBookingsPerMonthChart() {
        Map<String, Long> data = StatsUtils.countBookingsPerMonth(bookings);
        bookingsPerMonthBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        data.forEach((month, count) -> series.getData().add(new XYChart.Data<>(month, count)));
        bookingsPerMonthBarChart.getData().add(series);
    }

    @FXML
    private void exportStats() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Statistics");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8))) {
                writer.println("Tourist Nationality,Count");
                StatsUtils.countTouristsByNationality(tourists)
                        .forEach((k, v) -> writer.println(k + "," + v));

                writer.println("\nPopular Attractions,Count");
                StatsUtils.countBookingsByAttraction(bookings)
                        .forEach((k, v) -> writer.println(k + "," + v));

                writer.println("\nBookings Per Month,Count");
                StatsUtils.countBookingsPerMonth(bookings)
                        .forEach((k, v) -> writer.println(k + "," + v));

                showAlert(Alert.AlertType.INFORMATION, "Success", "Stats exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export stats: " + e.getMessage());
            }
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
