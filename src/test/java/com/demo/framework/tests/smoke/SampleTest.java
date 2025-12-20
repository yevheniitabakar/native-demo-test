package com.demo.framework.tests.smoke;

import com.demo.framework.pages.HomePageUI;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class SampleTest extends BaseTest {

    @Test(description = "Framework bootstrap and app launch sanity check")
    @Description("Verify that driver initializes successfully and app opens on the home screen")
    public void driverStarts() {
        verifyDriverInitialization();
        verifyAppOpened();
    }

    @Step("Step 1: Verify driver is initialized")
    private void verifyDriverInitialization() {
        assertNotNull(driver(), "Driver should be initialized for the test session");
    }

    @Step("Step 2: Verify app has been opened successfully")
    private void verifyAppOpened() {
        HomePageUI homePage = new HomePageUI();
        assertTrue(homePage.isPageLoaded(), "App should open and display the Home screen");
    }
}
