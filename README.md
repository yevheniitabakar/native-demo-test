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

- **SOLID Principles** and design patterns
- **Layered Architecture**: Tests → Flows → Page Interfaces → Platform Implementations
- **Cross-platform Support**: Android & iOS with platform-specific Page Objects
- **PageFactory Pattern**: Automatic platform resolution
- **TestNG Groups**: smoke, regression, login, swipe, webview, drag
- **Allure Reporting** with environment details and screenshots
- **Comprehensive Logging**: SLF4J + Logback

## Prerequisites

- Java 17
- Android SDK (for Android) / Xcode (for iOS)
- Appium: `npm install -g appium`
- Appium drivers: `appium driver install uiautomator2` / `appium driver install xcuitest`

### WebView Testing (Android)

ChromeDriver is required for WebView context switching. Setup:

```bash
# Install Chromium driver (optional, for auto-download support)
appium driver install chromium

# Or manually download ChromeDriver matching your WebView Chrome version:
mkdir -p ~/.appium/chromedriver
cd ~/.appium/chromedriver
# Download from: https://chromedriver.chromium.org/downloads
# Example for Chrome 113:
curl -O https://chromedriver.storage.googleapis.com/113.0.5672.63/chromedriver_mac64.zip
unzip chromedriver_mac64.zip && rm chromedriver_mac64.zip
```

## Running Tests

### Using run_tests.sh (Recommended)
Auto-manages device lifecycle (boots emulator/simulator if needed).

```bash
./run_tests.sh android    # Android tests
./run_tests.sh ios        # iOS tests
```

### Using Gradle (Device must be booted)

```bash
./gradlew test -Dplatform=android                               # All tests (default)
./gradlew test -Dplatform=ios                                   # All iOS tests
./gradlew test -Dplatform=android -DsuiteXmlFile=smoke.xml      # Smoke suite only
```

## Test Suites

| Suite | File | Description |
|-------|------|-------------|
| Default | `testng.xml` | All tests |
| Smoke | `smoke.xml` | Quick validation (group: smoke) |

## Test Scenarios & Groups

| Test Class | Groups | Test Cases |
|------------|--------|------------|
| `SampleTest` | smoke | Framework bootstrap sanity |
| `LoginTests` | login, smoke, regression | TC_1.1-1.3: Login flows |
| `SwipeTests` | swipe, smoke, regression | TC_2.1-2.3: Carousel swipe |
| `WebViewTests` | webview, smoke, regression | TC_3.1-3.3: WebView navigation |
| `DragAndDropTests` | drag, smoke, regression | TC_4.1-4.3: Drag & drop |

## Architecture

```
Test Classes (LoginTests, SwipeTests, WebViewTests, DragAndDropTests)
       ↓
Flow Layer (LoginFlow, SwipeFlow, WebViewFlow, DragAndDropFlow)
       ↓
Page Interfaces (HomePage, LoginPage, SwipePage, WebViewPage, DragPage)
       ↓
Platform Implementations (Android*/iOS*)
       ↓
PageFactory → Appium Driver
```

## Project Structure

```
src/main/java/com/demo/framework/
├── config/           Configuration management
├── drivers/          Appium & device management
├── flows/            Business action flows (LoginFlow, SwipeFlow, etc.)
├── pages/
│   ├── interfaces/   Page contracts (HomePage, LoginPage, etc.)
│   ├── android/      Android implementations
│   ├── ios/          iOS implementations
│   ├── BasePage.java
│   └── PageFactory.java
└── utils/            Utilities

src/test/java/com/demo/framework/
├── tests/            Feature-based test classes
│   ├── smoke/        SampleTest
│   ├── LoginTests.java
│   ├── SwipeTests.java
│   ├── WebViewTests.java
│   └── DragAndDropTests.java
└── listeners/        Allure reporting

src/test/resources/
├── smoke.xml         Smoke suite
└── testng.xml        Default suite (all tests)
```

## Generate Allure Report

```bash
# Clean previous Allure results (recommended before new test run)
./gradlew cleanAllureResults

# Run tests
./gradlew test -Dplatform=android

# Generate and open Allure report
./gradlew allureServe
```
