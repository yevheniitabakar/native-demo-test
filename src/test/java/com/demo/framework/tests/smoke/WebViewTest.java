package com.demo.framework.tests.smoke;

import com.demo.framework.pages.HomePageUI;
import com.demo.framework.pages.WebViewPage;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("WebView Interactions")
@Story("WebView Navigation")
public class WebViewTest extends BaseTest {

    private HomePageUI homePage;
    private WebViewPage webViewPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LOG.info("Initializing Home Page for WebView tests");
        homePage = new HomePageUI();
        assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }

    @Test(testName = "TC_3.1_WebViewNavigation", description = "Test Case 3.1: WebView Navigation")
    @Description("Verify user can navigate to and interact with WebView")
    public void testWebViewNavigation() {
        LOG.info("Starting Test Case 3.1: WebView Navigation");

        navigateToWebViewPage();

        LOG.info("Verifying WebView is displayed");
        assertTrue(webViewPage.isWebViewDisplayed(), "WebView should be displayed");

        LOG.info("Entering URL for WebView navigation");
        String testUrl = "https://webdriver.io";
        webViewPage.navigateToUrl(testUrl);

        try {
            Thread.sleep(2000); // Wait for page load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying WebView content is loaded");
        assertTrue(webViewPage.isWebViewDisplayed(), "WebView should display content");

        LOG.info("Getting WebView page source to verify load");
        String pageSource = webViewPage.getWebViewPageSource();
        LOG.info("WebView page source length: {}", pageSource.length());

        webViewPage.captureScreenshot();
        LOG.info("Test Case 3.1 completed successfully");
    }

    @Test(testName = "TC_3.1_WebViewNavigation_Extended", description = "Test Case 3.1: WebView Navigation Extended")
    @Description("Verify WebView navigation controls (back, forward, refresh)")
    public void testWebViewNavigationControls() {
        LOG.info("Starting Test Case 3.1 Extended: WebView Navigation Controls");

        navigateToWebViewPage();

        LOG.info("Verifying WebView is displayed");
        assertTrue(webViewPage.isWebViewDisplayed(), "WebView should be displayed");

        LOG.info("Entering first URL");
        String firstUrl = "https://webdriver.io";
        webViewPage.navigateToUrl(firstUrl);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Entering second URL");
        String secondUrl = "https://appium.io";
        webViewPage.navigateToUrl(secondUrl);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Clicking WebView back button");
        webViewPage.clickBackButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying navigation back was successful");
        assertTrue(webViewPage.isWebViewDisplayed(), "WebView should display previous content");

        LOG.info("Clicking WebView forward button");
        webViewPage.clickForwardButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying navigation forward was successful");
        assertTrue(webViewPage.isWebViewDisplayed(), "WebView should display forward content");

        LOG.info("Clicking WebView refresh button");
        webViewPage.clickRefreshButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying WebView refresh completed");
        assertTrue(webViewPage.isWebViewDisplayed(), "WebView should refresh successfully");

        webViewPage.captureScreenshot();
        LOG.info("Test Case 3.1 Extended completed successfully");
    }

    @Step("Navigate to WebView Page")
    private void navigateToWebViewPage() {
        LOG.info("Navigating to WebView page from home page");
        webViewPage = homePage.navigateToWebView();
        assertTrue(webViewPage.isPageLoaded(), "WebView page should be loaded");
    }
}

