package com.portfolio.hybrid.data;

import com.portfolio.hybrid.models.*;
import java.time.LocalDate;
import java.util.*;

public final class TestData {
    private TestData() { }
    public static Room room() {
        String marker = "portfolio-" + UUID.randomUUID();
        return new Room(0, marker, "Single", false, "/images/room1.jpg", marker, List.of("WiFi"), 125);
    }
    public static Booking booking(int roomId) {
        var start = LocalDate.now().plusDays(60);
        return new Booking(0, roomId, "Portfolio" + UUID.randomUUID().toString().replace("-", "").substring(0, 8), "Guest", false,
            new Booking.Dates(start.toString(), start.plusDays(2).toString()));
    }
}
