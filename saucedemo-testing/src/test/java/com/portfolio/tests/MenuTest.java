/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.tests;

import java.net.URI;

import com.portfolio.base.BaseTest;
import com.portfolio.config.ConfigReader;
import com.portfolio.data.TestData;
import com.portfolio.utils.ExtentLogger;
import com.portfolio.pages.CartPage;
import com.portfolio.pages.InventoryPage;
import com.portfolio.pages.LoginPage;
import com.portfolio.pages.MenuPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MenuTest extends BaseTest {

    /**
     * Verifies the sidebar exposes every menu option after opening.
     */
    @Test(description = "Menu - TC070 Verify Menu Opens",
            groups = {"menu", "regression"})
    public void verifyMenuOpens() {
        login();
        MenuPage menu = openMenu();
        ExtentLogger.step("Verify all sidebar controls are displayed",
                () -> Assert.assertTrue(menu.isMenuDisplayed(),
                        "Sidebar controls are missing or hidden."));
    }

    /**
     * Verifies All Items returns from the cart to the inventory.
     */
    @Test(description = "Menu - TC071 Verify All Items",
            groups = {"menu", "regression"})
    public void verifyAllItems() {
        InventoryPage inventory = login();
        ExtentLogger.step("Open the cart", inventory::openCart);
        MenuPage menu = openMenu();
        InventoryPage products = ExtentLogger.step("Select All Items",
                menu::clickAllItems);
        ExtentLogger.step("Verify the Products page is displayed",
                () -> {
                    Assert.assertTrue(products.isInventoryPageDisplayed(),
                            "All Items did not open inventory.");
                    Assert.assertEquals(products.getPageTitle(), TestData.PRODUCTS_TITLE);
                });
    }

    /**
     * Verifies About navigates to the actual HTTPS Sauce Labs host.
     */
    @Test(description = "Menu - TC072 Verify About",
            groups = {"menu", "regression"})
    public void verifyAbout() {
        login();
        MenuPage menu = openMenu();
        ExtentLogger.step("Select About", menu::clickAbout);
        ExtentLogger.step("Verify the Sauce Labs destination",
                () -> {
                    URI destination = URI.create(driver.getCurrentUrl());
                    URI expected = URI.create(ConfigReader.getProperty("aboutUrl"));
                    Assert.assertEquals(destination.getScheme(), expected.getScheme());
                    Assert.assertEquals(destination.getHost(), expected.getHost(),
                            "About did not navigate to Sauce Labs.");
                });
    }

    /**
     * Verifies Reset App State clears a populated cart without logging out.
     */
    @Test(description = "Menu - TC073 Verify Reset App State",
            groups = {"menu", "regression"})
    public void verifyResetAppState() {
        InventoryPage inventory = login();
        ExtentLogger.step("Add backpack and bike light",
                () -> {
                    inventory.addBackpackToCart();
                    inventory.addBikeLightToCart();
                });
        ExtentLogger.step("Verify two products were added",
                () -> Assert.assertEquals(inventory.getCartCount(),
                        Integer.toString(TestData.PURCHASE_PRODUCT_NAMES.size())));
        MenuPage menu = openMenu();
        ExtentLogger.step("Select Reset App State", menu::resetAppState);
        ExtentLogger.step("Close the sidebar", menu::close);
        CartPage cart = ExtentLogger.step("Open the cart after resetting",
                inventory::openCart);
        ExtentLogger.step("Verify the authenticated cart is empty",
                () -> {
                    Assert.assertEquals(cart.getPageTitle(), TestData.CART_TITLE);
                    Assert.assertEquals(cart.getProductCount(), 0,
                            "Reset App State did not clear all products.");
                    Assert.assertFalse(inventory.isCartBadgeDisplayed(),
                            "Cart badge should disappear after reset.");
                });
    }

    /**
     * Verifies Logout returns to a usable login form.
     */
    @Test(description = "Menu - TC074 Verify Logout",
            groups = {"menu", "regression"})
    public void verifyLogout() {
        login();
        MenuPage menu = openMenu();
        LoginPage loginPage = ExtentLogger.step("Select Logout", menu::logout);
        ExtentLogger.step("Verify the login form is displayed",
                () -> {
                    Assert.assertTrue(loginPage.isUsernameFieldDisplayed());
                    Assert.assertTrue(loginPage.isPasswordFieldDisplayed());
                    Assert.assertTrue(loginPage.isLoginButtonDisplayed());
                });
    }

    /**
     * Authenticates using the configured public demo account.
     *
     * @return authenticated inventory page
     */
    private InventoryPage login() {
        LoginPage loginPage = new LoginPage(driver);
        return ExtentLogger.step("Log in with the valid demo account",
                () -> loginPage.login(ConfigReader.getProperty("username"),
                        ConfigReader.getProperty("password")));
    }

    /**
     * Opens the shared sidebar on the current authenticated page.
     *
     * @return opened menu
     */
    private MenuPage openMenu() {
        MenuPage menu = new MenuPage(driver);
        ExtentLogger.step("Open the sidebar menu", menu::open);
        return menu;
    }
}
