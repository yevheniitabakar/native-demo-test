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

    private static final By EMAIL_INPUT = AppiumBy.accessibilityId("input-email");
    private static final By PASSWORD_INPUT = AppiumBy.accessibilityId("input-password");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("button-LOGIN");
    private static final By INVALID_EMAIL_ERROR_MESSAGE = AppiumBy.accessibilityId(
            "Please enter a valid email address");
    private static final By INVALID_PASSWORD_ERROR_MESSAGE = AppiumBy.iOSClassChain(
            "**/XCUIElementTypeOther[`label CONTAINS 'Password'`]"
                    + "/XCUIElementTypeStaticText[`name == 'Please enter at least 8 characters'`]");
    private static final By SUCCESS_TITLE = AppiumBy.iOSClassChain(
            "**/XCUIElementTypeStaticText[`name == \"Success\"`]");
    private static final By SUCCESS_MESSAGE = AppiumBy.iOSClassChain(
            "**/XCUIElementTypeStaticText[`name == \"You are logged in!\"`]");

    @Override
    public boolean isPageLoaded() {
        log.info("Checking if iOS Login Page is loaded");
        try {
            wait.untilVisible(EMAIL_INPUT);
            return true;
        } catch (Exception e) {
            log.warn("iOS Login Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void enterUsername(String username) {
        log.info("Entering username on iOS: {}", username);
        actions.clearText(EMAIL_INPUT);
        actions.sendText(EMAIL_INPUT, username);
    }

    @Override
    public void enterPassword(String password) {
        log.info("Entering password on iOS");
        actions.sendTextToSecureField(PASSWORD_INPUT, password);
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
            String title = wait.untilVisible(SUCCESS_TITLE).getText();
            String message = driver.findElement(SUCCESS_MESSAGE).getText();
            return "Success".equals(title) && "You are logged in!".equals(message);
        } catch (Exception e) {
            log.warn("Login success check failed: {}", e.getMessage());
            return false;
        }
    }
}
