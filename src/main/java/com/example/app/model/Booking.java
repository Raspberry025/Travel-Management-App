package com.example.app.model;
import java.time.LocalDate;

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
}
