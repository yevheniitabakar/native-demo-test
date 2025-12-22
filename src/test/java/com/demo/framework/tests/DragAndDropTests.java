package com.demo.framework.tests;

import com.demo.framework.flows.DragAndDropFlow;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.demo.framework.utils.AllureStepUtils.allureStep;
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
        allureStep("Step 1: Navigate to Drag and Drop screen");
        dragAndDropFlow.navigateToDragAndDrop();
        assertTrue(dragAndDropFlow.isDragPageLoaded(), "Drag and Drop page should be loaded");

        allureStep("Step 2: Verify draggable element and drop zone are visible");
        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should be visible");
        assertTrue(dragAndDropFlow.isDropZoneVisible(), "Drop zone should be visible");

        allureStep("Step 3: Drag element to drop zone");
        dragAndDropFlow.dragElementToDropZone();

        allureStep("Step 4: Verify drag and drop was successful");
        assertTrue(dragAndDropFlow.isDropSuccessful(), "Drag and drop should be successful");
    }

    @Test(groups = {"drag", "regression"},
          description = "TC_4.3: Verify drag and drop element state verification")
    @Description("Verify element states during drag operations: initial state, cancelled drag, successful drag, and reset")
    public void testDragAndDropElementStateVerification() {
        allureStep("Step 1: Navigate to Drag tab");
        dragAndDropFlow.navigateToDragAndDrop();
        assertTrue(dragAndDropFlow.isDragPageLoaded(), "Drag and Drop page should be loaded");

        allureStep("Step 2: Verify initial state of draggable element (position, visibility)");
        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should be visible");
        String initialState = dragAndDropFlow.getElementState();

        allureStep("Step 3: Perform drag action but release elsewhere (don't drop in target)");
        dragAndDropFlow.dragAndReleaseElsewhere();

        allureStep("Step 4: Verify element returns to original position");
        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should still be visible");
        assertFalse(dragAndDropFlow.isDropSuccessful(), "Drop should not be successful when released elsewhere");

        allureStep("Step 5: Perform successful drag and drop");
        dragAndDropFlow.dragElementToDropZone();
        assertTrue(dragAndDropFlow.isDropSuccessful(), "Drag and drop should be successful");

        allureStep("Step 6: Verify final state differs from initial state");
        String finalState = dragAndDropFlow.getElementState();
        assertNotEquals(initialState, finalState, "Final state should differ from initial state");

        allureStep("Step 7: Tap 'Retry' button");
        dragAndDropFlow.resetDragDrop();

        allureStep("Step 8: Verify initial state is reset");
        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should be visible after reset");
        assertFalse(dragAndDropFlow.isDropSuccessful(), "Success state should be reset");
    }
}

