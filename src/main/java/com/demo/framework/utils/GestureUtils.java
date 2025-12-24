package com.demo.framework.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

/**
 * Utility class for gesture operations on mobile devices
 */
public class GestureUtils {

    private static final Logger LOG = LoggerFactory.getLogger(GestureUtils.class);
    private static final Duration SWIPE_DURATION = Duration.ofMillis(600);
    private final AppiumDriver driver;

    public GestureUtils(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * Swipe to the left
     */
    public void swipeLeft() {
        LOG.info("Swiping left");
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.getWidth() * 0.8);
        int endX = (int) (size.getWidth() * 0.2);
        int y = size.getHeight() / 2;

        performSwipe(startX, y, endX, y);
    }

    /**
     * Swipe to the right
     */
    public void swipeRight() {
        LOG.info("Swiping right");
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.getWidth() * 0.2);
        int endX = (int) (size.getWidth() * 0.8);
        int y = size.getHeight() / 2;

        performSwipe(startX, y, endX, y);
    }

    /**
     * Swipe up
     */
    public void swipeUp() {
        LOG.info("Swiping up ");
        Dimension size = driver.manage().window().getSize();
        int x = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.9);
        int endY = (int) (size.getHeight() * 0.2);

        performSwipe(x, startY, x, endY);
    }

    /**
     * Swipe down
     */
    public void swipeDown() {
        LOG.info("Swiping down");
        Dimension size = driver.manage().window().getSize();
        int x = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.2);
        int endY = (int) (size.getHeight() * 0.8);

        performSwipe(x, startY, x, endY);
    }

    /**
     * Small scroll down (gentle - 20% of screen)
     */
    public void scrollDownSmall() {
        LOG.info("Small scroll down");
        Dimension size = driver.manage().window().getSize();
        int x = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.5);
        int endY = (int) (size.getHeight() * 0.3);

        performSwipe(x, startY, x, endY);
    }

    /**
     * Small scroll up (gentle - 20% of screen)
     */
    public void scrollUpSmall() {
        LOG.info("Small scroll up");
        Dimension size = driver.manage().window().getSize();
        int x = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.3);
        int endY = (int) (size.getHeight() * 0.5);

        performSwipe(x, startY, x, endY);
    }

    /**
     * Perform swipe gesture using W3C Actions API
     */
    private void performSwipe(int startX, int startY, int endX, int endY) {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence sequence = new Sequence(finger, 0)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(finger.createPointerMove(SWIPE_DURATION, PointerInput.Origin.viewport(), endX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(sequence));
        } catch (Exception e) {
            LOG.warn("Error performing swipe gesture", e);
        }
    }

    /**
     * Scroll to element
     */
    public void scrollToElement(By locator) {
        LOG.info("Scrolling to element: {}", locator);
        try {
            driver.findElement(locator);
        } catch (Exception e) {
            LOG.debug("Element not visible, scrolling down");
            swipeUp();
        }
    }

    /**
     * Scroll down the page (swipe up to reveal content below)
     */
    public void scrollDown() {
        LOG.info("Scrolling down");
        swipeUp();
    }

    /**
     * Scroll up the page (swipe down to reveal content above)
     */
    public void scrollUp() {
        LOG.info("Scrolling up");
        swipeDown();
    }

    /**
     * Long press on element
     */
    public void longPress(By locator, int duration) {
        LOG.info("Long pressing on element for {} ms: {}", duration, locator);
        try {
            org.openqa.selenium.WebElement element = driver.findElement(locator);
            new Actions(driver)
                    .clickAndHold(element)
                    .pause(Duration.ofMillis(duration))
                    .release()
                    .perform();
            LOG.info("Long press completed");
        } catch (Exception e) {
            LOG.warn("Error performing long press", e);
        }
    }

    /**
     * Tap on coordinates
     */
    public void tap(int x, int y) {
        LOG.info("Tapping at coordinates ({}, {})", x, y);
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence sequence = new Sequence(finger, 0)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Arrays.asList(sequence));
            LOG.info("Tap completed");
        } catch (Exception e) {
            LOG.warn("Error performing tap", e);
        }
    }
}

