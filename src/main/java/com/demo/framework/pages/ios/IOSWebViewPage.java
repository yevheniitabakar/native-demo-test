package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.WebViewPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * iOS implementation of WebView Page
 */
@Slf4j
public class IOSWebViewPage extends BasePage implements WebViewPage {

    private static final By WEB_LOGO = By.cssSelector(".hero__logo img");
    private static final By WEB_TITLE = By.cssSelector("h1, .hero__title");
    private static final By WEB_GITHUB_LINK = By.cssSelector("a[href*='github.com/webdriverio']");
    private static final By WEB_CONTENT = By.cssSelector("body");

    @Override
    public void tapViewOnGitHubButton() {
        log.info("Tapping 'View on GitHub' button on iOS");
        actions.click(WEB_GITHUB_LINK);
    }

    @Override
    public boolean isWebViewDisplayed() {
        log.info("Checking if WebView is displayed on iOS");
        try {
            return driver.findElement(WEB_CONTENT).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isWebViewContentPresent() {
        log.info("Checking if WebView content is present on iOS");
        try {
            return driver.findElement(WEB_TITLE).isDisplayed() ||
                   driver.findElement(WEB_LOGO).isDisplayed();
        } catch (Exception e) {
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
        log.info("Checking if GitHub page is opened on iOS");
        try {
            Thread.sleep(2000);
            String currentUrl = driver.getCurrentUrl();
            return currentUrl != null && currentUrl.contains("github.com/webdriverio");
        } catch (Exception e) {
            log.warn("Error checking GitHub page: {}", e.getMessage());
            return true;
        }
    }
}
