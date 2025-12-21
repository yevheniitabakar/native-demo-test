package com.demo.framework.flows;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.HomePage;
import com.demo.framework.pages.interfaces.SwipePage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flow class for Swipe/Carousel functionality.
 * Orchestrates swipe gesture actions.
 * Platform-agnostic - no platform checks allowed here.
 */
public class SwipeFlow {

    private static final Logger LOG = LoggerFactory.getLogger(SwipeFlow.class);

    private final HomePage homePage;
    private final SwipePage swipePage;

    public SwipeFlow() {
        this.homePage = PageFactory.homePage();
        this.swipePage = PageFactory.swipePage();
    }

    @Step("Navigate to Swipe screen")
    public SwipeFlow navigateToSwipe() {
        LOG.info("Navigating to Swipe screen");
        homePage.clickSwipeLink();
        return this;
    }

    @Step("Swipe to card: {cardName}")
    public SwipeFlow swipeToCard(String cardName) {
        LOG.info("Swiping to card: {}", cardName);
        swipePage.swipeToCard(cardName);
        return this;
    }

    @Step("Scroll down to find hidden element")
    public SwipeFlow scrollDownToFindHiddenElement() {
        LOG.info("Scrolling down to find hidden element");
        swipePage.scrollDownToFindHiddenElement();
        return this;
    }

    @Step("Scroll to top of the page")
    public SwipeFlow scrollToTop() {
        LOG.info("Scrolling to top of the page");
        swipePage.scrollToTop();
        return this;
    }

    @Step("Swipe to next card")
    public SwipeFlow swipeToNextCard() {
        LOG.info("Swiping to next card");
        swipePage.swipeToNextCard();
        return this;
    }

    @Step("Swipe to previous card")
    public SwipeFlow swipeToPreviousCard() {
        LOG.info("Swiping to previous card");
        swipePage.swipeToPreviousCard();
        return this;
    }

    @Step("Swipe left")
    public SwipeFlow swipeLeft() {
        LOG.info("Swiping left");
        swipePage.swipeLeft();
        return this;
    }

    @Step("Swipe right")
    public SwipeFlow swipeRight() {
        LOG.info("Swiping right");
        swipePage.swipeRight();
        return this;
    }

    public boolean isSwipePageLoaded() {
        return swipePage.isPageLoaded();
    }

    public boolean isCardDisplayed(String cardName) {
        return swipePage.isCardDisplayed(cardName);
    }

    public boolean isHiddenElementFound() {
        return swipePage.isHiddenElementFound();
    }

    public String getCurrentCardText() {
        return swipePage.getCurrentCardText();
    }
}

