package com.demo.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for test data generation and manipulation
 */
public class TestDataUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataUtils.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TestDataUtils() {
    }

    /**
     * Generate random email
     */
    public static String generateRandomEmail() {
        String email = "user_" + System.currentTimeMillis() + "@test.com";
        LOG.debug("Generated email: {}", email);
        return email;
    }

    /**
     * Generate random username
     */
    public static String generateRandomUsername() {
        String username = "user_" + System.currentTimeMillis();
        LOG.debug("Generated username: {}", username);
        return username;
    }

    /**
     * Generate random phone number
     */
    public static String generateRandomPhoneNumber() {
        long randomNum = System.currentTimeMillis() % 1000000000L;
        String phone = String.format("555%010d", randomNum);
        LOG.debug("Generated phone: {}", phone);
        return phone;
    }

    /**
     * Generate random numeric string
     */
    public static String generateRandomNumeric(int length) {
        long maxValue = (long) Math.pow(10, length);
        long randomNum = System.currentTimeMillis() % maxValue;
        String result = String.format("%0" + length + "d", randomNum);
        LOG.debug("Generated numeric string: {}", result);
        return result;
    }

    /**
     * Generate random string
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        LOG.debug("Generated random string: {}", result);
        return result.toString();
    }

    /**
     * Get current timestamp
     */
    public static String getCurrentTimestamp() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        LOG.debug("Current timestamp: {}", timestamp);
        return timestamp;
    }

    /**
     * Generate test user data
     */
    public static TestUserData generateTestUser() {
        TestUserData user = new TestUserData(
                generateRandomUsername(),
                generateRandomEmail(),
                generateRandomString(8)
        );
        LOG.debug("Generated test user: {}", user);
        return user;
    }

    /**
     * Test user data class
     */
    public static class TestUserData {
        private final String username;
        private final String email;
        private final String password;

        public TestUserData(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public String toString() {
            return "TestUserData{" +
                    "username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", password='***'" +
                    '}';
        }
    }
}

