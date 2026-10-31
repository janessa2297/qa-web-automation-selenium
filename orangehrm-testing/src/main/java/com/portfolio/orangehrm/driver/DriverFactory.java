package com.portfolio.orangehrm.driver;

import com.portfolio.orangehrm.config.FrameworkConfig;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** One browser per test-method thread. Selenium Manager resolves drivers automatically. */
public final class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() { }

    public static void start() {
        if (DRIVER.get() != null) throw new IllegalStateException("Browser already running on this test thread");
        WebDriver driver;
        switch (FrameworkConfig.browser()) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (FrameworkConfig.headless()) options.addArguments("--headless=new");
                options.addArguments("--window-size=1440,1000", "--disable-notifications");
                driver = new ChromeDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (FrameworkConfig.headless()) options.addArguments("--headless=new");
                options.addArguments("--window-size=1440,1000", "--disable-notifications");
                driver = new EdgeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (FrameworkConfig.headless()) options.addArguments("-headless");
                driver = new FirefoxDriver(options);
            }
            default -> throw new IllegalArgumentException(
                "Unsupported browser: " + FrameworkConfig.browser() + " (chrome, edge, firefox)");
        }
        DRIVER.set(driver);
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
            if (!FrameworkConfig.headless()) driver.manage().window().maximize();
        } catch (RuntimeException setupFailure) {
            try {
                stop();
            } catch (RuntimeException cleanupFailure) {
                setupFailure.addSuppressed(cleanupFailure);
            }
            throw setupFailure;
        }
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) throw new IllegalStateException("Call DriverFactory.start() first");
        return driver;
    }

    public static WebDriver getOrNull() {
        return DRIVER.get();
    }

    public static void stop() {
        WebDriver driver = DRIVER.get();
        try {
            if (driver != null) driver.quit();
        } finally {
            DRIVER.remove();
        }
    }
}
