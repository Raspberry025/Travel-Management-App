package com.example.app.util;

import com.example.app.model.Tourist;
import com.example.app.model.Guide;
import com.example.app.model.Attraction;
import com.example.app.model.Booking;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static void saveTourists(List<Tourist> tourists, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Tourist t : tourists) {
                writer.write(t.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tourists: " + e.getMessage());
        }
    }

    public static List<Tourist> loadTourists(String path) {
        List<Tourist> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Tourist t = Tourist.fromString(line);
                list.add(t);
            }
        } catch (IOException e) {
            System.err.println("Error loading tourists: " + e.getMessage());
        }
        return list;
    }

    // --- GUIDE ---
    public static void saveGuides(List<Guide> guides, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Guide g : guides) {
                writer.write(g.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving guides: " + e.getMessage());
        }
    }

    public static List<Guide> loadGuides(String path) {
        List<Guide> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Guide.fromString(line));
            }
        } catch (IOException e) {
            System.err.println("Error loading guides: " + e.getMessage());
        }
        return list;
    }

    // --- ATTRACTION ---
    public static void saveAttractions(List<Attraction> attractions, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Attraction a : attractions) {
                writer.write(a.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving attractions: " + e.getMessage());
        }
    }

    public static List<Attraction> loadAttractions(String path) {
        List<Attraction> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Attraction.fromString(line));
            }
        } catch (IOException e) {
            System.err.println("Error loading attractions: " + e.getMessage());
        }
        return list;
    }

    // --- BOOKING ---
    public static void saveBookings(List<Booking> bookings, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Booking b : bookings) {
                writer.write(b.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    public static List<Booking> loadBookings(String path, List<Tourist> tourists, List<Guide> guides, List<Attraction> attractions) {
        List<Booking> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Booking.fromString(line, tourists, guides, attractions));
            }
        } catch (IOException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
        return list;
    }

}
