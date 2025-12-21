package com.demo.framework.pages.android;

import com.demo.framework.pages.BasePage;
import com.demo.framework.pages.interfaces.DragPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;

/**
 * Android implementation of Drag and Drop Page
 */
public class AndroidDragPage extends BasePage implements DragPage {

    // Android-specific locators
    private static final By DRAG_SCREEN = By.xpath("//android.widget.TextView[@text='Drag and Drop']");
    private static final By DRAGGABLE_ELEMENT = By.xpath("//android.view.ViewGroup[@content-desc='drag-l1']");
    private static final By DROP_ZONE = By.xpath("//android.view.ViewGroup[@content-desc='drop-l1']");
    private static final By SUCCESS_MESSAGE = By.xpath("//android.widget.TextView[@text='Congratulations']");
    private static final By RESET_BUTTON = By.xpath("//android.widget.Button[@text='Reset']");

    @Override
    public String getPageTitle() {
        return "Drag and Drop Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Android Drag Page is loaded");
        try {
            wait.untilVisible(DRAGGABLE_ELEMENT);
            LOG.info("Android Drag Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Android Drag Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void dragElementToDropZone() {
        LOG.info("Dragging element to drop zone on Android");
        try {
            WebElement draggable = wait.untilVisible(DRAGGABLE_ELEMENT);
            WebElement dropZone = wait.untilVisible(DROP_ZONE);

            int startX = draggable.getLocation().getX() + draggable.getSize().getWidth() / 2;
            int startY = draggable.getLocation().getY() + draggable.getSize().getHeight() / 2;
            int endX = dropZone.getLocation().getX() + dropZone.getSize().getWidth() / 2;
            int endY = dropZone.getLocation().getY() + dropZone.getSize().getHeight() / 2;

            performDragDrop(startX, startY, endX, endY);
        } catch (Exception e) {
            LOG.error("Error dragging element to drop zone on Android", e);
            throw new RuntimeException("Failed to drag element", e);
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

        driver.perform(Arrays.asList(sequence));
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
        if (actions.isDisplayed(RESET_BUTTON)) {
            actions.click(RESET_BUTTON);
        }
    }

    @Override
    public boolean isDraggableElementVisible() {
        LOG.info("Checking if draggable element is visible on Android");
        return actions.isDisplayed(DRAGGABLE_ELEMENT);
    }

    @Override
    public boolean isDropZoneVisible() {
        LOG.info("Checking if drop zone is visible on Android");
        return actions.isDisplayed(DROP_ZONE);
    }

    @Override
    public String getSuccessMessage() {
        LOG.info("Getting success message on Android");
        return actions.getText(SUCCESS_MESSAGE);
    }
}

