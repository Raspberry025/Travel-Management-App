package com.example.app.model;

public class Tourist {
    private String name;
    private String nationality;
    private String contact;
    private String emergencyContact;

    public Tourist(String name, String nationality, String contact, String emergencyContact) {
        this.name = name;
        this.nationality = nationality;
        this.contact = contact;
        this.emergencyContact = emergencyContact;
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

    @Override
    public String toString() {
        return name + ";" + nationality + ";" + contact + ";" + emergencyContact;
    }

    public static Tourist fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        String[] parts = str.split(";");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid Tourist String: " + str);
        }
        return new Tourist(parts[0], parts[1], parts[2], parts[3]);
    }

}
