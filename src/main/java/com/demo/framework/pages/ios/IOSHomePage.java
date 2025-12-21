package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.HomePage;
import org.openqa.selenium.By;

/**
 * iOS implementation of Home Page
 */
public class IOSHomePage extends BasePage implements HomePage {

    // iOS-specific locators
    private static final By HOME_SCREEN = By.xpath("//XCUIElementTypeStaticText[@name='WEBDRIVER']");
    private static final By LOGIN_BUTTON = By.xpath("//XCUIElementTypeButton[@name='Login']");
    private static final By SWIPE_BUTTON = By.xpath("//XCUIElementTypeButton[@name='Swipe']");
    private static final By WEBVIEW_BUTTON = By.xpath("//XCUIElementTypeButton[@name='Webview']");
    private static final By DRAG_BUTTON = By.xpath("//XCUIElementTypeButton[@name='Drag']");

    @Override
    public String getPageTitle() {
        return "Home Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if iOS Home Page is loaded");
        try {
            wait.untilVisible(HOME_SCREEN);
            LOG.info("iOS Home Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("iOS Home Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void clickLoginLink() {
        LOG.info("Clicking login link on iOS");
        actions.click(LOGIN_BUTTON);
    }

    @Override
    public void clickSwipeLink() {
        LOG.info("Clicking swipe link on iOS");
        actions.click(SWIPE_BUTTON);
    }

    @Override
    public void clickWebViewLink() {
        LOG.info("Clicking WebView link on iOS");
        actions.click(WEBVIEW_BUTTON);
    }

    @Override
    public void clickDragDropLink() {
        LOG.info("Clicking drag and drop link on iOS");
        actions.click(DRAG_BUTTON);
    }
}

