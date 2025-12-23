package com.demo.framework.flows;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.HomePage;
import com.demo.framework.pages.interfaces.LoginPage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Flow class for Login functionality.
 * Orchestrates actions across HomePage and LoginPage.
 */
@Slf4j
public class LoginFlow {

    private final HomePage homePage;
    private final LoginPage loginPage;

    public LoginFlow() {
        this.homePage = PageFactory.homePage();
        this.loginPage = PageFactory.loginPage();
    }

    @Step("Navigate to Login screen")
    public LoginFlow navigateToLogin() {
        log.info("Navigating to Login screen");
        homePage.clickLoginLink();
        return this;
    }

    @Step("Enter username: {username}")
    public LoginFlow enterUsername(String username) {
        log.info("Entering username: {}", username);
        loginPage.enterUsername(username);
        return this;
    }

    @Step("Enter password")
    public LoginFlow enterPassword(String password) {
        log.info("Entering password");
        loginPage.enterPassword(password);
        return this;
    }

    @Step("Click login button")
    public LoginFlow clickLoginButton() {
        log.info("Clicking login button");
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
}

