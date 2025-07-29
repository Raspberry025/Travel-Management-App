package com.example.app.model;
import java.time.LocalDate;
import java.util.List;

public class Booking {
    private Tourist tourist;
    private Guide guide;
    private Attraction attraction;
    private LocalDate date;
    private String status;

    public Booking(Tourist tourist, Guide  guide, Attraction attraction, LocalDate date, String status) {
        this.tourist = tourist;
        this.guide = guide;
        this.attraction = attraction;
        this.date = date;
        this.status = status;
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

    @Override
    public String toString() {
        return tourist.getName() + ";" +
                guide.getName() + ";" +
                attraction.getName() + ";" +
                date.toString() + ";" +
                status;
    }

    public static Booking fromString(String line, List<Tourist> tourists, List<Guide> guides, List<Attraction> attractions) {
        String[] parts = line.split(";");
        Tourist tourist = tourists.stream().filter(t -> t.getName().equals(parts[0])).findFirst().orElse(null);
        Guide guide = guides.stream().filter(g -> g.getName().equals(parts[1])).findFirst().orElse(null);
        Attraction attraction = attractions.stream().filter(a -> a.getName().equals(parts[2])).findFirst().orElse(null);
        LocalDate date = LocalDate.parse(parts[3]);
        String status = parts[4];
        return new Booking(tourist, guide, attraction, date, status);
    }

}
