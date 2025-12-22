package com.demo.framework.utils;

import com.demo.framework.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Manages context switching between Native and WebView contexts.
 * Used for hybrid app testing where WebView components are present.
 */
public class ContextManager {

    private static final Logger LOG = LoggerFactory.getLogger(ContextManager.class);
    private static final String NATIVE_CONTEXT = "NATIVE_APP";
    private static final String WEBVIEW_PREFIX = "WEBVIEW";
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int POLL_INTERVAL_MS = 500;

    private final AppiumDriver driver;

    public ContextManager() {
        this.driver = DriverManager.getDriver();
    }

    /**
     * Wait for WebView context to become available
     */
    public void waitForWebView() {
        waitForWebView(DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Wait for WebView context to become available with custom timeout
     *
     * @param timeoutSeconds Maximum time to wait for WebView
     */
    public void waitForWebView(int timeoutSeconds) {
        LOG.info("Waiting for WebView context to be available (timeout: {}s)", timeoutSeconds);
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        while (System.currentTimeMillis() < endTime) {
            Set<String> contexts = getContextSwitchingDriver().getContextHandles();
            for (String context : contexts) {
                if (context.contains(WEBVIEW_PREFIX)) {
                    LOG.info("WebView context found: {}", context);
                    return;
                }
            }
            try {
                Thread.sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.warn("Wait for WebView interrupted");
                break;
            }
        }
        LOG.warn("WebView context not found within {}s timeout", timeoutSeconds);
    }

    /**
     * Switch to WebView context
     */
    public void switchToWebView() {
        LOG.info("Switching to WebView context");
        SupportsContextSwitching contextDriver = getContextSwitchingDriver();
        Set<String> contexts = contextDriver.getContextHandles();
        LOG.debug("Available contexts: {}", contexts);

        for (String context : contexts) {
            if (context.contains(WEBVIEW_PREFIX)) {
                contextDriver.context(context);
                LOG.info("Switched to WebView context: {}", context);
                return;
            }
        }
        LOG.warn("No WebView context available. Available contexts: {}", contexts);
    }

    /**
     * Switch back to Native context
     */
    public void switchToNative() {
        LOG.info("Switching to Native context");
        getContextSwitchingDriver().context(NATIVE_CONTEXT);
        LOG.info("Switched to Native context: {}", NATIVE_CONTEXT);
    }

    /**
     * Get current context
     *
     * @return Current context name
     */
    public String getCurrentContext() {
        String context = getContextSwitchingDriver().getContext();
        LOG.debug("Current context: {}", context);
        return context;
    }

    /**
     * Check if currently in WebView context
     *
     * @return true if in WebView context
     */
    public boolean isInWebViewContext() {
        String context = getCurrentContext();
        return context != null && context.contains(WEBVIEW_PREFIX);
    }

    /**
     * Check if currently in Native context
     *
     * @return true if in Native context
     */
    public boolean isInNativeContext() {
        String context = getCurrentContext();
        return NATIVE_CONTEXT.equals(context);
    }

    /**
     * Get all available contexts
     *
     * @return Set of available context names
     */
    public Set<String> getAvailableContexts() {
        Set<String> contexts = getContextSwitchingDriver().getContextHandles();
        LOG.debug("Available contexts: {}", contexts);
        return contexts;
    }

    /**
     * Cast driver to SupportsContextSwitching for context operations
     */
    private SupportsContextSwitching getContextSwitchingDriver() {
        return (SupportsContextSwitching) driver;
    }
}

