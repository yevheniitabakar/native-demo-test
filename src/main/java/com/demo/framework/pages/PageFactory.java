package com.demo.framework.pages;

import com.demo.framework.config.ConfigProvider;
import com.demo.framework.pages.android.*;
import com.demo.framework.pages.interfaces.*;
import com.demo.framework.pages.ios.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory for creating platform-specific Page Object instances.
 */
@Slf4j
@UtilityClass
public class PageFactory {

    private static final String PLATFORM = new ConfigProvider().getPlatform().toLowerCase();

    public static HomePage homePage() {
        log.debug("Creating HomePage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidHomePage() : new IOSHomePage();
    }

    public static LoginPage loginPage() {
        log.debug("Creating LoginPage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidLoginPage() : new IOSLoginPage();
    }

    public static SwipePage swipePage() {
        log.debug("Creating SwipePage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidSwipePage() : new IOSSwipePage();
    }

    public static WebViewPage webViewPage() {
        log.debug("Creating WebViewPage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidWebViewPage() : new IOSWebViewPage();
    }

    public static DragPage dragPage() {
        log.debug("Creating DragPage for platform: {}", PLATFORM);
        return isAndroid() ? new AndroidDragPage() : new IOSDragPage();
    }

    private static boolean isAndroid() {
        return "android".equalsIgnoreCase(PLATFORM);
    }
}

