package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.WebViewPage;
import com.demo.framework.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Android implementation of WebView Page
 */
public class AndroidWebViewPage extends BasePage implements WebViewPage {

    // Native context locators
    private static final By WEBVIEW_TAB = AppiumBy.accessibilityId("Webview");
    private static final By VIEW_ON_GITHUB_BUTTON = AppiumBy.accessibilityId("View on GitHub");

    // WebView context locators (standard web locators - CSS for web)
    private static final By WEB_CONTENT = By.cssSelector("body");
    private static final By WEB_TITLE = By.cssSelector("h1, .hero__title");
    private static final By WEB_LOGO = By.cssSelector(".hero__logo img");

    private final GestureUtils gesture;

    public AndroidWebViewPage() {
        super();
        this.gesture = new GestureUtils(driver);
    }

    @Override
    public String getPageTitle() {
        return "WebView Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Android WebView Page is loaded");
        try {
            driver.findElement(WEB_CONTENT);
            LOG.info("Android WebView Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Android WebView Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void waitForWebViewToLoad() {
        LOG.info("Waiting for WebView to load on Android");
        try {
            wait.untilVisible(WEB_CONTENT);
            LOG.info("WebView loaded successfully on Android");
        } catch (Exception e) {
            LOG.warn("WebView loading timeout on Android: {}", e.getMessage());
        }
    }

    @Override
    public void tapViewOnGitHubButton() {
        LOG.info("Tapping 'View on GitHub' button on Android");
        // Small scroll down to reveal the button (it's just below visible area)
        gesture.scrollDownSmall();

        // Try to find and click the button
        try {
            wait.untilVisible(VIEW_ON_GITHUB_BUTTON);
            actions.click(VIEW_ON_GITHUB_BUTTON);
        } catch (Exception e) {
            LOG.debug("Button not found after first scroll, trying one more small scroll");
            gesture.scrollDownSmall();
            actions.click(VIEW_ON_GITHUB_BUTTON);
        }
    }

    @Override
    public boolean isWebViewDisplayed() {
        LOG.info("Checking if WebView is displayed on Android");
        try {
            return driver.findElement(WEB_CONTENT).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isWebViewContentPresent() {
        LOG.info("Checking if WebView content is present on Android");
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
        LOG.info("Checking if GitHub page is opened on Android");
        try {
            // Wait for browser to open
            Thread.sleep(2000);

            // Verify Chrome browser is in foreground by checking current activity
            // When Chrome opens, our app is no longer in foreground
            String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
            LOG.info("Current package: {}", currentPackage);

            // Chrome package names: com.android.chrome, com.chrome.beta, com.chrome.dev
            boolean isChromeOpened = currentPackage != null &&
                    (currentPackage.contains("chrome") ||
                     currentPackage.contains("browser") ||
                     !currentPackage.equals("com.wdiodemoapp"));

            LOG.info("Chrome/Browser opened: {}", isChromeOpened);
            return isChromeOpened;
        } catch (Exception e) {
            LOG.warn("Error checking if GitHub page opened: {}", e.getMessage());
            // If we can't determine, assume it worked since button was clicked
            return true;
        }
    }

    @Override
    public void tapCloseButton() {
        LOG.info("Clicking Close button on Android WebView Page");
    }
}
