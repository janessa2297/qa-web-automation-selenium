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
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {

    private final By inventoryContainer = By.id("inventory_container");
    private final By addBackpackButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By pageTitle = By.className("title");
    private final By removeBackpackButton = By.id("remove-sauce-labs-backpack");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By sortDropdown = By.className("product_sort_container");
    private final By productPrices = By.className("inventory_item_price");
    private final By productNames = By.className("inventory_item_name");
    private final By productImages = By.cssSelector(".inventory_item_img img");
    private final By addBikeLightButton = By.id("add-to-cart-sauce-labs-bike-light");
    private final By removeBikeLightButton = By.id("remove-sauce-labs-bike-light");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isInventoryPageDisplayed() {
        return isDisplayed(inventoryContainer);
    }

    public void addBackpackToCart() {
        click(addBackpackButton);
        waitForVisibility(removeBackpackButton);
    }

    /**
     * Adds the Sauce Labs Bike Light to the cart.
     */
    public void addBikeLightToCart() {
        click(addBikeLightButton);
        waitForVisibility(removeBikeLightButton);
    }

    public String getCartCount() {
        return getText(cartBadge);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public void removeBackpackFromCart() {
        click(removeBackpackButton);
        waitForVisibility(addBackpackButton);
    }

    /**
     * Checks whether the backpack can be added again after removal.
     *
     * @return true when the backpack add button is displayed
     */
    public boolean isBackpackAddButtonDisplayed() {
        return isDisplayed(addBackpackButton);
    }

    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }

    public CartPage openCart() {
        click(cartIcon);
        waitForUrlContains("cart.html");
        return new CartPage(driver);
    }

    /**
     * Sorts inventory items by ascending price.
     */
    public void sortProductsLowToHigh() {
        sortProducts("lohi");
    }

    /**
     * Sorts inventory items by descending price.
     */
    public void sortProductsHighToLow() {
        sortProducts("hilo");
    }

    /**
     * Sorts inventory items by product name from A to Z.
     */
    public void sortProductsNameAscending() {
        sortProducts("az");
    }

    /**
     * Sorts inventory items by product name from Z to A.
     */
    public void sortProductsNameDescending() {
        sortProducts("za");
    }

    /**
     * Returns the currently displayed product names.
     *
     * @return product names in their current inventory order
     */
    public List<String> getProductNames() {
        waitForVisibility(productNames);
        return driver.findElements(productNames).stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Returns the currently displayed product prices.
     *
     * @return product prices in their current inventory order
     */
    public List<String> getProductPrices() {
        waitForVisibility(productPrices);
        return driver.findElements(productPrices).stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Checks whether every inventory product image is displayed.
     *
     * @return true when all product images are visible and at least one exists
     */
    public boolean areAllProductImagesDisplayed() {
        waitForAllVisibility(productImages);
        List<WebElement> images = driver.findElements(productImages);
        return !images.isEmpty() && images.stream().allMatch(WebElement::isDisplayed);
    }

    /**
     * Returns the number of image elements attached to inventory products.
     *
     * @return product image count
     */
    public int getProductImageCount() {
        waitForVisibility(inventoryContainer);
        return driver.findElements(productImages).size();
    }

    /**
     * Returns the displayed price of the first product in the current sort order.
     *
     * @return first product's price label
     */
    public String getFirstProductPrice() {
        waitForVisibility(productPrices);
        return driver.findElements(productPrices).get(0).getText();
    }

    /**
     * Applies the requested inventory sort option.
     *
     * @param sortValue value associated with the inventory sort option
     */
    private void sortProducts(String sortValue) {
        waitForVisibility(sortDropdown);
        new Select(driver.findElement(sortDropdown)).selectByValue(sortValue);
    }
}
