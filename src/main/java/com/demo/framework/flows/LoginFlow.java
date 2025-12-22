package com.demo.framework.flows;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.HomePage;
import com.demo.framework.pages.interfaces.LoginPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flow class for Login functionality.
 * Orchestrates actions across HomePage and LoginPage.
 * Platform-agnostic - no platform checks allowed here.
 */
public class LoginFlow {

    private static final Logger LOG = LoggerFactory.getLogger(LoginFlow.class);

    private final HomePage homePage;
    private final LoginPage loginPage;

    public LoginFlow() {
        this.homePage = PageFactory.homePage();
        this.loginPage = PageFactory.loginPage();
    }

    @Step("Navigate to Login screen")
    public LoginFlow navigateToLogin() {
        LOG.info("Navigating to Login screen");
        homePage.clickLoginLink();
        return this;
    }

    @Step("Login with username: {username}")
    public LoginFlow login(String username, String password) {
        LOG.info("Performing login with username: {}", username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        return this;
    }

    @Step("Enter username: {username}")
    public LoginFlow enterUsername(String username) {
        LOG.info("Entering username: {}", username);
        loginPage.enterUsername(username);
        return this;
    }

    @Step("Enter password")
    public LoginFlow enterPassword(String password) {
        LOG.info("Entering password");
        loginPage.enterPassword(password);
        return this;
    }

    @Step("Click login button")
    public LoginFlow clickLoginButton() {
        LOG.info("Clicking login button");
        loginPage.clickLoginButton();
        return this;
    }

    public boolean isLoginPageLoaded() {
        return loginPage.isPageLoaded();
    }

    public boolean isLoginSuccessful() {
        return loginPage.isLoginSuccessful();
    }

    public boolean isEmailErrorMessageDisplayed() {
        return loginPage.isInvalidEmailErrorMessageDisplayed();
    }

    public boolean isPasswordErrorMessageDisplayed() {
        return loginPage.isInvalidPasswordErrorMessageDisplayed();
    }

    public String getErrorMessage() {
        return loginPage.getErrorMessage();
    }
}

