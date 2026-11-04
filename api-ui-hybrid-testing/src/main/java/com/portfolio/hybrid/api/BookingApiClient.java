package com.portfolio.hybrid.api;

import com.portfolio.hybrid.models.Booking;
import io.restassured.response.Response;
import java.util.List;

public final class BookingApiClient extends ApiClient {
    public BookingApiClient() { super(); }
    public BookingApiClient(String token) { super(token); }
    public Response create(Object booking) { return post("/booking", booking); }
    public Booking booking(int id) { return get("/booking/" + id).then().statusCode(200).spec(JSON).extract().as(Booking.class); }
    public List<Booking> bookings(int roomId) {
        return request().queryParam("roomid", roomId).get("/booking").then().statusCode(200).spec(JSON)
            .extract().jsonPath().getList("bookings", Booking.class);
    }
    public Response update(int id, Booking booking) { return put("/booking/" + id, booking); }
    public Response remove(int id) { return delete("/booking/" + id); }
}
