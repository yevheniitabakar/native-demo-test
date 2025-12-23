package com.demo.framework.utils;

import com.demo.framework.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility for managing app lifecycle
 */
@Slf4j
@UtilityClass
public class AppUtils {

    public static void closeBrowser() {
        AppiumDriver driver = DriverManager.getDriver();

        if (driver instanceof AndroidDriver) {
            terminateApp(driver, "com.android.chrome");
        } else if (driver instanceof IOSDriver) {
            terminateApp(driver, "com.apple.mobilesafari");
        }
    }

    private static void terminateApp(AppiumDriver driver, String bundleId) {
        log.info("Terminating app: {}", bundleId);
        try {
            ((InteractsWithApps) driver).terminateApp(bundleId);
            log.info("App terminated: {}", bundleId);
        } catch (Exception e) {
            log.warn("Failed to terminate app {}: {}", bundleId, e.getMessage());
        }
    }
}

