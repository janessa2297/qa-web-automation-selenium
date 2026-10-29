/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.portfolio.base.BaseTest;
import com.portfolio.utils.ExtentLogger;
import com.portfolio.utils.ExtentManager;
import com.portfolio.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.IConfigurationListener;
import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener, IConfigurationListener, IExecutionListener {

    private final ExtentReports extent;
    private static final String REPORT_ATTRIBUTE = TestListener.class.getName() + ".report";
    private static final Pattern CASE_TITLE = Pattern.compile("(TC\\d+\\b.*|E2E-\\d+\\b.*)");

    public TestListener() {
        this(ExtentManager.getInstance());
    }

    /** Allows listener tests to keep synthetic failures out of the execution report. */
    TestListener(ExtentReports extent) {
        this.extent = java.util.Objects.requireNonNull(extent);
    }

    /**
     * Creates a scenario before business actions are logged.
     *
     * @param result current invocation
     */
    @Override
    public void onTestStart(ITestResult result) {
        ExtentLogger.setCurrentTest(reportFor(result));
    }

    /**
     * Completes a passing scenario without adding generic success noise.
     *
     * @param result completed invocation
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = reportFor(result);
        try {
            if (test.getModel().getLogs().isEmpty()) {
                test.pass(reportName(result) + ": validations completed successfully");
            }
            finish(test, result);
        } finally {
            ExtentLogger.clearCurrentTest();
        }
    }

    /**
     * Records the original failure and embeds portable screenshot evidence.
     *
     * @param result failed invocation
     */
    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = reportFor(result);
        try {
            if (test.getStatus() != Status.FAIL) {
                recordFailure(test, result);
            }
            attachFailureScreenshot(test, result);
            finish(test, result);
        } finally {
            ExtentLogger.clearCurrentTest();
        }
    }

    /**
     * Reports skipped cases even when setup prevented onTestStart from running.
     *
     * @param result skipped invocation
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = reportFor(result);
        try {
            if (result.getThrowable() != null) {
                test.skip(result.getThrowable());
            } else {
                test.skip("Scenario was not executed; inspect dependencies or configuration.");
            }
            finish(test, result);
        } finally {
            ExtentLogger.clearCurrentTest();
        }
    }

    /**
     * Makes browser setup and teardown errors visible as execution failures.
     *
     * @param result failed configuration invocation
     */
    @Override
    public void onConfigurationFailure(ITestResult result) {
        ExtentTest test = extent.createTest(featureName(result) + " - Configuration - "
                + result.getMethod().getMethodName());
        test.assignCategory("Configuration", featureName(result));
        recordFailure(test, result);
        attachFailureScreenshot(test, result);
        finish(test, result);
    }

    /**
     * Writes the final report after every test context and teardown has completed.
     */
    @Override
    public void onExecutionFinish() {
        extent.flush();
    }

    /**
     * Retrieves or creates exactly one report entry per TestNG result.
     *
     * @param result current invocation
     * @return associated scenario
     */
    private ExtentTest reportFor(ITestResult result) {
        Object existing = result.getAttribute(REPORT_ATTRIBUTE);
        if (existing instanceof ExtentTest test) {
            return test;
        }
        ExtentTest test = extent.createTest(reportName(result));
        test.getModel().setStartTime(new Date(result.getStartMillis()));
        test.assignCategory(featureName(result));
        test.assignCategory(Arrays.stream(result.getMethod().getGroups())
                .map(this::categoryName).toArray(String[]::new));
        result.setAttribute(REPORT_ATTRIBUTE, test);
        return test;
    }

    /**
     * Preserves TestNG timestamps and exposes the execution duration explicitly.
     *
     * @param test scenario to complete
     * @param result completed invocation
     */
    private void finish(ExtentTest test, ITestResult result) {
        long start = result.getStartMillis();
        long end = Math.max(start, result.getEndMillis());
        test.info("Start Time: " + new Date(start).toInstant()
                + " | End Time: " + new Date(end).toInstant()
                + " | Duration: " + (end - start) + " ms");
        test.getModel().setStartTime(new Date(start));
        test.getModel().setEndTime(new Date(end));
    }

    /**
     * Records failures without converting them to successful report entries.
     *
     * @param test report entry
     * @param result failed invocation
     */
    private void recordFailure(ExtentTest test, ITestResult result) {
        if (result.getThrowable() != null) {
            test.fail(result.getThrowable());
        } else {
            test.fail("Execution failed without a reported exception.");
        }
    }

    /**
     * Embeds evidence so archived reports survive Maven clean and relocation.
     *
     * @param test report entry
     * @param result failed invocation
     */
    private void attachFailureScreenshot(ExtentTest test, ITestResult result) {
        if (!(result.getInstance() instanceof BaseTest baseTest)) {
            return;
        }
        WebDriver driver = baseTest.getDriver();
        if (driver == null) {
            test.warning("Screenshot unavailable: no active browser session.");
            return;
        }
        try {
            String path = ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());
            String image = Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of(path)));
            test.addScreenCaptureFromBase64String(image, "Failure evidence");
        } catch (IOException | IllegalArgumentException | IllegalStateException exception) {
            test.warning("Failure screenshot could not be attached: " + exception.getMessage());
        }
    }

    /**
     * Resolves scenario placeholders and prefixes case IDs with a business feature.
     *
     * @param result current invocation; credential parameters are never rendered
     * @return feature and test case title
     */
    private String reportName(ITestResult result) {
        String description = result.getMethod().getDescription();
        if (description == null || description.isBlank()) {
            String words = result.getMethod().getMethodName()
                    .replaceAll("([a-z0-9])([A-Z])", "$1 $2");
            description = Character.toUpperCase(words.charAt(0)) + words.substring(1);
        }
        Object[] parameters = result.getParameters();
        if (description.contains("{scenario}") && parameters.length > 0
                && parameters[0] instanceof String scenario) {
            description = description.replace("{scenario}", scenario);
        }
        Matcher match = CASE_TITLE.matcher(description);
        return featureName(result) + " - " + (match.find() ? match.group() : description);
    }

    /**
     * Derives a meaningful feature from the test class rather than action categories.
     *
     * @param result current invocation
     * @return business feature
     */
    private String featureName(ITestResult result) {
        return switch (result.getTestClass().getRealClass().getSimpleName()) {
            case "InventoryTest" -> "Products";
            case "E2EPurchaseTest" -> "Purchase";
            case "MoneyUtilsTest" -> "Framework";
            default -> result.getTestClass().getRealClass().getSimpleName().replaceFirst("Test$", "");
        };
    }

    /**
     * Formats report tags without changing TestNG's existing group selectors.
     *
     * @param group TestNG group name
     * @return readable report category
     */
    private String categoryName(String group) {
        if (group.equalsIgnoreCase("e2e")) {
            return "E2E";
        }
        if (group.equalsIgnoreCase("ui")) {
            return "UI";
        }
        return group.substring(0, 1).toUpperCase(Locale.ROOT) + group.substring(1);
    }
}
