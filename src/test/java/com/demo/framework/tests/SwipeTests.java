package com.demo.framework.tests;

import com.demo.framework.flows.SwipeFlow;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test class for Swipe/Carousel functionality.
 * Uses SwipeFlow for business actions.
 * Contains only orchestration and assertions.
 */
@Feature("Gestures")
@Story("Swipe Actions")
public class SwipeTests extends BaseTest {

    private SwipeFlow swipeFlow;

    @BeforeMethod(alwaysRun = true)
    public void setUpSwipeFlow() {
        swipeFlow = new SwipeFlow();
    }

    @Test(groups = {"swipe", "smoke", "regression"},
          description = "TC_2.1: Verify swipe through carousel and scroll functionality")
    @Description("Navigate to Swipe tab, swipe to COMPATIBLE card, scroll down to find hidden image, scroll back to top")
    public void testSwipeThroughCarousel() {
        LOG.info("Starting TC_2.1: Swipe Through Carousel");

        // Step 1: Navigate to Swipe tab
        LOG.info("Step 1: Navigate to Swipe tab");
        swipeFlow.navigateToSwipe();
        assertTrue(swipeFlow.isSwipePageLoaded(), "Swipe page should be loaded");

        // Step 2: Swipe multiple times to reach the 'COMPATIBLE' card
        LOG.info("Step 2: Swipe to reach COMPATIBLE card");
        swipeFlow.swipeToCard("COMPATIBLE");
        assertTrue(swipeFlow.isCardDisplayed("COMPATIBLE"), "COMPATIBLE card should be displayed");

        // Step 3: Scroll the screen down to find a hidden image with text 'You found me!!!'
        LOG.info("Step 3: Scroll down to find hidden image");
        swipeFlow.scrollDownToFindHiddenElement();
        assertTrue(swipeFlow.isHiddenElementFound(), "Hidden element with 'You found me!!!' should be visible");

        // Step 4: Scroll the screen to the top
        LOG.info("Step 4: Scroll back to top");
        swipeFlow.scrollToTop();
        assertTrue(swipeFlow.isSwipePageLoaded(), "Should be back at the top of the page");
    }
}

