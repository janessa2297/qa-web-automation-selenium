/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.portfolio.utils.WaitUtils;

public abstract class BasePage {

    protected final WebDriver driver;
    private final WaitUtils waits;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new WaitUtils(driver);
    }

    /**
     * Waits for the requested element to become visible.
     *
     * @param locator element locator
     */
    protected void waitForVisibility(By locator) {
        waits.waitForVisibility(locator);
    }

    /**
     * Waits for all matching elements rather than only the first match.
     *
     * @param locator collection locator
     */
    protected void waitForAllVisibility(By locator) {
        waits.waitForAllVisibility(locator);
    }

    /**
     * Waits for the requested element to become clickable.
     *
     * @param locator element locator
     */
    protected void waitForClickable(By locator) {
        waits.waitForClickable(locator);
    }

    protected void waitForInvisibility(By locator) {
        waits.waitForInvisibility(locator);
    }

    protected void click(By locator) {
        WebElement element = waits.waitForClickable(locator);
        // Move the pointer to the settled target before sending the click sequence.
        new Actions(driver).moveToElement(element).click().perform();
    }

    protected void type(By locator, String value) {
        WebElement element = waits.waitForVisibility(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String getText(By locator) {
        return waits.waitForVisibility(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waits.waitForVisibility(locator).isDisplayed();
        } catch (NoSuchElementException | TimeoutException
                 | StaleElementReferenceException exception) {
            return false;
        }
    }

    /**
     * Waits until the browser URL contains the requested value.
     *
     * @param value expected URL fragment
     */
    protected void waitForUrlContains(String value) {
        waits.waitForUrlContains(value);
    }

    protected void waitForEitherVisible(By first, By second) {
        waits.waitForEitherVisible(first, second);
    }
}
