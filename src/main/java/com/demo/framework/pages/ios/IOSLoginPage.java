package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.LoginPage;
import org.openqa.selenium.By;

/**
 * iOS implementation of Login Page
 */
public class IOSLoginPage extends BasePage implements LoginPage {

    // iOS-specific locators
    private static final By LOGIN_SCREEN = By.xpath("//XCUIElementTypeStaticText[@name='Login / Sign up Form']");
    private static final By EMAIL_INPUT = By.xpath("//XCUIElementTypeTextField[@name='input-email']");
    private static final By PASSWORD_INPUT = By.xpath("//XCUIElementTypeSecureTextField[@name='input-password']");
    private static final By LOGIN_BUTTON = By.xpath("//XCUIElementTypeButton[@name='LOGIN']");
    private static final By ERROR_MESSAGE = By.xpath("//XCUIElementTypeStaticText[contains(@name,'Invalid')]");
    private static final By SUCCESS_MESSAGE = By.xpath("//XCUIElementTypeStaticText[@name='You are logged in!']");

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
        return actions.getText(ERROR_MESSAGE);
    }

    @Override
    public boolean isErrorMessageDisplayed() {
        LOG.info("Checking if error message is displayed on iOS");
        return actions.isDisplayed(ERROR_MESSAGE);
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

