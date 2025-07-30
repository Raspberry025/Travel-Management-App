package com.example.app.controller;

import com.example.app.model.User;
import com.example.app.util.LangSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class DashboardController {

    @FXML private BorderPane rootPane;

    @FXML private Label welcomeText;
    @FXML private Label centerWelcomeLabel;

    @FXML private Button Tourists;
    @FXML private Button Attractions;
    @FXML private Button Bookings;
    @FXML private Button Guides;
    @FXML private Button Exit;
    @FXML private Button adminOnlyButton;
    @FXML private User CurrentUser;
    @FXML private Menu viewMenu;
    @FXML private MenuItem statsMenuItem;

    @FXML private VBox leftVBox;

    @FXML private HBox languageToggleContainer;

    private Locale currentLocale = new Locale("en");
    private ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);
    private LangSwitch languageSwitcher;

    @FXML
    public void initialize() {
        Font notoFont = Font.loadFont(getClass().getResourceAsStream("/Fonts/NotoSansDevanagari-Regular.ttf"), 16);

        // Apply the font to labels that show Nepali text
        if (notoFont != null) {
            welcomeText.setFont(notoFont);
            centerWelcomeLabel.setFont(notoFont);
        } else {
            System.err.println("Failed to load Noto Sans Devanagari font.");
        }
        languageSwitcher = new LangSwitch(this::switchLanguage, bundle);
        rootPane.setTop(languageSwitcher);
        BorderPane.setAlignment(languageSwitcher, Pos.TOP_RIGHT);
        BorderPane.setMargin(languageSwitcher, new javafx.geometry.Insets(10));

        updateTexts();
    }

    private void switchLanguage(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("messages", currentLocale);

        languageSwitcher.updateButtonText(bundle);
        updateTexts();
    }

    private void updateTexts() {
        welcomeText.setText(bundle.getString("dashboard.title"));
        centerWelcomeLabel.setText(bundle.getString("label.welcome"));

        Tourists.setText(bundle.getString("button.tourists"));
        Attractions.setText(bundle.getString("button.attractions"));
        Bookings.setText(bundle.getString("button.bookings"));
        Guides.setText(bundle.getString("button.guides"));
        Exit.setText(bundle.getString("button.exit"));
    }

    private void openWindow(String fxmlFile, String title) {
        try {
            // Note the leading slash - this makes it absolute from classpath root
            String fullPath = "/com/example/app/" + fxmlFile;
            URL fxmlUrl = getClass().getResource(fullPath);
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find resource: " + fullPath);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load window");
            alert.setContentText("Failed to load" + fxmlFile + ": " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void openStatsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app/stats.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Statistics Dashboard");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Optional: block interaction with main window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.CurrentUser = user;

        welcomeText.setText("Welcome, " + user.getUsername());

        boolean isAdmin = user.getisAdmin();

        Bookings.setVisible(isAdmin);
        Guides.setVisible(isAdmin);

        if(!isAdmin) {
            viewMenu.setVisible(false);
        }
    }

    @FXML
    public void handleTourists(ActionEvent event) {
        openWindow("tourist.fxml", "Tourist Management");
    }

    @FXML
    public void handleAttractions(ActionEvent event) {
        openWindow("attraction.fxml", "Attraction Management");
    }

    @FXML
    public void handleBookings(ActionEvent event) {
        openWindow("booking.fxml", "Booking Management");
    }

    @FXML
    public void handleGuides(ActionEvent event) {
        openWindow("guide.fxml", "Guide Management");
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
