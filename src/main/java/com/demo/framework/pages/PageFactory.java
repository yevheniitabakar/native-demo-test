package com.demo.framework.pages;

import com.demo.framework.config.ConfigProvider;
import com.demo.framework.pages.android.*;
import com.demo.framework.pages.interfaces.*;
import com.demo.framework.pages.ios.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory for creating platform-specific Page Object instances.
 * Platform is resolved dynamically at runtime to support multi-platform test runs.
 */
@Slf4j
@UtilityClass
public class PageFactory {

    /**
     * Get current platform dynamically (not cached) to support runtime changes
     */
    private static String getPlatform() {
        return new ConfigProvider().getPlatform().toLowerCase();
    }

    public static HomePage homePage() {
        String platform = getPlatform();
        log.debug("Creating HomePage for platform: {}", platform);
        return isAndroid(platform) ? new AndroidHomePage() : new IOSHomePage();
    }

    public static LoginPage loginPage() {
        String platform = getPlatform();
        log.debug("Creating LoginPage for platform: {}", platform);
        return isAndroid(platform) ? new AndroidLoginPage() : new IOSLoginPage();
    }

    public static SwipePage swipePage() {
        String platform = getPlatform();
        log.debug("Creating SwipePage for platform: {}", platform);
        return isAndroid(platform) ? new AndroidSwipePage() : new IOSSwipePage();
    }

    public static WebViewPage webViewPage() {
        String platform = getPlatform();
        log.debug("Creating WebViewPage for platform: {}", platform);
        return isAndroid(platform) ? new AndroidWebViewPage() : new IOSWebViewPage();
    }

    public static DragPage dragPage() {
        String platform = getPlatform();
        log.debug("Creating DragPage for platform: {}", platform);
        return isAndroid(platform) ? new AndroidDragPage() : new IOSDragPage();
    }

    private static boolean isAndroid(String platform) {
        return "android".equalsIgnoreCase(platform);
    }
}

