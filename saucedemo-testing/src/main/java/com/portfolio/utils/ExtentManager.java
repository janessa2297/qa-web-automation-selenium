/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.portfolio.config.ConfigReader;

public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {
    }

    /**
     * Returns the singleton ExtentReports instance, initializing it only once.
     *
     * @return configured report manager
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String configuredReportPath = System.getProperty("reportPath");
            Path reportPath = configuredReportPath == null || configuredReportPath.isBlank()
                    ? ReportPaths.runDirectory().resolve("extent-report.html")
                    : Path.of(configuredReportPath.trim());
            try {
                Path reportDirectory = reportPath.toAbsolutePath().getParent();
                if (reportDirectory != null) {
                    Files.createDirectories(reportDirectory);
                }
            } catch (IOException exception) {
                throw new IllegalStateException(
                        "Unable to create the Extent Reports output directory.", exception);
            }

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath.toString());
            spark.config().setDocumentTitle("SauceDemo | QA Execution Report");
            spark.config().setReportName("SauceDemo - QA Execution Results");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setEncoding("UTF-8");
            spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            spark.config().setTimelineEnabled(true);
            spark.config().setCss(reportAsset("report-theme.css"));
            spark.config().setJs(reportAsset("report-theme.js"));

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "Selenium WebDriver with TestNG");
            extent.setSystemInfo("Browser", ConfigReader.getProperty("browser"));
            extent.setSystemInfo("Base URL", ConfigReader.getProperty("baseUrl"));
            extent.setSystemInfo("Headless", ConfigReader.getProperty("headless"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Run", ReportPaths.runDirectory().getFileName().toString());
            extent.setSystemInfo("Environment",
                    System.getenv().getOrDefault("CI", "local").equalsIgnoreCase("true")
                            ? "GitHub Actions" : "local");
        }
        return extent;
    }

    private static String reportAsset(String name) {
        try (InputStream input = ExtentManager.class.getResourceAsStream("/report/" + name)) {
            if (input == null) {
                throw new IllegalStateException("Missing report asset: " + name);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load report asset: " + name, exception);
        }
    }
}
