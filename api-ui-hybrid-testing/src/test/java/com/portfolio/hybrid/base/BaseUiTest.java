package com.portfolio.hybrid.base;

import com.portfolio.hybrid.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public abstract class BaseUiTest {
    private WebDriver driver;
    public WebDriver driver() { return driver; }
    protected boolean requiresWrites() { return false; }
    @BeforeMethod(alwaysRun = true) public void startBrowser() {
        if (requiresWrites()) OwnedResources.requireWrites();
        driver = DriverFactory.create();
    }
    @AfterMethod(alwaysRun = true) public void stopBrowser() {
        if (driver != null) { try { driver.quit(); } finally { driver = null; } }
    }
}
