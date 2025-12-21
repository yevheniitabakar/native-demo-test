package com.demo.framework.pages.interfaces;

/**
 * Interface for WebView Page
 * Defines UI contract for WebView interactions
 */
public interface WebViewPage {

    boolean isPageLoaded();

    void waitForWebViewToLoad();

    void tapViewOnGitHubButton();

    boolean isWebViewDisplayed();

    boolean isWebViewContentPresent();

    boolean isGitHubPageOpened(String expectedUrl);

    String getPageTitle();
}

