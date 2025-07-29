package com.example.app.util;

import java.time.LocalDate;
import java.util.List;

public class SeasonalConfig {
    public static final List<LocalDate> FESTIVAL_DATES = List.of(
            LocalDate.of(2025,10,10),
            LocalDate.of(2025,11,1)
    );

    public static boolean isFestival(LocalDate date) {
        return FESTIVAL_DATES.contains(date);

    }

    public static final double FESTIVAL_DISCOUNT = 0.15;
}
