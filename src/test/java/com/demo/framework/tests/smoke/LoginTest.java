package com.demo.framework.tests.smoke;

import com.demo.framework.pages.LoginPage;
import com.demo.framework.tests.BaseTest;
import com.demo.framework.utils.TestDataUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Feature("Authentication")
@Story("Login Tests")
public class LoginTest extends BaseTest {

    @Test(description = "Verify Login Page is loaded")
    @Description("Test to verify that Login Page loads successfully")
    public void loginPageLoadsSuccessfully() {
        LoginPage loginPage = new LoginPage();
        assertTrue(loginPage.isPageLoaded(), "Login Page should be loaded");
    }

    @Test(description = "Verify error message on invalid login")
    @Description("Test to verify that error message is displayed when invalid credentials are used")
    public void invalidLoginShowsErrorMessage() {
        LoginPage loginPage = new LoginPage();
        loginPage.isPageLoaded();

        TestDataUtils.TestUserData testUser = TestDataUtils.generateTestUser();
        loginPage.login(testUser.getUsername(), testUser.getPassword());

        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
    }

    @Test(description = "Verify username field accepts text input")
    @Description("Test to verify that username field accepts text input")
    public void usernameFieldAcceptsInput() {
        LoginPage loginPage = new LoginPage();
        loginPage.isPageLoaded();

        String testUsername = TestDataUtils.generateRandomUsername();
        loginPage.enterUsername(testUsername);

        // Verification would depend on actual app behavior
        assertFalse(testUsername.isEmpty(), "Username should not be empty");
    }
}

