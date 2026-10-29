/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.utils;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MoneyUtilsTest {

    /**
     * Verifies supported prices and summary labels retain exact decimal values.
     *
     * @param label displayed label
     * @param expected expected amount
     */
    @Test(dataProvider = "validAmounts", groups = "framework")
    public void verifyValidAmount(String label, String expected) {
        Assert.assertEquals(MoneyUtils.amountFrom(label), new BigDecimal(expected));
    }

    /**
     * Supplies representative USD prices, summaries, and a zero amount.
     *
     * @return label and expected amount pairs
     */
    @DataProvider
    public Object[][] validAmounts() {
        return new Object[][] {
                {"$9.99", "9.99"},
                {"Item total: $39.98", "39.98"},
                {"Tax: $3.20", "3.20"},
                {"Total: $43.18", "43.18"},
                {"$0.00", "0.00"}
        };
    }

    /**
     * Verifies missing and malformed labels fail explicitly rather than becoming zero.
     *
     * @param label invalid displayed label
     */
    @Test(dataProvider = "invalidAmounts", groups = "framework",
            expectedExceptions = IllegalArgumentException.class)
    public void verifyInvalidAmount(String label) {
        MoneyUtils.amountFrom(label);
    }

    /**
     * Supplies missing currency, invalid precision, and malformed amount cases.
     *
     * @return invalid labels
     */
    @DataProvider
    public Object[][] invalidAmounts() {
        return new Object[][] {
                {null}, {""}, {"9.99"}, {"$"}, {"$9.9"},
                {"$9.999"}, {"$-1.00"}, {"$1.00 extra"}, {"$1.00$2.00"}
        };
    }
}
