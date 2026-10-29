/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.tests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.portfolio.base.BaseTest;
import com.portfolio.config.ConfigReader;
import com.portfolio.data.TestData;
import com.portfolio.utils.ExtentLogger;
import com.portfolio.pages.CartPage;
import com.portfolio.pages.InventoryPage;
import com.portfolio.pages.LoginPage;
import com.portfolio.utils.MoneyUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InventoryTest extends BaseTest {

    @Test(description = "Products - TC020 Verify Products Page Displayed",
            groups = {"smoke", "regression"})
    public void verifyProductsPageDisplayed() {
        InventoryPage inventoryPage = login();

        ExtentLogger.step("Verify the page title is Products",
                () -> Assert.assertEquals(inventoryPage.getPageTitle(),
                        TestData.PRODUCTS_TITLE, "Products page title mismatch."));
    }

    @Test(description = "Products - TC021 Verify Product Names Displayed",
            groups = "regression")
    public void verifyProductNamesDisplayed() {
        InventoryPage inventoryPage = login();

        ExtentLogger.step("Verify all expected catalog names are displayed",
                () -> {
                    List<String> names = inventoryPage.getProductNames();
                    Assert.assertEqualsNoOrder(names.toArray(), TestData.CATALOG_NAMES.toArray(),
                            "Displayed product names do not match the expected catalog.");
                });
    }

    @Test(description = "Products - TC022 Verify Product Prices Displayed",
            groups = "regression")
    public void verifyProductPricesDisplayed() {
        InventoryPage inventoryPage = login();

        ExtentLogger.step("Verify all expected catalog prices are displayed",
                () -> {
                    List<String> prices = inventoryPage.getProductPrices();
                    Assert.assertEqualsNoOrder(prices.toArray(), TestData.CATALOG_PRICES.toArray(),
                            "Displayed prices do not match the expected catalog.");
                });
    }

    @Test(description = "Products - TC023 Verify Product Images Displayed",
            groups = "regression")
    public void verifyAllProductImagesDisplayed() {
        InventoryPage inventoryPage = login();

        ExtentLogger.step("Verify every expected product has a displayed image",
                () -> {
                    Assert.assertEquals(inventoryPage.getProductImageCount(),
                            TestData.CATALOG_NAMES.size(), "A product image is missing.");
                    Assert.assertTrue(inventoryPage.areAllProductImagesDisplayed(),
                            "One or more product images are not displayed.");
                });
    }

    @Test(description = "Add To Cart - TC024 Verify Add Backpack",
            groups = "regression")
    public void verifyAddBackpackToCart() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);

        ExtentLogger.step("Verify the cart badge shows one item",
                () -> Assert.assertEquals(inventoryPage.getCartCount(),
                        TestData.ONE_ITEM_COUNT, "Cart count mismatch."));
    }

    @Test(description = "Add To Cart - TC025 Verify Add Bike Light",
            groups = "regression")
    public void verifyAddBikeLightToCart() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Bike Light to the cart",
                inventoryPage::addBikeLightToCart);

        ExtentLogger.step("Verify the cart badge shows one item",
                () -> Assert.assertEquals(inventoryPage.getCartCount(),
                        TestData.ONE_ITEM_COUNT, "Cart count mismatch."));
    }

    @Test(description = "Add To Cart - TC026 Verify Add Multiple Products",
            groups = "regression")
    public void verifyAddMultipleProductsToCart() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        ExtentLogger.step("Add the Sauce Labs Bike Light to the cart",
                inventoryPage::addBikeLightToCart);

        CartPage cartPage = ExtentLogger.step("Open the shopping cart",
                inventoryPage::openCart);
        ExtentLogger.step("Verify both selected products are in the cart",
                () -> Assert.assertEquals(cartPage.getProductNames(),
                        TestData.PURCHASE_PRODUCT_NAMES,
                        "Cart does not contain both selected products."));
    }

    @Test(description = "Add To Cart - TC027 Verify Cart Badge Count",
            groups = "regression")
    public void verifyCartBadgeCount() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        ExtentLogger.step("Add the Sauce Labs Bike Light to the cart",
                inventoryPage::addBikeLightToCart);

        ExtentLogger.step("Verify the cart badge count is two",
                () -> Assert.assertEquals(inventoryPage.getCartCount(),
                        Integer.toString(TestData.PURCHASE_PRODUCT_NAMES.size()),
                        "Cart badge count mismatch."));
    }

    @Test(description = "Remove - TC028 Verify Remove Product",
            groups = "regression")
    public void verifyRemoveProduct() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        ExtentLogger.step("Remove the Sauce Labs Backpack from the cart",
                inventoryPage::removeBackpackFromCart);

        ExtentLogger.step("Verify the backpack can be added again",
                () -> Assert.assertTrue(inventoryPage.isBackpackAddButtonDisplayed(),
                        "Backpack was not removed from the inventory cart."));
    }

    @Test(description = "Remove - TC029 Verify Cart Badge Removed",
            groups = "regression")
    public void verifyCartBadgeRemoved() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        ExtentLogger.step("Remove the Sauce Labs Backpack from the cart",
                inventoryPage::removeBackpackFromCart);

        ExtentLogger.step("Verify the cart badge is no longer displayed",
                () -> Assert.assertFalse(inventoryPage.isCartBadgeDisplayed(),
                        "Cart badge should not be displayed."));
    }

    @Test(description = "Sorting - TC030 Verify Sort Name A-Z",
            groups = "regression")
    public void verifySortNameAscending() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Sort products by name from A to Z",
                inventoryPage::sortProductsNameAscending);

        ExtentLogger.step("Verify product names are sorted A to Z",
                () -> assertProductNamesSorted(inventoryPage, Comparator.naturalOrder()));
    }

    @Test(description = "Sorting - TC031 Verify Sort Name Z-A",
            groups = "regression")
    public void verifySortNameDescending() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Sort products by name from Z to A",
                inventoryPage::sortProductsNameDescending);

        ExtentLogger.step("Verify product names are sorted Z to A",
                () -> assertProductNamesSorted(inventoryPage, Comparator.reverseOrder()));
    }

    @Test(description = "Sorting - TC032 Verify Sort Price Low-High",
            groups = "regression")
    public void verifyProductsSortLowToHigh() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Sort products by price from low to high",
                inventoryPage::sortProductsLowToHigh);

        ExtentLogger.step("Verify product prices are sorted low to high",
                () -> assertProductPricesSorted(inventoryPage, true));
    }

    @Test(description = "Sorting - TC033 Verify Sort Price High-Low",
            groups = "regression")
    public void verifyProductsSortHighToLow() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Sort products by price from high to low",
                inventoryPage::sortProductsHighToLow);

        ExtentLogger.step("Verify product prices are sorted high to low",
                () -> assertProductPricesSorted(inventoryPage, false));
    }

    @Test(description = "Positive - User opens the cart",
            groups = "regression")
    public void verifyUserCanOpenCart() {
        InventoryPage inventoryPage = login();
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        CartPage cartPage = ExtentLogger.step("Open the shopping cart",
                inventoryPage::openCart);

        ExtentLogger.step("Verify the cart page is displayed",
                () -> Assert.assertEquals(cartPage.getPageTitle(), TestData.CART_TITLE,
                        "Cart page title mismatch."));
    }

    /**
     * Authenticates with the public demo account for an independent inventory scenario.
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

    /**
     * Compares displayed product names with their expected sorted order.
     *
     * @param inventoryPage current inventory page
     * @param comparator requested name ordering
     */
    private void assertProductNamesSorted(
            InventoryPage inventoryPage, Comparator<String> comparator) {
        List<String> actualNames = inventoryPage.getProductNames();
        List<String> expectedNames = new ArrayList<>(TestData.CATALOG_NAMES);
        expectedNames.sort(comparator);
        Assert.assertEquals(actualNames, expectedNames, "Product names are not in the expected order.");
    }

    /**
     * Compares displayed product prices with their expected numeric order.
     *
     * @param inventoryPage current inventory page
     * @param ascending true for low-to-high order, false for high-to-low order
     */
    private void assertProductPricesSorted(InventoryPage inventoryPage, boolean ascending) {
        List<String> actualPrices = inventoryPage.getProductPrices();
        List<BigDecimal> expectedPrices = TestData.CATALOG_PRICES.stream()
                .map(MoneyUtils::amountFrom)
                .sorted(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder())
                .toList();
        List<BigDecimal> displayedPrices = actualPrices.stream()
                .map(MoneyUtils::amountFrom)
                .toList();
        Assert.assertEquals(displayedPrices, expectedPrices,
                "Product prices are not in the expected order.");
    }
}
