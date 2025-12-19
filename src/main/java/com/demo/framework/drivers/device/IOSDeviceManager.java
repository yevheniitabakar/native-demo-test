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
 * iOS Device Manager implementation
 * Manages iOS simulators using xcrun and instruments
 */
public class IOSDeviceManager implements IDeviceManager {

    private static final Logger LOG = LoggerFactory.getLogger(IOSDeviceManager.class);
    private static final String XCRUN_COMMAND = "xcrun";
    private static final String SIMCTL_SUBCOMMAND = "simctl";
    private static final String PLATFORM_TYPE = "iOS";

    @Override
    public List<DeviceInfo> getAvailableDevices() {
        LOG.info("Getting available iOS devices");
        List<DeviceInfo> devices = new ArrayList<>();
        try {
            String output = executeCommand(XCRUN_COMMAND, SIMCTL_SUBCOMMAND, "list", "devices", "available");
            String[] lines = output.split("\n");

            String currentRuntime = null;
            for (String line : lines) {
                line = line.trim();

                // Extract runtime version (e.g., "iOS 17.2")
                if (line.contains("--") && !line.contains("(")) {
                    currentRuntime = line.replaceAll("--", "").trim();
                    continue;
                }

                // Extract device info
                if (line.contains("(") && line.contains(")") && !line.contains("--")) {
                    String[] parts = line.split("\\(");
                    if (parts.length >= 2) {
                        String deviceName = parts[0].trim();
                        String udid = parts[1].replace(")", "").trim();

                        DeviceInfo info = new DeviceInfo(
                                deviceName,
                                PLATFORM_TYPE,
                                currentRuntime != null ? currentRuntime : "unknown",
                                udid,
                                true // iOS simulators are always emulators
                        );
                        devices.add(info);
                        LOG.debug("Found device: {}", info);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error getting available devices", e);
            throw new FrameworkException("Failed to get iOS devices", e);
        }

        LOG.info("Found {} iOS devices", devices.size());
        return devices;
    }

    @Override
    public DeviceInfo getDeviceByUdid(String udid) {
        LOG.info("Getting device info for UDID: {}", udid);
        return getAvailableDevices().stream()
                .filter(device -> device.getUdid().equals(udid))
                .findFirst()
                .orElseThrow(() -> new FrameworkException("iOS Device not found: " + udid));
    }

    @Override
    public void startDevice(String deviceName) {
        LOG.info("Starting iOS simulator: {}", deviceName);
        try {
            // Get UDID first
            DeviceInfo device = getAvailableDevices().stream()
                    .filter(d -> d.getDeviceName().equals(deviceName))
                    .findFirst()
                    .orElseThrow(() -> new FrameworkException("Simulator not found: " + deviceName));

            ProcessBuilder pb = new ProcessBuilder(XCRUN_COMMAND, SIMCTL_SUBCOMMAND, "boot", device.getUdid());
            Process process = pb.start();
            process.waitFor();

            LOG.info("Simulator started: {}", deviceName);
            Thread.sleep(5000); // Wait for simulator to fully load
        } catch (Exception e) {
            LOG.error("Error starting simulator", e);
            throw new FrameworkException("Failed to start iOS simulator: " + deviceName, e);
        }
    }

    @Override
    public void stopDevice(String deviceName) {
        LOG.info("Stopping iOS simulator: {}", deviceName);
        try {
            // Get UDID first
            DeviceInfo device = getAvailableDevices().stream()
                    .filter(d -> d.getDeviceName().equals(deviceName))
                    .findFirst()
                    .orElseThrow(() -> new FrameworkException("Simulator not found: " + deviceName));

            ProcessBuilder pb = new ProcessBuilder(XCRUN_COMMAND, SIMCTL_SUBCOMMAND, "shutdown", device.getUdid());
            Process process = pb.start();
            process.waitFor();

            LOG.info("Simulator stopped: {}", deviceName);
        } catch (Exception e) {
            LOG.warn("Error stopping simulator", e);
        }
    }

    @Override
    public boolean isDeviceConnected(String udid) {
        LOG.debug("Checking if iOS device is connected: {}", udid);
        try {
            return getAvailableDevices().stream()
                    .anyMatch(device -> device.getUdid().equals(udid));
        } catch (Exception e) {
            LOG.error("Error checking device connection", e);
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
}

