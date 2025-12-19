package com.demo.framework.drivers.device;

import java.util.List;

/**
 * Interface for device management
 * Allows for multiple implementations (ADB for Android, instruments for iOS, etc.)
 */
public interface IDeviceManager {

    /**
     * Get list of available devices
     */
    List<DeviceInfo> getAvailableDevices();

    /**
     * Get device by UDID
     */
    DeviceInfo getDeviceByUdid(String udid);

    /**
     * Start emulator/simulator for given device name
     */
    void startDevice(String deviceName);

    /**
     * Stop emulator/simulator for given device name
     */
    void stopDevice(String deviceName);

    /**
     * Check if device is connected
     */
    boolean isDeviceConnected(String udid);

    /**
     * Get platform type
     */
    String getPlatformType();
}

