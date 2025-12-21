package com.demo.framework.flows;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.DragPage;
import com.demo.framework.pages.interfaces.HomePage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flow class for Drag and Drop functionality.
 * Orchestrates drag and drop gesture actions.
 * Platform-agnostic - no platform checks allowed here.
 */
public class DragAndDropFlow {

    private static final Logger LOG = LoggerFactory.getLogger(DragAndDropFlow.class);

    private final HomePage homePage;
    private final DragPage dragPage;

    public DragAndDropFlow() {
        this.homePage = PageFactory.homePage();
        this.dragPage = PageFactory.dragPage();
    }

    @Step("Navigate to Drag and Drop screen")
    public DragAndDropFlow navigateToDragAndDrop() {
        LOG.info("Navigating to Drag and Drop screen");
        homePage.clickDragDropLink();
        return this;
    }

    @Step("Drag element to drop zone")
    public DragAndDropFlow dragElementToDropZone() {
        LOG.info("Dragging element to drop zone");
        dragPage.dragElementToDropZone();
        return this;
    }

    @Step("Drag element from position {sourceIndex} to position {targetIndex}")
    public DragAndDropFlow dragElement(int sourceIndex, int targetIndex) {
        LOG.info("Dragging element from {} to {}", sourceIndex, targetIndex);
        dragPage.dragElement(sourceIndex, targetIndex);
        return this;
    }

    @Step("Reset drag and drop")
    public DragAndDropFlow resetDragDrop() {
        LOG.info("Resetting drag and drop");
        dragPage.resetDragDrop();
        return this;
    }

    public boolean isDragPageLoaded() {
        return dragPage.isPageLoaded();
    }

    public boolean isDropSuccessful() {
        return dragPage.isDropSuccessful();
    }

    public boolean isDraggableElementVisible() {
        return dragPage.isDraggableElementVisible();
    }

    public boolean isDropZoneVisible() {
        return dragPage.isDropZoneVisible();
    }

    public String getSuccessMessage() {
        return dragPage.getSuccessMessage();
    }
}

