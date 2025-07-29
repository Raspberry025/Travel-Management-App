package com.example.app.controller;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;


public class ReportsController {
    @FXML private PieChart nationalityChart;
    @FXML private BarChart<String, Number> bookingChart;

    @FXML
    public void initialize() {
        nationalityChart.getData().addAll(
                new PieChart.Data("USA", 40),
                new PieChart.Data("Nepal", 30),
                new PieChart.Data("UK", 20),
                new PieChart.Data("Others", 10)
        );

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2025");
        series.getData().add(new XYChart.Data<>("Jan", 5));
        series.getData().add(new XYChart.Data<>("Feb", 9));
        series.getData().add(new XYChart.Data<>("Mar", 15));
        bookingChart.getData().add(series);
    }
}
