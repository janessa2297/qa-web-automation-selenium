package com.portfolio.hybrid.api;

import com.portfolio.hybrid.base.OwnedResources;
import com.portfolio.hybrid.data.TestData;
import com.portfolio.hybrid.models.Booking;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class BookingApiTest {
    @Test(groups = {"api", "negative", "regression"}, description = "API-05: missing required booking fields rejected")
    public void missingFields() {
        new BookingApiClient().create(java.util.Map.of("firstname", "")).then().statusCode(400)
            .body("errors", org.hamcrest.Matchers.not(org.hamcrest.Matchers.empty()));
    }
    @Test(groups = {"api", "negative", "regression"}, description = "API-06: malformed JSON maps to proxy 500 (known limitation)")
    public void malformedPayload() { new BookingApiClient().create("{invalid").then().statusCode(500); }
    @Test(groups = {"api", "negative", "regression"}, description = "API-07: unauthenticated booking retrieval is rejected by the configured routing contract")
    public void unauthorizedRead() { new BookingApiClient().get("/booking/1").then().statusCode(ApiContract.current().unauthorizedBookingStatus()); }
    @Test(groups = {"api", "regression"}, description = "API-08: isolated booking create/read/update/delete and conflict rejection")
    public void bookingLifecycle() {
        try (var owned = new OwnedResources()) {
            var payload = TestData.booking(owned.room.roomid());
            owned.trackGuest(payload.firstname(), payload.lastname());
            var response = owned.bookings.create(payload);
            // Register the ID before assertions so a contract assertion cannot bypass cleanup.
            Integer id = response.jsonPath().get("bookingid");
            if (id != null && id > 0) owned.trackBooking(id);
            response.then().statusCode(201);
            assertNotNull(id, "Proxy must return booking with generated bookingid");
            assertTrue(id > 0);
            var created = owned.bookings.booking(id);
            assertEquals(created.roomid(), payload.roomid());
            assertEquals(created.firstname(), payload.firstname());
            assertEquals(created.bookingdates(), payload.bookingdates());
            owned.bookings.create(payload).then().statusCode(409);
            var updated = new Booking(id, created.roomid(), "Updated", created.lastname(), true, created.bookingdates());
            ApiContract.current().verifyBookingUpdate(owned.bookings.update(id, updated), id);
            assertEquals(owned.bookings.booking(id), updated);
        }
    }
}
