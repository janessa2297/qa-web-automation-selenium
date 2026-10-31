package com.portfolio.orangehrm.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Configuration precedence: JVM -D property, environment variable, classpath properties, fallback. */
public final class FrameworkConfig {
    private static final Properties DEFAULTS = loadDefaults();

    private FrameworkConfig() { }

    private static Properties loadDefaults() {
        Properties properties = new Properties();
        try (InputStream input = FrameworkConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new IllegalStateException("Missing src/main/resources/config.properties");
            properties.load(input);
            return properties;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load config.properties", ex);
        }
    }

    private static String value(String systemKey, String envKey, String fileKey, String fallback) {
        String fromSystem = System.getProperty(systemKey);
        if (fromSystem != null && !fromSystem.isBlank()) return fromSystem.strip();
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) return fromEnv.strip();
        String fromFile = fileKey == null ? null : DEFAULTS.getProperty(fileKey);
        return fromFile == null || fromFile.isBlank() ? fallback : fromFile.strip();
    }

    public static String baseUrl() {
        return value("base.url", "BASE_URL", "base.url", "https://opensource-demo.orangehrmlive.com")
            .replaceAll("/+$", "");
    }

    public static String browser() {
        return value("browser", "BROWSER", "browser", "chrome").toLowerCase(java.util.Locale.ROOT);
    }

    public static boolean headless() {
        return Boolean.parseBoolean(value("headless", "HEADLESS", "headless", "false"));
    }

    public static int waitSeconds() {
        int seconds = Integer.parseInt(value("wait.seconds", "WAIT_SECONDS", "wait.seconds", "20"));
        if (seconds < 1 || seconds > 120) throw new IllegalArgumentException("wait.seconds must be 1..120");
        return seconds;
    }

    // These credentials are explicitly published on the OrangeHRM demo login screen.
    // Override using environment variables for your own environments; do not commit private credentials.
    public static String username() {
        return value("orangehrm.username", "ORANGEHRM_USERNAME", null, "Admin");
    }

    public static String password() {
        return value("orangehrm.password", "ORANGEHRM_PASSWORD", null, "admin123");
    }

    public static boolean writeTestsEnabled() {
        return Boolean.parseBoolean(value("allow.write.tests", "ALLOW_WRITE_TESTS", null, "false"));
    }
}
