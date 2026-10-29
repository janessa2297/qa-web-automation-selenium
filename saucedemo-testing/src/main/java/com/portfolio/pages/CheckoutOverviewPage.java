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

public class CheckoutOverviewPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By finishButton = By.id("finish");
    private final By productName = By.className("inventory_item_name");
    private final By productPrice = By.className("inventory_item_price");
    private final By productNames = By.cssSelector(".cart_item .inventory_item_name");
    private final By productPrices = By.cssSelector(".cart_item .inventory_item_price");
    private final By subtotal = By.className("summary_subtotal_label");
    private final By tax = By.className("summary_tax_label");
    private final By total = By.className("summary_total_label");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    /**
     * Returns the name of the first product in the order.
     *
     * @return product name
     */
    public String getProductName() {
        return getText(productName);
    }

    /**
     * Returns the displayed price of the first product in the order.
     *
     * @return product price label
     */
    public String getProductPrice() {
        return getText(productPrice);
    }

    /**
     * Returns product names listed in the order overview.
     *
     * @return product names in overview order
     */
    public List<String> getProductNames() {
        waitForVisibility(productNames);
        return driver.findElements(productNames).stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Returns item prices listed in the order overview.
     *
     * @return product prices in overview order
     */
    public List<String> getProductPrices() {
        waitForVisibility(productPrices);
        return driver.findElements(productPrices).stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Returns the order subtotal label.
     *
     * @return subtotal label
     */
    public String getSubtotal() {
        return getText(subtotal);
    }

    /**
     * Returns the calculated tax label.
     *
     * @return tax label
     */
    public String getTax() {
        return getText(tax);
    }

    /**
     * Returns the order total label.
     *
     * @return total label
     */
    public String getTotal() {
        return getText(total);
    }

    public CheckoutCompletePage clickFinish() {
        click(finishButton);
        waitForUrlContains("checkout-complete.html");
        return new CheckoutCompletePage(driver);
    }
}
