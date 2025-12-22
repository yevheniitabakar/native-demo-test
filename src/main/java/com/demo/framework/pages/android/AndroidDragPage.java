package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.DragPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Android implementation of Drag and Drop Page
 */
public class AndroidDragPage extends BasePage implements DragPage {

    // Android locators using Accessibility ID (content-desc)
    private static final By DRAG_SCREEN = AppiumBy.accessibilityId("Drag-screen");
    // Map of draggable elements to their corresponding drop zones
    private static final Map<String, String> DRAG_DROP_PAIRS = Map.of(
            "drag-l1", "drop-l1",
            "drag-l2", "drop-l2",
            "drag-l3", "drop-l3",
            "drag-c1", "drop-c1",
            "drag-c2", "drop-c2",
            "drag-c3", "drop-c3",
            "drag-r1", "drop-r1",
            "drag-r2", "drop-r2",
            "drag-r3", "drop-r3"
    );
    private static final By SUCCESS_MESSAGE = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"You made it, click retry if you want to try it again.\")");
    private static final By RESET_BUTTON = AppiumBy.accessibilityId("button-Retry");

    @Override
    public String getPageTitle() {
        return "Drag and Drop Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Android Drag Page is loaded");
        try {
            wait.untilVisible(new AppiumBy.ByAccessibilityId("drag-l1"));
            LOG.info("Android Drag Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Android Drag Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void dragElementToDropZone() {
        LOG.info("Dragging all elements to drop zones on Android");
        try {
            for (Map.Entry<String, String> pair : DRAG_DROP_PAIRS.entrySet()) {
                String dragId = pair.getKey();
                String dropId = pair.getValue();

                LOG.info("Dragging {} to {}", dragId, dropId);

                By draggableLocator = AppiumBy.accessibilityId(dragId);
                By dropZoneLocator = AppiumBy.accessibilityId(dropId);

                WebElement draggable = wait.untilVisible(draggableLocator);
                WebElement dropZone = wait.untilVisible(dropZoneLocator);

                int startX = draggable.getLocation().getX() + draggable.getSize().getWidth() / 2;
                int startY = draggable.getLocation().getY() + draggable.getSize().getHeight() / 2;
                int endX = dropZone.getLocation().getX() + dropZone.getSize().getWidth() / 2;
                int endY = dropZone.getLocation().getY() + dropZone.getSize().getHeight() / 2;

                performDragDrop(startX, startY, endX, endY);
            }
        } catch (Exception e) {
            LOG.error("Error dragging elements to drop zones on Android", e);
            throw new RuntimeException("Failed to drag elements", e);
        }
    }

    @Override
    public void dragElement(int sourceIndex, int targetIndex) {
        LOG.info("Dragging element from {} to {} on Android", sourceIndex, targetIndex);
        // TODO: Implement multi-element drag based on actual app structure
        dragElementToDropZone();
    }

    private void performDragDrop(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(sequence));
        LOG.info("Drag and drop gesture completed on Android");
    }

    @Override
    public boolean isDropSuccessful() {
        LOG.info("Checking if drop was successful on Android");
        try {
            return actions.isDisplayed(SUCCESS_MESSAGE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void resetDragDrop() {
        LOG.info("Resetting drag and drop on Android");
        try {
            wait.untilVisible(RESET_BUTTON);
            actions.click(RESET_BUTTON);
        } catch (Exception e) {
            LOG.warn("Reset button not found: {}", e.getMessage());
        }
    }

    @Override
    public void dragAndReleaseElsewhere() {
        LOG.info("Dragging element and releasing elsewhere on Android");
        try {
            By draggableLocator = AppiumBy.accessibilityId("drag-l1");
            WebElement draggable = wait.untilVisible(draggableLocator);

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
        LOG.info("Checking if draggable element is visible on Android");
        try {
            return actions.isDisplayed(AppiumBy.accessibilityId("drag-l1"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isDropZoneVisible() {
        LOG.info("Checking if drop zone is visible on Android");
        try {
            return actions.isDisplayed(AppiumBy.accessibilityId("drop-l1"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSuccessMessage() {
        LOG.info("Getting success message on Android");
        return actions.getText(SUCCESS_MESSAGE);
    }

    @Override
    public String getElementState() {
        LOG.info("Getting element state on Android");
        try {
            WebElement draggable = driver.findElement(AppiumBy.accessibilityId("drag-l1"));
            return String.format("x:%d,y:%d,visible:%s",
                    draggable.getLocation().getX(),
                    draggable.getLocation().getY(),
                    draggable.isDisplayed());
        } catch (Exception e) {
            return isDropSuccessful() ? "dropped" : "initial";
        }
    }
}
