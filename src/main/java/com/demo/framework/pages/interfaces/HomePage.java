package com.demo.framework.pages.interfaces;

/**
 * Interface for Home Page
 * Defines UI contract for the main navigation hub
 */
public interface HomePage {

    boolean isPageLoaded();

    void clickLoginLink();

    void clickSwipeLink();

    void clickWebViewLink();

    void clickDragDropLink();
}

