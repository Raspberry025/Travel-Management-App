package com.example.app.util;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class LangSwitch extends HBox{
    private final ToggleButton toggleButton = new ToggleButton();
    private Locale currentLocale = new Locale("en");

    public LangSwitch(Consumer<Locale> onLanguageChange, ResourceBundle bundle) {
        this.getChildren().add(toggleButton);
        updateButtonText(bundle);

        toggleButton.setOnAction(e ->{
           currentLocale = toggleButton.isSelected() ? new Locale("ne") : new Locale("en");
           onLanguageChange.accept(currentLocale);
        });
    }

    public void updateButtonText(ResourceBundle bundle) {
        if(toggleButton.isSelected()) {
            toggleButton.setText(bundle.getString("toggle.nepali"));
        } else{
            toggleButton.setText(bundle.getString("toggle.english"));
        }
    }
}
