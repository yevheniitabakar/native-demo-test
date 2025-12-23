package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.HomePage;
import io.appium.java_client.AppiumBy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * Android implementation of Home Page
 */
@Slf4j
public class AndroidHomePage extends BasePage implements HomePage {

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
        log.info("Checking if Android Home Page is loaded");
        try {
            wait.untilVisible(HOME_SCREEN);
            log.info("Android Home Page loaded successfully");
            return true;
        } catch (Exception e) {
            log.warn("Android Home Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void clickLoginLink() {
        log.info("Clicking login link on Android");
        actions.click(LOGIN_BUTTON);
    }

    @Override
    public void clickSwipeLink() {
        log.info("Clicking swipe link on Android");
        actions.click(SWIPE_BUTTON);
    }

    @Override
    public void clickWebViewLink() {
        log.info("Clicking WebView link on Android");
        actions.click(WEBVIEW_BUTTON);
    }

    @Override
    public void clickDragDropLink() {
        log.info("Clicking drag and drop link on Android");
        actions.click(DRAG_BUTTON);
    }
}

