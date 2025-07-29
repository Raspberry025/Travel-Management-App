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
}
