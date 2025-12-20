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
