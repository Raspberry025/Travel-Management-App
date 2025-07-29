package com.example.app.util;

import com.example.app.model.Attraction;
import java.time.LocalDate;
import java.time.Month;

public class BookingUtils {

    public static boolean isBookingAllowed(Attraction attraction, LocalDate date) {
        if (attraction == null || date == null) return true;

        int altitude = attraction.getAltitude(); // You MUST have a getAltitude() method
        int month = date.getMonthValue();

        // High-altitude block: June–August
        if(altitude > 3000 && (month >= 6 && month <= 8)) {
            return false;
        }

        return true;
    }
}