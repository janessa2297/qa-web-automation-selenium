package com.portfolio.hybrid.ui;

import com.portfolio.hybrid.base.BaseUiTest;
import com.portfolio.hybrid.pages.*;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class BookingUiTest extends BaseUiTest {
    @Test(groups = {"ui", "smoke", "regression"}, description = "UI-01: home page displays rooms and booking navigation")
    public void homeLoads() {
        var home = new HomePage(driver()).open();
        assertTrue(home.roomCount() > 0);
        assertTrue(home.hasReservationLinks());
    }
    @Test(groups = {"ui", "negative", "regression"}, description = "UI-02: guest form visible; blank fields rejected")
    public void requiredFields() {
        var page = openExistingRoom();
        assertTrue(page.formVisible());
        page.submit();
        assertTrue(page.errors().contains("Firstname should not be blank"));
        assertTrue(page.errors().contains("Lastname should not be blank"));
    }
    @Test(groups = {"ui", "negative", "regression"}, description = "UI-03: malformed email and short phone rejected")
    public void invalidContact() {
        var page = openExistingRoom();
        page.guest("Portfolio", "Guest", "invalid-email", "123");
        page.submit();
        String errors = page.errors();
        assertTrue(errors.contains("email"), errors);
        assertTrue(errors.contains("11"), errors);
    }
    private ReservationPage openExistingRoom() {
        int id = new HomePage(driver()).open().firstRoomId();
        var dates = com.portfolio.hybrid.data.TestData.booking(id).bookingdates();
        var page = new ReservationPage(driver()).open(id, dates.checkin(), dates.checkout());
        page.showGuestForm(); return page;
    }
}
