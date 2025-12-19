package com.demo.framework.pages;

import org.openqa.selenium.By;

/**
 * Example Page Object for Login Screen
 * Demonstrates PO pattern for authentication flow
 */
public class LoginPage extends BasePage {

    // Locators
    private static final By LOGIN_TITLE = By.xpath("//android.widget.TextView[@text='Login']");
    private static final By USERNAME_FIELD = By.id("username_input");
    private static final By PASSWORD_FIELD = By.id("password_input");
    private static final By LOGIN_BUTTON = By.id("login_button");
    private static final By ERROR_MESSAGE = By.id("error_message");
    private static final By FORGOT_PASSWORD_LINK = By.id("forgot_password_link");

    @Override
    public String getPageTitle() {
        return "Login Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Login Page is loaded");
        try {
            wait.untilVisible(LOGIN_TITLE);
            LOG.info("Login Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Login Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Enter username
     */
    public void enterUsername(String username) {
        LOG.info("Entering username: {}", username);
        actions.sendText(USERNAME_FIELD, username);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        LOG.info("Entering password");
        actions.sendText(PASSWORD_FIELD, password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        LOG.info("Clicking login button");
        actions.click(LOGIN_BUTTON);
    }

    /**
     * Login with credentials
     */
    public void login(String username, String password) {
        LOG.info("Logging in with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        LOG.info("Getting error message");
        return actions.getText(ERROR_MESSAGE);
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        LOG.info("Checking if error message is displayed");
        return actions.isDisplayed(ERROR_MESSAGE);
    }

    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        LOG.info("Clicking forgot password link");
        actions.click(FORGOT_PASSWORD_LINK);
    }
}

