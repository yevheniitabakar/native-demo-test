package com.demo.framework.tests;

import com.demo.framework.config.AppiumConfig;
import com.demo.framework.config.ConfigProvider;
import com.demo.framework.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

/**
 * Base test class for all test classes
 * Implements setup/teardown for driver and configuration
 * Follows Template Method pattern
 */
public abstract class BaseTest {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);
    private static AppiumConfig appiumConfig;

    /**
     * Load configuration before suite runs
     */
    @BeforeSuite(alwaysRun = true)
    public void loadConfig() {
        LOG.info("Loading framework configuration");
        ConfigProvider provider = new ConfigProvider();
        appiumConfig = provider.getAppiumConfig();
        LOG.info("Framework configuration loaded: {}", appiumConfig);
    }

    /**
     * Start driver before each test
     */
    @BeforeMethod(alwaysRun = true)
    public void startDriver() {
        LOG.info("Starting Appium driver");

        if (appiumConfig == null) {
            throw new IllegalStateException("Configuration not loaded. BeforeSuite should have been executed.");
        }

        LOG.info("Creating driver for device: {}", appiumConfig.deviceName());
        DriverManager.createDriver(appiumConfig);
        LOG.info("Driver started successfully");
    }

    /**
     * Quit driver after each test
     * Also terminates the app to ensure clean state for next test
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        LOG.info("Tearing down driver");
        
        // Terminate app before quitting driver to ensure clean state for next test
        terminateApp();
        
        DriverManager.quitDriver();
        LOG.info("Driver teardown completed");
    }

    /**
     * Terminate the app to reset its state
     * This is faster than fullReset but ensures clean app state between tests
     */
    private void terminateApp() {
        if (appiumConfig == null) {
            return;
        }
        
        try {
            AppiumDriver driver = DriverManager.getDriver();
            String bundleId = getBundleIdForPlatform(appiumConfig.platformName());
            
            if (bundleId != null && driver != null) {
                LOG.info("Terminating app: {}", bundleId);
                Map<String, Object> params = new HashMap<>();
                params.put("bundleId", bundleId);
                driver.executeScript("mobile: terminateApp", params);
                LOG.info("App terminated successfully");
            }
        } catch (Exception e) {
            LOG.warn("Could not terminate app (may already be closed): {}", e.getMessage());
        }
    }

    /**
     * Cleanup after entire test suite completes
     * Uninstalls the app from device/simulator
     */
    @AfterSuite(alwaysRun = true)
    public void cleanupAfterSuite() {
        LOG.info("Suite completed - cleaning up app from device");
        
        if (appiumConfig == null) {
            LOG.warn("Configuration not available for cleanup");
            return;
        }
        
        String platform = appiumConfig.platformName();
        String bundleId = getBundleIdForPlatform(platform);
        
        if (bundleId != null) {
            uninstallApp(platform, bundleId, appiumConfig.udid());
        }
    }

    /**
     * Get bundle ID / package name based on platform
     */
    private String getBundleIdForPlatform(String platform) {
        if ("IOS".equalsIgnoreCase(platform)) {
            // iOS bundle ID from app path or hardcoded
            return "org.reactjs.native.example.wdiodemoapp";
        } else if ("ANDROID".equalsIgnoreCase(platform)) {
            // Android package name
            return "com.wdiodemoapp";
        }
        return null;
    }

    /**
     * Uninstall app from device/simulator
     */
    private void uninstallApp(String platform, String bundleId, String udid) {
        try {
            ProcessBuilder pb;
            if ("IOS".equalsIgnoreCase(platform)) {
                if (udid != null && !udid.isBlank()) {
                    pb = new ProcessBuilder("xcrun", "simctl", "uninstall", udid, bundleId);
                } else {
                    pb = new ProcessBuilder("xcrun", "simctl", "uninstall", "booted", bundleId);
                }
            } else {
                pb = new ProcessBuilder("adb", "uninstall", bundleId);
            }
            
            LOG.info("Uninstalling app: {} from {}", bundleId, platform);
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                LOG.info("App uninstalled successfully");
            } else {
                LOG.warn("App uninstall returned exit code: {}", exitCode);
            }
        } catch (Exception e) {
            LOG.error("Failed to uninstall app: {}", e.getMessage());
        }
    }

    /**
     * Get driver instance for test
     */
    protected AppiumDriver driver() {
        return DriverManager.getDriver();
    }

    /**
     * Hook for subclasses to add custom setup
     */
    protected void customSetup() {
        LOG.debug("No custom setup defined");
    }

    /**
     * Hook for subclasses to add custom teardown
     */
    protected void customTeardown() {
        LOG.debug("No custom teardown defined");
    }
}

