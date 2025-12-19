package com.demo.framework.config;

import com.demo.framework.exceptions.FrameworkException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

/**
 * Configuration provider for loading and managing application configuration
 * Implements Single Responsibility Principle: only responsible for config loading
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
        return new AppiumConfig(
                getRequired("platformName"),
                getRequired("platformVersion"),
                getRequired("deviceName"),
                getRequired("automationName"),
                properties.getProperty("app", ""),
                URI.create(getRequired("appiumServerUrl")),
                Duration.ofSeconds(Long.parseLong(properties.getProperty("newCommandTimeout", "120")))
        );
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
