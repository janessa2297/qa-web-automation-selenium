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

public class CheckoutInfoPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    /**
     * Waits until checkout information is reflected in the current URL.
     */
    public void waitForCheckoutInfoPage() {
        waitForUrlContains("checkout-step-one");
    }

    public void enterFirstName(String firstName) {
        type(firstNameField, firstName);
    }

    public void enterLastName(String lastName) {
        type(lastNameField, lastName);
    }

    public void enterPostalCode(String postalCode) {
        type(postalCodeField, postalCode);
    }

    /**
     * Returns the validation message displayed after invalid checkout submission.
     *
     * @return validation message
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Submits the currently entered checkout information.
     */
    public void submitInformation() {
        click(continueButton);
    }

    /**
     * Submits the entered customer details and opens the order overview.
     *
     * @return checkout overview page
     */
    public CheckoutOverviewPage continueToOverview() {
        submitInformation();
        waitForUrlContains("checkout-step-two.html");
        return new CheckoutOverviewPage(driver);
    }

    public CheckoutOverviewPage continueCheckout(
            String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return continueToOverview();
    }
}
