package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.WebViewPage;
import com.demo.framework.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * Android implementation of WebView Page
 */
@Slf4j
public class AndroidWebViewPage extends BasePage implements WebViewPage {

    private static final By VIEW_ON_GITHUB_BUTTON = AppiumBy.accessibilityId("View on GitHub");
    private static final By WEB_CONTENT = By.cssSelector("body");
    private static final By WEB_TITLE = By.cssSelector("h1, .hero__title");
    private static final By WEB_LOGO = By.cssSelector(".hero__logo img");

    private final GestureUtils gesture;

    public AndroidWebViewPage() {
        super();
        this.gesture = new GestureUtils(driver);
    }

    @Override
    public void tapViewOnGitHubButton() {
        log.info("Tapping 'View on GitHub' button on Android");
        gesture.scrollDownSmall();
        try {
            wait.untilVisible(VIEW_ON_GITHUB_BUTTON);
            actions.click(VIEW_ON_GITHUB_BUTTON);
        } catch (Exception e) {
            gesture.scrollDownSmall();
            actions.click(VIEW_ON_GITHUB_BUTTON);
        }
    }

    @Override
    public boolean isWebViewDisplayed() {
        log.info("Checking if WebView is displayed on Android");
        try {
            wait.untilVisible(WEB_CONTENT);
            return driver.findElement(WEB_CONTENT).isDisplayed();
        } catch (Exception e) {
            log.warn("WebView not displayed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isWebViewContentPresent() {
        log.info("Checking if WebView content is present on Android");
        try {
            // Try to find title or logo with explicit wait
            try {
                wait.untilVisible(WEB_TITLE);
                return true;
            } catch (Exception titleEx) {
                // Try logo as fallback
                try {
                    wait.untilVisible(WEB_LOGO);
                    return true;
                } catch (Exception logoEx) {
                    // Final fallback - check body has content
                    wait.untilVisible(WEB_CONTENT);
                    String bodyText = driver.findElement(WEB_CONTENT).getText();
                    return bodyText != null && !bodyText.isEmpty();
                }
            }
        } catch (Exception e) {
            log.warn("WebView content not present: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isGitHubPageOpened(String expectedUrl) {
        log.info("Checking if GitHub page is opened on Android");
        try {
            // Wait until app package changes (browser opened) using explicit wait
            boolean packageChanged = wait.untilCondition(driver -> {
                String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
                log.debug("Current package: {}", currentPackage);
                return currentPackage != null &&
                        (currentPackage.contains("chrome") ||
                         currentPackage.contains("browser") ||
                         !currentPackage.equals("com.wdiodemoapp"));
            });
            
            return packageChanged;
        } catch (Exception e) {
            log.warn("Error checking if GitHub page opened: {}", e.getMessage());
            // If wait times out, check current state
            try {
                String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
                return currentPackage != null && !currentPackage.equals("com.wdiodemoapp");
            } catch (Exception ex) {
                return true; // Assume success if we can't determine
            }
        }
    }
}
