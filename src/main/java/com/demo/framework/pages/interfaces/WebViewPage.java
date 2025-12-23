package com.demo.framework.pages.interfaces;

/**
 * Interface for WebView Page
 */
public interface WebViewPage {

    void tapViewOnGitHubButton();

    boolean isWebViewDisplayed();

    boolean isWebViewContentPresent();

    boolean isGitHubPageOpened(String expectedUrl);
}

