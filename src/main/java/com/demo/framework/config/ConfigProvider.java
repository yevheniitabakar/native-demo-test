package com.demo.framework.config;

import com.demo.framework.exceptions.FrameworkException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

/**
 * Configuration provider for loading and managing application configuration
 */
public class ConfigProvider {

    private static final String DEFAULT_CONFIG = "config/appium.properties";
    private final Properties properties;

    public ConfigProvider() {
        this(DEFAULT_CONFIG);
    }

    public ConfigProvider(String resourcePath) {
        properties = new Properties();
        try (InputStream inputStream = getResourceAsStream(resourcePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new FrameworkException("Unable to load config from " + resourcePath, e);
        }
    }

    /**
     * Get Appium configuration
     */
    public AppiumConfig getAppiumConfig() {
        String platformName = getPlatformName();
        String appPath = getAppPathForPlatform(platformName);
        
        // Default to noReset=true and fullReset=false for both platforms
        // This keeps the device/emulator alive and uses terminateApp() between tests
        // for faster execution while still ensuring clean app state
        boolean fullReset = getBoolean("appium:fullReset", false);
        boolean noReset = getBoolean("appium:noReset", true);

        return new AppiumConfig(
                platformName,
                getPlatformVersion(platformName),
                getDeviceName(platformName),
                getAutomationName(platformName),
                appPath,
                URI.create(getRequired("appiumServerUrl")),
                Duration.ofSeconds(Long.parseLong(properties.getProperty("newCommandTimeout", "120"))),
                fullReset,
                noReset,
                getDeviceUdid(platformName),
                getBoolean("appium:usePrebuiltWDA", true),
                getBoolean("appium:skipDeviceInitialization", true)
        );
    }
    
    /**
     * Get device UDID based on platform
     * For iOS, automatically detects booted simulator UDID if not specified
     */
    private String getDeviceUdid(String platformName) {
        if (platformName == null) {
            return null;
        }
        
        String configuredUdid = switch (platformName.toUpperCase()) {
            case "ANDROID" -> properties.getProperty("device.android.udid", null);
            case "IOS" -> properties.getProperty("device.ios.udid", null);
            default -> null;
        };
        
        // If UDID is configured, use it
        if (configuredUdid != null && !configuredUdid.isBlank()) {
            return configuredUdid.trim();
        }
        
        // For iOS, try to detect booted simulator UDID automatically
        if ("IOS".equalsIgnoreCase(platformName)) {
            return detectBootedIOSSimulatorUdid();
        }
        
        return null;
    }
    
    /**
     * Detect the UDID of a booted iOS simulator
     */
    private String detectBootedIOSSimulatorUdid() {
        try {
            ProcessBuilder pb = new ProcessBuilder("xcrun", "simctl", "list", "devices");
            Process process = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            process.waitFor();
            String[] lines = output.toString().split("\n");
            
            for (String line : lines) {
                if (line.contains("(Booted)")) {
                    // Extract UDID from line like: "iPhone 15 (UDID) (Booted)"
                    // Find the UUID pattern
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                            "\\(([A-F0-9-]{36})\\)");
                    java.util.regex.Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        return matcher.group(1);
                    }
                }
            }
        } catch (Exception e) {
            // Log but don't fail - UDID detection is optional
            System.err.println("Warning: Could not detect booted iOS simulator UDID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get current platform (Android or iOS)
     */
    public String getPlatform() {
        return getPlatformName();
    }

    /**
     * Get platform name from system property or default to Android
     */
    private String getPlatformName() {
        String platformFromSystem = System.getProperty("platform");
        if (platformFromSystem != null && !platformFromSystem.isBlank()) {
            String normalized = platformFromSystem.trim().toUpperCase();
            if ("ANDROID".equals(normalized) || "IOS".equals(normalized)) {
                return normalized;
            }
        }
        return getRequired("platformName");
    }

    /**
     * Get device name based on platform
     */
    private String getDeviceName(String platformName) {
        if (platformName == null) {
            throw new FrameworkException("Platform name must not be null");
        }

        return switch (platformName.toUpperCase()) {
            case "ANDROID" -> properties.getProperty("device.android.name", "Android Emulator");
            case "IOS" -> properties.getProperty("device.ios.name", "iPhone 15");
            default -> throw new FrameworkException("Unsupported platform: " + platformName);
        };
    }

    /**
     * Get platform version based on platform
     */
    private String getPlatformVersion(String platformName) {
        if (platformName == null) {
            throw new FrameworkException("Platform name must not be null");
        }

        return switch (platformName.toUpperCase()) {
            case "ANDROID" -> properties.getProperty("device.android.platformVersion", "14");
            case "IOS" -> properties.getProperty("device.ios.platformVersion", "18.0");
            default -> throw new FrameworkException("Unsupported platform: " + platformName);
        };
    }

    /**
     * Get automation name based on platform
     */
    private String getAutomationName(String platformName) {
        if (platformName == null) {
            throw new FrameworkException("Platform name must not be null");
        }

        return switch (platformName.toUpperCase()) {
            case "ANDROID" -> properties.getProperty("device.android.automationName", "UiAutomator2");
            case "IOS" -> properties.getProperty("device.ios.automationName", "XCUITest");
            default -> throw new FrameworkException("Unsupported platform: " + platformName);
        };
    }

    /**
     * Get application path based on platform
     */
    private String getAppPathForPlatform(String platformName) {
        if (platformName == null) {
            throw new FrameworkException("Platform name must not be null");
        }

        String appPath = switch (platformName.toUpperCase()) {
            case "ANDROID" -> properties.getProperty("app.android.path", "");
            case "IOS" -> properties.getProperty("app.ios.path", "");
            default -> throw new FrameworkException("Unsupported platform: " + platformName);
        };

        if (appPath.isBlank()) {
            throw new FrameworkException("Application path not configured for platform: " + platformName);
        }

        return convertToAbsolutePath(appPath);
    }

    /**
     * Convert relative path to absolute path
     */
    private String convertToAbsolutePath(String appPath) {
        Path path = Paths.get(appPath);

        // If already absolute, return as is
        if (path.isAbsolute()) {
            if (!Files.exists(path)) {
                throw new FrameworkException("Application file not found: " + appPath);
            }
            return path.toString();
        }

        // Convert relative path to absolute
        Path absolutePath = path.toAbsolutePath();
        if (!Files.exists(absolutePath)) {
            throw new FrameworkException("Application file not found: " + absolutePath);
        }
        return absolutePath.toString();
    }

    /**
     * Get string property with default value
     */
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get required string property
     */
    public String getRequired(String key) {
        String value = properties.getProperty(key);
        if (Objects.isNull(value) || value.isBlank()) {
            throw new FrameworkException("Missing required property: " + key);
        }
        return value.trim();
    }

    /**
     * Get optional string property
     */
    public String getOptional(String key) {
        String value = properties.getProperty(key);
        return value != null ? value.trim() : null;
    }

    /**
     * Get boolean property with default value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    /**
     * Get integer property with default value
     */
    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new FrameworkException("Invalid integer property: " + key + " = " + value);
        }
    }

    /**
     * Get long property with default value
     */
    public long getLong(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new FrameworkException("Invalid long property: " + key + " = " + value);
        }
    }

    /**
     * Get all properties
     */
    public Properties getProperties() {
        return new Properties(properties);
    }

    /**
     * Get resource as input stream
     */
    private InputStream getResourceAsStream(String resourcePath) {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new FrameworkException("Config resource not found: " + resourcePath);
        }
        return stream;
    }
}
