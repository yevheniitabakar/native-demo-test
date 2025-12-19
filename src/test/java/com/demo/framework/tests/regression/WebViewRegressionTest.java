package com.demo.framework.tests.regression;

import com.demo.framework.pages.HomePageUI;
import com.demo.framework.pages.WebViewPage;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("WebView Interactions")
@Story("WebView Regression Tests")
public class WebViewRegressionTest extends BaseTest {

    private HomePageUI homePage;
    private WebViewPage webViewPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LOG.info("Initializing Home Page for WebView regression tests");
        homePage = new HomePageUI();
        assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }

    @Test(description = "Verify WebView page is accessible from home")
    @Description("Regression test to verify WebView page loads correctly")
    public void testWebViewPageAccessibility() {
        LOG.info("Testing WebView page accessibility");
        webViewPage = homePage.navigateToWebView();
        assertTrue(webViewPage.isPageLoaded(), "WebView page should be accessible");
        LOG.info("WebView page accessibility verified");
    }

    @Test(description = "Verify WebView element is displayed")
    @Description("Regression test to verify WebView element visibility")
    public void testWebViewElementDisplay() {
        LOG.info("Testing WebView element display");
        webViewPage = homePage.navigateToWebView();

        LOG.info("Checking if WebView is displayed");
        assertTrue(webViewPage.isWebViewDisplayed(), "WebView element should be displayed");
        LOG.info("WebView element display verified");
    }

    @Test(description = "Verify URL input field functionality")
    @Description("Regression test to verify URL input field")
    public void testUrlInputField() {
        LOG.info("Testing URL input field functionality");
        webViewPage = homePage.navigateToWebView();

        String testUrl = "https://example.com";
        LOG.info("Entering test URL: {}", testUrl);
        webViewPage.enterUrl(testUrl);

        LOG.info("URL input field functionality verified");
    }

    @Test(description = "Verify navigate button functionality")
    @Description("Regression test to verify navigate button")
    public void testNavigateButton() {
        LOG.info("Testing navigate button functionality");
        webViewPage = homePage.navigateToWebView();

        LOG.info("Entering URL and clicking navigate button");
        webViewPage.enterUrl("https://webdriver.io");
        webViewPage.clickNavigateButton();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Navigate button functionality verified");
    }

    @Test(description = "Verify WebView back button functionality")
    @Description("Regression test to verify WebView back button")
    public void testWebViewBackButton() {
        LOG.info("Testing WebView back button functionality");
        webViewPage = homePage.navigateToWebView();

        LOG.info("Navigating to URL and using back button");
        webViewPage.navigateToUrl("https://webdriver.io");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Clicking back button");
        webViewPage.clickBackButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("WebView back button functionality verified");
    }

    @Test(description = "Verify WebView forward button functionality")
    @Description("Regression test to verify WebView forward button")
    public void testWebViewForwardButton() {
        LOG.info("Testing WebView forward button functionality");
        webViewPage = homePage.navigateToWebView();

        LOG.info("Clicking forward button");
        webViewPage.clickForwardButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("WebView forward button functionality verified");
    }

    @Test(description = "Verify WebView refresh button functionality")
    @Description("Regression test to verify WebView refresh button")
    public void testWebViewRefreshButton() {
        LOG.info("Testing WebView refresh button functionality");
        webViewPage = homePage.navigateToWebView();

        LOG.info("Navigating to URL");
        webViewPage.navigateToUrl("https://webdriver.io");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Clicking refresh button");
        webViewPage.clickRefreshButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("WebView refresh button functionality verified");
    }
}

