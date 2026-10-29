/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.tests;

import static com.portfolio.utils.MoneyUtils.amountFrom;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.portfolio.base.BaseTest;
import com.portfolio.config.ConfigReader;
import com.portfolio.data.TestData;
import com.portfolio.utils.ExtentLogger;
import com.portfolio.pages.CartPage;
import com.portfolio.pages.CheckoutCompletePage;
import com.portfolio.pages.CheckoutInfoPage;
import com.portfolio.pages.CheckoutOverviewPage;
import com.portfolio.pages.InventoryPage;
import com.portfolio.pages.LoginPage;
import com.portfolio.utils.MoneyUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class E2EPurchaseTest extends BaseTest {

    /**
     * Demonstrates a complete single-product purchase with order confirmation.
     */
    @Test(description = "E2E-001 - Purchase Backpack Successfully",
            groups = {"showcase", "e2e", "smoke", "regression"})
    public void verifyBackpackPurchase() {
        InventoryPage inventory = login();
        ExtentLogger.step("Add Sauce Labs Backpack", "Backpack added to cart",
                inventory::addBackpackToCart);
        CartPage cart = ExtentLogger.step("Open cart page", "Cart page opened",
                inventory::openCart);
        ExtentLogger.step("Verify the selected backpack and its price",
                () -> {
                    Assert.assertEquals(cart.getPageTitle(), TestData.CART_TITLE);
                    Assert.assertEquals(cart.getProductNames(), List.of(TestData.BACKPACK_NAME));
                    Assert.assertEquals(cart.getProductPrices(), List.of(TestData.BACKPACK_PRICE));
                });
        CheckoutOverviewPage overview = checkout(cart);
        ExtentLogger.step("Verify the backpack is retained in overview",
                () -> Assert.assertEquals(overview.getProductNames(),
                        List.of(TestData.BACKPACK_NAME)));
        finishAndVerifySuccess(overview);
    }

    /**
     * Demonstrates a multiple-product purchase with independently calculated totals.
     */
    @Test(description = "E2E-002 - Purchase Multiple Products And Validate Totals",
            groups = {"showcase", "e2e", "regression"})
    public void verifyMultipleProductPurchase() {
        InventoryPage inventory = login();
        ExtentLogger.step("Add Sauce Labs Backpack", "Backpack added to cart",
                inventory::addBackpackToCart);
        ExtentLogger.step("Add Sauce Labs Bike Light", "Bike Light added to cart",
                inventory::addBikeLightToCart);
        ExtentLogger.step("Verify the cart badge shows two products",
                () -> Assert.assertEquals(inventory.getCartCount(),
                        Integer.toString(TestData.PURCHASE_PRODUCT_NAMES.size())));
        CartPage cart = ExtentLogger.step("Open the cart", inventory::openCart);
        List<String> expectedNames = TestData.PURCHASE_PRODUCT_NAMES;
        List<String> expectedPrices = TestData.PURCHASE_PRODUCT_PRICES;
        ExtentLogger.step("Verify both selected products and their prices in the cart",
                () -> {
                    Assert.assertEquals(cart.getPageTitle(), TestData.CART_TITLE);
                    Assert.assertEquals(cart.getProductNames(), expectedNames);
                    Assert.assertEquals(cart.getProductPrices(), expectedPrices);
                });
        CheckoutOverviewPage overview = checkout(cart);
        ExtentLogger.step("Verify both products and prices are retained in overview",
                () -> {
                    Assert.assertEquals(overview.getProductNames(), expectedNames);
                    Assert.assertEquals(overview.getProductPrices(), expectedPrices);
                });
        BigDecimal expectedSubtotal = expectedPrices.stream()
                .map(MoneyUtils::amountFrom)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTax = expectedSubtotal.multiply(TestData.SALES_TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        ExtentLogger.step("Verify subtotal equals the selected product prices",
                () -> Assert.assertEquals(amountFrom(overview.getSubtotal()), expectedSubtotal));
        ExtentLogger.step("Verify tax equals the rounded expected sales tax",
                () -> Assert.assertEquals(amountFrom(overview.getTax()), expectedTax));
        ExtentLogger.step("Verify total equals the expected subtotal plus tax",
                () -> Assert.assertEquals(amountFrom(overview.getTotal()),
                        expectedSubtotal.add(expectedTax)));
        ExtentLogger.step("Verify displayed subtotal plus displayed tax equals total",
                () -> Assert.assertEquals(
                        amountFrom(overview.getSubtotal()).add(amountFrom(overview.getTax())),
                        amountFrom(overview.getTotal())));
        finishAndVerifySuccess(overview);
    }

    /**
     * Logs in and verifies inventory is available before purchasing.
     *
     * @return authenticated inventory page
     */
    private InventoryPage login() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventory = ExtentLogger.step("Login with valid user", "Login successful",
                () -> loginPage.login(ConfigReader.getProperty("username"),
                        ConfigReader.getProperty("password")));
        ExtentLogger.step("Verify the Products page is displayed",
                () -> Assert.assertTrue(inventory.isInventoryPageDisplayed(),
                        "Login did not open the inventory."));
        return inventory;
    }

    /**
     * Reports customer-information entry and verifies each checkout destination.
     *
     * @param cart populated cart
     * @return checkout overview page
     */
    private CheckoutOverviewPage checkout(CartPage cart) {
        CheckoutInfoPage info = ExtentLogger.step("Proceed to checkout",
                "Checkout information page opened", cart::clickCheckout);
        ExtentLogger.step("Verify the checkout information page",
                () -> Assert.assertEquals(info.getPageTitle(), TestData.CHECKOUT_INFO_TITLE));
        ExtentLogger.step("Enter first name",
                () -> info.enterFirstName(TestData.CHECKOUT_FIRST_NAME));
        ExtentLogger.step("Enter last name",
                () -> info.enterLastName(TestData.CHECKOUT_LAST_NAME));
        ExtentLogger.step("Enter postal code",
                () -> info.enterPostalCode(TestData.CHECKOUT_POSTAL_CODE));
        CheckoutOverviewPage overview = ExtentLogger.step("Continue to overview",
                "Customer information accepted; overview opened",
                info::continueToOverview);
        ExtentLogger.step("Verify the overview page",
                () -> Assert.assertEquals(overview.getPageTitle(), TestData.CHECKOUT_OVERVIEW_TITLE));
        return overview;
    }

    /**
     * Finishes the purchase and verifies both order-success messages.
     *
     * @param overview reviewed order
     */
    private void finishAndVerifySuccess(CheckoutOverviewPage overview) {
        CheckoutCompletePage complete = ExtentLogger.step("Finish the order",
                "Order submitted successfully",
                overview::clickFinish);
        ExtentLogger.step("Verify the thank-you heading", "Purchase success confirmed",
                () -> Assert.assertEquals(complete.getCompleteHeader(), TestData.ORDER_COMPLETE_HEADER));
        ExtentLogger.step("Verify the completion message", "Order confirmation message validated",
                () -> Assert.assertEquals(complete.getCompleteMessage(), TestData.ORDER_COMPLETE_MESSAGE));
    }

}
