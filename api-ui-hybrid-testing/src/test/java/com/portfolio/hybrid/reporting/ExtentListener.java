package com.portfolio.hybrid.reporting;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.portfolio.hybrid.base.BaseUiTest;
import com.portfolio.hybrid.config.FrameworkConfig;
import org.openqa.selenium.*;
import org.testng.*;
import java.nio.file.*;
import java.util.*;

public final class ExtentListener implements ITestListener, IExecutionListener {
    private final ExtentReports report = new ExtentReports();
    private final Map<ITestResult, ExtentTest> tests = new IdentityHashMap<>();
    @Override public void onExecutionStart() {
        try { Files.createDirectories(Path.of("target", "reports")); }
        catch (java.io.IOException e) { throw new IllegalStateException(e); }
        report.attachReporter(new ExtentSparkReporter("target/reports/extent.html"));
        report.setSystemInfo("UI environment", FrameworkConfig.baseUrl());
        report.setSystemInfo("API environment", FrameworkConfig.apiUrl());
        report.setSystemInfo("Java", System.getProperty("java.version"));
        report.setSystemInfo("API contract", FrameworkConfig.value("api.contract"));
        report.setSystemInfo("Headless", String.valueOf(FrameworkConfig.headless()));
    }
    private ExtentTest test(ITestResult result) {
        return tests.computeIfAbsent(result, r -> report.createTest(r.getMethod().getQualifiedName(),
            r.getMethod().getDescription()).assignCategory(r.getMethod().getGroups()));
    }
    @Override public void onTestStart(ITestResult result) { test(result); }
    @Override public void onTestSuccess(ITestResult result) { finish(result, Status.PASS); }
    @Override public void onTestSkipped(ITestResult result) { finish(result, Status.SKIP); }
    @Override public void onTestFailure(ITestResult result) {
        if (result.getInstance() instanceof BaseUiTest ui && ui.driver() != null) {
            try {
                Path path = Path.of("target", "screenshots", result.getMethod().getMethodName() + "-" + UUID.randomUUID() + ".png");
                Files.createDirectories(path.getParent());
                Files.write(path, ((TakesScreenshot) ui.driver()).getScreenshotAs(OutputType.BYTES));
                test(result).addScreenCaptureFromPath("../screenshots/" + path.getFileName());
            } catch (Exception e) { test(result).warning("Screenshot capture failed: " + e.getClass().getSimpleName()); }
        }
        finish(result, Status.FAIL);
    }
    private void finish(ITestResult result, Status status) {
        ExtentTest entry = test(result);
        entry.getModel().setStartTime(new Date(result.getStartMillis()));
        entry.getModel().setEndTime(new Date(result.getEndMillis()));
        entry.log(status, "Duration: " + (result.getEndMillis() - result.getStartMillis()) + " ms");
        if (result.getThrowable() != null) entry.log(status, result.getThrowable());
        if (result.getInstance() instanceof BaseUiTest ui && ui.driver() instanceof HasCapabilities caps)
            entry.info("Browser: " + caps.getCapabilities().getBrowserName() + " " + caps.getCapabilities().getBrowserVersion());
    }
    @Override public void onExecutionFinish() { report.flush(); }
}
