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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * iOS implementation of Drag and Drop Page
 */
@Slf4j
public class IOSDragPage extends BasePage implements DragPage {

    private static final String CLASS_CHAIN = "**/XCUIElementTypeOther[`name == \"%s\"`]/XCUIElementTypeOther";
    private static final Map<String, String> DRAG_DROP_PAIRS = new LinkedHashMap<>() {{
        put("drag-l1", "drop-l1");
        put("drag-l2", "drop-l2");
        put("drag-l3", "drop-l3");
        put("drag-c1", "drop-c1");
        put("drag-c2", "drop-c2");
        put("drag-c3", "drop-c3");
        put("drag-r1", "drop-r1");
        put("drag-r2", "drop-r2");
        put("drag-r3", "drop-r3");
    }};
    private static final By DRAGGABLE_ELEMENT = AppiumBy.iOSClassChain(String.format(CLASS_CHAIN, "drag-l1"));
    private static final By DROP_ZONE = AppiumBy.iOSClassChain(String.format(CLASS_CHAIN, "drop-l1"));
    private static final By SUCCESS_MESSAGE = AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText"
            + "[`name == \"You made it, click retry if you want to try it again.\"`]");
    private static final By RESET_BUTTON = AppiumBy.accessibilityId("renew");

    @Override
    public boolean isPageLoaded() {
        log.info("Checking if iOS Drag Page is loaded");
        try {
            wait.untilVisible(RESET_BUTTON);
            return true;
        } catch (Exception e) {
            log.warn("iOS Drag Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void dragElementToDropZone() {
        log.info("Dragging single element to drop zone on iOS");
        Map.Entry<String, String> pair = DRAG_DROP_PAIRS.entrySet().iterator().next();
        performDragDropForPairs(pair);
    }

    @Override
    public void dragElementsToDropZone() {
        log.info("Dragging all elements to drop zones on iOS");
        for (Map.Entry<String, String> pair : DRAG_DROP_PAIRS.entrySet()) {
            performDragDropForPairs(pair);
        }
    }

    private void performDragDropForPairs(Map.Entry<String, String> pair) {
        WebElement draggable = wait.untilVisible(AppiumBy.iOSClassChain(String.format(CLASS_CHAIN, pair.getKey())));
        WebElement dropZone = wait.untilVisible(AppiumBy.iOSClassChain(String.format(CLASS_CHAIN, pair.getValue())));
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
    public boolean isElementDroppedSuccessfully() {
        log.info("Checking if single element was dropped successfully");
        
        try {
            // Give a moment for UI to update after drop
            Thread.sleep(300);
            
            // After successful drop, the draggable element should no longer be at the drag position
            // It either disappears or moves to the drop zone position
            WebElement draggable = driver.findElement(DRAGGABLE_ELEMENT);
            WebElement dropZone = driver.findElement(DROP_ZONE);
            
            // Get current positions
            int draggableX = draggable.getLocation().getX();
            int draggableY = draggable.getLocation().getY();
            int dropZoneX = dropZone.getLocation().getX();
            int dropZoneY = dropZone.getLocation().getY();
            
            // If draggable is now at/near drop zone position, drop was successful
            // Use a tolerance of ~50px to account for slight positioning differences
            int tolerance = 50;
            boolean positionsMatch = Math.abs(draggableX - dropZoneX) < tolerance 
                    && Math.abs(draggableY - dropZoneY) < tolerance;
            
            log.info("Draggable pos: ({}, {}), DropZone pos: ({}, {}), Match: {}", 
                    draggableX, draggableY, dropZoneX, dropZoneY, positionsMatch);
            
            return positionsMatch;
            
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // If draggable element is no longer found, it was successfully dropped
            log.info("Draggable element no longer found - drop successful");
            return true;
        } catch (Exception e) {
            log.warn("Error checking drop status: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isCaptchaCompleted() {
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
            String result = String.format("x:%d,y:%d,visible:%s",
                    draggable.getLocation().getX(),
                    draggable.getLocation().getY(),
                    draggable.isDisplayed());
            log.info("Element state is: {}", result);
            return result;
        } catch (Exception e) {
            return isCaptchaCompleted() ? "dropped" : "initial";
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
