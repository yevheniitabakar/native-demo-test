package com.demo.framework.pages.ios;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.DragPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;

/**
 * iOS implementation of Drag and Drop Page
 */
public class IOSDragPage extends BasePage implements DragPage {

    // iOS locators using Accessibility ID (name/label)
    private static final By DRAG_SCREEN = AppiumBy.accessibilityId("Drag-screen");
    private static final By DRAGGABLE_ELEMENT = AppiumBy.accessibilityId("drag-l1");
    private static final By DROP_ZONE = AppiumBy.accessibilityId("drop-l1");
    private static final By SUCCESS_MESSAGE = AppiumBy.accessibilityId("success-message");
    private static final By RESET_BUTTON = AppiumBy.accessibilityId("button-Retry");

    @Override
    public String getPageTitle() {
        return "Drag and Drop Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if iOS Drag Page is loaded");
        try {
            wait.untilVisible(DRAGGABLE_ELEMENT);
            LOG.info("iOS Drag Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("iOS Drag Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void dragElementToDropZone() {
        LOG.info("Dragging element to drop zone on iOS");
        try {
            WebElement draggable = wait.untilVisible(DRAGGABLE_ELEMENT);
            WebElement dropZone = wait.untilVisible(DROP_ZONE);

            int startX = draggable.getLocation().getX() + draggable.getSize().getWidth() / 2;
            int startY = draggable.getLocation().getY() + draggable.getSize().getHeight() / 2;
            int endX = dropZone.getLocation().getX() + dropZone.getSize().getWidth() / 2;
            int endY = dropZone.getLocation().getY() + dropZone.getSize().getHeight() / 2;

            performDragDrop(startX, startY, endX, endY);
        } catch (Exception e) {
            LOG.error("Error dragging element to drop zone on iOS", e);
            throw new RuntimeException("Failed to drag element", e);
        }
    }

    @Override
    public void dragElement(int sourceIndex, int targetIndex) {
        LOG.info("Dragging element from {} to {} on iOS", sourceIndex, targetIndex);
        dragElementToDropZone();
    }

    private void performDragDrop(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(sequence));
        LOG.info("Drag and drop gesture completed on iOS");
    }

    @Override
    public boolean isDropSuccessful() {
        LOG.info("Checking if drop was successful on iOS");
        try {
            return actions.isDisplayed(SUCCESS_MESSAGE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void resetDragDrop() {
        LOG.info("Resetting drag and drop on iOS");
        try {
            wait.untilVisible(RESET_BUTTON);
            actions.click(RESET_BUTTON);
        } catch (Exception e) {
            LOG.warn("Reset button not found: {}", e.getMessage());
        }
    }

    @Override
    public void dragAndReleaseElsewhere() {
        LOG.info("Dragging element and releasing elsewhere on iOS");
        try {
            WebElement draggable = wait.untilVisible(DRAGGABLE_ELEMENT);

            int startX = draggable.getLocation().getX() + draggable.getSize().getWidth() / 2;
            int startY = draggable.getLocation().getY() + draggable.getSize().getHeight() / 2;
            // Release at empty area (offset from original position)
            int endX = startX + 50;
            int endY = startY + 50;

            performDragDrop(startX, startY, endX, endY);
        } catch (Exception e) {
            LOG.error("Error during drag and release elsewhere", e);
        }
    }

    @Override
    public boolean isDraggableElementVisible() {
        LOG.info("Checking if draggable element is visible on iOS");
        try {
            return actions.isDisplayed(DRAGGABLE_ELEMENT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isDropZoneVisible() {
        LOG.info("Checking if drop zone is visible on iOS");
        try {
            return actions.isDisplayed(DROP_ZONE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSuccessMessage() {
        LOG.info("Getting success message on iOS");
        return actions.getText(SUCCESS_MESSAGE);
    }

    @Override
    public String getElementState() {
        LOG.info("Getting element state on iOS");
        try {
            WebElement draggable = driver.findElement(DRAGGABLE_ELEMENT);
            return String.format("x:%d,y:%d,visible:%s",
                    draggable.getLocation().getX(),
                    draggable.getLocation().getY(),
                    draggable.isDisplayed());
        } catch (Exception e) {
            return isDropSuccessful() ? "dropped" : "initial";
        }
    }
}
