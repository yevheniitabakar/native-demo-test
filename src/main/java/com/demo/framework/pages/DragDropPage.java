package com.demo.framework.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;

/**
 * Page Object for Drag and Drop Screen
 * Contains elements and actions for drag and drop interactions
 */
public class DragDropPage extends BasePage {

    private static final Logger LOG = LoggerFactory.getLogger(DragDropPage.class);

    // Locators
    private static final By DRAGDROP_TITLE = By.xpath("//android.widget.TextView[@text='Drag and Drop']");
    private static final By DRAGGABLE_ELEMENT = By.id("draggable_element");
    private static final By DROP_ZONE = By.id("drop_zone");
    private static final By DROP_ZONE_CONTAINER = By.id("drop_container");
    private static final By SUCCESS_MESSAGE = By.id("drop_success_message");
    private static final By RESET_BUTTON = By.id("reset_button");

    @Override
    public String getPageTitle() {
        return "Drag and Drop Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Drag and Drop Page is loaded");
        try {
            wait.untilVisible(DRAGDROP_TITLE);
            LOG.info("Drag and Drop Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Drag and Drop Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Drag element to drop zone
     */
    public void dragElementToDropZone() {
        LOG.info("Dragging element to drop zone");
        try {
            WebElement draggable = wait.untilVisible(DRAGGABLE_ELEMENT);
            WebElement dropZone = wait.untilVisible(DROP_ZONE);

            int startX = draggable.getLocation().getX() + draggable.getSize().getWidth() / 2;
            int startY = draggable.getLocation().getY() + draggable.getSize().getHeight() / 2;
            int endX = dropZone.getLocation().getX() + dropZone.getSize().getWidth() / 2;
            int endY = dropZone.getLocation().getY() + dropZone.getSize().getHeight() / 2;

            performDragDrop(startX, startY, endX, endY);
        } catch (Exception e) {
            LOG.error("Error dragging element to drop zone", e);
            throw new RuntimeException("Failed to drag element", e);
        }
    }

    /**
     * Perform drag and drop gesture using W3C Actions API
     */
    private void performDragDrop(int startX, int startY, int endX, int endY) {
        LOG.debug("Performing drag from ({}, {}) to ({}, {})", startX, startY, endX, endY);
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence sequence = new Sequence(finger, 0)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Arrays.asList(sequence));
            LOG.info("Drag and drop gesture completed");
        } catch (Exception e) {
            LOG.error("Error performing drag and drop", e);
            throw new RuntimeException("Drag and drop failed", e);
        }
    }

    /**
     * Check if drop was successful
     */
    public boolean isDropSuccessful() {
        LOG.info("Checking if drop was successful");
        try {
            return actions.isDisplayed(SUCCESS_MESSAGE);
        } catch (Exception e) {
            LOG.debug("Success message not displayed");
            return false;
        }
    }

    /**
     * Get success message text
     */
    public String getSuccessMessage() {
        LOG.info("Getting success message");
        return actions.getText(SUCCESS_MESSAGE);
    }

    /**
     * Click reset button to reset drag and drop state
     */
    public void clickResetButton() {
        LOG.info("Clicking reset button");
        actions.click(RESET_BUTTON);
    }

    /**
     * Verify draggable element is in original position
     */
    public boolean isDraggableElementVisible() {
        LOG.info("Checking if draggable element is visible");
        return actions.isDisplayed(DRAGGABLE_ELEMENT);
    }

    /**
     * Verify drop zone is visible
     */
    public boolean isDropZoneVisible() {
        LOG.info("Checking if drop zone is visible");
        return actions.isDisplayed(DROP_ZONE_CONTAINER);
    }

    /**
     * Get draggable element text/label
     */
    public String getDraggableElementText() {
        LOG.info("Getting draggable element text");
        return actions.getText(DRAGGABLE_ELEMENT);
    }
}

