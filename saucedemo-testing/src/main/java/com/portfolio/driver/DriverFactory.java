/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.driver;

import java.time.Duration;
import java.util.Locale;

import com.portfolio.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVERS = new ThreadLocal<>();

    private DriverFactory() {
    }

    /**
     * Returns the current thread's WebDriver, creating it when necessary.
     *
     * @return configured WebDriver
     */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVERS.get();
        if (driver == null) {
            int pageLoadTimeout = ConfigReader.getPositiveInt("pageLoadTimeoutSeconds");
            boolean headless = ConfigReader.getBoolean("headless");
            driver = createDriver(ConfigReader.getProperty("browser"), headless);
            try {
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1440, 1000));
                if (!headless) {
                    driver.manage().window().maximize();
                }
                DRIVERS.set(driver);
            } catch (RuntimeException failure) {
                try {
                    driver.quit();
                } catch (RuntimeException cleanupFailure) {
                    failure.addSuppressed(cleanupFailure);
                }
                throw failure;
            }
        }
        return driver;
    }

    /**
     * Quits the current thread's browser and releases its reference.
     */
    public static void quitDriver() {
        WebDriver current = DRIVERS.get();
        DRIVERS.remove();
        if (current != null) {
            current.quit();
        }
    }

    /**
     * Creates the configured supported browser.
     *
     * @param browserName configured browser name
     * @param headless whether to disable the browser window
     * @return newly created WebDriver
     */
    private static WebDriver createDriver(String browserName, boolean headless) {
        return switch (browserName.trim().toLowerCase(Locale.ROOT)) {
            case "chrome" -> new ChromeDriver(chromeOptions(headless));
            case "edge" -> new EdgeDriver(edgeOptions(headless));
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                yield new FirefoxDriver(options);
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + browserName
                            + "'. Supported values are chrome, edge and firefox.");
        };
    }

    /**
     * Builds Chrome options for the selected execution mode.
     *
     * @param headless whether to use headless mode
     * @return Chrome options
     */
    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    /**
     * Builds Edge options for the selected execution mode.
     *
     * @param headless whether to use headless mode
     * @return Edge options
     */
    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }
}
