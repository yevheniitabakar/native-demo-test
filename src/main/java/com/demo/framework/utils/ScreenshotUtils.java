package com.demo.framework.utils;

import com.demo.framework.drivers.DriverManager;
import com.demo.framework.exceptions.FrameworkException;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for taking screenshots and integrating with Allure reporting
 */
public class ScreenshotUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "screenshots";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

    private ScreenshotUtils() {
    }

    /**
     * Take screenshot and attach to Allure report
     */
    public static void takeScreenshot(String name) {
        try {
            if (!DriverManager.isDriverInitialized()) {
                LOG.warn("Driver not initialized, skipping screenshot");
                return;
            }

            AppiumDriver driver = DriverManager.getDriver();
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String fileName = generateFileName(name);
            Path screenshotPath = saveScreenshot(screenshot, fileName);

            attachToAllure(screenshotPath, name);
            LOG.info("Screenshot saved: {}", screenshotPath);
        } catch (Exception e) {
            LOG.error("Failed to take screenshot", e);
        }
    }

    /**
     * Take screenshot and save to file
     */
    private static Path saveScreenshot(File source, String fileName) throws IOException {
        Path dir = Paths.get(SCREENSHOT_DIR);
        Files.createDirectories(dir);

        Path destination = dir.resolve(fileName);
        FileUtils.copyFile(source, destination.toFile());

        return destination;
    }

    /**
     * Attach screenshot to Allure report
     */
    private static void attachToAllure(Path screenshotPath, String name) throws IOException {
        try (FileInputStream fis = new FileInputStream(screenshotPath.toFile())) {
            Allure.addAttachment(name, "image/png", fis, ".png");
        }
    }

    /**
     * Generate unique screenshot filename
     */
    private static String generateFileName(String name) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        return String.format("%s_%s.png", name.replaceAll("\\s+", "_"), timestamp);
    }

    /**
     * Take screenshot on assertion failure
     */
    public static void takeScreenshotOnFailure() {
        takeScreenshot("assertion_failure");
    }
}

