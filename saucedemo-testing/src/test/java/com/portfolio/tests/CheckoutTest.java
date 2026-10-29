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

public class CheckoutTest extends BaseTest {

    @Test(description = "Checkout Info - TC050 Verify Checkout Information Page",
            groups = "regression")
    public void verifyCheckoutInformationPage() {
        CheckoutInfoPage page = openCheckoutInfoPage();
        ExtentLogger.step("Verify checkout information page title",
                () -> Assert.assertEquals(page.getPageTitle(), TestData.CHECKOUT_INFO_TITLE,
                        "Checkout information page title mismatch."));
    }

    @Test(description = "Checkout Info - TC051 Verify Empty First Name",
            groups = {"negative", "regression"})
    public void verifyEmptyFirstName() {
        CheckoutInfoPage page = openCheckoutInfoPage();
        ExtentLogger.step("Enter last name and postal code",
                () -> {
                    page.enterLastName(TestData.CHECKOUT_LAST_NAME);
                    page.enterPostalCode(TestData.CHECKOUT_POSTAL_CODE);
                });
        assertCheckoutError(page, TestData.FIRST_NAME_REQUIRED_MESSAGE,
                "Verify first name is required");
    }

    @Test(description = "Checkout Info - TC052 Verify Empty Last Name",
            groups = {"negative", "regression"})
    public void verifyEmptyLastName() {
        CheckoutInfoPage page = openCheckoutInfoPage();
        ExtentLogger.step("Enter first name and postal code",
                () -> {
                    page.enterFirstName(TestData.CHECKOUT_FIRST_NAME);
                    page.enterPostalCode(TestData.CHECKOUT_POSTAL_CODE);
                });
        assertCheckoutError(page, TestData.LAST_NAME_REQUIRED_MESSAGE,
                "Verify last name is required");
    }

    @Test(description = "Checkout Info - TC053 Verify Empty Postal Code",
            groups = {"negative", "regression"})
    public void verifyEmptyPostalCode() {
        CheckoutInfoPage page = openCheckoutInfoPage();
        ExtentLogger.step("Enter first and last name",
                () -> {
                    page.enterFirstName(TestData.CHECKOUT_FIRST_NAME);
                    page.enterLastName(TestData.CHECKOUT_LAST_NAME);
                });
        assertCheckoutError(page, TestData.POSTAL_CODE_REQUIRED_MESSAGE,
                "Verify postal code is required");
    }

    @Test(description = "Checkout Info - TC054 Verify All Fields Empty",
            groups = {"negative", "regression"})
    public void verifyAllCheckoutFieldsEmpty() {
        CheckoutInfoPage page = openCheckoutInfoPage();
        assertCheckoutError(page, TestData.FIRST_NAME_REQUIRED_MESSAGE,
                "Verify all required checkout fields are validated");
    }

    @Test(description = "Overview - TC055 Verify Overview Page",
            groups = "regression")
    public void verifyOverviewPage() {
        CheckoutOverviewPage page = openCheckoutOverviewPage();
        ExtentLogger.step("Verify checkout overview title",
                () -> Assert.assertEquals(page.getPageTitle(),
                        TestData.CHECKOUT_OVERVIEW_TITLE,
                        "Checkout overview title mismatch."));
    }

    @Test(description = "Overview - TC056 Verify Products Displayed",
            groups = "regression")
    public void verifyOverviewProductsDisplayed() {
        CheckoutOverviewPage page = openCheckoutOverviewWithMultipleProducts();
        ExtentLogger.step("Verify both cart products are shown in overview",
                () -> Assert.assertEquals(page.getProductNames(),
                        TestData.PURCHASE_PRODUCT_NAMES,
                        "Overview product list mismatch."));
    }

    @Test(description = "Overview - TC057 Verify Item Total",
            groups = "regression")
    public void verifyItemTotal() {
        CheckoutOverviewPage page = openCheckoutOverviewPage();
        ExtentLogger.step("Verify item total matches the displayed item prices",
                () -> {
                    BigDecimal itemTotal = page.getProductPrices().stream()
                            .map(MoneyUtils::amountFrom)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Assert.assertEquals(amountFrom(page.getSubtotal()), itemTotal,
                            "Item total does not match the sum of item prices.");
                });
    }

    @Test(description = "Overview - TC058 Verify Tax",
            groups = "regression")
    public void verifyTaxDisplayed() {
        CheckoutOverviewPage page = openCheckoutOverviewPage();
        ExtentLogger.step("Verify tax matches the expected sales tax",
                () -> {
                    BigDecimal expectedTax = amountFrom(TestData.BACKPACK_PRICE)
                            .multiply(TestData.SALES_TAX_RATE)
                            .setScale(2, RoundingMode.HALF_UP);
                    Assert.assertEquals(amountFrom(page.getTax()), expectedTax,
                            "Tax does not match the expected rate and rounding.");
                });
    }

    @Test(description = "Overview - TC059 Verify Total",
            groups = "regression")
    public void verifyTotalDisplayed() {
        CheckoutOverviewPage page = openCheckoutOverviewPage();
        ExtentLogger.step("Verify total matches the expected item price plus tax",
                () -> {
                    BigDecimal subtotal = amountFrom(TestData.BACKPACK_PRICE);
                    BigDecimal tax = subtotal.multiply(TestData.SALES_TAX_RATE)
                            .setScale(2, RoundingMode.HALF_UP);
                    Assert.assertEquals(amountFrom(page.getTotal()), subtotal.add(tax),
                            "Total does not match the expected price and tax.");
                });
    }

