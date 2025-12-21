package com.demo.framework.pages.interfaces;

/**
 * Interface for Drag and Drop Page
 * Defines UI contract for drag and drop interactions
 */
public interface DragPage {

    boolean isPageLoaded();

    void dragElementToDropZone();

    void dragElement(int sourceIndex, int targetIndex);

    boolean isDropSuccessful();

    void resetDragDrop();

    boolean isDraggableElementVisible();

    boolean isDropZoneVisible();

    String getSuccessMessage();
}

