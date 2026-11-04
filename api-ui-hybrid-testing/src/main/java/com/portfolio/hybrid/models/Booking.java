package com.portfolio.hybrid.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Booking(int bookingid, int roomid, String firstname, String lastname, boolean depositpaid,
                      Dates bookingdates) {
    public record Dates(String checkin, String checkout) { }
}
