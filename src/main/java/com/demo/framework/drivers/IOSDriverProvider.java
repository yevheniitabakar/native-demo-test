package com.demo.framework.drivers;

import com.demo.framework.config.AppiumConfig;
import com.demo.framework.exceptions.FrameworkException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOSDriverProvider implements DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(IOSDriverProvider.class);

    @Override
    public AppiumDriver createDriver(AppiumConfig config) {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName(config.platformName())
                .setPlatformVersion(config.platformVersion())
                .setDeviceName(config.deviceName())
                .setAutomationName(config.automationName())
                .setApp(config.appPath())
                .setNewCommandTimeout(config.newCommandTimeout())
                .setFullReset(config.fullReset())
                .setNoReset(config.noReset());
        
        // Set UDID if available - critical for reusing the same simulator
        if (config.udid() != null && !config.udid().isBlank()) {
            options.setUdid(config.udid());
            LOG.info("Using simulator UDID: {}", config.udid());
        }
        
        // Use prebuilt WebDriverAgent to avoid rebuilding each session
        if (config.usePrebuiltWDA()) {
            options.setCapability("appium:usePrebuiltWDA", true);
            LOG.info("Using prebuilt WebDriverAgent");
        }
        
        // Skip device initialization to prevent simulator reset
        if (config.skipDeviceInitialization()) {
            options.setCapability("appium:skipDeviceInitialization", true);
            LOG.info("Skipping device initialization");
        }
        
        // Additional iOS-specific optimizations to prevent simulator restart
        options.setCapability("appium:shouldTerminateApp", false);
        options.setCapability("appium:forceSimulatorSoftwareKeyboardPresence", false);

        LOG.info("Starting iOS driver with capabilities for device: {}", config.deviceName());
        LOG.info("Platform Version: {}, Automation: {}", config.platformVersion(), config.automationName());
        LOG.info("Full Reset: {}, No Reset: {}", config.fullReset(), config.noReset());
        try {
            return new IOSDriver(config.serverUrl().toURL(), options);
        } catch (Exception e) {
            throw new FrameworkException("Failed to start iOS driver", e);
        }
    }
}
