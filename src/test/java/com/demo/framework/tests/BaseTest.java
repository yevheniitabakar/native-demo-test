package com.demo.framework.tests;

import com.demo.framework.config.AppiumConfig;
import com.demo.framework.config.ConfigProvider;
import com.demo.framework.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Base test class for all test classes
 * Implements setup/teardown for driver and configuration
 * Follows Template Method pattern
 */
public abstract class BaseTest {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);
    private static AppiumConfig appiumConfig;

    /**
     * Load configuration before suite runs
     */
    @BeforeSuite(alwaysRun = true)
    public void loadConfig() {
        LOG.info("Loading framework configuration");
        ConfigProvider provider = new ConfigProvider();
        appiumConfig = provider.getAppiumConfig();
        LOG.info("Framework configuration loaded: {}", appiumConfig);
    }

    /**
     * Start driver before each test
     */
    @BeforeMethod(alwaysRun = true)
    public void startDriver() {
        LOG.info("Starting Appium driver");

        if (appiumConfig == null) {
            throw new IllegalStateException("Configuration not loaded. BeforeSuite should have been executed.");
        }

        LOG.info("Creating driver for device: {}", appiumConfig.deviceName());
        DriverManager.createDriver(appiumConfig);
        LOG.info("Driver started successfully");
    }

    /**
     * Quit driver after each test
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        LOG.info("Tearing down driver");
        DriverManager.quitDriver();
        LOG.info("Driver teardown completed");
    }

    /**
     * Get driver instance for test
     */
    protected AppiumDriver driver() {
        return DriverManager.getDriver();
    }

    /**
     * Hook for subclasses to add custom setup
     */
    protected void customSetup() {
        LOG.debug("No custom setup defined");
    }

    /**
     * Hook for subclasses to add custom teardown
     */
    protected void customTeardown() {
        LOG.debug("No custom teardown defined");
    }
}

