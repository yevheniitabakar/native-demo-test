package com.demo.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility class for checking device availability before test execution
 * Provides methods to verify if Android emulator or iOS simulator is booted
 */
public class DeviceCheckerUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceCheckerUtil.class);

    private DeviceCheckerUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Check if Android emulator is booted
     */
    public static boolean isAndroidEmulatorBooted() {
        LOG.info("Checking if Android emulator is booted...");
        try {
            String output = executeCommand("adb", "devices");
            String[] lines = output.split("\n");

            // Check for any emulator device with "device" status
            for (String line : lines) {
                if (line.contains("emulator") && line.contains("device")) {
                    LOG.info("Android emulator is booted");
                    return true;
                }
            }

            LOG.info("No Android emulator is currently booted");
            return false;
        } catch (Exception e) {
            LOG.error("Error checking Android emulator status", e);
            return false;
        }
    }

    /**
     * Check if iOS simulator is booted
     */
    public static boolean isIOSSimulatorBooted() {
        LOG.info("Checking if iOS simulator is booted...");
        try {
            String output = executeCommand("xcrun", "simctl", "list", "devices");

            // Check if any device has "(Booted)" status
            if (output.contains("(Booted)")) {
                LOG.info("iOS simulator is booted");
                return true;
            }

            LOG.info("No iOS simulator is currently booted");
            return false;
        } catch (Exception e) {
            LOG.error("Error checking iOS simulator status", e);
            return false;
        }
    }

    /**
     * Execute shell command and return output
     */
    private static String executeCommand(String... command) throws IOException, InterruptedException {
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
     * Get booted Android emulator ID
     */
    public static String getBootedAndroidEmulatorId() {
        LOG.debug("Getting booted Android emulator ID...");
        try {
            String output = executeCommand("adb", "devices", "-l");
            String[] lines = output.split("\n");

            for (String line : lines) {
                if (line.contains("emulator") && line.contains("device")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length > 0) {
                        LOG.debug("Found booted emulator: {}", parts[0]);
                        return parts[0];
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error getting booted Android emulator ID", e);
        }
        return null;
    }

    /**
     * Get booted iOS simulator UDID
     */
    public static String getBootedIOSSimulatorUdid() {
        LOG.debug("Getting booted iOS simulator UDID...");
        try {
            String output = executeCommand("xcrun", "simctl", "list", "devices");
            String[] lines = output.split("\n");

            for (String line : lines) {
                if (line.contains("(Booted)")) {
                    // Extract UDID from line like: "iPhone 15 (UDID) (Booted)"
                    int start = line.lastIndexOf("(");
                    int end = line.lastIndexOf(")");
                    if (start > 0 && end > start) {
                        String udid = line.substring(start + 1, end).trim();
                        // Make sure it's the UDID (should be long UUID format)
                        if (udid.length() > 10) {
                            LOG.debug("Found booted simulator UDID: {}", udid);
                            return udid;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error getting booted iOS simulator UDID", e);
        }
        return null;
    }
}

