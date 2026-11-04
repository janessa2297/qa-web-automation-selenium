package com.portfolio.hybrid.base;

import com.portfolio.hybrid.api.*;
import com.portfolio.hybrid.config.FrameworkConfig;
import com.portfolio.hybrid.data.TestData;
import com.portfolio.hybrid.models.Room;
import org.testng.SkipException;
import java.util.List;

/** Owns one unique room. Cleanup attempts all resources and makes failures visible. */
public final class OwnedResources implements AutoCloseable {
    public final RoomApiClient rooms;
    public final BookingApiClient bookings;
    public final Room room;
    private final java.util.Set<Integer> bookingIds = new java.util.LinkedHashSet<>();
    private final ApiClient authenticated;
    private String guestFirst;
    private String guestLast;
    private boolean notifications;
    public static void requireWrites() {
        if (!FrameworkConfig.mutations()) throw new SkipException("Writes disabled: set ALLOW_MUTATIONS=true on an authorized environment");
        if (FrameworkConfig.username().isBlank() || FrameworkConfig.password().isBlank())
            throw new SkipException("BOOKER_USERNAME and BOOKER_PASSWORD required for authentication and cleanup");
    }
    public OwnedResources() {
        requireWrites();
        String token = ApiClient.login();
        if (token == null || token.isBlank()) throw new IllegalStateException("Login did not return a token");
        rooms = new RoomApiClient(token);
        bookings = new BookingApiClient(token);
        authenticated = new ApiClient(token);
        Room payload = TestData.room();
        // Proxy returns {success:true}, not the generated ID. Discover only our unique marker.
        io.restassured.response.Response created;
        List<Room> matches;
        try {
            created = rooms.create(payload);
            matches = rooms.rooms().stream().filter(r -> payload.roomName().equals(r.roomName())).toList();
        } catch (RuntimeException | AssertionError e) {
            throw new IllegalStateException("Room creation/discovery failed; inspect owned marker " + payload.roomName(), e);
        }
        if (matches.size() != 1) throw new IllegalStateException("Cannot identify created room; marker=" + payload.roomName()
            + ", HTTP=" + created.statusCode() + ". Inspect this marker manually.");
        room = matches.get(0);
        try { created.then().statusCode(200).body("success", org.hamcrest.Matchers.is(true)); }
        catch (AssertionError e) { try { close(); } catch (Throwable cleanup) { e.addSuppressed(cleanup); } throw e; }
    }
    public void trackBooking(int id) { bookingIds.add(id); }
    /** Register before UI submission, including when confirmation fails after a successful server write. */
    public void trackGuest(String firstname, String lastname) { guestFirst = firstname; guestLast = lastname; }
    public void trackNotifications() { notifications = true; }
    @Override public void close() {
        Throwable failure = null;
        if (guestFirst != null) {
            try {
                bookings.bookings(room.roomid()).stream()
                    .filter(b -> guestFirst.equals(b.firstname()) && guestLast.equals(b.lastname()))
                    .forEach(b -> bookingIds.add(b.bookingid()));
            } catch (Throwable e) { failure = e; }
        }
        if (notifications) {
            try {
                List<java.util.Map<String, Object>> messages = authenticated.get("/message").then().statusCode(200)
                    .extract().jsonPath().getList("messages");
                for (var message : messages) {
                    if ((guestFirst + " " + guestLast).equals(message.get("name"))) {
                        try { ApiContract.current().verifyDeletion(authenticated.delete("/message/" + message.get("id"))); }
                        catch (Throwable e) { if (failure == null) failure = e; else failure.addSuppressed(e); }
                    }
                }
            } catch (Throwable e) { if (failure == null) failure = e; else failure.addSuppressed(e); }
        }
        for (int id : bookingIds) {
            try { ApiContract.current().verifyDeletion(bookings.remove(id)); }
            catch (Throwable e) { if (failure == null) failure = e; else failure.addSuppressed(e); }
        }
        if (!bookingIds.isEmpty()) {
            try {
                if (bookings.bookings(room.roomid()).stream().anyMatch(b -> bookingIds.contains(b.bookingid())))
                    throw new AssertionError("Deleted booking still present in owned room " + room.roomid());
            } catch (Throwable e) { if (failure == null) failure = e; else failure.addSuppressed(e); }
        }
        try { ApiContract.current().verifyDeletion(rooms.remove(room.roomid())); }
        catch (Throwable e) { if (failure == null) failure = e; else failure.addSuppressed(e); }
        if (failure != null) throw new AssertionError("Cleanup failed for owned room " + room.roomid(), failure);
        if (rooms.rooms().stream().anyMatch(r -> r.roomid() == room.roomid()))
            throw new AssertionError("Deleted room still present: " + room.roomid());
    }
}
