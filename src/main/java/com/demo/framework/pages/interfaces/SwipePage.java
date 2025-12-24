package com.demo.framework.pages.interfaces;

/**
 * Interface for Swipe/Carousel Page
 */
public interface SwipePage {

    boolean isPageLoaded();

    void swipeToCard(String cardName);

    void scrollDownToFindHiddenElement();

    void scrollToTop();

    boolean isCardDisplayed(String cardName);

    boolean isHiddenTextFound();
}

