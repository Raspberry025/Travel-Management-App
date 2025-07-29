package com.example.app.model;

public class Attraction {
    private String name;
    private String type;
    private String location;
    private int altitude;

    public Attraction(String name, String type, String location, int altitude) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.altitude = altitude;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {this.name = name; }

    public String getType() {
        return type;
    }
    public void setType(String type) {this.type = type; }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {this.location = location; }

    public int getAltitude() {
        return altitude;
    }
    public void setAltitude(int altitude) {this.altitude = altitude; }
}
