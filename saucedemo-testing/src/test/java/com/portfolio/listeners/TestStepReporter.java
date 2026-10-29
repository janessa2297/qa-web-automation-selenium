/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.listeners;

import java.util.function.Supplier;

import com.aventstack.extentreports.ExtentTest;
import com.portfolio.utils.ExtentLogger;

public final class TestStepReporter {

    private TestStepReporter() {
    }

    /**
     * Associates the active Extent scenario with the current test thread.
     *
     * @param test active Extent test
     */
    static void setCurrentTest(ExtentTest test) {
        ExtentLogger.setCurrentTest(test);
    }

    /**
     * Clears the active scenario after the TestNG invocation finishes.
     */
    static void clearCurrentTest() {
        ExtentLogger.clearCurrentTest();
    }

    /**
     * Runs and reports an action as a pass/fail step in the active scenario.
     *
     * @param description readable test step
     * @param action action to execute
     */
    public static void step(String description, Runnable action) {
        ExtentLogger.step(description, action);
    }

    /**
     * Runs and reports an action that returns a value as a pass/fail step.
     *
     * @param description readable test step
     * @param action action to execute
     * @param <T> returned value type
     * @return result of the action
     */
    public static <T> T step(String description, Supplier<T> action) {
        return ExtentLogger.step(description, action);
    }
}
