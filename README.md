# Native Mobile Test Automation Framework

Production-ready mobile test automation framework using Appium, Java 17, TestNG, and Allure Reporting.

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| TestNG | 7.10.2 |
| Appium | 9.2.2 |
| Gradle | 8.6 |
| Allure | 2.27.0 |

## Quick Start

```bash
./gradlew clean build -x test    # Build
./gradlew test                    # Run tests (Appium server required)
./gradlew allureServe             # View Allure report
```

## Key Features

- **POM Architecture** with BasePage pattern
- **Cross-platform** Android & iOS support
- **Device Management** with ADB automation
- **SOLID Principles** fully implemented
- **Comprehensive Logging** via SLF4J + Logback
- **Allure Reporting** with screenshots on failure
- **ThreadLocal Drivers** for parallel execution

## Test Scenarios

**Scenario 1: Login Functionality**
- TC_1.1: Successful Login
- TC_1.2: Invalid Credentials
- TC_1.3: Empty Fields

**Scenario 2: Swipe/Gesture Actions**
- TC_2.1: Swipe Through Carousel

**Scenario 3: WebView Interaction**
- TC_3.1: WebView Navigation

**Scenario 4: Drag and Drop**
- TC_4.1: Successful Interaction
- TC_4.2: Element State Verification

## Configuration

Edit `src/test/resources/config/appium.properties`:

```properties
platformName=Android
deviceName=Android Emulator
platformVersion=16
appiumServerUrl=http://127.0.0.1:4723/wd/hub
app.android.path=testApps/android/android.wdio.native.app.v1.0.8.apk
app.ios.path=testApps/ios/Payload/wdiodemoapp.app
```

## Project Structure

```
src/main/java/com/demo/framework/
├── config/        - Configuration providers
├── drivers/       - Driver management, device managers
├── exceptions/    - Framework exceptions
├── pages/         - Page Objects
└── utils/         - Utilities (Wait, Action, Gesture, Screenshot)

src/test/java/com/demo/framework/
├── tests/smoke/   - Smoke test cases
├── tests/regression/ - Regression test suites
├── listeners/     - Allure test listeners
└── annotations/   - Custom test annotations
```

## Running Tests

```bash
# All tests
./gradlew test

# Smoke tests only
./gradlew test --tests "*.smoke.*"

# Regression tests only
./gradlew test --tests "*.regression.*"
```

## Best Practices

- Use Page Objects for all interactions
- Use explicit waits, never Thread.sleep()
- Log all operations
- Add Allure annotations (@Feature, @Story, @Step)
- Handle exceptions with FrameworkException
- Take screenshots on failure

