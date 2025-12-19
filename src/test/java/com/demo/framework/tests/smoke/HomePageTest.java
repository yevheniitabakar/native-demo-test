package com.demo.framework.tests.smoke;

import com.demo.framework.pages.HomePage;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("Framework Bootstrap")
@Story("Smoke Tests")
public class HomePageTest extends BaseTest {

    @Test(description = "Verify Home Page is loaded")
    @Description("Test to verify that Home Page loads successfully")
    public void homePageLoadsSuccessfully() {
        HomePage homePage = new HomePage();
        assertTrue(homePage.isPageLoaded(), "Home Page should be loaded");
    }

    @Test(description = "Verify Start Button is visible on Home Page")
    @Description("Test to verify that Start Button is visible on Home Page")
    public void startButtonIsVisible() {
        HomePage homePage = new HomePage();
        homePage.isPageLoaded();
        assertTrue(homePage.isStartButtonVisible(), "Start Button should be visible");
    }
}

