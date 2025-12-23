package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.WebViewPage;
import com.demo.framework.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * iOS implementation of WebView Page
 */
@Slf4j
public class IOSWebViewPage extends BasePage implements WebViewPage {

    private static final By WEB_LOGO = By.cssSelector(".hero__logo img");
    private static final By WEB_TITLE = By.cssSelector("h1, .hero__title");
    private static final By WEB_GITHUB_LINK = AppiumBy.iOSClassChain(
            "**/XCUIElementTypeStaticText[`name == \"View on GitHub\"`]");
    private static final By WEB_CONTENT = By.cssSelector("body");

    private final GestureUtils gesture;

    public IOSWebViewPage() {
        super();
        this.gesture = new GestureUtils(driver);
    }

    @Override
    public void tapViewOnGitHubButton() {
        log.info("Tapping 'View on GitHub' button on iOS");
        gesture.scrollDownSmall();
        actions.click(WEB_GITHUB_LINK);
    }

    @Override
    public boolean isWebViewDisplayed() {
        log.info("Checking if WebView is displayed on iOS");
        try {
            Thread.sleep(2000); // Brief wait for WebView to load
            return driver.findElement(WEB_CONTENT).isDisplayed();
        } catch (Exception e) {
            log.warn("WebView not displayed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isWebViewContentPresent() {
        log.info("Checking if WebView content is present on iOS");
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
        log.info("Checking if GitHub page is opened on iOS");
        try {
            // GitHub page opens within the app - check for GitHub navigation element
            By githubNavigation = AppiumBy.iOSClassChain(
                    "**/XCUIElementTypeOther[`name == \"Organization, navigation\"`]"
                    + "/XCUIElementTypeOther/XCUIElementTypeOther[1]");
            
            wait.untilVisible(githubNavigation);
            return actions.isDisplayed(githubNavigation);
            
        } catch (Exception e) {
            log.warn("Error checking GitHub page: {}", e.getMessage());
            return false;
        }
    }
}
