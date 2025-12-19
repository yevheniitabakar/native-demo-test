package com.demo.framework.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class WaitUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WaitUtils.class);
    private final WebDriverWait wait;

    public WaitUtils(AppiumDriver driver) {
        this(driver, Duration.ofSeconds(15));
    }

    public WaitUtils(AppiumDriver driver, Duration timeout) {
        this.wait = new WebDriverWait(driver, timeout);
    }

    /**
     * Wait until element is visible
     */
    public WebElement untilVisible(By locator) {
        LOG.debug("Waiting for element to be visible: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until element is clickable
     */
    public WebElement untilClickable(By locator) {
        LOG.debug("Waiting for element to be clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until element is present in DOM
     */
    public WebElement untilPresent(By locator) {
        LOG.debug("Waiting for element to be present: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait until element is invisible
     */
    public boolean untilInvisible(By locator) {
        LOG.debug("Waiting for element to be invisible: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait until text is present in element
     */
    public boolean untilTextPresent(By locator, String text) {
        LOG.debug("Waiting for text '{}' in element: {}", text, locator);
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Wait until element has specific count
     */
    public int untilElementCount(By locator, int count) {
        LOG.debug("Waiting for element count {} for locator: {}", count, locator);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, count - 1));
        return count;
    }
}
