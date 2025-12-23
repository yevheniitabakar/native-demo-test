package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.LoginPage;
import io.appium.java_client.AppiumBy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * iOS implementation of Login Page
 */
@Slf4j
public class IOSLoginPage extends BasePage implements LoginPage {

    private static final By LOGIN_SCREEN = AppiumBy.accessibilityId("Login-screen");
    private static final By EMAIL_INPUT = AppiumBy.accessibilityId("input-email");
    private static final By PASSWORD_INPUT = AppiumBy.accessibilityId("input-password");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("button-LOGIN");
    private static final By INVALID_EMAIL_ERROR_MESSAGE = AppiumBy.iOSNsPredicateString(
            "name CONTAINS 'Invalid' OR label CONTAINS 'Invalid'");
    private static final By INVALID_PASSWORD_ERROR_MESSAGE = AppiumBy.iOSNsPredicateString(
            "name CONTAINS 'Invalid' OR label CONTAINS 'Invalid'");
    private static final By SUCCESS_MESSAGE = AppiumBy.accessibilityId("success-message");

    @Override
    public boolean isPageLoaded() {
        log.info("Checking if iOS Login Page is loaded");
        try {
            wait.untilVisible(LOGIN_SCREEN);
            return true;
        } catch (Exception e) {
            log.warn("iOS Login Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void enterUsername(String username) {
        log.info("Entering username on iOS: {}", username);
        actions.sendText(EMAIL_INPUT, username);
    }

    @Override
    public void enterPassword(String password) {
        log.info("Entering password on iOS");
        actions.sendText(PASSWORD_INPUT, password);
    }

    @Override
    public void clickLoginButton() {
        log.info("Clicking login button on iOS");
        actions.click(LOGIN_BUTTON);
    }

    @Override
    public boolean isInvalidEmailErrorMessageDisplayed() {
        log.info("Checking if email error message is displayed on iOS");
        return actions.isDisplayed(INVALID_EMAIL_ERROR_MESSAGE);
    }

    @Override
    public boolean isInvalidPasswordErrorMessageDisplayed() {
        log.info("Checking if password error message is displayed on iOS");
        return actions.isDisplayed(INVALID_PASSWORD_ERROR_MESSAGE);
    }

    @Override
    public boolean isLoginSuccessful() {
        log.info("Checking if login was successful on iOS");
        try {
            wait.untilVisible(SUCCESS_MESSAGE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
