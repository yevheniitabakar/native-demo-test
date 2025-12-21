package com.demo.framework.tests;

import com.demo.framework.flows.DragAndDropFlow;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test class for Drag and Drop functionality.
 * Uses DragAndDropFlow for business actions.
 * Contains only orchestration and assertions.
 */
@Feature("Gestures")
@Story("Drag and Drop")
public class DragAndDropTests extends BaseTest {

    private DragAndDropFlow dragAndDropFlow;

    @BeforeMethod(alwaysRun = true)
    public void setUpDragAndDropFlow() {
        dragAndDropFlow = new DragAndDropFlow();
    }

    @Test(groups = {"drag", "regression"},
          description = "TC_4.1: Verify successful drag and drop interaction")
    @Description("Verify user can drag an element to the drop zone successfully")
    public void testDragAndDropSuccessfulInteraction() {
        LOG.info("Starting TC_4.1: Drag and Drop - Successful Interaction");

        dragAndDropFlow.navigateToDragAndDrop();
        assertTrue(dragAndDropFlow.isDragPageLoaded(), "Drag and Drop page should be loaded");

        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should be visible");
        assertTrue(dragAndDropFlow.isDropZoneVisible(), "Drop zone should be visible");

        dragAndDropFlow.dragElementToDropZone();

        assertTrue(dragAndDropFlow.isDropSuccessful(), "Drag and drop should be successful");
    }

    @Test(groups = {"drag", "regression"},
          description = "TC_4.2: Verify element state after drag and drop")
    @Description("Verify element state changes correctly after drag and drop")
    public void testDragAndDropElementStateVerification() {
        LOG.info("Starting TC_4.2: Drag and Drop - Element State Verification");

        dragAndDropFlow.navigateToDragAndDrop();
        assertTrue(dragAndDropFlow.isDragPageLoaded(), "Drag and Drop page should be loaded");

        // Verify initial state
        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should be visible initially");

        dragAndDropFlow.dragElementToDropZone();

        // Verify state after drop
        assertTrue(dragAndDropFlow.isDropSuccessful(), "Drop should be successful");
        assertNotNull(dragAndDropFlow.getSuccessMessage(), "Success message should be displayed");
    }

    @Test(groups = {"drag", "regression"},
          description = "TC_4.3: Verify drag and drop reset functionality")
    @Description("Verify drag and drop can be reset after successful drop")
    public void testDragAndDropReset() {
        LOG.info("Starting TC_4.3: Drag and Drop - Reset");

        dragAndDropFlow.navigateToDragAndDrop();
        assertTrue(dragAndDropFlow.isDragPageLoaded(), "Drag and Drop page should be loaded");

        dragAndDropFlow.dragElementToDropZone();
        assertTrue(dragAndDropFlow.isDropSuccessful(), "Initial drag and drop should be successful");

        dragAndDropFlow.resetDragDrop();

        // Verify reset worked - elements should be back to initial state
        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should be visible after reset");
    }
}

