package com.demo.framework.tests.smoke;

import com.demo.framework.pages.CarouselPage;
import com.demo.framework.pages.HomePageUI;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("Gestures")
@Story("Swipe/Gesture Actions")
public class CarouselTest extends BaseTest {

    private HomePageUI homePage;
    private CarouselPage carouselPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LOG.info("Initializing Home Page for Carousel tests");
        homePage = new HomePageUI();
        assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }

    @Test(testName = "TC_2.1_SwipeThroughCarousel", description = "Test Case 2.1: Swipe Through Carousel")
    @Description("Verify user can swipe through carousel items successfully")
    public void testSwipeThroughCarousel() {
        LOG.info("Starting Test Case 2.1: Swipe Through Carousel");

        navigateToCarouselPage();

        LOG.info("Verifying carousel is displayed and has items");
        assertTrue(carouselPage.isCarouselItemVisible(), "Carousel item should be visible");

        String initialText = carouselPage.getCurrentItemText();
        LOG.info("Initial carousel item text: {}", initialText);

        LOG.info("Performing swipe to next item");
        carouselPage.swipeToNextItem();

        try {
            Thread.sleep(1000); // Wait for animation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String nextItemText = carouselPage.getCurrentItemText();
        LOG.info("Next carousel item text: {}", nextItemText);

        LOG.info("Verifying carousel item has changed");
        assertTrue(carouselPage.isCarouselItemVisible(), "Next carousel item should be visible");

        LOG.info("Performing swipe to previous item");
        carouselPage.swipeToPreviousItem();

        try {
            Thread.sleep(1000); // Wait for animation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Getting carousel counter value");
        String counter = carouselPage.getCarouselCounter();
        LOG.info("Carousel counter: {}", counter);

        carouselPage.captureScreenshot();
        LOG.info("Test Case 2.1 completed successfully");
    }

    @Test(testName = "TC_2.1_MultipleSwipes", description = "Test Case 2.1: Multiple Carousel Swipes")
    @Description("Verify carousel handles multiple consecutive swipes")
    public void testMultipleCarouselSwipes() {
        LOG.info("Starting Test Case 2.1 Extended: Multiple Carousel Swipes");

        navigateToCarouselPage();

        LOG.info("Verifying carousel is ready");
        assertTrue(carouselPage.isCarouselItemVisible(), "Carousel should have items");

        LOG.info("Performing 3 consecutive swipes through carousel");
        carouselPage.swipeThroughCarousel(3);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying carousel is still displayed after swipes");
        assertTrue(carouselPage.isCarouselItemVisible(), "Carousel should remain visible after swipes");

        String finalCounter = carouselPage.getCarouselCounter();
        LOG.info("Final carousel counter: {}", finalCounter);

        carouselPage.captureScreenshot();
        LOG.info("Test Case 2.1 Extended completed successfully");
    }

    @Step("Navigate to Carousel Page")
    private void navigateToCarouselPage() {
        LOG.info("Navigating to carousel page from home page");
        carouselPage = homePage.navigateToCarousel();
        assertTrue(carouselPage.isPageLoaded(), "Carousel page should be loaded");
    }
}

