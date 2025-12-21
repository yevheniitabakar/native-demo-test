package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.LoginPage;
import org.openqa.selenium.By;

/**
 * Android implementation of Login Page
 */
public class AndroidLoginPage extends BasePage implements LoginPage {

    // Android-specific locators
    private static final By LOGIN_SCREEN = By.xpath("//android.widget.TextView[@text='Login / Sign up Form']");
    private static final By EMAIL_INPUT = By.xpath("//android.widget.EditText[@content-desc='input-email']");
    private static final By PASSWORD_INPUT = By.xpath("//android.widget.EditText[@content-desc='input-password']");
    private static final By LOGIN_BUTTON = By.xpath("//android.widget.TextView[@text='LOGIN']");
    private static final By ERROR_MESSAGE = By.xpath("//android.widget.TextView[contains(@text,'Invalid')]");
    private static final By SUCCESS_MESSAGE = By.xpath("//android.widget.TextView[@text='You are logged in!']");

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
        return actions.getText(ERROR_MESSAGE);
    }

    @Override
    public boolean isErrorMessageDisplayed() {
        LOG.info("Checking if error message is displayed on Android");
        return actions.isDisplayed(ERROR_MESSAGE);
    }

    @Override
    public boolean isLoginSuccessful() {
        LOG.info("Checking if login was successful on Android");
        try {
            wait.untilVisible(SUCCESS_MESSAGE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

