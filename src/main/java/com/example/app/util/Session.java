package com.example.app.util;

import com.example.app.model.User;

public final class Session {
    private static User currentUser;

    private Session() {}

    public static void setCurrentUser(User u) { currentUser = u; }

    public static User getCurrentUser() { return currentUser; }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.getisAdmin(); // uses your getter
    }

    // The “owner id” we’ll write on each row = username
    public static String getUserId() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
}
