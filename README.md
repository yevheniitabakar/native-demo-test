# Native Mobile Test Automation Framework

Production-ready mobile test automation framework using Appium, Java 21, TestNG, and Allure Reporting.

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| TestNG | 7.10.2 |
| Appium | 9.2.2 |
| Gradle | 8.6 |
| Allure | 2.27.0 |

## Features

- **POM Architecture** with BasePage pattern
- **Cross-platform** Android & iOS support
- **ThreadLocal Drivers** for parallel execution
- **SOLID Principles** fully implemented
- **Comprehensive Logging** via SLF4J + Logback
- **Allure Reporting** with screenshots on failure

## Quick Start

See [EXECUTION_GUIDE.md](EXECUTION_GUIDE.md) for detailed setup instructions.

```bash
./gradlew clean build -x test    # Build
./gradlew test                    # Run tests
./gradlew allureServe             # View Allure report
```

## Test Scenarios

- **Scenario 1:** Login (TC_1.1, TC_1.2, TC_1.3)
- **Scenario 2:** Swipe Gestures (TC_2.1)
- **Scenario 3:** WebView (TC_3.1)
- **Scenario 4:** Drag & Drop (TC_4.1, TC_4.2)

## Project Structure

```
src/main/java/com/demo/framework/
├── config/        - Configuration providers
├── drivers/       - Driver & device management
├── pages/         - Page Objects
└── utils/         - Utilities

src/test/java/com/demo/framework/
├── tests/         - Test cases (smoke, regression)
└── listeners/     - Allure reporting

testApps/
├── android/       - Android APK
└── ios/           - iOS app bundle
```

## Documentation

- [EXECUTION_GUIDE.md](EXECUTION_GUIDE.md) - Setup and run tests
- [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) - Architecture details


