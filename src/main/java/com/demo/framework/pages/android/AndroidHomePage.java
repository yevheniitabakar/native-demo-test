package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.HomePage;
import org.openqa.selenium.By;

/**
 * Android implementation of Home Page
 */
public class AndroidHomePage extends BasePage implements HomePage {

    // Android-specific locators
    private static final By HOME_SCREEN = By.xpath("//android.widget.TextView[@text='WEBDRIVER']");
    private static final By LOGIN_BUTTON = By.xpath("//android.widget.TextView[@text='Login']");
    private static final By SWIPE_BUTTON = By.xpath("//android.widget.TextView[@text='Swipe']");
    private static final By WEBVIEW_BUTTON = By.xpath("//android.widget.TextView[@text='Webview']");
    private static final By DRAG_BUTTON = By.xpath("//android.widget.TextView[@text='Drag']");

    @Override
    public String getPageTitle() {
        return "Home Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Android Home Page is loaded");
        try {
            wait.untilVisible(HOME_SCREEN);
            LOG.info("Android Home Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Android Home Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void clickLoginLink() {
        LOG.info("Clicking login link on Android");
        actions.click(LOGIN_BUTTON);
    }

    @Override
    public void clickSwipeLink() {
        LOG.info("Clicking swipe link on Android");
        actions.click(SWIPE_BUTTON);
    }

    @Override
    public void clickWebViewLink() {
        LOG.info("Clicking WebView link on Android");
        actions.click(WEBVIEW_BUTTON);
    }

    @Override
    public void clickDragDropLink() {
        LOG.info("Clicking drag and drop link on Android");
        actions.click(DRAG_BUTTON);
    }
}

