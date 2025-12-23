package com.demo.framework.pages.interfaces;

/**
 * Interface for Drag and Drop Page
 */
public interface DragPage {

    boolean isPageLoaded();

    void dragElementToDropZone();

    void dragAndReleaseElsewhere();

    boolean isDropSuccessful();

    void resetDragDrop();

    boolean isDraggableElementVisible();

    boolean isDropZoneVisible();


    String getElementState();
}

