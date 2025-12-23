package com.demo.framework.pages;

import com.demo.framework.drivers.DriverManager;
import com.demo.framework.utils.ActionUtils;
import com.demo.framework.utils.ScreenshotUtils;
import com.demo.framework.utils.WaitUtils;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Base class for all Page Objects
 */
@Slf4j
public abstract class BasePage {

    protected final AppiumDriver driver;
    protected final WaitUtils wait;
    protected final ActionUtils actions;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WaitUtils(driver);
        this.actions = new ActionUtils(driver, wait);
    }

    public String getPageTitle() {
        return this.getClass().getSimpleName();
    }

    public boolean isPageLoaded() {
        return true;
    }

    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public void captureScreenshot() {
        try {
            ScreenshotUtils.takeScreenshot(getPageTitle());
            log.info("Screenshot captured for page: {}", getPageTitle());
        } catch (Exception e) {
            log.warn("Failed to capture screenshot", e);
        }
    }

    public void navigateBack() {
        log.info("Navigating back");
        driver.navigate().back();
    }

    public void navigateForward() {
        log.info("Navigating forward");
        driver.navigate().forward();
    }

    public void refresh() {
        log.info("Refreshing page");
        driver.navigate().refresh();
    }
}
