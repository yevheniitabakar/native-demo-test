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
    public void navigateToDragAndDrop() {
        log.info("Navigating to Drag and Drop screen");
        homePage.clickDragDropLink();
    }

    @Step("Drag element to drop zone")
    public void dragElementToDropZone() {
        log.info("Dragging element to drop zone");
        dragPage.dragElementToDropZone();
    }

    @Step("Drag all elements to drop zones")
    public void dragAllElementsToDropZones() {
        log.info("Dragging all elements to drop zones");
        dragPage.dragElementsToDropZone();
    }

    @Step("Reset drag and drop")
    public void resetDragDrop() {
        log.info("Resetting drag and drop");
        dragPage.resetDragDrop();
    }

    @Step("Drag element and release elsewhere (not on target)")
    public void dragAndReleaseElsewhere() {
        log.info("Dragging element and releasing elsewhere");
        dragPage.dragAndReleaseElsewhere();
    }

    public boolean isDragPageLoaded() {
        return dragPage.isPageLoaded();
    }

    public boolean isElementDroppedSuccessfully() {
        return dragPage.isElementDroppedSuccessfully();
    }

    public boolean isCaptchaCompleted() {
        return dragPage.isCaptchaCompleted();
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

