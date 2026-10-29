/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.utils;

import java.util.function.Supplier;

import com.aventstack.extentreports.ExtentTest;

public final class ExtentLogger {

    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    /**
     * Prevents instantiation of the reporting utility.
     */
    private ExtentLogger() {
    }

    /**
     * Associates the listener-created report with the current test invocation.
     *
     * @param test active scenario; never share between test invocations
     */
    public static void setCurrentTest(ExtentTest test) {
        if (test == null) {
            throw new IllegalArgumentException("An active Extent scenario is required.");
        }
        CURRENT_TEST.set(test);
    }

    /**
     * Releases the thread's report reference after a test completes.
     */
    public static void clearCurrentTest() {
        CURRENT_TEST.remove();
    }

    /**
     * Logs a business action without changing its result; do not log credentials.
     *
     * @param message business action or execution context
     */
    public static void info(String message) {
        currentTest().info(message);
    }

    /**
     * Logs the successful outcome of an action or assertion.
     *
     * @param message verified business outcome
     */
    public static void pass(String message) {
        currentTest().pass(message);
    }

    /**
     * Logs a failed business outcome and fails TestNG as well as the report.
     *
     * @param message failure explanation without private data
     * @throws AssertionError always, so execution cannot silently continue as passed
     */
    public static void fail(String message) {
        currentTest().fail(message);
        throw new AssertionError(message);
    }

    /**
     * Executes an action with an INFO entry and a business-specific outcome.
     *
     * @param description action being executed
     * @param action action or assertion
     */
    public static void step(String description, Runnable action) {
        step(description, description + ": completed successfully", action);
    }

    /**
     * Executes a value-returning action with INFO and outcome entries.
     *
     * @param description action being executed
     * @param action action or assertion
     * @param <T> action result type
     * @return action result
     */
    public static <T> T step(String description, Supplier<T> action) {
        return step(description, description + ": completed successfully", action);
    }

    /**
     * Reports an action and a separately worded successful business outcome.
     *
     * @param description action being executed
     * @param successMessage outcome recorded only if the action succeeds
     * @param action action or assertion
     */
    public static void step(String description, String successMessage, Runnable action) {
        step(description, successMessage, () -> {
            action.run();
            return null;
        });
    }

    /**
     * Reports a value-returning action without suppressing exceptions or assertions.
     *
     * @param description action being executed
     * @param successMessage outcome recorded only if the action succeeds
     * @param action action or assertion
     * @param <T> action result type
     * @return action result
     */
    public static <T> T step(String description, String successMessage, Supplier<T> action) {
        ExtentTest test = currentTest();
        info(description);
        try {
            T value = action.get();
            pass(successMessage);
            return value;
        } catch (RuntimeException | AssertionError failure) {
            test.fail(description + ": failed");
            test.fail(failure);
            throw failure;
        }
    }

    /**
     * Resolves the active scenario and rejects logging outside a listener-managed test.
     *
     * @return report for the current invocation
     */
    private static ExtentTest currentTest() {
        ExtentTest test = CURRENT_TEST.get();
        if (test == null) {
            throw new IllegalStateException(
                    "No active Extent scenario. Run tests through the configured TestNG suite.");
        }
        return test;
    }
}
