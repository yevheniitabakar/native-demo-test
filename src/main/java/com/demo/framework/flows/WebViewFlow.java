package com.demo.framework.flows;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.HomePage;
import com.demo.framework.pages.interfaces.WebViewPage;
import com.demo.framework.utils.ContextManager;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flow class for WebView functionality.
 * Orchestrates WebView navigation and interactions.
 * Platform-agnostic - no platform checks allowed here.
 */
public class WebViewFlow {

    private static final Logger LOG = LoggerFactory.getLogger(WebViewFlow.class);

    private final HomePage homePage;
    private final WebViewPage webViewPage;
    private final ContextManager contextManager;

    public WebViewFlow() {
        this.homePage = PageFactory.homePage();
        this.webViewPage = PageFactory.webViewPage();
        this.contextManager = new ContextManager();
    }

    @Step("Navigate to WebView screen and switch to WebView context")
    public WebViewFlow navigateToWebView() {
        LOG.info("Navigating to WebView screen");
        homePage.clickWebViewLink();

        contextManager.waitForWebView();
        contextManager.switchToWebView();

        return this;
    }

    @Step("Wait for WebView to load completely")
    public WebViewFlow waitForWebViewToLoad() {
        LOG.info("Waiting for WebView to load completely");
        webViewPage.waitForWebViewToLoad();
        return this;
    }

    @Step("Tap on 'View on GitHub' button")
    public WebViewFlow tapViewOnGitHubButton() {
        LOG.info("Tapping on 'View on GitHub' button");
        // Switch back to native context to interact with native button
        contextManager.switchToNative();
        webViewPage.tapViewOnGitHubButton();
        return this;
    }

    public boolean isWebViewPageLoaded() {
        return webViewPage.isPageLoaded();
    }

    public boolean isWebViewDisplayed() {
        return webViewPage.isWebViewDisplayed();
    }

    public boolean isWebViewContentPresent() {
        return webViewPage.isWebViewContentPresent();
    }

    public boolean isGitHubPageOpened(String expectedUrl) {
        return webViewPage.isGitHubPageOpened(expectedUrl);
    }
}

