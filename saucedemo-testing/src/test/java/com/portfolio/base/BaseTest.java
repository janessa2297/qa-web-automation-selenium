/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.portfolio.config.ConfigReader;
import com.portfolio.driver.DriverFactory;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getProperty("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            DriverFactory.quitDriver();
        } finally {
            driver = null;
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}
