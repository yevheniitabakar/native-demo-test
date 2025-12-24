# Native Mobile Test Automation Framework

A mobile test automation framework for Android and iOS applications using Appium, Java, TestNG, and Allure reporting.

## Project Overview

This framework provides cross-platform mobile test automation with a clean separation between test logic and infrastructure management. It supports both Android emulators and iOS simulators, with automated device provisioning and lifecycle management.

**Supported Platforms:**
- Android (emulator)
- iOS (simulator)

## Technology Stack

| Component | Version |
|-----------|---------|
| Java | 17 |
| Appium | 3.x (java-client 9.2.2) |
| TestNG | 7.10.2 |
| Gradle | 8.6 |
| Allure | 2.28.1 |

## Prerequisites

The following tools must be installed before running tests:

| Tool | Required For | Installation |
|------|--------------|--------------|
| Java JDK 17 | Framework runtime | [Download](https://adoptium.net/) or `brew install openjdk@17` |
| Node.js 18+ | Appium server | `brew install node` |
| Appium 3.x | Mobile automation | `npm install -g appium` |
| Appium UiAutomator2 Driver | Android testing | `appium driver install uiautomator2` |
| Appium XCUITest Driver | iOS testing | `appium driver install xcuitest` |
| Android SDK | Android testing | [Android Studio](https://developer.android.com/studio) or `brew install --cask android-studio` |
| Xcode | iOS testing | Mac App Store (macOS only) |
| Xcode Command Line Tools | iOS testing | `xcode-select --install` |
| Allure CLI | Report generation | `brew install allure` |

<details>
<summary><strong>Verify Installation</strong></summary>

```bash
# Java
java -version                    # Should show Java 17

# Node.js
node -v                          # Should show v18+

# Appium
appium -v                        # Should show 3.x
appium driver list --installed   # Should show uiautomator2 and/or xcuitest

# Android (if testing Android)
adb --version
emulator -list-avds

# iOS (if testing iOS, macOS only)
xcrun simctl list devices
```

</details>

<details>
<summary><strong>Setup Instructions</strong></summary>

### 1. Clone the Repository

```bash
git clone <repository-url>
cd native-demo-test
```

### 2. Install Appium and Drivers

```bash
# Install Appium globally
npm install -g appium

# Install platform drivers
appium driver install uiautomator2    # For Android
appium driver install xcuitest        # For iOS
```

### 3. Verify Android Setup (for Android testing)

```bash
# Ensure ANDROID_HOME is set
echo $ANDROID_HOME

# If not set, add to ~/.zshrc or ~/.bashrc:
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator:$ANDROID_HOME/platform-tools

# Verify adb is available
adb --version
```

### 4. Verify iOS Setup (for iOS testing, macOS only)

```bash
# Accept Xcode license
sudo xcodebuild -license accept

# Verify simulators are available
xcrun simctl list devices
```

### 5. WebView Testing (Android)

For tests involving WebView context switching, ChromeDriver is required:

```bash
# Option 1: Install Appium Chromium driver (recommended)
appium driver install chromium

# Option 2: Manual ChromeDriver installation
mkdir -p ~/.appium/chromedriver
# Download matching version from https://chromedriver.chromium.org/downloads
```

</details>

## Running Tests

### Recommended: `run_tests.sh`

The `run_tests.sh` script is the **primary entry point** for running tests. It handles the complete test lifecycle automatically:

1. **Device Management** — Boots an emulator/simulator, or creates one if none exists
2. **Appium Server** — Starts Appium if not already running
3. **App Installation** — Installs the test application on the device
4. **Test Execution** — Runs all tests via Gradle
5. **Cleanup** — Shuts down the device after execution

```bash
# Run with platform argument
./run_tests.sh android
./run_tests.sh ios

# Run interactively (prompts for platform selection)
./run_tests.sh
```

This is the recommended approach because it ensures a consistent, reproducible test environment without manual setup.

### Alternative: Direct Gradle Execution

Use direct Gradle commands when you need more control, such as running specific tests or when the device and Appium are already running.

**Prerequisites for direct execution:**
- Device/emulator must be booted and ready
- Appium server must be running (`appium` in a separate terminal)
- App must be installed on the device

```bash
# Run all tests for a platform
./gradlew test -Dplatform=android
./gradlew test -Dplatform=ios

# Run a specific test suite
./gradlew test -Dplatform=android -DsuiteXmlFile=smoke.xml

# Run specific test groups
./gradlew test -Dplatform=ios -Dgroups=login,smoke

# Run a single test class
./gradlew test -Dplatform=android --tests "LoginTests"
```

## Device Management

Device management is implemented in shell scripts (`run_tests.sh`, `scripts/device-manager.sh`) rather than in Java code. This is a deliberate architectural decision:

**Why shell-based device management?**
- **Separation of concerns** — Java code focuses purely on test logic, Page Objects, and framework architecture
- **Simplicity** — Shell scripts are the natural tool for interacting with `adb`, `emulator`, and `xcrun simctl`
- **Portability** — Device setup can be modified without recompiling Java code
- **Industry alignment** — CI/CD pipelines typically manage devices at the infrastructure level, not within test code

### Default Devices

| Platform | Device | OS Version |
|----------|--------|------------|
| Android | Pixel 7 | API 34 (Android 14) |
| iOS | iPhone 16 | iOS 18.1 |

### Standalone Device Management

The device manager script can be used independently:

```bash
# Check if a device is running
./scripts/device-manager.sh android check
./scripts/device-manager.sh ios check

# Start a device (creates if none exists)
./scripts/device-manager.sh android start
./scripts/device-manager.sh ios start

# List available devices
./scripts/device-manager.sh android list
./scripts/device-manager.sh ios list
./scripts/device-manager.sh ios runtimes    # List available iOS runtimes
```

## Test Reports

Test results are generated using Allure. Results are written to `build/allure-results/` during test execution.

### Generate and View Report

```bash
# Clean previous results (recommended before a new test run)
./gradlew cleanAllureResults

# Run tests
./run_tests.sh android

# Generate and open report in browser
./gradlew allureServe
```

The report includes:
- Test execution timeline
- Pass/fail statistics
- Screenshots on failure
- Environment information
- Step-by-step test execution details

## Test Suites and Groups

### Available Suites

| Suite | File | Description |
|-------|------|-------------|
| Default | `testng.xml` | All tests |
| Smoke | `smoke.xml` | Quick validation tests |

### Test Groups

| Test Class | Groups | Description |
|------------|--------|-------------|
| `LoginTests` | login, smoke, regression | Login validation flows |
| `SwipeTests` | swipe, regression | Carousel swipe gestures |
| `WebViewTests` | webview, regression | WebView context switching |
| `DragAndDropTests` | drag, regression | Drag and drop interactions |

## Project Structure

```
├── run_tests.sh                    # Primary test entry point
├── scripts/
│   └── device-manager.sh           # Device lifecycle management
├── src/main/java/com/demo/framework/
│   ├── config/                     # Configuration management
│   ├── drivers/                    # Appium driver setup
│   ├── flows/                      # Business action flows
│   ├── pages/
│   │   ├── interfaces/             # Page contracts
│   │   ├── android/                # Android implementations
│   │   ├── ios/                    # iOS implementations
│   │   └── PageFactory.java        # Platform-aware page creation
│   └── utils/                      # Utilities (actions, waits, gestures)
├── src/test/java/com/demo/framework/
│   ├── tests/                      # Test classes
│   └── listeners/                  # Allure reporting listeners
├── src/test/resources/
│   ├── testng.xml                  # Default test suite
│   └── smoke.xml                   # Smoke test suite
└── testApps/
    ├── android/                    # Android APK
    └── ios/                        # iOS app bundle
```

## Architecture

```
Test Classes (LoginTests, SwipeTests, WebViewTests, DragAndDropTests)
       ↓
Flow Layer (LoginFlow, SwipeFlow, WebViewFlow, DragAndDropFlow)
       ↓
Page Interfaces (HomePage, LoginPage, SwipePage, WebViewPage, DragPage)
       ↓
Platform Implementations (AndroidHomePage, IOSHomePage, etc.)
       ↓
PageFactory → Appium Driver
```

## Notes

This project was developed with the assistance of AI-based tools (ChatGPT 5.2, Claude Opus 4.5)
for accelerating implementation and reducing boilerplate.

All architectural decisions, framework design, locator strategies,
and testing approaches were defined and reviewed by the author.

AI tools were used as a productivity aid, not as a substitute for engineering judgment.
