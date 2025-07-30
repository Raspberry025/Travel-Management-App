package com.example.app.model;

public class User {
    private String username;
    private String passwordHash;  // store hashed password
    private String role;
    private boolean isAdmin;

    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isAdmin = role.equalsIgnoreCase("admin");
    }

    public User(String username, String passwordHash, String role, boolean isAdmin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isAdmin = isAdmin;  // Explicitly set isAdmin
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public boolean getisAdmin() {
        return isAdmin;
    }

    public static User fromString(String line) {
        String[] parts = line.split(";");
        if (parts.length == 4) {
            return new User(parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[3]));
        } else if (parts.length == 3) {
            return new User(parts[0], parts[1], parts[2]); // auto-derive isAdmin
        } else {
            return null; // invalid format
        }
    }


    @Override
    public String toString() {
        return username + ";" + passwordHash + ";" + role + ";" + isAdmin;
    }

}
