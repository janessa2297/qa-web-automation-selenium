package com.portfolio.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Personnel Information Management (PIM) employee list. */
public class EmployeeListPage extends BasePage {
    private static final By ADD_EMPLOYEE = By.xpath("//button[contains(@class,'oxd-button')][normalize-space()='Add']");
    private static final By EMPLOYEE_ID = By.xpath(
        "//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By SEARCH = By.cssSelector(".oxd-form-actions button[type='submit']");
    private static final By RESET = By.xpath("//button[normalize-space()='Reset']");
    private static final By TABLE_BODY = By.cssSelector(".oxd-table-body");
    private static final By EMPTY_RESULT = By.xpath("//span[normalize-space()='No Records Found']");
    private static final By CONFIRM_DELETE = By.cssSelector(".oxd-dialog-container button.oxd-button--label-danger");

    public EmployeeListPage(WebDriver driver) {
        super(driver);
    }

    public EmployeeListPage waitUntilLoaded() {
        waitForUrl("/pim/viewEmployeeList");
        visible(EMPLOYEE_ID);
        resultBody();
        waitForLoadingToFinish();
        return this;
    }

    public boolean hasEmployeeSearch() {
        return visible(EMPLOYEE_ID).isDisplayed() && visible(SEARCH).isDisplayed();
    }

    public AddEmployeePage openAddEmployee() {
        click(ADD_EMPLOYEE);
        return new AddEmployeePage(driver).waitUntilLoaded();
    }

    public EmployeeListPage searchByEmployeeId(String id) {
        WebElement previousResults = resultBody();
        fill(EMPLOYEE_ID, id);
        click(SEARCH);
        // The table body is replaced by a loader during each search.
        wait.until(ExpectedConditions.stalenessOf(previousResults));
        resultBody();
        waitForLoadingToFinish();
        wait.until(d -> !d.findElements(By.cssSelector(".oxd-table-card")).isEmpty()
            || !d.findElements(EMPTY_RESULT).isEmpty());
        return this;
    }

    public boolean hasNoRecords() {
        return visible(EMPTY_RESULT).isDisplayed();
    }

    public EmployeeListPage resetSearch() {
        WebElement previousResults = resultBody();
        click(RESET);
        wait.until(d -> "".equals(d.findElement(EMPLOYEE_ID).getDomProperty("value")));
        wait.until(ExpectedConditions.stalenessOf(previousResults));
        resultBody();
        waitForLoadingToFinish();
        return this;
    }

    public String employeeIdFilter() {
        return visible(EMPLOYEE_ID).getDomProperty("value");
    }

    private WebElement resultBody() {
        // An empty result body has no height, so it is present rather than visible.
        return wait.until(ExpectedConditions.presenceOfElementLocated(TABLE_BODY));
    }

    public boolean hasResults() {
        return !wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.cssSelector(".oxd-table-card"))).isEmpty();
    }

    private By employeeRow(String employeeId) {
        // IDs come from the application's generated employee-ID input, not free-form user text.
        return By.xpath("//div[contains(@class,'oxd-table-card')][.//*[normalize-space(text())='"
            + employeeId + "']]");
    }

    public boolean hasEmployee(String employeeId, String firstName, String lastName) {
        WebElement row = visible(employeeRow(employeeId));
        String details = row.getText();
        return details.contains(firstName) && details.contains(lastName);
    }

    /** Only delete a row after verifying it belongs to our just-created test employee. */
    public void deleteCreatedEmployee(String employeeId, String firstName, String lastName) {
        WebElement row = visible(employeeRow(employeeId));
        String details = row.getText();
        if (!details.contains(firstName) || !details.contains(lastName)) {
            throw new IllegalStateException("Employee row did not match test-owned record; deletion aborted");
        }
        row.findElement(By.xpath(".//button[.//i[contains(@class,'bi-trash')]]")).click();
        click(CONFIRM_DELETE);
        wait.until(ExpectedConditions.invisibilityOf(row));
    }
}
