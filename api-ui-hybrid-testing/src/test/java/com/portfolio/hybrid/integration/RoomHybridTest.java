package com.portfolio.hybrid.integration;

import com.portfolio.hybrid.base.*;
import com.portfolio.hybrid.models.Room;
import com.portfolio.hybrid.pages.*;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RoomHybridTest extends BaseUiTest {
    @Override protected boolean requiresWrites() { return true; }
    @Test(groups = {"integration", "regression"}, description = "HYB-02: API room -> UI booking and confirmation -> API persistence; remove notification, booking and room")
    public void uiBookingPersistsThroughApi() {
        try (var owned = new OwnedResources()) {
            int id = owned.room.roomid();
            String firstname = "Portfolio" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            String lastname = "Guest";
            owned.trackGuest(firstname, lastname);
            owned.trackNotifications();
            var dates = com.portfolio.hybrid.data.TestData.booking(id).bookingdates();
            var page = new ReservationPage(driver()).open(id, dates.checkin(), dates.checkout());
            assertEquals(page.description(), owned.room.description());
            page.showGuestForm();
            page.guest(firstname, lastname, firstname.toLowerCase() + "@example.com", "01234567890");
            page.submit();
            assertTrue(page.confirmation().contains(dates.checkin()));
            var matches = owned.bookings.bookings(id).stream()
                .filter(b -> firstname.equals(b.firstname()) && lastname.equals(b.lastname())).toList();
            assertEquals(matches.size(), 1, "UI submission must persist exactly one booking for this guest and room");
            var booking = matches.get(0);
            owned.trackBooking(booking.bookingid());
            assertTrue(booking.bookingid() > 0);
            assertEquals(booking.roomid(), id);
            assertEquals(booking.bookingdates().checkin(), dates.checkin());
            assertEquals(booking.bookingdates().checkout(), dates.checkout());
            assertEquals(owned.bookings.booking(booking.bookingid()), booking);
        }
    }
    @Test(groups = {"integration", "regression"}, description = "HYB-01: API room creation/update reflected in its reservation UI for the same generated ID")
    public void apiRoomIsVisibleAndUpdatedInUi() {
        try (var owned = new OwnedResources()) {
            int id = owned.room.roomid();
            var dates = com.portfolio.hybrid.data.TestData.booking(id).bookingdates();
            var page = new ReservationPage(driver()).open(id, dates.checkin(), dates.checkout());
            assertEquals(page.description(), owned.room.description());
            assertEquals(java.net.URI.create(driver().getCurrentUrl()).getPath(), "/reservation/" + id);
            assertEquals(page.heading(), owned.room.type() + " Room");
            page.showGuestForm();
            assertTrue(page.formVisible());
            var r = owned.room;
            var updated = new Room(id, r.roomName(), r.type(), r.accessible(), r.image(), r.description() + " revised", r.features(), r.roomPrice());
            com.portfolio.hybrid.api.ApiContract.current().verifyRoomMutation(owned.rooms.update(id, updated));
            assertEquals(owned.rooms.room(id).description(), updated.description());
            page.open(id, dates.checkin(), dates.checkout());
            assertEquals(page.description(), updated.description());
        }
    }
}
