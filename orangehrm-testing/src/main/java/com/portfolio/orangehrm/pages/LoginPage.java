package com.portfolio.orangehrm.pages;

import com.portfolio.orangehrm.config.FrameworkConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private static final By TITLE = By.cssSelector(".orangehrm-login-title");
    private static final By USERNAME = By.cssSelector("input[name='username']");
    private static final By PASSWORD = By.cssSelector("input[name='password']");
    private static final By SUBMIT = By.cssSelector("button[type='submit']");
    private static final By ERROR_MESSAGE = By.cssSelector(".oxd-alert-content-text");
    private static final By REQUIRED_MESSAGES = By.cssSelector(".oxd-input-field-error-message");

    public boolean isUsernameRequired() {
        return requiredFor(USERNAME);
    }

    public boolean isPasswordRequired() {
        return requiredFor(PASSWORD);
    }

    private boolean requiredFor(By field) {
        return visible(field).findElement(By.xpath(
            "./ancestor::div[contains(@class,'oxd-input-group')]"))
            .findElements(REQUIRED_MESSAGES).stream()
            .anyMatch(message -> message.isDisplayed() && message.getText().equals("Required"));
    }

    public String passwordInputType() {
        return visible(PASSWORD).getDomAttribute("type");
    }

    public LoginPage openProtectedPath(String path) {
        driver.get(FrameworkConfig.baseUrl() + path);
        waitForUrl("/auth/login");
        visible(USERNAME);
        return this;
    }

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(FrameworkConfig.baseUrl() + "/web/index.php/auth/login");
        visible(USERNAME);
        return this;
    }

    public boolean isLoaded() {
        return visible(TITLE).getText().equalsIgnoreCase("Login")
            && visible(USERNAME).isDisplayed()
            && visible(PASSWORD).isDisplayed();
    }

    public boolean isLoginButtonVisible() {
        return visible(SUBMIT).isDisplayed();
    }

    public void submit(String username, String password) {
        fill(USERNAME, username);
        fill(PASSWORD, password);
        click(SUBMIT);
    }

    public DashboardPage loginSuccessfully(String username, String password) {
        submit(username, password);
        return new DashboardPage(driver).waitUntilLoaded();
    }

    public String errorMessage() {
        return visible(ERROR_MESSAGE).getText();
    }

    public int requiredFieldMessageCount() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(REQUIRED_MESSAGES, 0));
        return driver.findElements(REQUIRED_MESSAGES).size();
    }
}
