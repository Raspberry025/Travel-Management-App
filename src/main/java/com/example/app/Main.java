package com.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.scene.text.Font;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Locale defaultLocale = new Locale("en");
        ResourceBundle bundle = ResourceBundle.getBundle("messages", defaultLocale);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"), bundle);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add(getClass().getResource("/styles/nepali-theme.css").toExternalForm());
        primaryStage.setTitle(bundle.getString("login.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
