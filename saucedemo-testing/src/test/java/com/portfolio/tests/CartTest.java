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
import com.portfolio.pages.CartPage;
import com.portfolio.pages.CheckoutInfoPage;
import com.portfolio.pages.InventoryPage;
import com.portfolio.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(description = "Cart - TC040 Verify Open Cart",
            groups = "regression")
    public void verifyCartOpens() {
        CartPage cartPage = openCartWithBackpack();

        ExtentLogger.step("Verify the cart page is displayed",
                () -> Assert.assertEquals(cartPage.getPageTitle(), TestData.CART_TITLE,
                        "Cart page title mismatch."));
    }

    @Test(description = "Cart - TC041 Verify Backpack Exists",
            groups = "regression")
    public void verifyBackpackExistsInCart() {
        CartPage cartPage = openCartWithBackpack();

        ExtentLogger.step("Verify the backpack exists in the cart",
                () -> Assert.assertEquals(cartPage.getProductName(),
                        TestData.BACKPACK_NAME, "Backpack is missing from cart."));
    }

    @Test(description = "Cart - TC042 Verify Multiple Products Exist",
            groups = "regression")
    public void verifyMultipleProductsExistInCart() {
        CartPage cartPage = openCartWithBackpackAndBikeLight();

        ExtentLogger.step("Verify both selected products exist in the cart",
                () -> Assert.assertEquals(cartPage.getProductNames(),
                        TestData.PURCHASE_PRODUCT_NAMES,
                        "Cart contents do not match selected products."));
    }

    @Test(description = "Cart - TC043 Verify Product Price Displayed",
            groups = "regression")
    public void verifyProductPriceDisplayed() {
        CartPage cartPage = openCartWithBackpack();

        ExtentLogger.step("Verify the backpack price is displayed",
                () -> Assert.assertTrue(cartPage.getProductPrices()
                                .contains(TestData.BACKPACK_PRICE),
                        "Backpack price is missing or incorrect."));
    }

    @Test(description = "Removal - TC044 Verify Remove Product In Cart",
            groups = "regression")
    public void verifyRemoveProductFromCart() {
        CartPage cartPage = openCartWithBackpack();
        ExtentLogger.step("Remove the selected backpack from the cart",
                cartPage::removeBackpack);

        ExtentLogger.step("Verify the backpack is no longer displayed",
                () -> Assert.assertFalse(cartPage.isBackpackDisplayed(),
                        "Product should be removed from cart."));
    }

    @Test(description = "Removal - TC045 Verify Remove All Products",
            groups = "regression")
    public void verifyRemoveAllProductsFromCart() {
        CartPage cartPage = openCartWithBackpackAndBikeLight();
        ExtentLogger.step("Remove the backpack from the cart", cartPage::removeBackpack);
        ExtentLogger.step("Remove the bike light from the cart", cartPage::removeBikeLight);

        ExtentLogger.step("Verify the cart contains no products",
                () -> {
                    Assert.assertEquals(cartPage.getProductCount(), 0,
                            "Cart should be empty after removing all products.");
                    Assert.assertTrue(cartPage.getProductNames().isEmpty());
                    Assert.assertTrue(cartPage.getProductPrices().isEmpty());
                });
    }

    @Test(description = "Navigation - TC046 Verify Continue Shopping",
            groups = "regression")
    public void verifyContinueShoppingReturnsToProducts() {
        CartPage cartPage = openCartWithBackpack();
        InventoryPage inventoryPage = ExtentLogger.step("Select Continue Shopping",
                cartPage::continueShopping);

        ExtentLogger.step("Verify the products page is displayed again",
                () -> Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(),
                        "Continuing shopping should return to the products page."));
    }

    @Test(description = "Navigation - TC047 Verify Checkout Button",
            groups = "regression")
    public void verifyCheckoutButtonOpensCheckout() {
        CartPage cartPage = openCartWithBackpack();
        CheckoutInfoPage checkoutInfoPage = ExtentLogger.step(
                "Select the checkout button", cartPage::clickCheckout);

        ExtentLogger.step("Verify checkout information page is displayed",
                () -> Assert.assertEquals(checkoutInfoPage.getPageTitle(),
                        TestData.CHECKOUT_INFO_TITLE,
                        "Checkout information page title mismatch."));
    }

    /**
     * Starts an independent cart scenario with the demo backpack.
     *
     * @return cart page containing the backpack
     */
    private CartPage openCartWithBackpack() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        return ExtentLogger.step("Open the shopping cart", inventoryPage::openCart);
    }

    /**
     * Starts an independent cart scenario with a backpack and bike light.
     *
     * @return cart page containing both products
     */
    private CartPage openCartWithBackpackAndBikeLight() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        ExtentLogger.step("Add the Sauce Labs Bike Light to the cart",
                inventoryPage::addBikeLightToCart);
        return ExtentLogger.step("Open the shopping cart", inventoryPage::openCart);
    }

    /**
     * Authenticates with the public demo account for an independent cart scenario.
     *
     * @return inventory page after successful login
     */
    private InventoryPage login() {
        LoginPage loginPage = new LoginPage(driver);
        return ExtentLogger.step("Log in with the valid demo account",
                () -> loginPage.login(
                        ConfigReader.getProperty("username"),
                        ConfigReader.getProperty("password")));
    }
}
