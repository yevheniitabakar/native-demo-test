package com.demo.framework.drivers.device;

import java.util.Objects;

/**
 * Device information class
 * Represents device capabilities and properties
 */
public class DeviceInfo {

    private final String deviceName;
    private final String platformName;
    private final String platformVersion;
    private final String udid;
    private final Boolean isEmulator;

    public DeviceInfo(String deviceName, String platformName, String platformVersion,
                      String udid, Boolean isEmulator) {
        this.deviceName = deviceName;
        this.platformName = platformName;
        this.platformVersion = platformVersion;
        this.udid = udid;
        this.isEmulator = isEmulator;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getUdid() {
        return udid;
    }

    public Boolean isEmulator() {
        return isEmulator;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "deviceName='" + deviceName + '\'' +
                ", platformName='" + platformName + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", udid='" + udid + '\'' +
                ", isEmulator=" + isEmulator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceInfo that = (DeviceInfo) o;
        return Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(platformName, that.platformName) &&
                Objects.equals(platformVersion, that.platformVersion) &&
                Objects.equals(udid, that.udid) &&
                Objects.equals(isEmulator, that.isEmulator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceName, platformName, platformVersion, udid, isEmulator);
    }
}

