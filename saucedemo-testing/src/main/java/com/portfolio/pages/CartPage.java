/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By backpackLabel = By.xpath(
            "//div[contains(@class,'inventory_item_name') and normalize-space()='Sauce Labs Backpack']");
    private final By removeBackpackButton = By.id("remove-sauce-labs-backpack");
    private final By removeBikeLightButton = By.id("remove-sauce-labs-bike-light");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By cartProductNames = By.cssSelector(".cart_item .inventory_item_name");
    private final By cartProductPrices = By.cssSelector(".cart_item .inventory_item_price");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public String getProductName() {
        return getText(backpackLabel);
    }

    /**
     * Returns product names currently shown in the cart.
     *
     * @return product names in cart order
     */
    public List<String> getProductNames() {
        waitForVisibility(checkoutButton);
        return driver.findElements(cartProductNames).stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Returns product prices currently shown in the cart.
     *
     * @return product prices in cart order
     */
    public List<String> getProductPrices() {
        waitForVisibility(checkoutButton);
        return driver.findElements(cartProductPrices).stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Returns the number of product rows currently shown in the cart.
     *
     * @return number of cart product rows
     */
    public int getProductCount() {
        waitForVisibility(checkoutButton);
        return driver.findElements(cartProductNames).size();
    }

    public void removeBackpack() {
        click(removeBackpackButton);
        waitForInvisibility(removeBackpackButton);
    }

    /**
     * Removes the Sauce Labs Bike Light from the cart.
     */
    public void removeBikeLight() {
        click(removeBikeLightButton);
        waitForInvisibility(removeBikeLightButton);
    }

    public boolean isBackpackDisplayed() {
        return isDisplayed(backpackLabel);
    }

    public CheckoutInfoPage clickCheckout() {
        click(checkoutButton);
        waitForUrlContains("checkout-step-one");
        return new CheckoutInfoPage(driver);
    }

    /**
     * Returns to inventory without clearing the cart.
     *
     * @return inventory page
     */
    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        waitForUrlContains("inventory.html");
        return new InventoryPage(driver);
    }
}
