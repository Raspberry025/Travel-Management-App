package com.example.app.util;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        String password = "admin123";  // Change to your desired password
        String hashed = PasswordUtils.hashPassword(password);
        System.out.println("Hashed password: " + hashed);
    }
}
