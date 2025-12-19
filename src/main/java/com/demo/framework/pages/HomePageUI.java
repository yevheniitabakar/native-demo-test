package com.demo.framework.pages;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for Home Screen
 * Main navigation hub for accessing various test screens
 */
public class HomePageUI extends BasePage {

    private static final Logger LOG = LoggerFactory.getLogger(HomePageUI.class);

    // Locators
    private static final By HOME_TITLE = By.xpath("//android.widget.TextView[@text='Home']");
    private static final By LOGIN_BUTTON = By.id("login_link");
    private static final By CAROUSEL_BUTTON = By.id("carousel_link");
    private static final By WEBVIEW_BUTTON = By.id("webview_link");
    private static final By DRAGDROP_BUTTON = By.id("dragdrop_link");

    @Override
    public String getPageTitle() {
        return "Home Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Home Page is loaded");
        try {
            wait.untilVisible(HOME_TITLE);
            LOG.info("Home Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Home Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Click on login link to navigate to login screen
     */
    public void clickLoginLink() {
        LOG.info("Clicking on login link");
        actions.click(LOGIN_BUTTON);
    }

    /**
     * Click on carousel link to navigate to carousel screen
     */
    public void clickCarouselLink() {
        LOG.info("Clicking on carousel link");
        actions.click(CAROUSEL_BUTTON);
    }

    /**
     * Click on WebView link to navigate to WebView screen
     */
    public void clickWebViewLink() {
        LOG.info("Clicking on WebView link");
        actions.click(WEBVIEW_BUTTON);
    }

    /**
     * Click on drag and drop link to navigate to drag and drop screen
     */
    public void clickDragDropLink() {
        LOG.info("Clicking on drag and drop link");
        actions.click(DRAGDROP_BUTTON);
    }

    /**
     * Navigate to login screen
     */
    public LoginPage navigateToLogin() {
        LOG.info("Navigating to login screen");
        clickLoginLink();
        return new LoginPage();
    }

    /**
     * Navigate to carousel screen
     */
    public CarouselPage navigateToCarousel() {
        LOG.info("Navigating to carousel screen");
        clickCarouselLink();
        return new CarouselPage();
    }

    /**
     * Navigate to WebView screen
     */
    public WebViewPage navigateToWebView() {
        LOG.info("Navigating to WebView screen");
        clickWebViewLink();
        return new WebViewPage();
    }

    /**
     * Navigate to drag and drop screen
     */
    public DragDropPage navigateToDragDrop() {
        LOG.info("Navigating to drag and drop screen");
        clickDragDropLink();
        return new DragDropPage();
    }
}

