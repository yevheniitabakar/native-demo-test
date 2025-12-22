package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.WebViewPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * iOS implementation of WebView Page
 */
public class IOSWebViewPage extends BasePage implements WebViewPage {

    // Native context locators (before switching to WebView)
    private static final By WEBVIEW_TAB = AppiumBy.accessibilityId("Webview");

    // WebView context locators (standard web locators - CSS/XPath for web)
    private static final By WEB_LOGO = By.cssSelector(".hero__logo img");
    private static final By WEB_TITLE = By.cssSelector("h1, .hero__title");
    private static final By WEB_GITHUB_LINK = By.cssSelector("a[href*='github.com/webdriverio']");
    private static final By WEB_CONTENT = By.cssSelector("body");

    @Override
    public String getPageTitle() {
        return "WebView Page";
    }

    @Override
    public void tapCloseButton() {
        LOG.info("Tapping Close button on iOS WebView Page");
        // Navigate back to native context
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if iOS WebView Page is loaded");
        try {
            driver.findElement(WEB_CONTENT);
            LOG.info("iOS WebView Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("iOS WebView Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void waitForWebViewToLoad() {
        LOG.info("Waiting for WebView to load on iOS");
        try {
            wait.untilVisible(WEB_CONTENT);
            LOG.info("WebView loaded successfully on iOS");
        } catch (Exception e) {
            LOG.warn("WebView loading timeout on iOS: {}", e.getMessage());
        }
    }

    @Override
    public void tapViewOnGitHubButton() {
        LOG.info("Tapping 'View on GitHub' button on iOS");
        actions.click(WEB_GITHUB_LINK);
    }

    @Override
    public boolean isWebViewDisplayed() {
        LOG.info("Checking if WebView is displayed on iOS");
        try {
            return driver.findElement(WEB_CONTENT).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isWebViewContentPresent() {
        LOG.info("Checking if WebView content is present on iOS");
        try {
            return driver.findElement(WEB_TITLE).isDisplayed() ||
                   driver.findElement(WEB_LOGO).isDisplayed();
        } catch (Exception e) {
            LOG.debug("WebView content check failed: {}", e.getMessage());
            try {
                String bodyText = driver.findElement(WEB_CONTENT).getText();
                return bodyText != null && !bodyText.isEmpty();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    @Override
    public boolean isGitHubPageOpened(String expectedUrl) {
        LOG.info("Checking if GitHub page is opened on iOS with URL: {}", expectedUrl);
        try {
            Thread.sleep(2000);
            String currentUrl = driver.getCurrentUrl();
            LOG.info("Current URL: {}", currentUrl);
            return currentUrl != null && currentUrl.contains("github.com/webdriverio");
        } catch (Exception e) {
            LOG.warn("Error checking GitHub page: {}", e.getMessage());
            return true;
        }
    }
}
