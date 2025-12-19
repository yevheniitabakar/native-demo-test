package com.demo.framework.pages;

import com.demo.framework.utils.GestureUtils;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for Carousel/Swipe Interaction Screen
 * Contains elements and actions for carousel operations
 */
public class CarouselPage extends BasePage {

    private static final Logger LOG = LoggerFactory.getLogger(CarouselPage.class);

    // Locators
    private static final By CAROUSEL_TITLE = By.xpath("//android.widget.TextView[@text='Carousel']");
    private static final By CAROUSEL_ITEM = By.id("carousel_item");
    private static final By CAROUSEL_COUNTER = By.id("carousel_counter");
    private static final By CAROUSEL_CONTAINER = By.id("carousel_container");

    private final GestureUtils gesture;

    public CarouselPage() {
        super();
        this.gesture = new GestureUtils(driver);
    }

    @Override
    public String getPageTitle() {
        return "Carousel Page";
    }

    @Override
    public boolean isPageLoaded() {
        LOG.info("Checking if Carousel Page is loaded");
        try {
            wait.untilVisible(CAROUSEL_TITLE);
            LOG.info("Carousel Page loaded successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Carousel Page not loaded: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Swipe to the left
     */
    public void swipeLeft() {
        LOG.info("Swiping left on carousel");
        gesture.swipeLeft();
    }

    /**
     * Swipe to the right
     */
    public void swipeRight() {
        LOG.info("Swiping right on carousel");
        gesture.swipeRight();
    }

    /**
     * Swipe through carousel to next item
     */
    public void swipeToNextItem() {
        LOG.info("Swiping to next carousel item");
        gesture.swipeLeft();
    }

    /**
     * Swipe through carousel to previous item
     */
    public void swipeToPreviousItem() {
        LOG.info("Swiping to previous carousel item");
        gesture.swipeRight();
    }

    /**
     * Get current carousel item counter
     */
    public String getCarouselCounter() {
        LOG.info("Getting carousel counter");
        return actions.getText(CAROUSEL_COUNTER);
    }

    /**
     * Get current carousel item text
     */
    public String getCurrentItemText() {
        LOG.info("Getting current carousel item text");
        return actions.getText(CAROUSEL_ITEM);
    }

    /**
     * Perform multiple swipes through carousel
     */
    public void swipeThroughCarousel(int times) {
        LOG.info("Swiping through carousel {} times", times);
        for (int i = 0; i < times; i++) {
            swipeToNextItem();
            try {
                Thread.sleep(500); // Wait between swipes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.warn("Swipe interrupted", e);
            }
        }
    }

    /**
     * Verify carousel item is visible
     */
    public boolean isCarouselItemVisible() {
        LOG.info("Checking if carousel item is visible");
        return actions.isDisplayed(CAROUSEL_ITEM);
    }
}

