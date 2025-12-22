package com.demo.framework.utils;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for Allure reporting with timestamp support
 */
public class AllureStepUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AllureStepUtils.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private AllureStepUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Log a step with timestamp to Allure report
     *
     * @param stepName Name of the step to display
     */
    public static void allureStep(String stepName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String stepWithTimestamp = String.format("[%s] %s", timestamp, stepName);

        LOG.info(stepWithTimestamp);
        Allure.step(stepWithTimestamp);
    }
}

