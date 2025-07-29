package com.example.app.model;
import java.util.ArrayList;
import java.util.List;

public class Guide {
    private String name;
    private List<String> languages;
    private int experienceYears;
    private String contact;

    public Guide(String name, List<String> languages, int experienceYears, String contact) {
        this.name = name;
        this.languages = languages;
        this.contact = contact;
        this.experienceYears = experienceYears;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public List<String> getLanguages() {
        return languages;
    }
    public void setLanguages(List<String> languages) { this.languages = languages; }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) { this.contact = contact; }

    public int getExperienceYears() {
        return experienceYears;
    }
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears; }

    @Override
    public String toString() {
        return name + ";" + String.join(",", languages) + ";" + experienceYears + ";" + contact;
    }

    public static Guide fromString(String line) {
        String[] parts = line.split(";");
        String name = parts[0];
        List<String> languages = List.of(parts[1].split(","));
        int experienceYears = Integer.parseInt(parts[2]);
        String contact = parts[3];
        return new Guide(name, languages, experienceYears, contact);
    }

}

