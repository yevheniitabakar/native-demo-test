# Native Mobile Test Automation Framework

Production-ready mobile test automation framework using Appium, Java 17, TestNG, and Allure Reporting.

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 17 |
| TestNG | 7.10.2 |
| Appium | 9.2.2 |
| Gradle | 8.6 |
| Allure | 2.28.1 |

## Features

- **SOLID Principles** design patterns
- **POM Architecture** with BasePage pattern
- **Cross-platform Support** Android & iOS
- **Sequential Test Execution** (thread-count=1) for reliable device state
- **ThreadLocal Driver Management** for thread-safe access
- **Comprehensive Logging** SLF4J + Logback
- **Allure Reporting** with environment details and screenshots
- **Platform-Specific Device Validation** before test execution

## Prerequisites

### Device Requirements

- **Using run_tests.sh**: Android SDK or Xcode required. Script auto-starts emulator/simulator if not booted.
- **Using Gradle directly**: Device/emulator/simulator must be pre-booted before test execution.
- **Appium Server**: Auto-started by run_tests.sh

### System Setup

1. Install Java 17
2. Install Android SDK (for Android testing)
3. Install Xcode (for iOS testing on macOS)
4. Install Appium: `npm install -g appium`
5. Ensure adb/xcrun are in PATH

## Running Tests

### Option 1: Using run_tests.sh (Recommended)

Automatically manages device lifecycle - checks if device is booted, starts it if needed, installs app, and runs tests.

```bash
# Android tests (emulator auto-start if not running)
./run_tests.sh android

# iOS tests (simulator auto-start if not running)
./run_tests.sh ios
```

### Option 2: Using Gradle Directly

Requires device to be already booted. Only validates device availability, does not start it. Use this when:
- Device/emulator/simulator is already running
- You want direct control without device management

```bash
# Android tests (emulator must be booted)
./gradlew test -Dplatform=android

# iOS tests (simulator must be booted)
./gradlew test -Dplatform=ios
```

## Test Scenarios

| Scenario | Test Cases |
|----------|-----------|
| Login | TC_1.1 Successful Login, TC_1.2 Invalid Credentials, TC_1.3 Empty Fields |
| Gestures | TC_2.1 Swipe Through Carousel |
| WebView | TC_3.1 WebView Navigation |
| Drag & Drop | TC_4.1 Successful Interaction, TC_4.2 Element State Verification |

## Generate Allure Report

```bash
# Run tests first
./gradlew test -Dplatform=android

# Generate and open Allure report
./gradlew allureServe
```

## Project Structure

```
src/main/java/com/demo/framework/
├── config/        Configuration management
├── drivers/       Appium & device management
├── pages/         Page Objects
├── listeners/     Test listeners
└── utils/         Utilities

src/test/java/com/demo/framework/
├── tests/         Test cases
└── listeners/     Allure reporting

testApps/
├── android/       Android APK
└── ios/           iOS app bundle
```

## Architecture Highlights

- **Sequential Execution**: Tests run sequentially to maintain consistent device state
- **Platform Isolation**: run_tests.sh validates only the specified platform device
- **Device Checking**: Pre-execution validation ensures device is booted and ready
- **Thread Safety**: ThreadLocal pattern for driver instance management
- **Configuration Management**: Platform-specific properties in appium.properties


