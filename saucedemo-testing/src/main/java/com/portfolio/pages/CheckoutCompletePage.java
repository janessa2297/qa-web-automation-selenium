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

public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");
    private final By completeMessage = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {

        super(driver);
    }

    public String getCompleteHeader() {

        return getText(completeHeader);
    }

    public String getCompleteMessage() {

        return getText(completeMessage);
    }

    /**
     * Returns to the inventory page after completing the order.
     *
     * @return inventory page
     */
    public InventoryPage clickBackHome() {
        click(backHomeButton);
        waitForUrlContains("inventory.html");
        return new InventoryPage(driver);
    }
}
