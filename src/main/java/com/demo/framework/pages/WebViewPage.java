package com.demo.framework.pages;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for WebView Screen
 * Contains elements and actions for WebView interactions
 */
public class WebViewPage extends BasePage {

    private static final Logger LOG = LoggerFactory.getLogger(WebViewPage.class);

    // Locators
    private static final By WEBVIEW_TITLE = By.xpath("//android.widget.TextView[@text='WebView']");
    private static final By WEBVIEW_ELEMENT = By.id("webview_container");
    private static final By NAVIGATE_BUTTON = By.id("navigate_button");
    private static final By URL_INPUT = By.id("url_input");
    private static final By BACK_BUTTON = By.id("webview_back");
    private static final By FORWARD_BUTTON = By.id("webview_forward");
    private static final By REFRESH_BUTTON = By.id("webview_refresh");

    @Override
    public String getPageTitle() {
        return "WebView Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if WebView Page is loaded");
        try {
            wait.untilVisible(WEBVIEW_TITLE);
            LOG.info("WebView Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("WebView Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Enter URL in input field
     */
    public void enterUrl(String url) {
        LOG.info("Entering URL: {}", url);
        actions.sendText(URL_INPUT, url);
    }

    /**
     * Click navigate button to load URL
     */
    public void clickNavigateButton() {
        LOG.info("Clicking navigate button");
        actions.click(NAVIGATE_BUTTON);
    }

    /**
     * Navigate to URL
     */
    public void navigateToUrl(String url) {
        LOG.info("Navigating to URL: {}", url);
        enterUrl(url);
        clickNavigateButton();
        wait.untilVisible(WEBVIEW_ELEMENT);
    }

    /**
     * Click back button in WebView
     */
    public void clickBackButton() {
        LOG.info("Clicking WebView back button");
        actions.click(BACK_BUTTON);
    }

    /**
     * Click forward button in WebView
     */
    public void clickForwardButton() {
        LOG.info("Clicking WebView forward button");
        actions.click(FORWARD_BUTTON);
    }

    /**
     * Click refresh button in WebView
     */
    public void clickRefreshButton() {
        LOG.info("Clicking WebView refresh button");
        actions.click(REFRESH_BUTTON);
    }

    /**
     * Verify WebView is displayed
     */
    public boolean isWebViewDisplayed() {
        LOG.info("Checking if WebView is displayed");
        return actions.isDisplayed(WEBVIEW_ELEMENT);
    }

    /**
     * Get WebView page source
     */
    public String getWebViewPageSource() {
        LOG.info("Getting WebView page source");
        return getPageSource();
    }
}

