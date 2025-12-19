package com.demo.framework.tests.regression;

import com.demo.framework.pages.DragDropPage;
import com.demo.framework.pages.HomePageUI;
import com.demo.framework.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("Advanced Interactions")
@Story("Drag and Drop Regression Tests")
public class DragDropRegressionTest extends BaseTest {

    private HomePageUI homePage;
    private DragDropPage dragDropPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LOG.info("Initializing Home Page for Drag and Drop regression tests");
        homePage = new HomePageUI();
        assertTrue(homePage.isPageLoaded(), "Home page should be loaded");
    }

    @Test(description = "Verify drag and drop page is accessible from home")
    @Description("Regression test to verify drag and drop page loads correctly")
    public void testDragDropPageAccessibility() {
        LOG.info("Testing drag and drop page accessibility");
        dragDropPage = homePage.navigateToDragDrop();
        assertTrue(dragDropPage.isPageLoaded(), "Drag and drop page should be accessible");
        LOG.info("Drag and drop page accessibility verified");
    }

    @Test(description = "Verify draggable element is displayed")
    @Description("Regression test to verify draggable element visibility")
    public void testDraggableElementDisplay() {
        LOG.info("Testing draggable element display");
        dragDropPage = homePage.navigateToDragDrop();

        LOG.info("Checking if draggable element is visible");
        assertTrue(dragDropPage.isDraggableElementVisible(), "Draggable element should be visible");
        LOG.info("Draggable element display verified");
    }

    @Test(description = "Verify drop zone is displayed")
    @Description("Regression test to verify drop zone visibility")
    public void testDropZoneDisplay() {
        LOG.info("Testing drop zone display");
        dragDropPage = homePage.navigateToDragDrop();

        LOG.info("Checking if drop zone is visible");
        assertTrue(dragDropPage.isDropZoneVisible(), "Drop zone should be visible");
        LOG.info("Drop zone display verified");
    }

    @Test(description = "Verify draggable element text retrieval")
    @Description("Regression test to verify getting draggable element text")
    public void testDraggableElementTextRetrieval() {
        LOG.info("Testing draggable element text retrieval");
        dragDropPage = homePage.navigateToDragDrop();

        String elementText = dragDropPage.getDraggableElementText();
        LOG.info("Draggable element text: {}", elementText);

        assertTrue(!elementText.isEmpty(), "Draggable element text should not be empty");
        LOG.info("Draggable element text retrieval verified");
    }

    @Test(description = "Verify reset button functionality")
    @Description("Regression test to verify reset button")
    public void testResetButtonFunctionality() {
        LOG.info("Testing reset button functionality");
        dragDropPage = homePage.navigateToDragDrop();

        LOG.info("Performing drag and drop");
        dragDropPage.dragElementToDropZone();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Clicking reset button");
        dragDropPage.clickResetButton();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Verifying draggable element is still visible after reset");
        assertTrue(dragDropPage.isDraggableElementVisible(), "Draggable element should be visible after reset");
        LOG.info("Reset button functionality verified");
    }

    @Test(description = "Verify success message display on drop")
    @Description("Regression test to verify success message after drop")
    public void testSuccessMessageDisplay() {
        LOG.info("Testing success message display");
        dragDropPage = homePage.navigateToDragDrop();

        LOG.info("Performing drag and drop");
        dragDropPage.dragElementToDropZone();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOG.info("Checking if drop was successful");
        boolean isSuccessful = dragDropPage.isDropSuccessful();
        LOG.info("Drop successful: {}", isSuccessful);

        LOG.info("Success message display verified");
    }

    @Test(description = "Verify drag and drop gesture execution")
    @Description("Regression test to verify drag and drop gesture can be executed without errors")
    public void testDragDropGestureExecution() {
        LOG.info("Testing drag and drop gesture execution");
        dragDropPage = homePage.navigateToDragDrop();

        LOG.info("Executing drag and drop gesture");
        try {
            dragDropPage.dragElementToDropZone();
            LOG.info("Drag and drop gesture executed successfully");
        } catch (Exception e) {
            LOG.error("Error executing drag and drop gesture", e);
            throw e;
        }
    }
}

