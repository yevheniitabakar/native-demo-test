package com.demo.framework.flows;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.HomePage;
import com.demo.framework.pages.interfaces.WebViewPage;
import com.demo.framework.utils.ContextManager;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Flow class for WebView functionality.
 * Orchestrates WebView navigation and interactions.
 */
@Slf4j
public class WebViewFlow {

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
        log.info("Navigating to WebView screen");
        homePage.clickWebViewLink();
        contextManager.waitForWebView();
        contextManager.switchToWebView();
        return this;
    }

    @Step("Tap on 'View on GitHub' button")
    public WebViewFlow tapViewOnGitHubButton() {
        log.info("Tapping on 'View on GitHub' button");
        contextManager.switchToNative();
        webViewPage.tapViewOnGitHubButton();
        return this;
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

