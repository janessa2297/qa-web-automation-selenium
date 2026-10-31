package com.portfolio.orangehrm.tests;

import com.portfolio.orangehrm.base.BaseTest;
import com.portfolio.orangehrm.config.FrameworkConfig;
import com.portfolio.orangehrm.pages.DashboardPage;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SessionTests extends BaseTest {
    @DataProvider(name = "protectedPages")
    public Object[][] protectedPages() {
        return new Object[][] {{"/web/index.php/dashboard/index"}, {"/web/index.php/pim/viewEmployeeList"}};
    }

    @Test(groups = {"regression", "session"}, dataProvider = "protectedPages",
        description = "Unauthenticated access to a protected page redirects to login")
    public void protectedPagesRequireAuthentication(String path) {
        Reporter.log("Navigate directly to protected route: " + path);
        Assert.assertTrue(loginPage.openProtectedPath(path).isLoaded(), "Login form should replace protected content");
    }

    @Test(groups = {"regression", "session"}, description = "Logout prevents direct access to the previous dashboard")
    public void loggedOutSessionCannotReopenDashboard() {
        Reporter.log("Log in, then explicitly log out");
        loginPage = loginPage.loginSuccessfully(FrameworkConfig.username(), FrameworkConfig.password()).logout();
        Reporter.log("Revisit the dashboard URL after logout");
        Assert.assertTrue(loginPage.openProtectedPath("/web/index.php/dashboard/index").isLoaded());
    }

    @Test(groups = {"regression", "session"}, description = "Authenticated session survives a browser refresh")
    public void dashboardRemainsAvailableAfterRefresh() {
        loginPage.loginSuccessfully(FrameworkConfig.username(), FrameworkConfig.password());
        Reporter.log("Refresh the dashboard within the authenticated session");
        driver.navigate().refresh();
        Assert.assertTrue(new DashboardPage(driver).waitUntilLoaded().isLoaded());
    }
}
