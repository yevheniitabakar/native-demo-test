package com.demo.framework.drivers;

import com.demo.framework.config.AppiumConfig;
import com.demo.framework.exceptions.FrameworkException;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages Appium driver instances using ThreadLocal pattern
 * Ensures thread-safe driver management for parallel test execution
 */
public class DriverManager {

    private static final Logger LOG = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Create and initialize Appium driver
     */
    public static void createDriver(AppiumConfig config) {
        LOG.info("Creating driver for platform: {}", config.platformName());

        if (DRIVER.get() != null) {
            LOG.warn("Driver already exists for current thread, quitting previous instance");
            quitDriver();
        }

        DriverFactory factory = getFactory(config.platformName());
        AppiumDriver driver = factory.createDriver(config);
        DRIVER.set(driver);
        LOG.info("Driver successfully created and stored in ThreadLocal");
    }

    /**
     * Get driver instance for current thread
     */
    public static AppiumDriver getDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new FrameworkException("Driver is not initialized for the current thread.");
        }
        return driver;
    }

    /**
     * Check if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return DRIVER.get() != null;
    }

    /**
     * Quit and release driver resources
     */
    public static void quitDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                LOG.info("Quitting driver and releasing resources");
                LOG.info("Note: If fullReset=true was set, app will be uninstalled from device");
                driver.quit();
                LOG.info("Driver quit successfully");
            } catch (Exception e) {
                LOG.error("Error while quitting driver", e);
            } finally {
                DRIVER.remove();
                LOG.debug("ThreadLocal cleared");
            }
        } else {
            LOG.debug("No driver to quit for current thread");
        }
    }

    /**
     * Get appropriate driver factory based on platform
     */
    private static DriverFactory getFactory(String platformName) {
        if (platformName == null) {
            throw new FrameworkException("Platform name must not be null");
        }

        switch (platformName.toUpperCase()) {
            case "ANDROID":
                LOG.debug("Creating Android driver factory");
                return new AndroidDriverProvider();
            case "IOS":
                LOG.debug("Creating iOS driver factory");
                return new IOSDriverProvider();
            default:
                throw new FrameworkException("Unsupported platform: " + platformName);
        }
    }
}
