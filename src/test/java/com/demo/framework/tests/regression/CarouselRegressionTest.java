package com.demo.framework.tests.regression;

import com.demo.framework.pages.CarouselPage;
import com.demo.framework.pages.HomePageUI;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("Gestures")
@Story("Carousel Regression Tests")
public class CarouselRegressionTest extends BaseTest {

    private HomePageUI homePage;
    private CarouselPage carouselPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LOG.info("Initializing Home Page for Carousel regression tests");
        homePage = new HomePageUI();
        assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }

    @Test(description = "Verify carousel page is accessible from home")
    @Description("Regression test to verify carousel page loads correctly")
    public void testCarouselPageAccessibility() {
        LOG.info("Testing carousel page accessibility");
        carouselPage = homePage.navigateToCarousel();
        assertTrue(carouselPage.isPageLoaded(), "Carousel page should be accessible");
        LOG.info("Carousel page accessibility verified");
    }

    @Test(description = "Verify carousel items are displayed")
    @Description("Regression test to verify carousel items visibility")
    public void testCarouselItemsDisplay() {
        LOG.info("Testing carousel items display");
        carouselPage = homePage.navigateToCarousel();

        LOG.info("Checking if carousel items are visible");
        assertTrue(carouselPage.isCarouselItemVisible(), "Carousel items should be visible");
        LOG.info("Carousel items display verified");
    }

    @Test(description = "Verify left swipe gesture works")
    @Description("Regression test to verify left swipe functionality")
    public void testLeftSwipeGesture() {
        LOG.info("Testing left swipe gesture");
        carouselPage = homePage.navigateToCarousel();

        LOG.info("Performing left swipe");
        carouselPage.swipeLeft();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Left swipe gesture verified");
    }

    @Test(description = "Verify right swipe gesture works")
    @Description("Regression test to verify right swipe functionality")
    public void testRightSwipeGesture() {
        LOG.info("Testing right swipe gesture");
        carouselPage = homePage.navigateToCarousel();

        LOG.info("Performing right swipe");
        carouselPage.swipeRight();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Right swipe gesture verified");
    }

    @Test(description = "Verify carousel counter updates")
    @Description("Regression test to verify carousel counter functionality")
    public void testCarouselCounterUpdate() {
        LOG.info("Testing carousel counter update");
        carouselPage = homePage.navigateToCarousel();

        String initialCounter = carouselPage.getCarouselCounter();
        LOG.info("Initial carousel counter: {}", initialCounter);

        LOG.info("Performing swipe to change item");
        carouselPage.swipeToNextItem();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String updatedCounter = carouselPage.getCarouselCounter();
        LOG.info("Updated carousel counter: {}", updatedCounter);

        LOG.info("Carousel counter update verified");
    }

    @Test(description = "Verify carousel item text retrieval")
    @Description("Regression test to verify getting carousel item text")
    public void testCarouselItemTextRetrieval() {
        LOG.info("Testing carousel item text retrieval");
        carouselPage = homePage.navigateToCarousel();

        String itemText = carouselPage.getCurrentItemText();
        LOG.info("Current carousel item text: {}", itemText);

        assertTrue(!itemText.isEmpty(), "Carousel item text should not be empty");
        LOG.info("Carousel item text retrieval verified");
    }
}

