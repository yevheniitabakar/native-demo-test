package com.demo.framework.pages;

import com.demo.framework.drivers.DriverManager;
import com.demo.framework.utils.ActionUtils;
import com.demo.framework.utils.ScreenshotUtils;
import com.demo.framework.utils.WaitUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all Page Objects
 * Implements SOLID principles: dependency injection, single responsibility
 */
public abstract class BasePage {

    protected static final Logger LOG = LoggerFactory.getLogger(BasePage.class);

    protected final AppiumDriver driver;
    protected final WaitUtils wait;
    protected final ActionUtils actions;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WaitUtils(driver);
        this.actions = new ActionUtils(driver, wait);
    }

    /**
     * Get page title - can be overridden in subclasses
     */
    public abstract String getPageTitle();

    /**
     * Verify if user is on the correct page
     */
    public abstract boolean isPageLoaded();

    /**
     * Find element by locator
     */
    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Get page source
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Take screenshot with page title
     */
    public void captureScreenshot() {
        try {
            ScreenshotUtils.takeScreenshot(getPageTitle());
            LOG.info("Screenshot captured for page: {}", getPageTitle());
        } catch (Exception e) {
            LOG.warn("Failed to capture screenshot", e);
        }
    }

    /**
     * Navigate back
     */
    public void navigateBack() {
        LOG.info("Navigating back");
        driver.navigate().back();
    }

    /**
     * Navigate forward
     */
    public void navigateForward() {
        LOG.info("Navigating forward");
        driver.navigate().forward();
    }

    /**
     * Refresh page
     */
    public void refresh() {
        LOG.info("Refreshing page");
        driver.navigate().refresh();
    }
}
