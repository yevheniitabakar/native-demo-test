package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.SwipePage;
import com.demo.framework.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.Map;

/**
 * iOS implementation of Swipe Page
 */
@Slf4j
public class IOSSwipePage extends BasePage implements SwipePage {

    private static final By SWIPE_SCREEN = AppiumBy.accessibilityId("Swipe-screen");
    private static final By CAROUSEL = AppiumBy.accessibilityId("Carousel");
    private static final By HIDDEN_TEXT = AppiumBy.iOSClassChain(
            "**/XCUIElementTypeStaticText[`name == \"You found me!!!\"`]");
    private final GestureUtils gesture;

    public IOSSwipePage() {
        super();
        this.gesture = new GestureUtils(driver);
    }

    @Override
    public boolean isPageLoaded() {
        log.info("Checking if iOS Swipe Page is loaded");
        try {
            wait.untilVisible(SWIPE_SCREEN);
            log.info("iOS Swipe Page loaded successfully");
            return true;
        } catch (Exception e) {
            log.warn("iOS Swipe Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void swipeToCard(String cardName) {
        log.info("Swiping to card: {} on iOS", cardName);
        int maxSwipes = 10;
        for (int i = 0; i < maxSwipes; i++) {
            if (isCardDisplayed(cardName)) {
                log.info("Card '{}' found after {} swipes", cardName, i);
                return;
            }
            swipeLeftOnCarousel();
        }
        log.warn("Card '{}' not found after {} swipes", cardName, maxSwipes);
    }

    /**
     * Swipe left on carousel using iOS-specific mobile: swipe command
     */
    private void swipeLeftOnCarousel() {
        try {
            RemoteWebElement carousel = (RemoteWebElement) driver.findElement(CAROUSEL);
            ((IOSDriver) driver).executeScript("mobile: swipe", Map.of(
                    "elementId", carousel.getId(),
                    "direction", "left",
                    "velocity", 1500
            ));
            log.info("iOS swipe left executed on carousel");
        } catch (Exception e) {
            log.warn("iOS mobile:swipe failed, falling back to gesture: {}", e.getMessage());
            gesture.swipeLeft();
        }
    }

    @Override
    public void scrollDownToFindHiddenElement() {
        log.info("Scrolling down to find hidden element on iOS");
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
        log.info("Scrolling to top on iOS");
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
        log.info("Checking if hidden element is found on iOS");
        try {
            return actions.isDisplayed(HIDDEN_TEXT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isCardDisplayed(String cardName) {
        try {
            // Look for the card title specifically - the card header text that shows the card name
            By cardLocator = AppiumBy.iOSClassChain(
                    String.format("**/XCUIElementTypeStaticText[`name == \"%s\"`]", cardName));
            // Use quick check without wait for swipe loops
            return actions.isDisplayedQuick(cardLocator);
        } catch (Exception e) {
            return false;
        }
    }
}
