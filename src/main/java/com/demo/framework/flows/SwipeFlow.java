package com.demo.framework.flows;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.HomePage;
import com.demo.framework.pages.interfaces.SwipePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Flow class for Swipe/Carousel functionality.
 * Orchestrates swipe gesture actions.
 */
@Slf4j
public class SwipeFlow {

    private final HomePage homePage;
    private final SwipePage swipePage;

    public SwipeFlow() {
        this.homePage = PageFactory.homePage();
        this.swipePage = PageFactory.swipePage();
    }

    @Step("Navigate to Swipe screen")
    public SwipeFlow navigateToSwipe() {
        log.info("Navigating to Swipe screen");
        homePage.clickSwipeLink();
        return this;
    }

    @Step("Swipe to card: {cardName}")
    public SwipeFlow swipeToCard(String cardName) {
        log.info("Swiping to card: {}", cardName);
        swipePage.swipeToCard(cardName);
        return this;
    }

    @Step("Scroll down to find hidden element")
    public SwipeFlow scrollDownToFindHiddenElement() {
        log.info("Scrolling down to find hidden element");
        swipePage.scrollDownToFindHiddenElement();
        return this;
    }

    @Step("Scroll to top of the page")
    public SwipeFlow scrollToTop() {
        log.info("Scrolling to top of the page");
        swipePage.scrollToTop();
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
}

