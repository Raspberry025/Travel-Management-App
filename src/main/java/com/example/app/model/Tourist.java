package com.example.app.model;

public class Tourist {
    private String name;
    private String nationality;
    private String contact;
    private String emergencyContact;
    private String ownerid;

    public Tourist(String name, String nationality, String contact, String emergencyContact, String ownerid) {
        this.name = name;
        this.nationality = nationality;
        this.contact = contact;
        this.emergencyContact = emergencyContact;
        this.ownerid = ownerid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmergencyContact() {
        return emergencyContact;
    }
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact; }

    public String getOwnerid() {
        return ownerid;
    }
    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    @Override
    public String toString() {
        return name + ";" + nationality + ";" + contact + ";" + emergencyContact + ";" + (ownerid != null ? ownerid : "");
    }

    public static Tourist fromString(String line) {
        String[] parts = line.split(";");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid tourist data: " + line);
        }
        String name = parts[0];
        String nationality = parts[1];
        String contact = parts[2];
        String emergencyContact = parts[3];
        // ownerId is optional; if missing, set to null or empty
        String ownerId = parts.length >= 5 ? parts[4] : null;

        return new Tourist(name, nationality, contact, emergencyContact, ownerId);
    }


}
