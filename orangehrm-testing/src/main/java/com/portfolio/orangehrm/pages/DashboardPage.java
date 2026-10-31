package com.portfolio.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {
    private static final By DASHBOARD_HEADING = By.cssSelector(".oxd-topbar-header-breadcrumb h6");
    private static final By PIM_LINK = By.cssSelector("a[href*='/pim/viewPimModule']");
    private static final By USER_MENU = By.cssSelector(".oxd-userdropdown-tab");
    private static final By LOGOUT = By.cssSelector("a[href*='/auth/logout']");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public DashboardPage waitUntilLoaded() {
        waitForUrl("/dashboard/index");
        visible(DASHBOARD_HEADING);
        return this;
    }

    public boolean isLoaded() {
        return visible(DASHBOARD_HEADING).getText().equalsIgnoreCase("Dashboard");
    }

    public EmployeeListPage openEmployeeList() {
        click(PIM_LINK);
        return new EmployeeListPage(driver).waitUntilLoaded();
    }

    public LoginPage logout() {
        click(USER_MENU);
        click(LOGOUT);
        waitForUrl("/auth/login");
        return new LoginPage(driver);
    }
}
