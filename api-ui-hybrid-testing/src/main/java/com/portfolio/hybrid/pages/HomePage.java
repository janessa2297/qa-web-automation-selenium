package com.portfolio.hybrid.pages;

import com.portfolio.hybrid.config.FrameworkConfig;
import org.openqa.selenium.*;

public final class HomePage extends BasePage {
    public HomePage(WebDriver driver) { super(driver); }
    public HomePage open() { driver.get(FrameworkConfig.baseUrl()); visible(By.cssSelector(".room-card")); return this; }
    public int roomCount() { return driver.findElements(By.cssSelector(".room-card")).size(); }
    public boolean hasReservationLinks() { return !driver.findElements(By.cssSelector(".room-card a[href^='/reservation/']")).isEmpty(); }
    public int firstRoomId() {
        String href = visible(By.cssSelector(".room-card a[href^='/reservation/']")).getDomAttribute("href");
        return Integer.parseInt(java.net.URI.create(href).getPath().replace("/reservation/", ""));
    }
}
