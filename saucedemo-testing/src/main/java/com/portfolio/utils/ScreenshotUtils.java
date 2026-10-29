package com.portfolio.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public final class ScreenshotUtils {

    private static final Path SCREENSHOT_DIRECTORY = ReportPaths.runDirectory().resolve("screenshots");
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    /**
     * Captures a screenshot using a sanitized test name and a unique timestamp.
     *
     * @param driver active browser to capture
     * @param testName test identifier used in the screenshot filename
     * @return absolute screenshot path suitable for report attachment
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            throw new IllegalArgumentException("Cannot capture a screenshot without a WebDriver.");
        }
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            throw new IllegalStateException("The active WebDriver does not support screenshots.");
        }

        String safeTestName = testName == null || testName.isBlank()
                ? "unnamed_test"
                : testName.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path destination = SCREENSHOT_DIRECTORY.resolve(
                safeTestName + "_" + LocalDateTime.now().format(TIMESTAMP_FORMAT)
                        + "_" + UUID.randomUUID() + ".png");

        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);
            Files.write(destination, screenshotDriver.getScreenshotAs(OutputType.BYTES));
            return destination.toAbsolutePath().toString();
        } catch (IOException | WebDriverException exception) {
            throw new IllegalStateException(
                    "Unable to capture screenshot for test '" + safeTestName + "'.", exception);
        }
    }
}
