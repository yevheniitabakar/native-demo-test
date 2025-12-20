package com.demo.framework.drivers;

import com.demo.framework.config.AppiumConfig;
import com.demo.framework.exceptions.FrameworkException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidDriverProvider implements DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AndroidDriverProvider.class);

    @Override
    public AppiumDriver createDriver(AppiumConfig config) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(config.platformName())
                .setPlatformVersion(config.platformVersion())
                .setDeviceName(config.deviceName())
                .setAutomationName(config.automationName())
                .setApp(config.appPath())
                .setNewCommandTimeout(config.newCommandTimeout())
                .setFullReset(config.fullReset())
                .setNoReset(config.noReset());

        LOG.info("Starting Android driver with capabilities:");
        LOG.info("  Device: {}", config.deviceName());
        LOG.info("  Platform Version: {}", config.platformVersion());
        LOG.info("  Automation: {}", config.automationName());
        LOG.info("  App Path: {}", config.appPath());
        LOG.info("  Full Reset (uninstall app after session): {}", config.fullReset());
        LOG.info("  No Reset (skip reset between sessions): {}", config.noReset());
        LOG.info("  New Command Timeout: {}", config.newCommandTimeout());

        try {
            return new AndroidDriver(config.serverUrl().toURL(), options);
        } catch (Exception e) {
            LOG.error("Failed to start Android driver", e);
            throw new FrameworkException("Failed to start Android driver", e);
        }
    }
}
