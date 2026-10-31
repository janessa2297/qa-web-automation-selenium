package com.portfolio.orangehrm.base;

import com.portfolio.orangehrm.driver.DriverFactory;
import com.portfolio.orangehrm.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.start();
        driver = DriverFactory.get();
        loginPage = new LoginPage(driver).open();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.stop();
        driver = null;
    }
}
