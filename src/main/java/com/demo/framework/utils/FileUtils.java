package com.demo.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;

/**
 * Utility class for file operations
 */
public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    /**
     * Read file content
     */
    public static String readFile(String filePath) {
        LOG.debug("Reading file: {}", filePath);
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            LOG.error("Error reading file: {}", filePath, e);
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    /**
     * Write content to file
     */
    public static void writeFile(String filePath, String content) {
        LOG.debug("Writing to file: {}", filePath);
        try {
            Files.write(Paths.get(filePath), content.getBytes());
        } catch (IOException e) {
            LOG.error("Error writing to file: {}", filePath, e);
            throw new RuntimeException("Failed to write file: " + filePath, e);
        }
    }

    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        LOG.debug("Checking if file exists: {}", filePath);
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Create directory if it doesn't exist
     */
    public static void createDirectory(String dirPath) {
        LOG.debug("Creating directory: {}", dirPath);
        try {
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) {
            LOG.error("Error creating directory: {}", dirPath, e);
            throw new RuntimeException("Failed to create directory: " + dirPath, e);
        }
    }

    /**
     * Delete file
     */
    public static void deleteFile(String filePath) {
        LOG.debug("Deleting file: {}", filePath);
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            LOG.error("Error deleting file: {}", filePath, e);
            throw new RuntimeException("Failed to delete file: " + filePath, e);
        }
    }

    /**
     * Get file extension
     */
    public static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot > 0) ? fileName.substring(lastDot + 1) : "";
    }

    /**
     * Get file name without extension
     */
    public static String getFileNameWithoutExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot > 0) ? fileName.substring(0, lastDot) : fileName;
    }
}

