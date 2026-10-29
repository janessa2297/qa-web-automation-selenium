/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.portfolio.driver.DriverFactory;

/** Compatibility adapter for the original page base; new pages should use the pages package. */
@Deprecated
public abstract class BasePage extends com.portfolio.pages.BasePage {
    protected BasePage() {
        super(DriverFactory.getDriver());
    }

    protected WebElement visible(By locator) {
        waitForVisibility(locator);
        return driver.findElement(locator);
    }

    protected String text(By locator) {
        return getText(locator);
    }
}
