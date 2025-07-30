package com.example.app.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Booking {
    private Tourist tourist;
    private Guide guide;
    private Attraction attraction;
    private LocalDate date;
    private String status;
    private String emergencyReport;
    private double price;
    private String ownerId;

    public Booking(Tourist tourist, Guide guide, Attraction attraction,
                   LocalDate date, String status, double price) {
        this(tourist, guide, attraction, date, status, price, null);
    }

    public Booking(Tourist tourist, Guide guide, Attraction attraction,
                   LocalDate date, String status, double price, String ownerId) {
        this.tourist = tourist;
        this.guide = guide;
        this.attraction = attraction;
        this.date = date;
        this.status = status;
        this.price = price;
        this.ownerId = ownerId;
    }

    public Tourist getTourist() {
        return tourist;
    }
    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public Guide getGuide() {
        return guide;
    }
    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public Attraction getAttraction() {
        return attraction;
    }
    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public void reportEmergency(String message) {
        this.emergencyReport = "[" + LocalDateTime.now() + "] " + message;
    }
    public String getEmergencyReport() { return emergencyReport; }
    @Override
    public String toString() {
        String touristName    = tourist != null ? safe(tourist.getName())       : "";
        String guideName      = guide != null   ? safe(guide.getName())         : "";
        String attractionName = attraction != null ? safe(attraction.getName()) : "";
        String dateStr        = date != null    ? date.toString()               : "";
        String statusStr      = status != null  ? status                         : "";
        String priceStr       = Double.toString(price);
        String emergency      = emergencyReport != null ? emergencyReport.replace(";", ",") : "";
        String owner          = ownerId != null ? ownerId : "";

        // IMPORTANT: include the semicolon between emergency and owner
        return String.join(";", touristName, guideName, attractionName, dateStr,
                statusStr, priceStr, emergency, owner);
    }

    private static String safe(String s) { return s == null ? "" : s.replace(";", ","); }

    public static Booking fromString(String line, List<Tourist> tourists, List<Guide> guides, List<Attraction> attractions) {
        // keep trailing empties if you can: split(";", -1)
        String[] parts = line.split(";", -1);
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid booking line: " + line);
        }

        String touristKey = parts[0];
        String guideKey   = parts[1];
        String attractionKey = parts[2];

        Tourist tourist = tourists.stream()
                .filter(t -> t.getName().equals(touristKey))
                .findFirst()
                .orElse(null);

        Guide guide = guideKey.isEmpty() ? null :
                guides.stream().filter(g -> g.getName().equals(guideKey)).findFirst().orElse(null);

        // REQUIRED: attraction must resolve, otherwise skip this row
        Attraction attraction = attractions.stream()
                .filter(a -> a.getName().equals(attractionKey))
                .findFirst()
                .orElse(null);
        if (attraction == null) {
            throw new IllegalArgumentException("Unknown attraction: " + attractionKey);
        }

        LocalDate date = parts[3].isEmpty() ? null : LocalDate.parse(parts[3]);
        String status  = parts[4].isEmpty() ? "BOOKED" : parts[4];

        double price = 0;
        try { price = parts[5].isEmpty() ? 0 : Double.parseDouble(parts[5]); } catch (NumberFormatException ignored) {}

        String emergency = parts.length >= 7 ? (parts[6].isEmpty() ? null : parts[6]) : null;
        String ownerId   = parts.length >= 8 ? (parts[7].isEmpty() ? null : parts[7]) : null;

        Booking b = new Booking(tourist, guide, attraction, date, status, price, ownerId);
        b.reportEmergency(emergency == null ? null : emergency); // or b.emergencyReport = emergency if you keep the field
        return b;
    }
}

