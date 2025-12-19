package com.demo.framework.pages;

import org.openqa.selenium.By;

/**
 * Example Page Object for Home/Main Screen
 * Demonstrates PO pattern with Appium/Selenium
 */
public class HomePage extends BasePage {

    // Locators
    private static final By HOME_TITLE = By.xpath("//android.widget.TextView[@text='Home']");
    private static final By WELCOME_TEXT = By.id("welcome_message");
    private static final By START_BUTTON = By.id("start_button");
    private static final By SETTINGS_ICON = By.id("settings_icon");

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
     * Get welcome message
     */
    public String getWelcomeMessage() {
        LOG.info("Getting welcome message");
        return actions.getText(WELCOME_TEXT);
    }

    /**
     * Click start button
     */
    public void clickStartButton() {
        LOG.info("Clicking start button");
        actions.click(START_BUTTON);
    }

    /**
     * Open settings
     */
    public void openSettings() {
        LOG.info("Opening settings");
        actions.click(SETTINGS_ICON);
    }

    /**
     * Check if start button is visible
     */
    public boolean isStartButtonVisible() {
        LOG.info("Checking if start button is visible");
        return actions.isDisplayed(START_BUTTON);
    }
}

