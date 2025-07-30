package com.example.app.util;

import com.example.app.model.Tourist;
import com.example.app.model.Attraction;
import com.example.app.model.Booking;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatsUtils {

    public static Map<String, Long> countTouristsByNationality(List<Tourist> tourists) {
        return tourists.stream()
                .collect(Collectors.groupingBy(Tourist::getNationality, Collectors.counting()));
    }

    public static Map<String, Long> countBookingsByAttraction(List<Booking> bookings) {
        return bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getAttraction().getName(), Collectors.counting()));
    }

    public static Map<String, Long> countBookingsPerMonth(List<Booking> bookings) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return bookings.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getDate().format(formatter),
                        TreeMap::new, // keeps keys sorted
                        Collectors.counting()
                ));
    }
}
