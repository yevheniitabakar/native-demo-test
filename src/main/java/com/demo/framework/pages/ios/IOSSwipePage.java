package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.SwipePage;
import com.demo.framework.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * iOS implementation of Swipe Page
 */
public class IOSSwipePage extends BasePage implements SwipePage {

    // iOS locators using Accessibility ID (name/label)
    private static final By SWIPE_SCREEN = AppiumBy.accessibilityId("Swipe-screen");
    private static final By HIDDEN_ELEMENT = AppiumBy.accessibilityId("WebdriverIO logo");

    private final GestureUtils gesture;

    public IOSSwipePage() {
        super();
        this.gesture = new GestureUtils(driver);
    }

    @Override
    public String getPageTitle() {
        return "Swipe Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if iOS Swipe Page is loaded");
        try {
            wait.untilVisible(SWIPE_SCREEN);
            LOG.info("iOS Swipe Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("iOS Swipe Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void swipeLeft() {
        LOG.info("Swiping left on iOS");
        gesture.swipeLeft();
    }

    @Override
    public void swipeRight() {
        LOG.info("Swiping right on iOS");
        gesture.swipeRight();
    }

    @Override
    public void swipeToNextCard() {
        LOG.info("Swiping to next card on iOS");
        swipeLeft();
    }

    @Override
    public void swipeToPreviousCard() {
        LOG.info("Swiping to previous card on iOS");
        swipeRight();
    }

    @Override
    public void swipeToCard(String cardName) {
        LOG.info("Swiping to card: {} on iOS", cardName);
        int maxSwipes = 10;
        for (int i = 0; i < maxSwipes; i++) {
            if (isCardDisplayed(cardName)) {
                LOG.info("Card '{}' found after {} swipes", cardName, i);
                return;
            }
            swipeLeft();
        }
        LOG.warn("Card '{}' not found after {} swipes", cardName, maxSwipes);
    }

    @Override
    public void scrollDownToFindHiddenElement() {
        LOG.info("Scrolling down to find hidden element on iOS");
        int maxScrolls = 5;
        for (int i = 0; i < maxScrolls; i++) {
            if (isHiddenElementFound()) {
                LOG.info("Hidden element found after {} scrolls", i);
                return;
            }
            gesture.scrollDown();
        }
        LOG.warn("Hidden element not found after {} scrolls", maxScrolls);
    }

    @Override
    public void scrollToTop() {
        LOG.info("Scrolling to top on iOS");
        int maxScrolls = 5;
        for (int i = 0; i < maxScrolls; i++) {
            gesture.scrollUp();
            if (isPageLoaded()) {
                LOG.info("Reached top after {} scrolls", i);
                return;
            }
        }
    }

    @Override
    public boolean isCardDisplayed(String cardName) {
        LOG.info("Checking if card '{}' is displayed on iOS", cardName);
        try {
            By cardLocator = AppiumBy.iOSNsPredicateString(
                    String.format("name CONTAINS '%s' OR label CONTAINS '%s'", cardName, cardName));
            return actions.isDisplayed(cardLocator);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isHiddenElementFound() {
        LOG.info("Checking if hidden element is found on iOS");
        try {
            return actions.isDisplayed(HIDDEN_ELEMENT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getCurrentCardText() {
        LOG.info("Getting current card text on iOS");
        return "";
    }
}
