/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.listeners;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.ScreenCapture;
import com.portfolio.base.BaseTest;
import com.portfolio.tests.LoginTest;
import com.portfolio.utils.ExtentLogger;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TestListenerTest {

    private static final long START = 1_792_843_200_000L;
    private static final long END = START + 1250;
    private static final String PNG =
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+aS1sAAAAASUVORK5CYII=";
    private final List<ExtentTest> syntheticReports = new ArrayList<>();
    private final ExtentReports isolatedReports = new ExtentReports();

    /**
     * Removes synthetic failures so listener validation does not pollute execution results.
     */
    @AfterMethod(alwaysRun = true)
    public void removeSyntheticReports() {
        syntheticReports.forEach(isolatedReports::removeTest);
        syntheticReports.clear();
        ExtentLogger.clearCurrentTest();
    }

    /**
     * Verifies feature titles, readable categories, and exact TestNG timestamps.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifyTitleCategoriesAndTiming() {
        ITestResult result = result("Positive - TC001 Verify Valid Login", new Object[0], null, null);
        TestListener listener = new TestListener(isolatedReports);
        listener.onTestStart(result);
        ExtentLogger.info("Login with valid user");
        ExtentLogger.pass("Login successful");
        listener.onTestSuccess(result);
        ExtentTest report = report(result);
        Assert.assertEquals(report.getModel().getName(), "Login - TC001 Verify Valid Login");
        List<String> categories = report.getModel().getCategorySet().stream()
                .map(category -> category.getName()).toList();
        Assert.assertTrue(categories.containsAll(List.of("Login", "Smoke", "Regression", "E2E")));
        Assert.assertEquals(report.getModel().getStartTime().getTime(), START);
        Assert.assertEquals(report.getModel().getEndTime().getTime(), END);
        Assert.assertTrue(report.getModel().getLogs().stream()
                .anyMatch(log -> log.getDetails().contains("Duration: 1250 ms")));
        Assert.expectThrows(IllegalStateException.class, () -> ExtentLogger.info("After completion"));
    }

    /**
     * Verifies data-driven case names do not expose credential parameters.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifyScenarioTitleDoesNotExposeCredentials() {
        ITestResult result = result("{scenario}",
                new Object[] {"Negative - TC002 Verify Locked Out User", "private-user", "private-password"},
                null, null);
        TestListener listener = new TestListener(isolatedReports);
        listener.onTestStart(result);
        listener.onTestSuccess(result);
        ExtentTest report = report(result);
        Assert.assertEquals(report.getModel().getName(), "Login - TC002 Verify Locked Out User");
        Assert.assertFalse(report.getModel().getName().contains("private"));
    }

    @Test(groups = {"reporting", "framework"})
    public void verifyMissingDescriptionUsesReadableTitle() {
        ITestResult result = result(null, new Object[0], null, null);
        TestListener listener = new TestListener(isolatedReports);
        listener.onTestStart(result);
        listener.onTestSuccess(result);
        Assert.assertEquals(report(result).getModel().getName(), "Login - Reporting Validation");
    }

    /**
     * Verifies setup-related skips can be recorded without an onTestStart callback.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifySkippedCaseWithoutStart() {
        ITestResult result = result("UI - TC009 Verify Username Field Displayed", new Object[0],
                new IllegalStateException("Browser setup failed"), null);
        new TestListener(isolatedReports).onTestSkipped(result);
        Assert.assertEquals(report(result).getStatus(), Status.SKIP);
    }

    /**
     * Verifies a failed browser test includes an embedded, portable screenshot.
     */
    @Test(groups = {"reporting", "framework"})
    public void verifyFailureScreenshotIsEmbedded() {
        WebDriver screenshotDriver = (WebDriver) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[] {WebDriver.class, TakesScreenshot.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getScreenshotAs")) {
                        return Base64.getDecoder().decode(PNG);
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
        ITestResult result = result("TC001 Verify Valid Login", new Object[0],
                new AssertionError("Expected inventory was not displayed"),
                new BrowserFixture(screenshotDriver));
        TestListener listener = new TestListener(isolatedReports);
        listener.onTestStart(result);
        listener.onTestFailure(result);
        ExtentTest report = report(result);
        Assert.assertEquals(report.getStatus(), Status.FAIL);
        Assert.assertEquals(report.getModel().getMedia().size(), 1);
        Assert.assertTrue(report.getModel().getMedia().get(0) instanceof ScreenCapture);
        ScreenCapture capture = (ScreenCapture) report.getModel().getMedia().get(0);
        Assert.assertTrue(capture.getBase64().endsWith(PNG));
    }

    /**
     * Retrieves the synthetic report associated by the real listener.
     *
     * @param result synthetic TestNG invocation
     * @return report entry tracked for cleanup
     */
    private ExtentTest report(ITestResult result) {
        Object value = result.getAttribute(TestListener.class.getName() + ".report");
        if (!(value instanceof ExtentTest report)) {
            throw new IllegalStateException("Listener did not associate a report.");
        }
        syntheticReports.add(report);
        return report;
    }

    /**
     * Supplies only the TestNG APIs consumed by the listener, without a browser launch.
     *
     * @param description test description
     * @param parameters invocation parameters
     * @param failure invocation failure
     * @param instance test instance, optionally exposing a screenshot-capable driver
     * @return synthetic result with real invocation attributes
     */
    private ITestResult result(
            String description, Object[] parameters, Throwable failure, Object instance) {
        ITestClass testClass = (ITestClass) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] {ITestClass.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getRealClass")) {
                        return LoginTest.class;
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
        ITestNGMethod testMethod = (ITestNGMethod) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] {ITestNGMethod.class}, (proxy, method, args) -> switch (method.getName()) {
                    case "getDescription" -> description;
                    case "getMethodName" -> "reportingValidation";
                    case "getGroups" -> new String[] {"smoke", "regression", "e2e"};
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        Map<String, Object> attributes = new HashMap<>();
        return (ITestResult) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] {ITestResult.class}, (proxy, method, args) -> switch (method.getName()) {
                    case "getTestClass" -> testClass;
                    case "getMethod" -> testMethod;
                    case "getParameters" -> parameters;
                    case "getStartMillis" -> START;
                    case "getEndMillis" -> END;
                    case "getThrowable" -> failure;
                    case "getInstance" -> instance;
                    case "getAttribute" -> attributes.get(args[0]);
                    case "setAttribute" -> attributes.put((String) args[0], args[1]);
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    private static final class BrowserFixture extends BaseTest {

        /**
         * Supplies a screenshot-only driver without launching a real browser.
         *
         * @param screenshotDriver driver fixture
         */
        private BrowserFixture(WebDriver screenshotDriver) {
            driver = screenshotDriver;
        }
    }
}
