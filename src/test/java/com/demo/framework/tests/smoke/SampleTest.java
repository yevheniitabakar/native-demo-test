package com.demo.framework.tests.smoke;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.HomePage;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Sample test for framework bootstrap and sanity check.
 */
@Feature("Framework")
public class SampleTest extends BaseTest {

    @Test(groups = {"smoke"},
          description = "Framework bootstrap and app launch sanity check")
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
        HomePage homePage = PageFactory.homePage();
        assertTrue(homePage.isPageLoaded(), "App should open and display the Home screen");
    }
}
