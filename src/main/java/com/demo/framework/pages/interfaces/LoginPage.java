package com.demo.framework.pages.interfaces;

/**
 * Interface for Login Page
 */
public interface LoginPage {

    boolean isPageLoaded();

    void enterUsername(String username);

    void enterPassword(String password);

    void clickLoginButton();


    boolean isInvalidEmailErrorMessageDisplayed();

    boolean isInvalidPasswordErrorMessageDisplayed();

    boolean isLoginSuccessful();
}

