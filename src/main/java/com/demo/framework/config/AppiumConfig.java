package com.demo.framework.config;

import java.net.URI;
import java.time.Duration;

public record AppiumConfig(String platformName, String platformVersion, String deviceName, String automationName,
                           String appPath, URI serverUrl, Duration newCommandTimeout,
                           boolean fullReset, boolean noReset, String udid,
                           boolean usePrebuiltWDA, boolean skipDeviceInitialization) {

    @Override
    public String toString() {
        return "AppiumConfig{" +
                "platformName='" + platformName + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", automationName='" + automationName + '\'' +
                ", appPath='" + appPath + '\'' +
                ", serverUrl=" + serverUrl +
                ", newCommandTimeout=" + newCommandTimeout +
                ", fullReset=" + fullReset +
                ", noReset=" + noReset +
                ", udid='" + udid + '\'' +
                ", usePrebuiltWDA=" + usePrebuiltWDA +
                ", skipDeviceInitialization=" + skipDeviceInitialization +
                '}';
    }

}
