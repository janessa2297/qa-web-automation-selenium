/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/


package com.portfolio.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MoneyUtils {

    private static final Pattern USD_LABEL =
            Pattern.compile("^[^$\\r\\n]*\\$(\\d+\\.\\d{2})$");

    /**
     * Prevents instantiation of the currency utility.
     */
    private MoneyUtils() {
    }

    /**
     * Parses a non-negative USD price or summary label without floating-point loss.
     *
     * @param label price such as "$9.99" or summary such as "Tax: $2.40"
     * @return exact two-decimal amount
     * @throws IllegalArgumentException when the label is missing or malformed
     */
    public static BigDecimal amountFrom(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Currency label must not be null.");
        }
        Matcher match = USD_LABEL.matcher(label);
        if (!match.matches()) {
            throw new IllegalArgumentException("Invalid USD currency label: " + label);
        }
        return new BigDecimal(match.group(1));
    }
}
