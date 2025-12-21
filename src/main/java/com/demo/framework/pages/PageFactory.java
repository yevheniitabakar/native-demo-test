package com.demo.framework.pages;

import com.demo.framework.config.ConfigProvider;
import com.demo.framework.pages.android.*;
import com.demo.framework.pages.interfaces.*;
import com.demo.framework.pages.ios.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating platform-specific Page Object instances.
 * This is the ONLY place where platform branching (Android vs iOS) is allowed for Pages.
 */
public class PageFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PageFactory.class);
    private static final String PLATFORM = new ConfigProvider().getPlatform().toLowerCase();

    private PageFactory() {
        // Utility class - prevent instantiation
    }

    /**
     * Create HomePage instance based on current platform
     */
    public static HomePage homePage() {
        LOG.debug("Creating HomePage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidHomePage() : new IOSHomePage();
    }

    /**
     * Create LoginPage instance based on current platform
     */
    public static LoginPage loginPage() {
        LOG.debug("Creating LoginPage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidLoginPage() : new IOSLoginPage();
    }

    /**
     * Create SwipePage instance based on current platform
     */
    public static SwipePage swipePage() {
        LOG.debug("Creating SwipePage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidSwipePage() : new IOSSwipePage();
    }

    /**
     * Create WebViewPage instance based on current platform
     */
    public static WebViewPage webViewPage() {
        LOG.debug("Creating WebViewPage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidWebViewPage() : new IOSWebViewPage();
    }

    /**
     * Create DragPage instance based on current platform
     */
    public static DragPage dragPage() {
        LOG.debug("Creating DragPage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidDragPage() : new IOSDragPage();
    }

    /**
     * Check if current platform is Android
     */
    private static boolean isAndroid() {
        return "android".equalsIgnoreCase(PLATFORM);
    }
}

