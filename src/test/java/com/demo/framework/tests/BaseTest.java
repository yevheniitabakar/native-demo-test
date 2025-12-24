package com.demo.framework.tests;

import com.demo.framework.config.AppiumConfig;
import com.demo.framework.config.ConfigProvider;
import com.demo.framework.drivers.DriverManager;
import com.demo.framework.drivers.device.DeviceManagerFactory;
import com.demo.framework.drivers.device.IDeviceManager;
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
     * Load configuration and ensure device is ready before suite runs
     */
    @BeforeSuite(alwaysRun = true)
    public void loadConfig() {
        LOG.info("Loading framework configuration");
        ConfigProvider provider = new ConfigProvider();
        appiumConfig = provider.getAppiumConfig();
        LOG.info("Framework configuration loaded: {}", appiumConfig);
        
        // Ensure device is booted before tests start
        ensureDeviceReady(appiumConfig);
    }
    
    /**
     * Ensure device/emulator/simulator is booted and ready for testing.
     * If UDID is configured, verifies that specific device is booted.
     * If no UDID, attempts to start the configured device by name.
     */
    private void ensureDeviceReady(AppiumConfig config) {
        String platform = config.platformName();
        String deviceName = config.deviceName();
        String udid = config.udid();
        
        LOG.info("Ensuring {} device is ready: {} (UDID: {})", platform, deviceName, udid);
        
        try {
            IDeviceManager deviceManager = DeviceManagerFactory.getDeviceManager(platform);
            
            // If UDID is specified, check if that device is booted
            if (udid != null && !udid.isBlank()) {
                if (deviceManager.isDeviceBooted(udid)) {
                    LOG.info("Device {} is already booted and ready", udid);
                    return;
                }
                LOG.warn("Device with UDID {} is not booted. Tests may fail if device is not available.", udid);
                return;
            }
            
            // No specific UDID - check if any device of the platform is booted
            boolean anyDeviceBooted = deviceManager.getAvailableDevices().stream()
                    .anyMatch(device -> deviceManager.isDeviceBooted(device.getUdid()));
            
            if (anyDeviceBooted) {
                LOG.info("A {} device is already booted and ready", platform);
                return;
            }
            
            // No device booted - attempt to start one
            LOG.info("No {} device is booted. Attempting to start: {}", platform, deviceName);
            deviceManager.startDevice(deviceName);
            LOG.info("Device {} started successfully", deviceName);
            
        } catch (Exception e) {
            // Log warning but don't fail - let the test fail with better error if device isn't available
            LOG.warn("Could not verify/start device (may already be managed externally): {}", e.getMessage());
            LOG.info("Continuing with test execution - Appium will report if device is unavailable");
        }
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
            String platform = appiumConfig.platformName();
            String appIdentifier = getBundleIdForPlatform(platform);
            
            if (appIdentifier != null && driver != null) {
                LOG.info("Terminating app: {} on {}", appIdentifier, platform);
                Map<String, Object> params = new HashMap<>();
                
                // iOS uses "bundleId", Android uses "appId"
                if ("IOS".equalsIgnoreCase(platform)) {
                    params.put("bundleId", appIdentifier);
                } else {
                    params.put("appId", appIdentifier);
                }
                
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

