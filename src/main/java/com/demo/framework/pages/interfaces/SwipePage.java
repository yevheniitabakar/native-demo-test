package com.demo.framework.pages.interfaces;

/**
 * Interface for Swipe/Carousel Page
 * Defines UI contract for swipe gesture interactions
 */
public interface SwipePage {

    boolean isPageLoaded();

    void swipeLeft();

    void swipeRight();

    void swipeToNextCard();

    void swipeToPreviousCard();

    void swipeToCard(String cardName);

    void scrollDownToFindHiddenElement();

    void scrollToTop();

    boolean isCardDisplayed(String cardName);

    boolean isHiddenElementFound();

    String getCurrentCardText();
}

