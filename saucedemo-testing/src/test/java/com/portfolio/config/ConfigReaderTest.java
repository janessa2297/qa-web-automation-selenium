/*************************************************************************************
 * Note: 
 * Sauce Demo Automation Framework is a project created for educational purposes.
 * It is not affiliated with or endorsed by Sauce Labs, Inc.
 * The framework is intended to demonstrate automation testing concepts and practices.
 * Created By: Janessa 
 ****************************************************************************************/

package com.portfolio.config;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigReaderTest {

    @Test(groups = "framework")
    public void loadsConfigurationFromTestDataResource() {
        Assert.assertFalse(ConfigReader.getProperty("baseUrl").isBlank());
        Assert.assertTrue(ConfigReader.getPositiveInt("explicitWaitSeconds") > 0);
    }

    @Test(groups = "framework")
    public void validatesSystemPropertyOverrides() {
        String key = "framework.validation.test";
        String previous = System.getProperty(key);
        try {
            System.setProperty(key, " 42 ");
            Assert.assertEquals(ConfigReader.getPositiveInt(key), 42);
            System.setProperty(key, "0");
            Assert.expectThrows(IllegalArgumentException.class,
                    () -> ConfigReader.getPositiveInt(key));
            System.setProperty(key, "invalid");
            Assert.expectThrows(IllegalArgumentException.class, () -> ConfigReader.getInt(key));
            Assert.expectThrows(IllegalArgumentException.class, () -> ConfigReader.getBoolean(key));
            System.setProperty(key, "TRUE");
            Assert.assertTrue(ConfigReader.getBoolean(key));
            System.setProperty(key, "false");
            Assert.assertFalse(ConfigReader.getBoolean(key));
        } finally {
            if (previous == null) {
                System.clearProperty(key);
            } else {
                System.setProperty(key, previous);
            }
        }
    }
}
