package com.portfolio.hybrid.config;

import java.io.IOException;
import java.util.Properties;

/** JVM property > environment > packaged default. Secrets are environment-only. */
public final class FrameworkConfig {
    private static final Properties DEFAULTS = new Properties();
    static {
        try (var in = FrameworkConfig.class.getResourceAsStream("/config.properties")) {
            if (in == null) throw new IllegalStateException("Missing config.properties");
            DEFAULTS.load(in);
        } catch (IOException e) { throw new ExceptionInInitializerError(e); }
    }
    private FrameworkConfig() { }
    public static String value(String key) {
        return System.getProperty(key, System.getenv().getOrDefault(key.toUpperCase().replace('.', '_'), DEFAULTS.getProperty(key, "")));
    }
    public static String baseUrl() { return value("base.url").replaceAll("/+$", ""); }
    public static String apiUrl() { return value("api.base.url").replaceAll("/+$", ""); }
    public static boolean headless() { return Boolean.parseBoolean(value("headless")); }
    public static boolean mutations() { return Boolean.parseBoolean(value("allow.mutations")); }
    public static int waitSeconds() { return Integer.parseInt(value("wait.seconds")); }
    public static String username() { return System.getenv().getOrDefault("BOOKER_USERNAME", ""); }
    public static String password() { return System.getenv().getOrDefault("BOOKER_PASSWORD", ""); }
}
