package com.demo.framework.listeners;

import com.demo.framework.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener for Allure reporting and logging
 * Implements ITestListener to intercept test lifecycle events
 */
public class AllureTestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(AllureTestListener.class);

    @Override
    public void onStart(ITestContext context) {
        LOG.info("========================================");
        LOG.info("Starting Test Suite: {}", context.getName());
        LOG.info("========================================");

        Allure.feature("Test Suite");
        Allure.story(context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("========================================");
        LOG.info("Finished Test Suite: {}", context.getName());
        LOG.info("Total Tests Run: {}", context.getAllTestMethods().length);
        LOG.info("Passed: {}", context.getPassedTests().size());
        LOG.info("Failed: {}", context.getFailedTests().size());
        LOG.info("Skipped: {}", context.getSkippedTests().size());
        LOG.info("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("------- Test Started: {} -------", result.getName());

        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();

        Allure.feature(className);
        Allure.story(methodName);
        addTestParameters(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("✓ Test PASSED: {}", result.getName());
        addTestDuration(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("✗ Test FAILED: {}", result.getName());
        LOG.error("Error message: {}", result.getThrowable().getMessage());

        // Take screenshot on failure
        try {
            ScreenshotUtils.takeScreenshotOnFailure();
        } catch (Exception e) {
            LOG.warn("Failed to take screenshot on test failure", e);
        }

        // Add failure details to Allure
        addFailureDetails(result);
        addTestDuration(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("⊘ Test SKIPPED: {}", result.getName());

        if (result.getThrowable() != null) {
            LOG.warn("Skip reason: {}", result.getThrowable().getMessage());
        }

        addTestDuration(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOG.warn("Test passed but within success percentage: {}", result.getName());
    }

    /**
     * Add test parameters to Allure report
     */
    @Step("Adding test parameters")
    private void addTestParameters(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                String paramName = "param_" + (i + 1);
                String paramValue = String.valueOf(parameters[i]);
                Allure.parameter(paramName, paramValue);
                LOG.debug("Parameter: {} = {}", paramName, paramValue);
            }
        }
    }

    /**
     * Add failure details to Allure
     */
    @Step("Adding failure details")
    private void addFailureDetails(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            StringBuilder failureDetails = new StringBuilder();
            failureDetails.append("Exception: ").append(throwable.getClass().getName()).append("\n");
            failureDetails.append("Message: ").append(throwable.getMessage()).append("\n");
            failureDetails.append("Stack Trace:\n");

            for (StackTraceElement element : throwable.getStackTrace()) {
                failureDetails.append(element.toString()).append("\n");
            }

            Allure.addAttachment("Failure Details", "text/plain", failureDetails.toString());
        }
    }

    /**
     * Add test duration to Allure
     */
    private void addTestDuration(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        LOG.info("Test Duration: {} ms", duration);
        Allure.parameter("Duration (ms)", String.valueOf(duration));
    }
}

