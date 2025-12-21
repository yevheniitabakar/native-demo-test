package com.demo.framework.tests;

import com.demo.framework.flows.LoginFlow;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test class for Login functionality.
 * Uses LoginFlow for business actions.
 * Contains only orchestration and assertions.
 */
@Feature("Authentication")
@Story("Login Functionality")
public class LoginTests extends BaseTest {

    private LoginFlow loginFlow;

    @BeforeMethod(alwaysRun = true)
    public void setUpLoginFlow() {
        loginFlow = new LoginFlow();
    }

    @Test(groups = {"login", "smoke"},
          description = "TC_1.1: Verify successful login with valid credentials")
    @Description("Verify user can successfully login with valid credentials")
    public void testSuccessfulLogin() {
        LOG.info("Starting TC_1.1: Successful Login");

        loginFlow.navigateToLogin();
        assertTrue(loginFlow.isLoginPageLoaded(), "Login page should be loaded");

        loginFlow.login("valid@email.com", "Password123");

        assertTrue(loginFlow.isLoginSuccessful(), "Login should be successful with valid credentials");
    }

    @Test(groups = {"login", "regression"},
          description = "TC_1.2: Verify error message with invalid credentials")
    @Description("Verify error message is displayed when using invalid credentials")
    public void testLoginWithInvalidCredentials() {
        LOG.info("Starting TC_1.2: Login with Invalid Credentials");

        loginFlow.navigateToLogin();
        assertTrue(loginFlow.isLoginPageLoaded(), "Login page should be loaded");

        loginFlow.login("notanemail", "wrongpassword");

        assertTrue(loginFlow.isErrorMessageDisplayed(), "Error message should be displayed for invalid credentials");
    }

    @Test(groups = {"login", "regression"},
          description = "TC_1.3: Verify error message with empty fields")
    @Description("Verify error message is displayed when login fields are empty")
    public void testLoginWithEmptyFields() {
        LOG.info("Starting TC_1.3: Login with Empty Fields");

        loginFlow.navigateToLogin();
        assertTrue(loginFlow.isLoginPageLoaded(), "Login page should be loaded");

        loginFlow.clickLoginButton();

        assertTrue(loginFlow.isErrorMessageDisplayed(), "Error message should be displayed for empty fields");
    }
}

