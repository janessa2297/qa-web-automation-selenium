package com.portfolio.orangehrm.tests;

import com.portfolio.orangehrm.base.BaseTest;
import com.portfolio.orangehrm.config.FrameworkConfig;
import com.portfolio.orangehrm.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardTests extends BaseTest {
    @Test(groups = {"smoke", "pim"}, description = "Authenticated user can navigate to employee list")
    public void employeeListCanBeOpened() {
        DashboardPage dashboard = loginPage.loginSuccessfully(
            FrameworkConfig.username(), FrameworkConfig.password());
        Assert.assertTrue(dashboard.openEmployeeList().hasEmployeeSearch());
    }

    @Test(groups = {"smoke", "auth"}, description = "Logout ends the active session")
    public void logoutReturnsToLogin() {
        DashboardPage dashboard = loginPage.loginSuccessfully(
            FrameworkConfig.username(), FrameworkConfig.password());
        Assert.assertTrue(dashboard.logout().isLoaded());
    }
}
