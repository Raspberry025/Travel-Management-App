package com.example.app.util;

import com.example.app.model.Attraction;
import com.example.app.model.Booking;
import com.example.app.model.Tourist;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public final class StatsUtils {
    private StatsUtils() {}

    /** Count tourists per nationality (null-safe). */
    public static Map<String, Long> countTouristsByNationality(List<Tourist> tourists) {
        if (tourists == null) return Collections.emptyMap();

        return tourists.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        t -> {
                            String n = t.getNationality();
                            return (n == null || n.isBlank()) ? "(Unknown)" : n;
                        },
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    /**
     * Count bookings per attraction name (null-safe).
     * Uncomment the filter below if you want to EXCLUDE cancelled bookings.
     */
    public static Map<String, Long> countBookingsByAttraction(List<Booking> bookings) {
        if (bookings == null) return Collections.emptyMap();

        return bookings.stream()
                .filter(Objects::nonNull)
                .filter(b -> b.getAttraction() != null)                    // ignore rows without an attraction
                // .filter(b -> !"CANCELLED".equalsIgnoreCase(b.getStatus())) // optional: exclude cancelled
                .collect(Collectors.groupingBy(
                        b -> {
                            Attraction a = b.getAttraction();
                            String name = (a == null) ? null : a.getName();
                            return (name == null || name.isBlank()) ? "(Unknown)" : name;
                        },
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    /**
     * Count bookings per month (yyyy-MM). Sorted ascending by month.
     * Null-safe; optionally exclude cancelled by uncommenting the filter.
     */
    public static Map<String, Long> countBookingsPerMonth(List<Booking> bookings) {
        if (bookings == null) return Collections.emptyMap();

        DateTimeFormatter ymFmt = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Long> counts = bookings.stream()
                .filter(Objects::nonNull)
                .map(Booking::getDate)
                .filter(Objects::nonNull)
                // .filter(d -> /* if you need to check booking status you’ll need the whole Booking, not just date */ true)
                .map(d -> YearMonth.from(d).format(ymFmt))
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        // Return a TreeMap to keep keys sorted by yyyy-MM
        return new TreeMap<>(counts);
    }

    /** Helper: top-N by value (desc). */
    public static List<Map.Entry<String, Long>> topN(Map<String, Long> counts, int n) {
        if (counts == null || counts.isEmpty() || n <= 0) return List.of();
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .toList();
    }
}
