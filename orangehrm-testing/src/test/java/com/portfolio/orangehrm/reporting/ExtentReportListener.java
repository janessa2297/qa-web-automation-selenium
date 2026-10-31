package com.portfolio.orangehrm.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.portfolio.orangehrm.config.FrameworkConfig;
import com.portfolio.orangehrm.driver.DriverFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.IConfigurationListener;
import org.testng.Reporter;

/** Thread-safe ExtentReports listener. Registers via TestNG suite XML. */
public class ExtentReportListener implements ITestListener, ISuiteListener, IConfigurationListener {
    private final ThreadLocal<ExtentTest> current = new ThreadLocal<>();
    private ExtentReports extent;
    private final AtomicInteger sequence = new AtomicInteger();

    @Override
    public void onStart(ISuite suite) {
        ExtentSparkReporter html = new ExtentSparkReporter("target/reports/extent.html");
        html.config().setDocumentTitle("OrangeHRM | QA Execution Report");
        html.config().setReportName("OrangeHRM | QA Execution Report");
        html.config().setTheme(Theme.STANDARD);
        html.config().setTimelineEnabled(true);
        // Give responsive Chart.js canvases a definite parent height in current browsers.
        html.config().setCss(reportResource("qa-report.css")
            + "\n.dashboard-view .card-body > div:has(> canvas) { height: 240px; }");
        html.config().setJs(reportResource("qa-report.js"));
        extent = new ExtentReports();
        extent.attachReporter(html);
        extent.setSystemInfo("Browser", FrameworkConfig.browser());
        extent.setSystemInfo("Headless", String.valueOf(FrameworkConfig.headless()));
        extent.setSystemInfo("Environment", FrameworkConfig.baseUrl());
        extent.setSystemInfo("Java", System.getProperty("java.version"));
        extent.setSystemInfo("Operating system", System.getProperty("os.name"));
        extent.setSystemInfo("Suite", suite.getName());
    }

    private String reportResource(String name) {
        try (var stream = ExtentReportListener.class.getResourceAsStream("/reporting/" + name)) {
            if (stream == null) throw new IllegalStateException("Missing report resource: " + name);
            return new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException failure) {
            throw new IllegalStateException("Unable to load report resource: " + name, failure);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentTest test = extent.createTest(String.format("%02d | %s :: %s", sequence.incrementAndGet(),
            result.getTestClass().getRealClass().getSimpleName(), result.getMethod().getMethodName()),
            description == null ? "" : description)
            .assignCategory(Arrays.stream(result.getMethod().getGroups())
                .map(this::toPascalCase).toArray(String[]::new));
        current.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (current.get() != null) {
            logSteps(result);
            current.get().pass("Passed in " + (result.getEndMillis() - result.getStartMillis()) + " ms");
            attachScreenshot(current.get(), result);
        }
        current.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (current.get() == null) onTestStart(result);
        if (current.get() != null) current.get().skip("Skipped: " + result.getThrowable());
        current.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = current.get();
        if (test != null) {
            logSteps(result);
            test.fail(result.getThrowable());
            attachScreenshot(test, result);
        }
        current.remove();
    }

    private void logSteps(ITestResult result) {
        for (String step : Reporter.getOutput(result)) {
            current.get().info(escape(step));
        }
    }

    private String toPascalCase(String group) {
        StringBuilder name = new StringBuilder();
        for (String word : group.split("[^A-Za-z0-9]+")) {
            if (!word.isEmpty()) {
                name.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return name.toString();
    }

    private String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private void attachScreenshot(ExtentTest test, ITestResult result) {
        WebDriver driver = DriverFactory.getOrNull();
        if (driver == null) return;
        try {
            Path screenshot = ScreenshotHelper.save(driver, result.getMethod().getMethodName());
            String encoded = Base64.getEncoder().encodeToString(Files.readAllBytes(screenshot));
            test.info("Browser at scenario completion",
                MediaEntityBuilder.createScreenCaptureFromBase64String(encoded).build());
        } catch (IOException | RuntimeException screenshotFailure) {
            test.warning("Unable to save screenshot: " + escape(screenshotFailure.toString()));
        }
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        ExtentTest test = extent.createTest("Configuration failure :: " + result.getMethod().getMethodName())
            .assignCategory("Configuration");
        test.fail(result.getThrowable());
        attachScreenshot(test, result);
    }

    @Override
    public void onFinish(ISuite suite) {
        if (extent != null) extent.flush();
    }
}
