package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.HomePage;
import io.appium.java_client.AppiumBy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * iOS implementation of Home Page
 */
@Slf4j
public class IOSHomePage extends BasePage implements HomePage {

    private static final By HOME_SCREEN = AppiumBy.accessibilityId("Home-screen");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("Login");
    private static final By SWIPE_BUTTON = AppiumBy.accessibilityId("Swipe");
    private static final By WEBVIEW_BUTTON = AppiumBy.accessibilityId("Webview");
    private static final By DRAG_BUTTON = AppiumBy.accessibilityId("Drag");

    @Override
    public String getPageTitle() {
        return "Home Page";
    }

    @Override
    public boolean isPageLoaded() {
        log.info("Checking if iOS Home Page is loaded");
        try {
            wait.untilVisible(HOME_SCREEN);
            log.info("iOS Home Page loaded successfully");
            return true;
        } catch (Exception e) {
            log.warn("iOS Home Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void clickLoginLink() {
        log.info("Clicking login link on iOS");
        actions.click(LOGIN_BUTTON);
    }

    @Override
    public void clickSwipeLink() {
        log.info("Clicking swipe link on iOS");
        actions.click(SWIPE_BUTTON);
    }

    @Override
    public void clickWebViewLink() {
        log.info("Clicking WebView link on iOS");
        actions.click(WEBVIEW_BUTTON);
    }

    @Override
    public void clickDragDropLink() {
        log.info("Clicking drag and drop link on iOS");
        actions.click(DRAG_BUTTON);
    }
}
