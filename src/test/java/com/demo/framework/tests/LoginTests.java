package com.demo.framework.tests;

import com.demo.framework.flows.LoginFlow;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.demo.framework.utils.AllureStepUtils.allureStep;
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
    @Description("Navigate to Login, enter valid credentials, verify successful login")
    public void testSuccessfulLogin() {
        allureStep("Step 1: Navigate to Login screen and enter valid email");
        loginFlow.navigateToLogin();
        assertTrue(loginFlow.isLoginPageLoaded(), "Login page should be loaded");
        loginFlow.enterUsername("test@example.com");

        allureStep("Step 2: Enter valid password (minimum 8 characters)");
        loginFlow.enterPassword("Password123");

        allureStep("Step 3: Tap Login button and verify successful login");
        loginFlow.clickLoginButton();
        assertTrue(loginFlow.isLoginSuccessful(), "User should reach home/success screen after login");
    }

    @Test(groups = {"login", "regression"},
          description = "TC_1.2: Verify error message with invalid credentials")
    @Description("Navigate to Login, enter invalid email format, verify error message")
    public void testLoginWithInvalidCredentials() {
        allureStep("Step 1: Navigate to Login screen and enter invalid email format");
        loginFlow.navigateToLogin();
        assertTrue(loginFlow.isLoginPageLoaded(), "Login page should be loaded");
        loginFlow.enterUsername("notanemail");

        allureStep("Step 2: Enter any password and tap Login button");
        loginFlow.enterPassword("anypassword");
        loginFlow.clickLoginButton();

        allureStep("Step 3: Verify error message is displayed");
        assertTrue(loginFlow.isErrorMessageDisplayed(), "Error message should be displayed for invalid email format");
    }

    @Test(groups = {"login", "regression"},
          description = "TC_1.3: Verify error message with empty fields")
    @Description("Navigate to Login, leave fields empty, verify validation messages")
    public void testLoginWithEmptyFields() {
        allureStep("Step 1: Navigate to Login screen");
        loginFlow.navigateToLogin();
        assertTrue(loginFlow.isLoginPageLoaded(), "Login page should be loaded");

        allureStep("Step 2: Leave email field empty");
        // Email field is empty by default

        allureStep("Step 3: Leave password field empty");
        // Password field is empty by default

        allureStep("Step 4: Tap Login button");
        loginFlow.clickLoginButton();

        allureStep("Step 5: Verify validation messages for both fields");
        assertTrue(loginFlow.isErrorMessageDisplayed(), "Validation messages should be displayed for empty fields");
    }
}

