package com.portfolio.hybrid.pages;

import com.portfolio.hybrid.config.FrameworkConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfig.waitSeconds()));
    }
    protected WebElement visible(By by) { return wait.until(ExpectedConditions.visibilityOfElementLocated(by)); }
    protected void click(By by) { wait.until(ExpectedConditions.elementToBeClickable(by)).click(); }
}
