package com.demo.framework.pages.interfaces;

/**
 * Interface for Drag and Drop Page
 */
public interface DragPage {

    boolean isPageLoaded();

    void dragElementToDropZone();

    void dragElementsToDropZone();

    void dragAndReleaseElsewhere();

    boolean isElementDroppedSuccessfully();

    boolean isCaptchaCompleted();

    void resetDragDrop();

    boolean isDraggableElementVisible();

    boolean isDropZoneVisible();


    String getElementState();
}

