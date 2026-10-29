/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.utils;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/** Shares one directory across the reports and screenshots in an execution. */
public final class ReportPaths {
    private static final Path RUN_DIRECTORY = resolveRunDirectory();

    private ReportPaths() {
    }

    public static Path runDirectory() {
        return RUN_DIRECTORY;
    }

    private static Path resolveRunDirectory() {
        String configured = System.getProperty("testReportDirectory");
        if (configured != null && !configured.isBlank()) {
            return Path.of(configured.trim());
        }
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
        return Path.of("test-reports", "run-" + timestamp + "-" + UUID.randomUUID());
    }
}
