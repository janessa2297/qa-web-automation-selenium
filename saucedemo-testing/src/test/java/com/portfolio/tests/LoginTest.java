/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.tests;

import com.portfolio.base.BaseTest;
import com.portfolio.config.ConfigReader;
import com.portfolio.data.TestData;
import com.portfolio.utils.ExtentLogger;
import com.portfolio.pages.InventoryPage;
import com.portfolio.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "UI - TC009 Verify Username Field Displayed",
            groups = {"ui", "regression"})
    public void verifyUsernameFieldIsDisplayed() {
        LoginPage loginPage = new LoginPage(driver);
        ExtentLogger.step("Verify the username field is displayed",
                () -> Assert.assertTrue(loginPage.isUsernameFieldDisplayed(),
                        "Username field is not displayed."));
    }

    @Test(description = "UI - TC010 Verify Password Field Displayed",
            groups = {"ui", "regression"})
    public void verifyPasswordFieldIsDisplayed() {
        LoginPage loginPage = new LoginPage(driver);
        ExtentLogger.step("Verify the password field is displayed",
                () -> Assert.assertTrue(loginPage.isPasswordFieldDisplayed(),
                        "Password field is not displayed."));
    }

    @Test(description = "UI - TC011 Verify Login Button Displayed",
            groups = {"ui", "regression"})
    public void verifyLoginButtonIsDisplayed() {
        LoginPage loginPage = new LoginPage(driver);
        ExtentLogger.step("Verify the login button is displayed",
                () -> Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                        "Login button is not displayed."));
    }

    @Test(description = "Positive - TC001 Verify Valid Login",
            groups = {"smoke", "regression"})
    public void verifyUserCanLogin() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = ExtentLogger.step(
                "Login with the valid demo user", "Login successful",
                () -> loginPage.login(
                        ConfigReader.getProperty("username"),
                        ConfigReader.getProperty("password")));

        ExtentLogger.step("Verify Products page displayed", "Products page validated",
                () -> Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(),
                        "Inventory page is not displayed."));
    }

    @Test(description = "{scenario}",
            dataProvider = "invalidLogins", dataProviderClass = TestData.class,
            groups = {"negative", "regression"})
    public void verifyInvalidCredentialsShowExpectedError(
            String scenario, String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(driver);
        ExtentLogger.step("Submit the " + scenario + " login data",
                () -> loginPage.login(username, password));

        ExtentLogger.step("Verify the matching login validation message",
                () -> Assert.assertEquals(loginPage.getErrorMessage(), expectedError,
                        "Login error message mismatch."));
    }
}
