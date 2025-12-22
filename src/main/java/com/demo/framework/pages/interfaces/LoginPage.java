package com.demo.framework.pages.interfaces;

/**
 * Interface for Login Page
 * Defines UI contract for authentication screen
 */
public interface LoginPage {

    boolean isPageLoaded();

    void enterUsername(String username);

    void enterPassword(String password);

    void clickLoginButton();

    void login(String username, String password);

    String getErrorMessage();

    boolean isInvalidEmailErrorMessageDisplayed();

    boolean isInvalidPasswordErrorMessageDisplayed();

    boolean isLoginSuccessful();
}