    @Test(description = "Validation - TC060 Verify Subtotal Plus Tax Equals Total",
            groups = "regression")
    public void verifySubtotalPlusTaxEqualsTotal() {
        CheckoutOverviewPage page = openCheckoutOverviewPage();
        ExtentLogger.step("Verify subtotal plus tax equals total",
                () -> Assert.assertEquals(
                        amountFrom(page.getSubtotal()).add(amountFrom(page.getTax())),
                        amountFrom(page.getTotal()),
                        "Subtotal plus tax should equal the total."));
    }

    @Test(description = "Completion - TC061 Verify Finish Order",
            groups = {"smoke", "e2e", "regression"})
    public void verifyFinishOrder() {
        CheckoutOverviewPage overviewPage = openCheckoutOverviewPage();
        CheckoutCompletePage completePage = ExtentLogger.step("Finish the order",
                overviewPage::clickFinish);
        ExtentLogger.step("Verify order completion page is displayed",
                () -> Assert.assertEquals(completePage.getCompleteHeader(),
                        TestData.ORDER_COMPLETE_HEADER,
                        "Order completion page was not displayed."));
    }

    @Test(description = "Completion - TC062 Verify Thank You Message",
            groups = {"smoke", "e2e", "regression"})
    public void verifyThankYouMessage() {
        CheckoutCompletePage page = completeOrder();
        ExtentLogger.step("Verify the thank-you heading",
                () -> Assert.assertEquals(page.getCompleteHeader(),
                        TestData.ORDER_COMPLETE_HEADER, "Thank-you heading mismatch."));
    }

    @Test(description = "Completion - TC063 Verify Completion Message",
            groups = {"smoke", "e2e", "regression"})
    public void verifyCompletionMessage() {
        CheckoutCompletePage page = completeOrder();
        ExtentLogger.step("Verify the order completion message",
                () -> Assert.assertEquals(page.getCompleteMessage(),
                        TestData.ORDER_COMPLETE_MESSAGE,
                        "Order completion message mismatch."));
    }

    @Test(description = "Completion - TC064 Verify Back Home",
            groups = "regression")
    public void verifyBackHome() {
        CheckoutCompletePage completePage = completeOrder();
        InventoryPage inventoryPage = ExtentLogger.step(
                "Select Back Home", completePage::clickBackHome);
        ExtentLogger.step("Verify the products page is displayed",
                () -> Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(),
                        "Back Home did not return to the products page."));
    }

    /**
     * Submits the checkout form and verifies the requested validation message.
     *
     * @param page checkout information page
     * @param expectedError expected validation text
     * @param stepDescription report step description
     */
    private void assertCheckoutError(
            CheckoutInfoPage page, String expectedError, String stepDescription) {
        ExtentLogger.step("Submit checkout information", page::submitInformation);
        ExtentLogger.step(stepDescription,
                () -> Assert.assertEquals(page.getErrorMessage(), expectedError,
                        "Checkout validation message mismatch."));
    }

    /**
     * Opens a new demo order through checkout completion.
     *
     * @return checkout completion page
     */
    private CheckoutCompletePage completeOrder() {
        CheckoutOverviewPage overviewPage = openCheckoutOverviewPage();
        return ExtentLogger.step("Finish the order", overviewPage::clickFinish);
    }

    /**
     * Creates a fresh authenticated cart and opens the checkout information page.
     *
     * @return checkout information page
     */
    private CheckoutInfoPage openCheckoutInfoPage() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = ExtentLogger.step(
                "Log in with the valid demo account",
                () -> loginPage.login(
                        ConfigReader.getProperty("username"),
                        ConfigReader.getProperty("password")));
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        CartPage cartPage = ExtentLogger.step("Open the shopping cart",
                inventoryPage::openCart);
        return ExtentLogger.step("Open checkout information",
                cartPage::clickCheckout);
    }

    /**
     * Submits valid customer information and opens order overview.
     *
     * @return checkout overview page
     */
    private CheckoutOverviewPage openCheckoutOverviewPage() {
        return submitValidCustomerInformation(openCheckoutInfoPage());
    }

    /**
     * Opens an overview containing a backpack and bike light.
     *
     * @return checkout overview page with both products
     */
    private CheckoutOverviewPage openCheckoutOverviewWithMultipleProducts() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = ExtentLogger.step(
                "Log in with the valid demo account",
                () -> loginPage.login(
                        ConfigReader.getProperty("username"),
                        ConfigReader.getProperty("password")));
        ExtentLogger.step("Add the Sauce Labs Backpack to the cart",
                inventoryPage::addBackpackToCart);
        ExtentLogger.step("Add the Sauce Labs Bike Light to the cart",
                inventoryPage::addBikeLightToCart);
        CartPage cartPage = ExtentLogger.step("Open the shopping cart",
                inventoryPage::openCart);
        CheckoutInfoPage infoPage = ExtentLogger.step("Open checkout information",
                cartPage::clickCheckout);
        return submitValidCustomerInformation(infoPage);
    }

    /**
     * Enters valid customer values as individually reported steps.
     *
     * @param checkoutInfoPage checkout information page
     * @return checkout overview page
     */
    private CheckoutOverviewPage submitValidCustomerInformation(
            CheckoutInfoPage checkoutInfoPage) {
        ExtentLogger.step("Enter the checkout first name",
                () -> checkoutInfoPage.enterFirstName(TestData.CHECKOUT_FIRST_NAME));
        ExtentLogger.step("Enter the checkout last name",
                () -> checkoutInfoPage.enterLastName(TestData.CHECKOUT_LAST_NAME));
        ExtentLogger.step("Enter the checkout postal code",
                () -> checkoutInfoPage.enterPostalCode(TestData.CHECKOUT_POSTAL_CODE));
        return ExtentLogger.step("Continue to the order overview",
                checkoutInfoPage::continueToOverview);
    }

}
