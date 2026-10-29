/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.portfolio.config.ConfigReader;

public class MenuPage extends BasePage {

    private final By openButton = By.id("react-burger-menu-btn");
    private final By closeButton = By.id("react-burger-cross-btn");
    private final By allItemsLink = By.id("inventory_sidebar_link");
    private final By aboutLink = By.id("about_sidebar_link");
    private final By resetLink = By.id("reset_sidebar_link");
    private final By logoutLink = By.id("logout_sidebar_link");

    /**
     * Creates a menu page object for the current authenticated page.
     *
     * @param driver current browser session
     */
    public MenuPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Opens the sidebar and waits for its navigation links.
     */
    public void open() {
        click(openButton);
        waitForVisibility(allItemsLink);
    }

    /**
     * Checks that the menu controls and every navigation option are visible.
     *
     * @return true when the complete sidebar is displayed
     */
    public boolean isMenuDisplayed() {
        return isDisplayed(closeButton) && isDisplayed(allItemsLink)
                && isDisplayed(aboutLink) && isDisplayed(resetLink)
                && isDisplayed(logoutLink);
    }

    /**
     * Opens the product inventory through All Items.
     *
     * @return inventory page
     */
    public InventoryPage clickAllItems() {
        click(allItemsLink);
        waitForUrlContains("inventory.html");
        return new InventoryPage(driver);
    }

    /**
     * Navigates to the public Sauce Labs website.
     */
    public void clickAbout() {
        click(aboutLink);
        waitForUrlContains(ConfigReader.getProperty("aboutUrl"));
    }

    /**
     * Resets the current demo application's cart state.
     */
    public void resetAppState() {
        click(resetLink);
    }

    /**
     * Closes the sidebar so underlying page controls are accessible.
     */
    public void close() {
        click(closeButton);
        waitForInvisibility(allItemsLink);
        waitForClickable(openButton);
    }

    /**
     * Logs out and waits for the login form.
     *
     * @return login page
     */
    public LoginPage logout() {
        click(logoutLink);
        waitForVisibility(By.id("login-button"));
        return new LoginPage(driver);
    }
}
