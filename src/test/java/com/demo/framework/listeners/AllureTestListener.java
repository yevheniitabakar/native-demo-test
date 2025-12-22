package com.demo.framework.listeners;

import com.demo.framework.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * TestNG Listener for Allure reporting and logging
 */
public class AllureTestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllureTestListener.class);

    @Override
    public void onStart(ITestContext context) {
        LOG.info("========================================");
        LOG.info("Starting Test Suite: {}", context.getName());
        LOG.info("========================================");

        generateAllureEnvironment();
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("========================================");
        LOG.info("Finished Test Suite: {}", context.getName());
        LOG.info("Passed: {}, Failed: {}, Skipped: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        LOG.info("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("------- Test Started: {} -------", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("✓ Test PASSED: {} ({}ms)", result.getName(), getTestDuration(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("✗ Test FAILED: {}", result.getName());
        LOG.error("Error: {}", result.getThrowable().getMessage());

        try {
            ScreenshotUtils.takeScreenshotOnFailure();
        } catch (Exception e) {
            LOG.warn("Failed to take screenshot", e);
        }

        addFailureAttachment(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("⊘ Test SKIPPED: {}", result.getName());
    }

    private void generateAllureEnvironment() {
        String platform = System.getProperty("platform", "android");
        String platformVersion = getPlatformVersion(platform);

        Properties props = new Properties();
        props.setProperty("Platform", platform.toUpperCase());
        props.setProperty("Platform.Version", platformVersion);

        String allureResultsDir = System.getProperty("allure.results.directory", "build/allure-results");
        try {
            java.io.File dir = new java.io.File(allureResultsDir);
            dir.mkdirs();
            try (FileOutputStream fos = new FileOutputStream(allureResultsDir + "/environment.properties")) {
                props.store(fos, null);
            }
            LOG.info("Generated Allure environment: Platform={}, Version={}", platform.toUpperCase(), platformVersion);
        } catch (IOException e) {
            LOG.warn("Failed to generate Allure environment.properties", e);
        }
    }

    private String getPlatformVersion(String platform) {
        if ("android".equalsIgnoreCase(platform)) {
            return System.getProperty("device.android.platformVersion", "14");
        } else if ("ios".equalsIgnoreCase(platform)) {
            return System.getProperty("device.ios.platformVersion", "18.0");
        }
        return "unknown";
    }

    private long getTestDuration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }

    private void addFailureAttachment(ITestResult result) {
        Throwable t = result.getThrowable();
        if (t != null) {
            StringBuilder details = new StringBuilder();
            details.append("Exception: ").append(t.getClass().getName()).append("\n");
            details.append("Message: ").append(t.getMessage()).append("\n\nStack Trace:\n");
            for (StackTraceElement e : t.getStackTrace()) {
                details.append(e.toString()).append("\n");
            }
            Allure.addAttachment("Failure Details", "text/plain", details.toString());
        }
    }
}

