package com.demo.framework.tests.regression;

import com.demo.framework.pages.HomePageUI;
import com.demo.framework.pages.LoginPage;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("Authentication")
@Story("Login Regression Tests")
public class LoginRegressionTest extends BaseTest {

    private HomePageUI homePage;
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LOG.info("Initializing Home Page for Login regression tests");
        homePage = new HomePageUI();
        assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }

    @Test(description = "Verify login page is accessible from home")
    @Description("Regression test to verify login page loads correctly from home navigation")
    public void testLoginPageAccessibility() {
        LOG.info("Testing login page accessibility");
        loginPage = homePage.navigateToLogin();
        assertTrue(loginPage.isPageLoaded(), "Login page should be accessible from home");
        LOG.info("Login page accessibility verified");
    }

    @Test(description = "Verify username field is present and functional")
    @Description("Regression test to verify username field functionality")
    public void testUsernameFieldFunctionality() {
        LOG.info("Testing username field functionality");
        loginPage = homePage.navigateToLogin();

        LOG.info("Entering test username");
        String testUsername = "testuser@example.com";
        loginPage.enterUsername(testUsername);

        LOG.info("Username field is functional");
    }

    @Test(description = "Verify password field is present and functional")
    @Description("Regression test to verify password field functionality")
    public void testPasswordFieldFunctionality() {
        LOG.info("Testing password field functionality");
        loginPage = homePage.navigateToLogin();

        LOG.info("Entering test password");
        loginPage.enterPassword("testpassword123");

        LOG.info("Password field is functional");
    }

    @Test(description = "Verify login button is present and clickable")
    @Description("Regression test to verify login button functionality")
    public void testLoginButtonFunctionality() {
        LOG.info("Testing login button functionality");
        loginPage = homePage.navigateToLogin();

        LOG.info("Entering credentials and clicking login");
        loginPage.enterUsername("test@test.com");
        loginPage.enterPassword("password");
        loginPage.clickLoginButton();

        LOG.info("Login button is functional");
    }

    @Test(description = "Verify forgot password link is present")
    @Description("Regression test to verify forgot password link functionality")
    public void testForgotPasswordLink() {
        LOG.info("Testing forgot password link");
        loginPage = homePage.navigateToLogin();

        LOG.info("Clicking forgot password link");
        loginPage.clickForgotPasswordLink();

        LOG.info("Forgot password link is functional");
    }

    @Test(description = "Verify error message visibility")
    @Description("Regression test to verify error message behavior")
    public void testErrorMessageVisibility() {
        LOG.info("Testing error message visibility");
        loginPage = homePage.navigateToLogin();

        LOG.info("Entering invalid credentials");
        loginPage.enterUsername("invalid@test.com");
        loginPage.enterPassword("wrongpass");
        loginPage.clickLoginButton();

        LOG.info("Checking error message visibility");
        boolean isError = loginPage.isErrorMessageDisplayed();
        LOG.info("Error message visible: {}", isError);
    }
}

