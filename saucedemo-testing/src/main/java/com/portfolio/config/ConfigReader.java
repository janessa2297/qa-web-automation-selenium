/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final String CONFIG_RESOURCE = "testdata/config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigReader() {
    }

    /**
     * Returns a required configuration value, allowing Maven JVM properties to override it.
     *
     * @param key configuration key
     * @return trimmed configuration value
     */
    public static String getProperty(String key) {
        String override = System.getProperty(key);
        String value = override == null || override.isBlank()
                ? PROPERTIES.getProperty(key)
                : override;
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Required configuration property is missing: " + key);
        }
        return value.trim();
    }

    /**
     * Returns a required integer configuration value.
     *
     * @param key configuration key
     * @return parsed integer value
     */
    public static int getInt(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Configuration property must be an integer: " + key, exception);
        }
    }

    /**
     * Returns a required boolean configuration value.
     *
     * @param key configuration key
     * @return parsed boolean value
     */
    public static boolean getBoolean(String key) {
        String value = getProperty(key);
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        throw new IllegalArgumentException(
                "Configuration property must be true or false: " + key);
    }

    public static int getPositiveInt(String key) {
        int value = getInt(key);
        if (value <= 0) {
            throw new IllegalArgumentException("Configuration property must be positive: " + key);
        }
        return value;
    }

    /**
     * Loads the configuration resource from the classpath.
     *
     * @return loaded configuration properties
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        ClassLoader classLoader = ConfigReader.class.getClassLoader();
        try (InputStream input = classLoader.getResourceAsStream(CONFIG_RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Unable to find " + CONFIG_RESOURCE + " on the classpath.");
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load " + CONFIG_RESOURCE + ".", exception);
        }
    }
}
