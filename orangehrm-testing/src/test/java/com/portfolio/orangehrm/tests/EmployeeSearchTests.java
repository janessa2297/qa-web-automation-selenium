package com.portfolio.orangehrm.tests;

import com.portfolio.orangehrm.base.BaseTest;
import com.portfolio.orangehrm.config.FrameworkConfig;
import com.portfolio.orangehrm.pages.EmployeeListPage;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class EmployeeSearchTests extends BaseTest {
    private EmployeeListPage employeeList() {
        return loginPage.loginSuccessfully(FrameworkConfig.username(), FrameworkConfig.password()).openEmployeeList();
    }

    @Test(groups = {"regression", "pim"}, description = "Searching for an unknown employee ID shows an empty result")
    public void unknownEmployeeIdShowsNoRecords() {
        Reporter.log("Search using an unknown employee ID within the supported ten-character limit");
        EmployeeListPage employees = employeeList().searchByEmployeeId("QA99999999");
        Assert.assertTrue(employees.hasNoRecords(), "Unknown ID should show No Records Found");
    }

    @Test(groups = {"regression", "pim"}, description = "Reset clears the employee filter and restores employee results")
    public void resetRestoresEmployeeList() {
        EmployeeListPage employees = employeeList().searchByEmployeeId("QA99999999");
        Assert.assertTrue(employees.hasNoRecords());
        Reporter.log("Reset the search and verify the filter is empty and employee rows return");
        employees.resetSearch();
        Assert.assertEquals(employees.employeeIdFilter(), "");
        Assert.assertTrue(employees.hasResults(), "Reset should restore the demo employee list");
    }

    @Test(groups = {"regression", "pim", "validation"},
        description = "Add Employee requires first and last name before saving")
    public void employeeNameIsRequired() {
        Reporter.log("Open Add Employee and submit with both name fields empty");
        var addEmployee = employeeList().openAddEmployee().submitEmptyName();
        Assert.assertTrue(addEmployee.hasRequiredNameValidation(), "First and last name should show Required");
        Assert.assertTrue(driver.getCurrentUrl().contains("/pim/addEmployee"), "Invalid form should stay on Add Employee");
    }
}
