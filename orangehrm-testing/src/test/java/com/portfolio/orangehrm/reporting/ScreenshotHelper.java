package com.portfolio.orangehrm.reporting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotHelper {
    private ScreenshotHelper() { }

    public static Path save(WebDriver driver, String testName) throws IOException {
        Path directory = Path.of("target", "screenshots");
        Files.createDirectories(directory);
        String safeName = testName.replaceAll("[^A-Za-z0-9_-]", "_");
        Path destination = directory.resolve(safeName + "-" + UUID.randomUUID() + ".png");
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        return destination;
    }
}
