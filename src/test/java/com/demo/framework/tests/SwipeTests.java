package com.demo.framework.tests;

import com.demo.framework.flows.SwipeFlow;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.demo.framework.utils.AllureStepUtils.allureStep;
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

    @Test(groups = {"swipe", "regression"},
          description = "TC_2.1: Verify swipe through carousel and scroll functionality")
    @Description("Navigate to Swipe tab, swipe to COMPATIBLE card, scroll down to find hidden image, scroll back to top")
    public void testSwipeThroughCarousel() {
        allureStep("Step 1: Navigate to Swipe tab");
        swipeFlow.navigateToSwipe();
        assertTrue(swipeFlow.isSwipePageLoaded(), "Swipe page should be loaded");

        allureStep("Step 2: Swipe multiple times to reach the 'COMPATIBLE' card");
        swipeFlow.swipeToCard("COMPATIBLE");
        assertTrue(swipeFlow.isCardDisplayed("COMPATIBLE"), "COMPATIBLE card should be displayed");

        allureStep("Step 3: Scroll the screen down to find a hidden image with text 'You found me!!!'");
        swipeFlow.scrollDownToFindHiddenElement();
        assertTrue(swipeFlow.isHiddenElementFound(), "Hidden element with 'You found me!!!' should be visible");

        allureStep("Step 4: Scroll the screen to the top");
        swipeFlow.scrollToTop();
        assertTrue(swipeFlow.isSwipePageLoaded(), "Should be back at the top of the page");
    }
}

