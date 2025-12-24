package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.SwipePage;
import com.demo.framework.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * Android implementation of Swipe Page
 */
@Slf4j
public class AndroidSwipePage extends BasePage implements SwipePage {

    private static final By SWIPE_SCREEN = AppiumBy.accessibilityId("Swipe-screen");
    private static final By HIDDEN_TEXT = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"You found me!!!\")");

    private final GestureUtils gesture;

    public AndroidSwipePage() {
        super();
        this.gesture = new GestureUtils(driver);
    }

    @Override
    public boolean isPageLoaded() {
        log.info("Checking if Android Swipe Page is loaded");
        try {
            wait.untilVisible(SWIPE_SCREEN);
            log.info("Android Swipe Page loaded successfully");
            return true;
        } catch (Exception e) {
            log.warn("Android Swipe Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void swipeToCard(String cardName) {
        log.info("Swiping to card: {} on Android", cardName);
        int maxSwipes = 10;
        for (int i = 0; i < maxSwipes; i++) {
            if (isCardDisplayed(cardName)) {
                log.info("Card '{}' found after {} swipes", cardName, i);
                return;
            }
            gesture.swipeLeft();
        }
        log.warn("Card '{}' not found after {} swipes", cardName, maxSwipes);
    }

    @Override
    public void scrollDownToFindHiddenElement() {
        log.info("Scrolling down to find hidden element on Android");
        int maxScrolls = 5;
        for (int i = 0; i < maxScrolls; i++) {
            if (isHiddenTextFound()) {
                log.info("Hidden element found after {} scrolls", i);
                return;
            }
            gesture.scrollDown();
        }
        log.warn("Hidden element not found after {} scrolls", maxScrolls);
    }

    @Override
    public void scrollToTop() {
        log.info("Scrolling to top on Android");
        int maxScrolls = 5;
        for (int i = 0; i < maxScrolls; i++) {
            gesture.scrollUp();
            if (isPageLoaded()) {
                log.info("Reached top after {} scrolls", i);
                return;
            }
        }
    }

    @Override
    public boolean isHiddenTextFound() {
        log.info("Checking if hidden element is found on Android");
        try {
            return actions.isDisplayed(HIDDEN_TEXT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isCardDisplayed(String cardName) {
        try {
            By cardLocator = AppiumBy.androidUIAutomator(
                    String.format("new UiSelector().textContains(\"%s\")", cardName));
            // Use quick check without wait for swipe loops
            return actions.isDisplayedQuick(cardLocator);
        } catch (Exception e) {
            return false;
        }
    }
}

