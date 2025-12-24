package com.demo.framework.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Function;

public class WaitUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WaitUtils.class);
    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(AppiumDriver driver) {
        this(driver, Duration.ofSeconds(15));
    }

    public WaitUtils(AppiumDriver driver, Duration timeout) {
        this.driver = driver;
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

    /**
     * Wait for a custom condition using a supplier
     * Useful for waiting until app state changes (e.g., after drag-drop, navigation)
     */
    public <T> T untilCondition(Function<org.openqa.selenium.WebDriver, T> condition) {
        LOG.debug("Waiting for custom condition");
        return wait.until(condition);
    }

    /**
     * Wait for a short duration with custom timeout
     * Use for brief UI stabilization waits instead of Thread.sleep
     */
    public void forDuration(Duration duration) {
        LOG.debug("Waiting for duration: {}", duration);
        try {
            new WebDriverWait(driver, duration)
                    .until(d -> {
                        try {
                            Thread.sleep(duration.toMillis());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        return true;
                    });
        } catch (Exception e) {
            // Duration elapsed, continue
        }
    }

    /**
     * Wait until element attribute has expected value
     */
    public boolean untilAttributeContains(By locator, String attribute, String value) {
        LOG.debug("Waiting for attribute '{}' to contain '{}' in element: {}", attribute, value, locator);
        return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    /**
     * Wait until element's position stabilizes (for drag-drop verification)
     * Returns true when element position doesn't change between checks
     */
    public boolean untilPositionStable(By locator) {
        LOG.debug("Waiting for element position to stabilize: {}", locator);
        return wait.until(d -> {
            try {
                WebElement element = d.findElement(locator);
                int x1 = element.getLocation().getX();
                int y1 = element.getLocation().getY();
                Thread.sleep(100); // Brief pause to detect movement
                int x2 = element.getLocation().getX();
                int y2 = element.getLocation().getY();
                return x1 == x2 && y1 == y2;
            } catch (Exception e) {
                return false;
            }
        });
    }
}
