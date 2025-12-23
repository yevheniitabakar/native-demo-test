package com.demo.framework.utils;

import io.qameta.allure.Allure;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for Allure reporting with timestamp support
 */
@Slf4j
@UtilityClass
public class AllureStepUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void allureStep(String stepName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String stepWithTimestamp = String.format("[%s] %s", timestamp, stepName);
        log.info(stepWithTimestamp);
        Allure.step(stepWithTimestamp);
    }
}

