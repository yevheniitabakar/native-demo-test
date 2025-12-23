package com.demo.framework.pages.ios;

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

/**
 * iOS implementation of Drag and Drop Page
 */
@Slf4j
public class IOSDragPage extends BasePage implements DragPage {

    private static final By DRAGGABLE_ELEMENT = AppiumBy.accessibilityId("drag-l1");
    private static final By DROP_ZONE = AppiumBy.accessibilityId("drop-l1");
    private static final By SUCCESS_MESSAGE = AppiumBy.accessibilityId("success-message");
    private static final By RESET_BUTTON = AppiumBy.accessibilityId("button-Retry");

    @Override
    public boolean isPageLoaded() {
        log.info("Checking if iOS Drag Page is loaded");
        try {
            wait.untilVisible(DRAGGABLE_ELEMENT);
            return true;
        } catch (Exception e) {
            log.warn("iOS Drag Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void dragElementToDropZone() {
        log.info("Dragging element to drop zone on iOS");
        WebElement draggable = wait.untilVisible(DRAGGABLE_ELEMENT);
        WebElement dropZone = wait.untilVisible(DROP_ZONE);
        performDragDrop(
                draggable.getLocation().getX() + draggable.getSize().getWidth() / 2,
                draggable.getLocation().getY() + draggable.getSize().getHeight() / 2,
                dropZone.getLocation().getX() + dropZone.getSize().getWidth() / 2,
                dropZone.getLocation().getY() + dropZone.getSize().getHeight() / 2
        );
    }

    @Override
    public void dragAndReleaseElsewhere() {
        log.info("Dragging element and releasing elsewhere on iOS");
        WebElement draggable = wait.untilVisible(DRAGGABLE_ELEMENT);
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
        log.info("Resetting drag and drop on iOS");
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
            return actions.isDisplayed(DRAGGABLE_ELEMENT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isDropZoneVisible() {
        try {
            return actions.isDisplayed(DROP_ZONE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getElementState() {
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
