package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.DragPage;
import io.appium.java_client.AppiumBy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * Android implementation of Drag and Drop Page
 */
@Slf4j
public class AndroidDragPage extends BasePage implements DragPage {

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
    public boolean isPageLoaded() {
        log.info("Checking if Android Drag Page is loaded");
        try {
            wait.untilVisible(AppiumBy.accessibilityId("drag-l1"));
            return true;
        } catch (Exception e) {
            log.warn("Android Drag Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void dragElementToDropZone() {
        log.info("Dragging all elements to drop zones on Android");
        for (Map.Entry<String, String> pair : DRAG_DROP_PAIRS.entrySet()) {
            WebElement draggable = wait.untilVisible(AppiumBy.accessibilityId(pair.getKey()));
            WebElement dropZone = wait.untilVisible(AppiumBy.accessibilityId(pair.getValue()));
            performDragDrop(
                    draggable.getLocation().getX() + draggable.getSize().getWidth() / 2,
                    draggable.getLocation().getY() + draggable.getSize().getHeight() / 2,
                    dropZone.getLocation().getX() + dropZone.getSize().getWidth() / 2,
                    dropZone.getLocation().getY() + dropZone.getSize().getHeight() / 2
            );
        }
    }

    @Override
    public void dragAndReleaseElsewhere() {
        log.info("Dragging element and releasing elsewhere on Android");
        WebElement draggable = wait.untilVisible(AppiumBy.accessibilityId("drag-l1"));
        int startX = draggable.getLocation().getX() + draggable.getSize().getWidth() / 2;
        int startY = draggable.getLocation().getY() + draggable.getSize().getHeight() / 2;
        performDragDrop(startX, startY, startX + 50, startY + 50);
    }

    @Override
    public boolean isDropSuccessful() {
        try {
            return actions.isDisplayed(SUCCESS_MESSAGE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void resetDragDrop() {
        log.info("Resetting drag and drop on Android");
        try {
            wait.untilVisible(RESET_BUTTON);
            actions.click(RESET_BUTTON);
        } catch (Exception e) {
            log.warn("Reset button not found: {}", e.getMessage());
        }
    }

    @Override
    public boolean isDraggableElementVisible() {
        try {
            return actions.isDisplayed(AppiumBy.accessibilityId("drag-l1"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isDropZoneVisible() {
        try {
            return actions.isDisplayed(AppiumBy.accessibilityId("drop-l1"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getElementState() {
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

    private void performDragDrop(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
    }
}
