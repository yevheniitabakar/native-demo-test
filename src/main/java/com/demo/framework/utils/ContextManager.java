package com.demo.framework.utils;

import com.demo.framework.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Manages context switching between Native and WebView contexts.
 */
@Slf4j
public class ContextManager {

    private static final String NATIVE_CONTEXT = "NATIVE_APP";
    private static final String WEBVIEW_PREFIX = "WEBVIEW";
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int POLL_INTERVAL_MS = 500;

    private final AppiumDriver driver;

    public ContextManager() {
        this.driver = DriverManager.getDriver();
    }

    public void waitForWebView() {
        waitForWebView(DEFAULT_TIMEOUT_SECONDS);
    }

    public void waitForWebView(int timeoutSeconds) {
        log.info("Waiting for WebView context (timeout: {}s)", timeoutSeconds);
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        while (System.currentTimeMillis() < endTime) {
            Set<String> contexts = getContextSwitchingDriver().getContextHandles();
            for (String context : contexts) {
                if (context.contains(WEBVIEW_PREFIX)) {
                    log.info("WebView context found: {}", context);
                    return;
                }
            }
            try {
                Thread.sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Wait for WebView interrupted");
                break;
            }
        }
        log.warn("WebView context not found within {}s timeout", timeoutSeconds);
    }

    public void switchToWebView() {
        log.info("Switching to WebView context");
        SupportsContextSwitching contextDriver = getContextSwitchingDriver();
        Set<String> contexts = contextDriver.getContextHandles();

        for (String context : contexts) {
            if (context.contains(WEBVIEW_PREFIX)) {
                contextDriver.context(context);
                log.info("Switched to WebView context: {}", context);
                return;
            }
        }
        log.warn("No WebView context available");
    }

    public void switchToNative() {
        log.info("Switching to Native context");
        getContextSwitchingDriver().context(NATIVE_CONTEXT);
    }

    public String getCurrentContext() {
        return getContextSwitchingDriver().getContext();
    }

    public boolean isInWebViewContext() {
        String context = getCurrentContext();
        return context != null && context.contains(WEBVIEW_PREFIX);
    }

    public boolean isInNativeContext() {
        return NATIVE_CONTEXT.equals(getCurrentContext());
    }

    public Set<String> getAvailableContexts() {
        return getContextSwitchingDriver().getContextHandles();
    }

    private SupportsContextSwitching getContextSwitchingDriver() {
        return (SupportsContextSwitching) driver;
    }
}

