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
    public void navigateToLogin() {
        log.info("Navigating to Login screen");
        homePage.clickLoginLink();
    }

    @Step("Enter username: {username}")
    public void enterUsername(String username) {
        log.info("Entering username: {}", username);
        loginPage.enterUsername(username);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        log.info("Entering password");
        loginPage.enterPassword(password);
    }

    @Step("Click login button")
    public void clickLoginButton() {
        log.info("Clicking login button");
        loginPage.clickLoginButton();
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

