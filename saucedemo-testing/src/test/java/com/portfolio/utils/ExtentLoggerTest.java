/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class ExtentLoggerTest {

    private ExtentTest scenario;

    /**
     * Creates an isolated, browser-free report scenario for logger validation.
     */
    private void createScenario() {
        scenario = new ExtentReports().createTest("Isolated logger validation");
        ExtentLogger.setCurrentTest(scenario);
    }

    /**
     * Prevents test-local logging context from leaking into the next invocation.
     */
    @AfterMethod(alwaysRun = true)
    public void clearScenario() {
        ExtentLogger.clearCurrentTest();
    }

    /**
     * Verifies direct messages retain their status and business descriptions.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifyDirectBusinessLogs() {
        createScenario();
        ExtentLogger.info("Login with valid user");
        ExtentLogger.pass("Login successful");
        Assert.assertEquals(scenario.getModel().getLogs().size(), 2);
        Assert.assertEquals(scenario.getModel().getLogs().get(0).getStatus(), Status.INFO);
        Assert.assertEquals(scenario.getModel().getLogs().get(0).getDetails(), "Login with valid user");
        Assert.assertEquals(scenario.getModel().getLogs().get(1).getStatus(), Status.PASS);
        Assert.assertEquals(scenario.getModel().getLogs().get(1).getDetails(), "Login successful");
    }

    /**
     * Verifies steps return action results and use the requested success message.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifySuccessfulStep() {
        createScenario();
        String value = ExtentLogger.step("Open cart", "Cart displayed successfully", () -> "cart");
        Assert.assertEquals(value, "cart");
        Assert.assertEquals(scenario.getModel().getLogs().size(), 2);
        Assert.assertEquals(scenario.getModel().getLogs().get(1).getDetails(),
                "Cart displayed successfully");
        Assert.assertTrue(scenario.getModel().getChildren().isEmpty(),
                "Business logs should appear directly in the scenario.");
    }

    /**
     * Verifies explicit failure cannot leave TestNG reporting a successful test.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifyExplicitFailure() {
        createScenario();
        Assert.expectThrows(AssertionError.class, () -> ExtentLogger.fail("Cart count mismatch"));
        Assert.assertEquals(scenario.getStatus(), Status.FAIL);
    }

    /**
     * Verifies runtime failures are preserved and never followed by PASS logs.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifyFailedAction() {
        createScenario();
        IllegalStateException original = new IllegalStateException("Checkout unavailable");
        IllegalStateException actual = Assert.expectThrows(IllegalStateException.class,
                () -> ExtentLogger.step("Proceed to checkout", (Runnable) () -> {
                    throw original;
                }));
        Assert.assertSame(actual, original);
        Assert.assertEquals(scenario.getStatus(), Status.FAIL);
        Assert.assertTrue(scenario.getModel().getLogs().stream()
                .noneMatch(log -> log.getStatus() == Status.PASS));
    }

    /**
     * Verifies assertion failures retain the original assertion instance.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifyFailedAssertion() {
        createScenario();
        AssertionError original = new AssertionError("Product missing");
        AssertionError actual = Assert.expectThrows(AssertionError.class,
                () -> ExtentLogger.step("Verify selected product", (Runnable) () -> {
                    throw original;
                }));
        Assert.assertSame(actual, original);
        Assert.assertEquals(scenario.getStatus(), Status.FAIL);
    }

    /**
     * Verifies missing invocation context is an explicit error, not a silent no-op.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifyMissingContext() {
        createScenario();
        ExtentLogger.clearCurrentTest();
        Assert.expectThrows(IllegalStateException.class, () -> ExtentLogger.info("Outside test"));
    }
}
