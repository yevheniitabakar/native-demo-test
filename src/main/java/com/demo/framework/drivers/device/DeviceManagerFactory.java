package com.demo.framework.drivers.device;

import com.demo.framework.exceptions.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Device Manager Factory
 * Provides device manager implementations based on platform
 */
public class DeviceManagerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceManagerFactory.class);

    private DeviceManagerFactory() {
    }

    /**
     * Get device manager for specified platform
     */
    public static IDeviceManager getDeviceManager(String platformName) {
        LOG.info("Creating device manager for platform: {}", platformName);

        if (platformName == null) {
            throw new FrameworkException("Platform name must not be null");
        }

        switch (platformName.toUpperCase()) {
            case "ANDROID":
                return new AndroidDeviceManager();
            case "IOS":
                return new IOSDeviceManager();
            default:
                throw new FrameworkException("Unsupported platform for device management: " + platformName);
        }
    }
}

