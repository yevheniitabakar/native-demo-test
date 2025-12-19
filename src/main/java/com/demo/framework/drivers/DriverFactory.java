package com.demo.framework.drivers;

import com.demo.framework.config.AppiumConfig;
import io.appium.java_client.AppiumDriver;

public interface DriverFactory {
    AppiumDriver createDriver(AppiumConfig config);
}
