/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.utils;

import java.time.Duration;

import com.portfolio.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WaitUtils {

    private final WebDriver driver;
    private final Duration timeout;

    /**
     * Creates wait helpers using the configured explicit-wait timeout.
     *
     * @param driver browser controlled by these waits
     */
    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.timeout = Duration.ofSeconds(ConfigReader.getPositiveInt("explicitWaitSeconds"));
    }

    /**
     * Waits until the locator resolves to a visible element.
     *
     * @param locator element locator
     */
    public WebElement waitForVisibility(By locator) {
        return createWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for every element matching the locator to become visible.
     *
     * @param locator locator for the complete collection
     */
    public void waitForAllVisibility(By locator) {
        createWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Waits until the located element can be clicked.
     *
     * @param locator element locator
     */
    public WebElement waitForClickable(By locator) {
        Rectangle[] previous = {null};
        return createWait().until(browser -> {
            try {
                WebElement element = ExpectedConditions.elementToBeClickable(locator).apply(browser);
                if (element == null) {
                    previous[0] = null;
                    return null;
                }
                Rectangle current = element.getRect();
                boolean stable = current.equals(previous[0]);
                previous[0] = current;
                return stable ? element : null;
            } catch (StaleElementReferenceException exception) {
                previous[0] = null;
                return null;
            }
        });
    }

    public void waitForInvisibility(By locator) {
        createWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until the current URL contains the requested value.
     *
     * @param value expected URL fragment
     */
    public void waitForUrlContains(String value) {
        createWait().until(ExpectedConditions.urlContains(value));
    }

    /** Waits until either of two possible page outcomes is visible. */
    public void waitForEitherVisible(By first, By second) {
        createWait().until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(first),
                ExpectedConditions.visibilityOfElementLocated(second)));
    }

    /**
     * Creates an explicit wait using this instance's configured timeout.
     *
     * @return wait bound to the configured WebDriver
     */
    private WebDriverWait createWait() {
        return new WebDriverWait(driver, timeout);
    }
}
