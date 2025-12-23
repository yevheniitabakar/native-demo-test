package com.demo.framework.flows;

import com.demo.framework.pages.PageFactory;
import com.demo.framework.pages.interfaces.DragPage;
import com.demo.framework.pages.interfaces.HomePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

/**
 * Flow class for Drag and Drop functionality.
 * Orchestrates drag and drop gesture actions.
 */
@Slf4j
public class DragAndDropFlow {

    private final HomePage homePage;
    private final DragPage dragPage;

    public DragAndDropFlow() {
        this.homePage = PageFactory.homePage();
        this.dragPage = PageFactory.dragPage();
    }

    @Step("Navigate to Drag and Drop screen")
    public DragAndDropFlow navigateToDragAndDrop() {
        log.info("Navigating to Drag and Drop screen");
        homePage.clickDragDropLink();
        return this;
    }

    @Step("Drag element to drop zone")
    public DragAndDropFlow dragElementToDropZone() {
        log.info("Dragging element to drop zone");
        dragPage.dragElementToDropZone();
        return this;
    }

    @Step("Reset drag and drop")
    public DragAndDropFlow resetDragDrop() {
        log.info("Resetting drag and drop");
        dragPage.resetDragDrop();
        return this;
    }

    @Step("Drag element and release elsewhere (not on target)")
    public DragAndDropFlow dragAndReleaseElsewhere() {
        log.info("Dragging element and releasing elsewhere");
        dragPage.dragAndReleaseElsewhere();
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


    public String getElementState() {
        return dragPage.getElementState();
    }
}

