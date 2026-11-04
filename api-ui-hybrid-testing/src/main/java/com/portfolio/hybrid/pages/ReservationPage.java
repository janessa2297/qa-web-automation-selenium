package com.portfolio.hybrid.pages;

import com.portfolio.hybrid.config.FrameworkConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class ReservationPage extends BasePage {
    public ReservationPage(WebDriver driver) { super(driver); }
    public ReservationPage open(int id, String checkin, String checkout) {
        driver.get(FrameworkConfig.baseUrl() + "/reservation/" + id + "?checkin=" + checkin + "&checkout=" + checkout);
        visible(By.id("doReservation")); return this;
    }
    public String heading() { return visible(By.tagName("h1")).getText(); }
    public String description() { return visible(By.cssSelector(".col-lg-8 h2 + p")).getText(); }
    public void showGuestForm() { click(By.id("doReservation")); visible(By.name("firstname")); }
    public boolean formVisible() { return visible(By.name("firstname")).isDisplayed(); }
    public void guest(String firstname, String lastname, String email, String phone) {
        fill("firstname", firstname); fill("lastname", lastname); fill("email", email); fill("phone", phone);
    }
    private void fill(String name, String value) { var input = visible(By.name(name)); input.clear(); input.sendKeys(value); }
    public void submit() { click(By.cssSelector(".booking-card form button.btn-primary")); }
    public String errors() { return visible(By.cssSelector("[role='alert']")).getText(); }
    public String confirmation() {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".booking-card h2"), "Booking Confirmed"));
        return visible(By.cssSelector(".booking-card")).getText();
    }
}
