package com.demo.framework.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for element interactions and user actions
 */
public class ActionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ActionUtils.class);
    private final AppiumDriver driver;
    private final WaitUtils wait;

    public ActionUtils(AppiumDriver driver, WaitUtils wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Click on element
     */
    public void click(By locator) {
        LOG.debug("Clicking on element: {}", locator);
        WebElement element = wait.untilClickable(locator);
        element.click();
    }

    /**
     * Send text to element
     */
    public void sendText(By locator, String text) {
        LOG.debug("Sending text '{}' to element: {}", text, locator);
        WebElement element = wait.untilVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get element text
     */
    public String getText(By locator) {
        LOG.debug("Getting text from element: {}", locator);
        WebElement element = wait.untilVisible(locator);
        return element.getText();
    }

    /**
     * Hover over element
     */
    public void hover(By locator) {
        LOG.debug("Hovering over element: {}", locator);
        WebElement element = wait.untilVisible(locator);
        new Actions(driver).moveToElement(element).perform();
    }

    /**
     * Clear element text
     */
    public void clearText(By locator) {
        LOG.debug("Clearing text from element: {}", locator);
        WebElement element = wait.untilVisible(locator);
        element.clear();
    }

    /**
     * Check if element is displayed
     */
    public boolean isDisplayed(By locator) {
        LOG.debug("Checking if element is displayed: {}", locator);
        try {
            return wait.untilVisible(locator).isDisplayed();
        } catch (Exception e) {
            LOG.debug("Element not displayed: {}", locator);
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    public boolean isEnabled(By locator) {
        LOG.debug("Checking if element is enabled: {}", locator);
        WebElement element = wait.untilPresent(locator);
        return element.isEnabled();
    }

    /**
     * Get element attribute value
     */
    public String getAttribute(By locator, String attribute) {
        LOG.debug("Getting attribute '{}' from element: {}", attribute, locator);
        WebElement element = wait.untilVisible(locator);
        return element.getAttribute(attribute);
    }

    /**
     * Double-click on element
     */
    public void doubleClick(By locator) {
        LOG.debug("Double clicking on element: {}", locator);
        WebElement element = wait.untilClickable(locator);
        new Actions(driver).doubleClick(element).perform();
    }

    /**
     * Right click on element
     */
    public void rightClick(By locator) {
        LOG.debug("Right clicking on element: {}", locator);
        WebElement element = wait.untilClickable(locator);
        new Actions(driver).contextClick(element).perform();
    }

    /**
     * Scroll to element until visible and click
     */
    public void scrollToAndClick(By locator) {
        LOG.debug("Scrolling to element and clicking: {}", locator);
        WebElement element = wait.untilPresent(locator);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
//        new Actions(driver)
//            .scrollToElement(element)
//            .click(element)
//            .perform();
    }
}

