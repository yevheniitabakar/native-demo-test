package com.demo.framework.utils;

import com.demo.framework.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for managing app lifecycle and navigation
 */
public class AppUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AppUtils.class);

    private AppUtils() {
    }

    /**
     * Close external browser (Chrome on Android, Safari on iOS)
     */
    public static void closeBrowser() {
        AppiumDriver driver = DriverManager.getDriver();

        if (driver instanceof AndroidDriver) {
            terminateApp(driver, "com.android.chrome");
        } else if (driver instanceof IOSDriver) {
            terminateApp(driver, "com.apple.mobilesafari");
        }
    }

    private static void terminateApp(AppiumDriver driver, String bundleId) {
        LOG.info("Terminating app: {}", bundleId);
        try {
            ((InteractsWithApps) driver).terminateApp(bundleId);
            LOG.info("App terminated: {}", bundleId);
        } catch (Exception e) {
            LOG.warn("Failed to terminate app {}: {}", bundleId, e.getMessage());
        }
    }
}

