package com.demo.framework.drivers.device;

import com.demo.framework.exceptions.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Android Device Manager implementation
 * Manages Android emulators and physical devices using ADB
 */
public class AndroidDeviceManager implements IDeviceManager {

    private static final Logger LOG = LoggerFactory.getLogger(AndroidDeviceManager.class);
    private static final String ADB_COMMAND = "adb";
    private static final String EMULATOR_COMMAND = "emulator";
    private static final String PLATFORM_TYPE = "Android";

    @Override
    public List<DeviceInfo> getAvailableDevices() {
        LOG.info("Getting available Android devices");
        List<DeviceInfo> devices = new ArrayList<>();
        try {
            String output = executeCommand(ADB_COMMAND, "devices", "-l");
            String[] lines = output.split("\n");

            for (String line : lines) {
                if (line.trim().isEmpty() || line.contains("List of attached")) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length > 0) {
                    String udid = parts[0];
                    String status = parts.length > 1 ? parts[1] : "unknown";

                    if ("device".equals(status)) {
                        DeviceInfo info = new DeviceInfo(
                                udid,
                                PLATFORM_TYPE,
                                getDeviceProperty(udid, "ro.build.version.release"),
                                udid,
                                isEmulator(udid)
                        );
                        devices.add(info);
                        LOG.debug("Found device: {}", info);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error getting available devices", e);
            throw new FrameworkException("Failed to get Android devices", e);
        }

        LOG.info("Found {} Android devices", devices.size());
        return devices;
    }

    @Override
    public DeviceInfo getDeviceByUdid(String udid) {
        LOG.info("Getting device info for UDID: {}", udid);
        return getAvailableDevices().stream()
                .filter(device -> device.getUdid().equals(udid))
                .findFirst()
                .orElseThrow(() -> new FrameworkException("Device not found: " + udid));
    }

    @Override
    public void startDevice(String deviceName) {
        LOG.info("Starting Android emulator: {}", deviceName);
        try {
            ProcessBuilder pb = new ProcessBuilder(EMULATOR_COMMAND, "-avd", deviceName);
            pb.start();
            LOG.info("Emulator started: {}", deviceName);

            // Wait for device to be ready
            Thread.sleep(5000);
            waitForDevice(deviceName);
        } catch (Exception e) {
            LOG.error("Error starting emulator", e);
            throw new FrameworkException("Failed to start Android emulator: " + deviceName, e);
        }
    }

    @Override
    public void stopDevice(String deviceName) {
        LOG.info("Stopping Android device: {}", deviceName);
        try {
            executeCommand(ADB_COMMAND, "shell", "reboot", "-p");
            LOG.info("Device stopped: {}", deviceName);
        } catch (Exception e) {
            LOG.warn("Error stopping device", e);
        }
    }

    @Override
    public boolean isDeviceConnected(String udid) {
        LOG.debug("Checking if device is connected: {}", udid);
        try {
            String output = executeCommand(ADB_COMMAND, "devices");
            return output.contains(udid);
        } catch (Exception e) {
            LOG.error("Error checking device connection", e);
            return false;
        }
    }

    @Override
    public boolean isDeviceBooted(String udid) {
        LOG.debug("Checking if device is booted: {}", udid);
        try {
            String output = executeCommand(ADB_COMMAND, "devices");
            // Device is booted if it appears in adb devices list with "device" status
            String[] lines = output.split("\n");
            for (String line : lines) {
                if (line.contains(udid) && line.contains("device")) {
                    // Verify it's not offline
                    return !line.contains("offline");
                }
            }
            return false;
        } catch (Exception e) {
            LOG.error("Error checking if device is booted", e);
            return false;
        }
    }

    @Override
    public String getPlatformType() {
        return PLATFORM_TYPE;
    }

    /**
     * Execute shell command
     */
    private String executeCommand(String... command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        process.waitFor();
        return output.toString();
    }

    /**
     * Get device property using adb shell
     */
    private String getDeviceProperty(String udid, String property) {
        try {
            return executeCommand(ADB_COMMAND, "-s", udid, "shell", "getprop", property)
                    .trim()
                    .replaceAll("[\\[\\]]", "");
        } catch (Exception e) {
            LOG.warn("Error getting device property: {}", property);
            return "unknown";
        }
    }

    /**
     * Check if device is an emulator
     */
    private Boolean isEmulator(String udid) {
        return udid.contains("emulator");
    }

    /**
     * Wait for device to be ready
     */
    private void waitForDevice(String deviceName) throws InterruptedException {
        long maxWaitTime = System.currentTimeMillis() + 60000; // 60 seconds
        while (System.currentTimeMillis() < maxWaitTime) {
            try {
                String output = executeCommand(ADB_COMMAND, "devices");
                if (output.contains(deviceName) && output.contains("device")) {
                    LOG.info("Device ready: {}", deviceName);
                    return;
                }
            } catch (Exception e) {
                LOG.debug("Device not ready yet");
            }
            Thread.sleep(2000);
        }
        throw new FrameworkException("Device timeout waiting for device: " + deviceName);
    }
}

