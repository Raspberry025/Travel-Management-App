package com.example.app.util;

import com.example.app.model.Tourist;
import com.example.app.model.Guide;
import com.example.app.model.Attraction;
import com.example.app.model.Booking;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String DATA_DIR = "app-data";

    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }

    // === TOURIST METHODS ===
    public static void saveTourists(List<Tourist> tourists) throws IOException {
        Path filePath = Paths.get(DATA_DIR, "tourists.txt");
        saveObjects(tourists, filePath);
    }

    public static List<Tourist> loadTourists() throws IOException {
        Path filePath = Paths.get(DATA_DIR, "tourists.txt");
        return loadObjects(filePath, Tourist::fromString);
    }

    // === GUIDE METHODS ===
    public static void saveGuides(List<Guide> guides) throws IOException {
        Path filePath = Paths.get(DATA_DIR, "guides.txt");
        saveObjects(guides, filePath);
    }

    public static List<Guide> loadGuides() throws IOException {
        Path filePath = Paths.get(DATA_DIR, "guides.txt");
        return loadObjects(filePath, Guide::fromString);
    }

    // === ATTRACTION METHODS ===
    public static void saveAttractions(List<Attraction> attractions) throws IOException {
        Path filePath = Paths.get(DATA_DIR, "attractions.txt");
        saveObjects(attractions, filePath);
    }

    public static List<Attraction> loadAttractions() throws IOException {
        Path filePath = Paths.get(DATA_DIR, "attractions.txt");
        return loadObjects(filePath, Attraction::fromString);
    }

    // === BOOKING METHODS ===
    public static void saveBookings(List<Booking> bookings) throws IOException {
        Path filePath = Paths.get(DATA_DIR, "bookings.txt");
        saveObjects(bookings, filePath);
    }

    public static List<Booking> loadBookings(List<Tourist> tourists, List<Guide> guides, List<Attraction> attractions) throws IOException {
        Path filePath = Paths.get(DATA_DIR, "bookings.txt");
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        List<Booking> bookings = new ArrayList<>();

        for (String line : lines) {
            try {
                if (!line.trim().isEmpty()) {
                    bookings.add(Booking.fromString(line.trim(), tourists, guides, attractions));
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Skipping invalid booking: " + e.getMessage());
            }
        }

        return bookings;
    }

    // === PRIVATE HELPER METHODS ===
    private static <T> void saveObjects(List<T> objects, Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());

        List<String> lines = new ArrayList<>();
        for (T obj : objects) {
            lines.add(obj.toString());
        }

        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    private static <T> List<T> loadObjects(Path filePath, Parser<T> parser) throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        List<T> objects = new ArrayList<>();

        for (String line : lines) {
            try {
                if (!line.trim().isEmpty()) {
                    objects.add(parser.parse(line.trim()));
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Skipping invalid data: " + e.getMessage());
            }
        }

        return objects;
    }

    @FunctionalInterface
    private interface Parser<T> {
        T parse(String s) throws IllegalArgumentException;
    }
}
