package com.portfolio.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddEmployeePage extends BasePage {
    private static final By FIRST_NAME = By.cssSelector("input[name='firstName']");
    private static final By LAST_NAME = By.cssSelector("input[name='lastName']");
    private static final By EMPLOYEE_ID = By.xpath(
        "//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By SAVE = By.cssSelector("button[type='submit']");

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public AddEmployeePage waitUntilLoaded() {
        waitForUrl("/pim/addEmployee");
        visible(FIRST_NAME);
        waitForLoadingToFinish();
        return this;
    }

    public AddEmployeePage enterEmployeeName(String firstName, String lastName) {
        fill(FIRST_NAME, firstName);
        fill(LAST_NAME, lastName);
        return this;
    }

    public String generatedEmployeeId() {
        return wait.until(d -> {
            String value = visible(EMPLOYEE_ID).getDomProperty("value");
            return value != null && !value.isBlank() ? value : null;
        });
    }

    public void save() {
        click(SAVE);
        waitForUrl("/pim/viewPersonalDetails/");
    }

    public AddEmployeePage submitEmptyName() {
        enterEmployeeName("", "");
        click(SAVE);
        wait.until(d -> d.findElements(By.cssSelector(".oxd-input-field-error-message")).size() == 2);
        return this;
    }

    public boolean hasRequiredNameValidation() {
        return nameIsRequired(FIRST_NAME) && nameIsRequired(LAST_NAME);
    }

    private boolean nameIsRequired(By input) {
        return visible(input).findElement(By.xpath("./ancestor::div[contains(@class,'oxd-input-group')]"))
            .findElements(By.cssSelector(".oxd-input-field-error-message")).stream()
            .anyMatch(error -> error.isDisplayed() && error.getText().equals("Required"));
    }
}
