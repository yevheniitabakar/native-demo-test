package com.demo.framework.tests.smoke;

import com.demo.framework.pages.DragDropPage;
import com.demo.framework.pages.HomePageUI;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("Advanced Interactions")
@Story("Drag and Drop Functionality")
public class DragDropTest extends BaseTest {

    private HomePageUI homePage;
    private DragDropPage dragDropPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LOG.info("Initializing Home Page for Drag and Drop tests");
        homePage = new HomePageUI();
        assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }

    @Test(testName = "TC_4.1_DragDropSuccess", description = "Test Case 4.1: Drag and Drop - Successful Interaction")
    @Description("Verify user can successfully drag and drop an element")
    public void testDragDropSuccessfulInteraction() {
        LOG.info("Starting Test Case 4.1: Drag and Drop - Successful Interaction");

        navigateToDragDropPage();

        LOG.info("Verifying draggable element is visible");
        assertTrue(dragDropPage.isDraggableElementVisible(), "Draggable element should be visible");

        LOG.info("Verifying drop zone is visible");
        assertTrue(dragDropPage.isDropZoneVisible(), "Drop zone should be visible");

        String draggableText = dragDropPage.getDraggableElementText();
        LOG.info("Draggable element text: {}", draggableText);

        LOG.info("Performing drag and drop operation");
        dragDropPage.dragElementToDropZone();

        try {
            Thread.sleep(1500); // Wait for drop animation and callback
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying drop was successful");
        assertTrue(dragDropPage.isDropSuccessful(), "Drop should be successful");

        String successMsg = dragDropPage.getSuccessMessage();
        LOG.info("Success message: {}", successMsg);

        dragDropPage.captureScreenshot();
        LOG.info("Test Case 4.1 completed successfully");
    }

    @Test(testName = "TC_4.2_DragDropElementStateVerification", description = "Test Case 4.2: Drag and Drop - Element State Verification")
    @Description("Verify element states before and after drag and drop operation")
    public void testDragDropElementStateVerification() {
        LOG.info("Starting Test Case 4.2: Drag and Drop - Element State Verification");

        navigateToDragDropPage();

        LOG.info("STEP 1: Verify initial state");
        assertTrue(dragDropPage.isDraggableElementVisible(), "Draggable element should be visible initially");
        assertTrue(dragDropPage.isDropZoneVisible(), "Drop zone should be visible");

        String initialDraggableText = dragDropPage.getDraggableElementText();
        LOG.info("Initial draggable element text: {}", initialDraggableText);

        LOG.info("Initial drop not successful (before drag)");
        boolean initialDropSuccess = dragDropPage.isDropSuccessful();
        LOG.info("Initial drop success state: {}", initialDropSuccess);

        LOG.info("STEP 2: Perform drag and drop");
        dragDropPage.dragElementToDropZone();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("STEP 3: Verify state after drop");
        LOG.info("Verifying drop was successful");
        boolean dropSuccessAfter = dragDropPage.isDropSuccessful();
        assertTrue(dropSuccessAfter, "Drop should be successful after drag operation");
        LOG.info("Drop success state after operation: {}", dropSuccessAfter);

        dragDropPage.captureScreenshot();

        LOG.info("STEP 4: Reset and verify reset state");
        dragDropPage.clickResetButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying element is reset");
        assertTrue(dragDropPage.isDraggableElementVisible(), "Draggable element should be visible after reset");

        dragDropPage.captureScreenshot();
        LOG.info("Test Case 4.2 completed successfully");
    }

    @Test(testName = "TC_4.2_DragDropMultipleInteractions", description = "Test Case 4.2: Drag and Drop - Multiple Interactions")
    @Description("Verify drag and drop works correctly with multiple consecutive operations")
    public void testDragDropMultipleInteractions() {
        LOG.info("Starting Test Case 4.2 Extended: Multiple Drag and Drop Interactions");

        navigateToDragDropPage();

        LOG.info("First drag and drop operation");
        assertTrue(dragDropPage.isDraggableElementVisible(), "Draggable element should be visible");
        dragDropPage.dragElementToDropZone();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying first drop was successful");
        assertTrue(dragDropPage.isDropSuccessful(), "First drop should be successful");

        LOG.info("Resetting for second operation");
        dragDropPage.clickResetButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Second drag and drop operation");
        assertTrue(dragDropPage.isDraggableElementVisible(), "Draggable element should be visible after reset");
        dragDropPage.dragElementToDropZone();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying second drop was successful");
        assertTrue(dragDropPage.isDropSuccessful(), "Second drop should be successful");

        dragDropPage.captureScreenshot();
        LOG.info("Test Case 4.2 Extended completed successfully");
    }

    @Step("Navigate to Drag and Drop Page")
    private void navigateToDragDropPage() {
        LOG.info("Navigating to drag and drop page from home page");
        dragDropPage = homePage.navigateToDragDrop();
        assertTrue(dragDropPage.isPageLoaded(), "Drag and drop page should be loaded");
    }
}

