package com.demo.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * Utility class for common operations and verifications
 */
public class CommonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

    private CommonUtils() {
    }

    /**
     * Wait for a specified duration
     */
    public static void sleep(long millis) {
        LOG.debug("Sleeping for {} ms", millis);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOG.warn("Sleep interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for a duration
     */
    public static void sleep(Duration duration) {
        sleep(duration.toMillis());
    }

    /**
     * Retry operation with exponential backoff
     */
    public static <T> T retry(RetryableOperation<T> operation, int maxAttempts, long delayMs) {
        LOG.debug("Retrying operation with max attempts: {}", maxAttempts);

        Exception lastException = null;
        long currentDelay = delayMs;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                LOG.debug("Attempt {}/{}", attempt, maxAttempts);
                return operation.execute();
            } catch (Exception e) {
                lastException = e;
                LOG.warn("Attempt {} failed: {}", attempt, e.getMessage());

                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(currentDelay);
                        currentDelay *= 2; // Exponential backoff
                    } catch (InterruptedException ie) {
                        LOG.warn("Retry sleep interrupted", ie);
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        throw new RuntimeException("Operation failed after " + maxAttempts + " attempts", lastException);
    }

    /**
     * Retry operation with default parameters
     */
    public static <T> T retry(RetryableOperation<T> operation) {
        return retry(operation, 3, 500);
    }

    /**
     * Format duration in human-readable format
     */
    public static String formatDuration(Instant start, Instant end) {
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        long millis = duration.toMillis() % 1000;
        return String.format("%d.%03d seconds", seconds, millis);
    }

    /**
     * Check if string contains any of the given values
     */
    public static boolean containsAny(String value, String... searchValues) {
        if (value == null || searchValues == null) {
            return false;
        }

        for (String search : searchValues) {
            if (value.contains(search)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Functional interface for retryable operations
     */
    @FunctionalInterface
    public interface RetryableOperation<T> {
        T execute() throws Exception;
    }
}

