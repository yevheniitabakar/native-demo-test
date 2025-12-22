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
          description = "TC_4.2: Verify element state after drag and drop")
    @Description("Verify element state changes correctly after drag and drop")
    public void testDragAndDropElementStateVerification() {
        allureStep("Step 1: Navigate to Drag and Drop screen");
        dragAndDropFlow.navigateToDragAndDrop();
        assertTrue(dragAndDropFlow.isDragPageLoaded(), "Drag and Drop page should be loaded");

        allureStep("Step 2: Verify initial state - draggable element is visible");
        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should be visible initially");

        allureStep("Step 3: Drag element to drop zone");
        dragAndDropFlow.dragElementToDropZone();

        allureStep("Step 4: Verify state after drop - success message displayed");
        assertTrue(dragAndDropFlow.isDropSuccessful(), "Drop should be successful");
        assertNotNull(dragAndDropFlow.getSuccessMessage(), "Success message should be displayed");
    }

    @Test(groups = {"drag", "regression"},
          description = "TC_4.3: Verify drag and drop reset functionality")
    @Description("Verify drag and drop can be reset after successful drop")
    public void testDragAndDropReset() {
        allureStep("Step 1: Navigate to Drag and Drop screen");
        dragAndDropFlow.navigateToDragAndDrop();
        assertTrue(dragAndDropFlow.isDragPageLoaded(), "Drag and Drop page should be loaded");

        allureStep("Step 2: Perform initial drag and drop");
        dragAndDropFlow.dragElementToDropZone();
        assertTrue(dragAndDropFlow.isDropSuccessful(), "Initial drag and drop should be successful");

        allureStep("Step 3: Reset drag and drop");
        dragAndDropFlow.resetDragDrop();

        allureStep("Step 4: Verify reset - draggable element is visible again");
        assertTrue(dragAndDropFlow.isDraggableElementVisible(), "Draggable element should be visible after reset");
    }
}

