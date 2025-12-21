package com.demo.framework.tests;

import com.demo.framework.flows.WebViewFlow;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test class for WebView functionality.
 * Uses WebViewFlow for business actions.
 * Contains only orchestration and assertions.
 */
@Feature("WebView")
@Story("WebView Navigation")
public class WebViewTests extends BaseTest {

    private WebViewFlow webViewFlow;
    private static final String GITHUB_URL = "https://github.com/webdriverio";

    @BeforeMethod(alwaysRun = true)
    public void setUpWebViewFlow() {
        webViewFlow = new WebViewFlow();
    }

    @Test(groups = {"webview", "smoke", "regression"},
          description = "TC_3.1: Verify WebView navigation and GitHub link")
    @Description("Navigate to WebView tab, verify content loads, tap View on GitHub button, verify browser opens with correct URL")
    public void testWebViewNavigation() {
        LOG.info("Starting TC_3.1: WebView Navigation");

        // Step 1: Navigate to WebView tab
        LOG.info("Step 1: Navigate to WebView tab");
        webViewFlow.navigateToWebView();
        assertTrue(webViewFlow.isWebViewPageLoaded(), "WebView page should be loaded");

        // Step 2: Wait for WebView to load completely
        LOG.info("Step 2: Wait for WebView to load completely");
        webViewFlow.waitForWebViewToLoad();
        assertTrue(webViewFlow.isWebViewDisplayed(), "WebView content should be displayed");

        // Step 3: Verify page title or specific web element is present
        LOG.info("Step 3: Verify page title or specific web element is present");
        assertTrue(webViewFlow.isWebViewContentPresent(), "WebView should display expected content");

        // Step 4: Tap on the button 'View on GitHub'
        LOG.info("Step 4: Tap on 'View on GitHub' button");
        webViewFlow.tapViewOnGitHubButton();

        // Step 5: Verify that browser opened with URL: https://github.com/webdriverio
        LOG.info("Step 5: Verify browser opened with GitHub URL");
        assertTrue(webViewFlow.isGitHubPageOpened(GITHUB_URL),
                   "Browser should open with URL: " + GITHUB_URL);
    }
}

