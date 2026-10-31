package com.portfolio.orangehrm.tests;

import com.portfolio.orangehrm.base.BaseTest;
import com.portfolio.orangehrm.config.FrameworkConfig;
import com.portfolio.orangehrm.data.LoginData;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {
    @Test(groups = {"smoke", "ui"}, description = "Login page contains all critical controls")
    public void loginPageRendersRequiredControls() {
        Assert.assertTrue(loginPage.isLoaded(), "Login title/inputs should be visible");
        Assert.assertTrue(loginPage.isLoginButtonVisible(), "Login button should be visible");
    }

    @Test(groups = {"smoke", "auth"}, description = "Admin demo user can access the dashboard")
    public void validLoginOpensDashboard() {
        Assert.assertTrue(loginPage.loginSuccessfully(
            FrameworkConfig.username(), FrameworkConfig.password()).isLoaded());
    }

    @Test(groups = {"regression", "auth"}, dataProvider = "invalidLogins", dataProviderClass = LoginData.class,
        description = "Invalid username or password is rejected with an actionable error")
    public void invalidCredentialsAreRejected(String username, String password) {
        loginPage.submit(username, password);
        Assert.assertTrue(loginPage.errorMessage().contains("Invalid credentials"),
            "Invalid credentials should show a useful error");
    }

    @Test(groups = {"regression", "validation"}, description = "Both empty login fields show Required validation")
    public void emptyCredentialsShowRequiredValidation() {
        loginPage.submit("", "");
        Assert.assertEquals(loginPage.requiredFieldMessageCount(), 2,
            "Both required fields should show validation");
    }

    @Test(groups = {"regression", "validation"}, description = "Missing username shows a field-specific Required message")
    public void missingUsernameShowsValidation() {
        Reporter.log("Submit a password with the username left empty");
        loginPage.submit("", FrameworkConfig.password());
        Assert.assertEquals(loginPage.requiredFieldMessageCount(), 1);
        Assert.assertTrue(loginPage.isUsernameRequired(), "Username should show Required");
        Assert.assertFalse(loginPage.isPasswordRequired(), "Populated password should have no Required error");
    }

    @Test(groups = {"regression", "validation"}, description = "Missing password shows a field-specific Required message")
    public void missingPasswordShowsValidation() {
        Reporter.log("Submit a username with the password left empty");
        loginPage.submit(FrameworkConfig.username(), "");
        Assert.assertEquals(loginPage.requiredFieldMessageCount(), 1);
        Assert.assertTrue(loginPage.isPasswordRequired(), "Password should show Required");
        Assert.assertFalse(loginPage.isUsernameRequired(), "Populated username should have no Required error");
    }

    @Test(groups = {"regression", "auth"}, description = "Password input uses browser masking")
    public void passwordInputIsMasked() {
        Assert.assertEquals(loginPage.passwordInputType(), "password", "Password input must mask typed values");
    }

    @Test(groups = {"regression", "auth"}, description = "User can correct an invalid login without reloading")
    public void validLoginWorksAfterRejectedAttempt() {
        Reporter.log("Submit invalid credentials and verify rejection");
        loginPage.submit("invalid_user", "wrong_password");
        Assert.assertEquals(loginPage.errorMessage(), "Invalid credentials");
        Reporter.log("Replace both fields with valid credentials on the same page");
        Assert.assertTrue(loginPage.loginSuccessfully(
            FrameworkConfig.username(), FrameworkConfig.password()).isLoaded());
    }
}
