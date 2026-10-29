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

public class LoginPage extends BasePage {

    private final By usernameField =
            By.id("user-name");

    private final By passwordField =
            By.id("password");

    private final By loginButton =
            By.id("login-button");

    private final By errorMessage =
            By.xpath("//h3[@data-test='error']");

    public LoginPage(WebDriver driver) {

        super(driver);
    }

    /**
     * Checks whether the username field is visible on the login page.
     *
     * @return true when the username field is displayed
     */
    public boolean isUsernameFieldDisplayed() {
        return isDisplayed(usernameField);
    }

    /**
     * Checks whether the password field is visible on the login page.
     *
     * @return true when the password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(passwordField);
    }

    /**
     * Checks whether the login button is visible on the login page.
     *
     * @return true when the login button is displayed
     */
    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }

    public LoginPage enterUsername(
            String username) {

        type(usernameField, username);

        return this;
    }

    public LoginPage enterPassword(
            String password) {

        type(passwordField, password);

        return this;
    }

    public String getErrorMessage() {

        return getText(errorMessage);
    }

    public InventoryPage clickLogin() {

        click(loginButton);
        // Submission can navigate asynchronously or remain here with validation.
        waitForEitherVisible(By.id("inventory_container"), errorMessage);

        return new InventoryPage(driver);
    }

    public InventoryPage login(
            String username,
            String password) {

        enterUsername(username);

        enterPassword(password);

        return clickLogin();
    }
}
