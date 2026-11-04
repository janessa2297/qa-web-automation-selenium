package com.portfolio.hybrid.driver;

import com.portfolio.hybrid.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

/** Serial suite: instance-owned driver; Selenium Manager resolves the installed Chrome driver. */
public final class DriverFactory {
    private DriverFactory() { }
    public static WebDriver create() {
        var options = new ChromeOptions();
        if (FrameworkConfig.headless()) options.addArguments("--headless=new");
        options.addArguments("--window-size=1440,1100", "--disable-dev-shm-usage");
        var driver = new ChromeDriver(options);
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
            return driver;
        } catch (RuntimeException e) { driver.quit(); throw e; }
    }
}
