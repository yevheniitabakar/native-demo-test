package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.LoginPage;
import io.appium.java_client.AppiumBy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * Android implementation of Login Page
 */
@Slf4j
public class AndroidLoginPage extends BasePage implements LoginPage {

    private static final By LOGIN_SCREEN = AppiumBy.accessibilityId("Login-screen");
    private static final By EMAIL_INPUT = AppiumBy.accessibilityId("input-email");
    private static final By PASSWORD_INPUT = AppiumBy.accessibilityId("input-password");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("button-LOGIN");
    private static final By INVALID_EMAIL_ERROR_MESSAGE = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Please enter a valid email address\")");
    private static final By INVALID_PASSWORD_ERROR_MESSAGE = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Please enter at least 8 characters\")");
    private static final By SUCCESS_TITLE = AppiumBy.id("android:id/alertTitle");
    private static final By SUCCESS_MESSAGE = AppiumBy.id("android:id/message");

    @Override
    public boolean isPageLoaded() {
        log.info("Checking if Android Login Page is loaded");
        try {
            wait.untilVisible(LOGIN_SCREEN);
            return true;
        } catch (Exception e) {
            log.warn("Android Login Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void enterUsername(String username) {
        log.info("Entering username on Android: {}", username);
        actions.sendText(EMAIL_INPUT, username);
    }

    @Override
    public void enterPassword(String password) {
        log.info("Entering password on Android");
        actions.sendText(PASSWORD_INPUT, password);
    }

    @Override
    public void clickLoginButton() {
        log.info("Clicking login button on Android");
        actions.click(LOGIN_BUTTON);
    }

    @Override
    public boolean isInvalidEmailErrorMessageDisplayed() {
        log.info("Checking if email error message is displayed on Android");
        return actions.isDisplayed(INVALID_EMAIL_ERROR_MESSAGE);
    }

    @Override
    public boolean isInvalidPasswordErrorMessageDisplayed() {
        log.info("Checking if password error message is displayed on Android");
        return actions.isDisplayed(INVALID_PASSWORD_ERROR_MESSAGE);
    }

    @Override
    public boolean isLoginSuccessful() {
        log.info("Checking if login was successful on Android");
        try {
            String title = wait.untilVisible(SUCCESS_TITLE).getText();
            String message = driver.findElement(SUCCESS_MESSAGE).getText();
            return "Success".equals(title) && "You are logged in!".equals(message);
        } catch (Exception e) {
            log.warn("Login success check failed: {}", e.getMessage());
            return false;
        }
    }
}
