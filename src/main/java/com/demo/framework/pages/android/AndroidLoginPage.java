package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.LoginPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * Android implementation of Login Page
 */
public class AndroidLoginPage extends BasePage implements LoginPage {

    // Android locators using Accessibility ID (content-desc)
    private static final By LOGIN_SCREEN = AppiumBy.accessibilityId("Login-screen");
    private static final By EMAIL_INPUT = AppiumBy.accessibilityId("input-email");
    private static final By PASSWORD_INPUT = AppiumBy.accessibilityId("input-password");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("button-LOGIN");
    // Fallback using UIAutomator for elements without accessibility id
    private static final By INVALID_EMAIL_ERROR_MESSAGE = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Please enter a valid email address\")");
    private static final By INVALID_PASSWORD_ERROR_MESSAGE = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Please enter at least 8 characters\")");
    // Success popup elements
    private static final By SUCCESS_TITLE = AppiumBy.id("android:id/alertTitle");
    private static final By SUCCESS_MESSAGE = AppiumBy.id("android:id/message");
    private static final String EXPECTED_SUCCESS_TITLE = "Success";
    private static final String EXPECTED_SUCCESS_MESSAGE = "You are logged in!";

    @Override
    public String getPageTitle() {
        return "Login Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Android Login Page is loaded");
        try {
            wait.untilVisible(LOGIN_SCREEN);
            LOG.info("Android Login Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Android Login Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void enterUsername(String username) {
        LOG.info("Entering username on Android: {}", username);
        actions.sendText(EMAIL_INPUT, username);
    }

    @Override
    public void enterPassword(String password) {
        LOG.info("Entering password on Android");
        actions.sendText(PASSWORD_INPUT, password);
    }

    @Override
    public void clickLoginButton() {
        LOG.info("Clicking login button on Android");
        actions.click(LOGIN_BUTTON);
    }

    @Override
    public void login(String username, String password) {
        LOG.info("Performing login on Android with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    @Override
    public String getErrorMessage() {
        LOG.info("Getting error message on Android");
        return actions.getText(INVALID_EMAIL_ERROR_MESSAGE);
    }

    @Override
    public boolean isInvalidEmailErrorMessageDisplayed() {
        LOG.info("Checking if error message is displayed on Android");
        return actions.isDisplayed(INVALID_EMAIL_ERROR_MESSAGE);
    }

    @Override
    public boolean isInvalidPasswordErrorMessageDisplayed() {
        LOG.info("Checking if password error message is displayed on Android");
        return actions.isDisplayed(INVALID_PASSWORD_ERROR_MESSAGE);
    }

    @Override
    public boolean isLoginSuccessful() {
        LOG.info("Checking if login was successful on Android");

        try {
            String title = wait.untilVisible(SUCCESS_TITLE).getText();
            LOG.info("Login popup title: {}", title);

            String message = driver.findElement(SUCCESS_MESSAGE).getText();
            LOG.info("Login popup message: {}", message);

            return EXPECTED_SUCCESS_TITLE.equals(title) &&
                    EXPECTED_SUCCESS_MESSAGE.equals(message);
        } catch (Exception e) {
            LOG.warn("Login success check failed: {}", e.getMessage());
            return false;
        }
    }
}
