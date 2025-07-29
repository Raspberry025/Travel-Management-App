package com.example.app.controller;

import com.example.app.model.Guide;
import com.example.app.util.FileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;
import java.util.List;

public class GuideController {

    @FXML private TableView<Guide> guideTable;
    @FXML private TableColumn<Guide, String> nameColumn;
    @FXML private TableColumn<Guide, String> languagesColumn;
    @FXML private TableColumn<Guide, String> contactColumn;
    @FXML private TableColumn<Guide, Integer> experienceColumn;

    @FXML private TextField nameField;
    @FXML private TextField languagesField;
    @FXML private TextField contactField;
    @FXML private Spinner<Integer> experienceSpinner;

    private final ObservableList<Guide> guideList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        contactColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getContact()));
        languagesColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.join(", ", data.getValue().getLanguages())));
        experienceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getExperienceYears()).asObject());

        List<Guide> loaded = FileHandler.loadGuides("/com/example/app/data/guides.txt");
        guideList.addAll(loaded);

        guideTable.setItems(guideList);
        experienceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 1));
    }

    @FXML
    public void handleAdd() {
        List<String> langs = Arrays.asList(languagesField.getText().split(",\\s*"));
        Guide guide = new Guide(nameField.getText(), langs, experienceSpinner.getValue(), contactField.getText());
        guideList.add(guide);
        saveData();
        clearFields();
    }

    @FXML
    public void handleDelete() {
        Guide selected = guideTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            guideList.remove(selected);
            saveData();
        }
    }

    @FXML
    public void handleUpdate() {
        Guide selected = guideTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setName(nameField.getText());
            selected.setContact(contactField.getText());
            selected.setLanguages(Arrays.asList(languagesField.getText().split(",\\s*")));
            selected.setExperienceYears(experienceSpinner.getValue());
            guideTable.refresh();
            clearFields();
            saveData();
        }
    }

    private void clearFields() {
        nameField.clear();
        contactField.clear();
        languagesField.clear();
        experienceSpinner.getValueFactory().setValue(1);
    }

    private void saveData() {
        FileHandler.saveGuides(guideList, "/com/example/app/data/guides.txt");
    }
}
