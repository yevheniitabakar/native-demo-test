package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.LoginPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * iOS implementation of Login Page
 */
public class IOSLoginPage extends BasePage implements LoginPage {

    // iOS locators using Accessibility ID (name/label)
    private static final By LOGIN_SCREEN = AppiumBy.accessibilityId("Login-screen");
    private static final By EMAIL_INPUT = AppiumBy.accessibilityId("input-email");
    private static final By PASSWORD_INPUT = AppiumBy.accessibilityId("input-password");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("button-LOGIN");
    // Fallback using iOS Predicate String for elements without accessibility id
    private static final By INVALID_EMAIL_ERROR_MESSAGE = AppiumBy.iOSNsPredicateString(
            "name CONTAINS 'Invalid' OR label CONTAINS 'Invalid'");
    private static final By INVALID_PASSWORD_ERROR_MESSAGE = AppiumBy.iOSNsPredicateString(
            "name CONTAINS 'Invalid' OR label CONTAINS 'Invalid'");
    private static final By SUCCESS_MESSAGE = AppiumBy.accessibilityId("success-message");

    @Override
    public String getPageTitle() {
        return "Login Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if iOS Login Page is loaded");
        try {
            wait.untilVisible(LOGIN_SCREEN);
            LOG.info("iOS Login Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("iOS Login Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void enterUsername(String username) {
        LOG.info("Entering username on iOS: {}", username);
        actions.sendText(EMAIL_INPUT, username);
    }

    @Override
    public void enterPassword(String password) {
        LOG.info("Entering password on iOS");
        actions.sendText(PASSWORD_INPUT, password);
    }

    @Override
    public void clickLoginButton() {
        LOG.info("Clicking login button on iOS");
        actions.click(LOGIN_BUTTON);
    }

    @Override
    public void login(String username, String password) {
        LOG.info("Performing login on iOS with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    @Override
    public String getErrorMessage() {
        LOG.info("Getting error message on iOS");
        return actions.getText(INVALID_EMAIL_ERROR_MESSAGE);
    }

    @Override
    public boolean isInvalidEmailErrorMessageDisplayed() {
        LOG.info("Checking if error message is displayed on iOS");
        return actions.isDisplayed(INVALID_EMAIL_ERROR_MESSAGE);
    }

    @Override
    public boolean isInvalidPasswordErrorMessageDisplayed() {
        LOG.info("Checking if password error message is displayed on iOS");
        return actions.isDisplayed(INVALID_PASSWORD_ERROR_MESSAGE);
    }

    @Override
    public boolean isLoginSuccessful() {
        LOG.info("Checking if login was successful on iOS");
        try {
            wait.untilVisible(SUCCESS_MESSAGE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
