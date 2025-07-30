package com.example.app.repo;

import com.example.app.model.*;
import com.example.app.util.FileHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class BookingRepository {
    private static final BookingRepository INSTANCE = new BookingRepository();

    private final ObservableList<Booking> master = FXCollections.observableArrayList();
    private List<Tourist> tourists = new ArrayList<>();
    private List<Guide> guides = new ArrayList<>();
    private List<Attraction> attractions = new ArrayList<>();

    private BookingRepository() {}

    public static BookingRepository get() { return INSTANCE; }

    public ObservableList<Booking> masterList() { return master; }
    public List<Tourist> getTourists() { return tourists; }
    public List<Guide> getGuides() { return guides; }
    public List<Attraction> getAttractions() { return attractions; }

    public void loadAll() throws IOException {
        tourists = FileHandler.loadTourists();
        guides = FileHandler.loadGuides();
        attractions = FileHandler.loadAttractions();
        master.setAll(FileHandler.loadBookings(tourists, guides, attractions));
    }

    public void reloadBookingsOnly() throws IOException {
        master.setAll(FileHandler.loadBookings(tourists, guides, attractions));
    }

    public void persist() throws IOException {
        FileHandler.saveBookings(new ArrayList<>(master));
    }

    public void addAndSave(Booking b) throws IOException {
        master.add(b);
        persist();
    }

    public void saveOnFxThread() {
        Platform.runLater(() -> {
            try { persist(); } catch (IOException ignored) {}
        });
    }
}
