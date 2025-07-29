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
}

