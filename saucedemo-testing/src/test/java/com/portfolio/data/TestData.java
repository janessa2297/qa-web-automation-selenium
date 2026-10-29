/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.data;

import java.math.BigDecimal;
import java.util.List;

import com.portfolio.config.ConfigReader;
import org.testng.annotations.DataProvider;

public final class TestData {

    public static final String INVALID_USERNAME = "invalid_user";
    public static final String INVALID_PASSWORD = "invalid_password";
    public static final String EMPTY_VALUE = "";

    public static final String LOCKED_OUT_MESSAGE =
            "Epic sadface: Sorry, this user has been locked out.";
    public static final String INVALID_CREDENTIALS_MESSAGE =
            "Epic sadface: Username and password do not match any user in this service";
    public static final String USERNAME_REQUIRED_MESSAGE =
            "Epic sadface: Username is required";
    public static final String PASSWORD_REQUIRED_MESSAGE =
            "Epic sadface: Password is required";

    public static final String PRODUCTS_TITLE = "Products";
    public static final String CART_TITLE = "Your Cart";
    public static final String CHECKOUT_INFO_TITLE = "Checkout: Your Information";
    public static final String CHECKOUT_OVERVIEW_TITLE = "Checkout: Overview";
    public static final String BACKPACK_NAME = "Sauce Labs Backpack";
    public static final String BACKPACK_PRICE = "$29.99";
    public static final String BIKE_LIGHT_NAME = "Sauce Labs Bike Light";
    public static final String BIKE_LIGHT_PRICE = "$9.99";
    public static final BigDecimal SALES_TAX_RATE = new BigDecimal("0.08");
    public static final String LOWEST_PRODUCT_PRICE = "$7.99";
    public static final String ONE_ITEM_COUNT = "1";
    public static final List<String> PURCHASE_PRODUCT_NAMES =
            List.of(BACKPACK_NAME, BIKE_LIGHT_NAME);
    public static final List<String> PURCHASE_PRODUCT_PRICES =
            List.of(BACKPACK_PRICE, BIKE_LIGHT_PRICE);
    public static final List<String> CATALOG_NAMES = List.of(
            BACKPACK_NAME, BIKE_LIGHT_NAME, "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket", "Sauce Labs Onesie",
            "Test.allTheThings() T-Shirt (Red)");
    public static final List<String> CATALOG_PRICES = List.of(
            BACKPACK_PRICE, BIKE_LIGHT_PRICE, "$15.99", "$49.99",
            LOWEST_PRODUCT_PRICE, "$15.99");
    public static final String ORDER_COMPLETE_HEADER = "Thank you for your order!";
    public static final String ORDER_COMPLETE_MESSAGE =
            "Your order has been dispatched, and will arrive just as fast as the pony can get there!";
    public static final String CHECKOUT_FIRST_NAME = "Demo";
    public static final String CHECKOUT_LAST_NAME = "Customer";
    public static final String CHECKOUT_POSTAL_CODE = "12345";
    public static final String FIRST_NAME_REQUIRED_MESSAGE =
            "Error: First Name is required";
    public static final String LAST_NAME_REQUIRED_MESSAGE =
            "Error: Last Name is required";
    public static final String POSTAL_CODE_REQUIRED_MESSAGE =
            "Error: Postal Code is required";

    private TestData() {
    }

    /**
     * Supplies login rejection scenarios, including each required empty-field case.
     *
     * @return username, password, and expected error message for each scenario
     */
    @DataProvider(name = "invalidLogins")
    public static Object[][] invalidLogins() {
        String password = ConfigReader.getProperty("password");
        String username = ConfigReader.getProperty("username");
        return new Object[][] {
                {"Negative - TC002 Verify Locked Out User",
                        ConfigReader.getProperty("lockedOutUsername"), password,
                        LOCKED_OUT_MESSAGE},
                {"Negative - TC003 Verify Invalid Username",
                        INVALID_USERNAME, password, INVALID_CREDENTIALS_MESSAGE},
                {"Negative - TC004 Verify Invalid Password",
                        username, INVALID_PASSWORD, INVALID_CREDENTIALS_MESSAGE},
                {"Negative - TC005 Verify Invalid Username And Password",
                        INVALID_USERNAME, INVALID_PASSWORD, INVALID_CREDENTIALS_MESSAGE},
                {"Negative - TC006 Verify Empty Username",
                        EMPTY_VALUE, password, USERNAME_REQUIRED_MESSAGE},
                {"Negative - TC007 Verify Empty Password",
                        username, EMPTY_VALUE, PASSWORD_REQUIRED_MESSAGE},
                {"Negative - TC008 Verify Empty Username And Password",
                        EMPTY_VALUE, EMPTY_VALUE, USERNAME_REQUIRED_MESSAGE}
        };
    }
}
