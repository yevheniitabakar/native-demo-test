# Project Architecture

## Directory Structure

```
native-demo-test/
├── src/main/java/com/demo/framework/
│   ├── config/              # AppiumConfig, ConfigProvider
│   ├── drivers/             # DriverManager, DriverFactory, device/
│   ├── exceptions/          # FrameworkException
│   ├── pages/               # BasePage, HomePage, LoginPage
│   └── utils/               # WaitUtils, ActionUtils, GestureUtils, etc.
├── src/main/resources/
│   └── logback.xml          # Logging configuration
├── src/test/java/com/demo/framework/
│   ├── tests/               # BaseTest, smoke/, regression/
│   ├── listeners/           # AllureTestListener
│   └── annotations/         # @Retry, @CaptureOnFailure
├── src/test/resources/
│   ├── testng.xml           # TestNG suite configuration
│   └── config/appium.properties
├── testApps/
│   ├── android/             # Android test app (APK)
│   └── ios/                 # iOS test app (extracted from zip)
├── build.gradle             # Gradle build configuration
└── gradle/wrapper/          # Gradle wrapper
```

## Architecture Layers

**Layer 1 - Configuration**: Load and manage test configuration from properties files.

**Layer 2 - Driver Management**: Create and manage Appium driver instances with ThreadLocal pattern for thread safety.

**Layer 3 - Device Management**: Manage Android emulators and iOS simulators using ADB and xcrun.

**Layer 4 - Page Objects**: Abstract UI interactions through Page Object Model with BasePage facade.

**Layer 5 - Utilities**: Provide helper methods for waits, actions, gestures, screenshots, logging, and test data.

**Layer 6 - Tests**: Test classes extending BaseTest with TestNG annotations and Allure reporting.

## Key Classes

| Class | Purpose |
|-------|---------|
| `AppiumConfig` | Configuration model |
| `ConfigProvider` | Loads properties from `appium.properties` |
| `DriverManager` | ThreadLocal driver lifecycle management |
| `DriverFactory` | Interface for driver creation |
| `AndroidDriverProvider` | Creates Android drivers |
| `IOSDriverProvider` | Creates iOS drivers |
| `IDeviceManager` | Interface for device management |
| `AndroidDeviceManager` | Manages Android emulators via ADB |
| `IOSDeviceManager` | Manages iOS simulators via xcrun |
| `BasePage` | Base class for Page Objects with utilities |
| `WaitUtils` | Explicit waits (visibilityOf, clickable, etc.) |
| `ActionUtils` | Element interactions (click, sendText, etc.) |
| `GestureUtils` | Mobile gestures (swipe, tap, pinch, etc.) |
| `ScreenshotUtils` | Capture screenshots with Allure integration |
| `BaseTest` | Test base class with setup/teardown |

## SOLID Principles

- **S** - Single Responsibility: Each class has one reason to change
- **O** - Open/Closed: Extensible through interfaces (IDeviceManager, DriverFactory)
- **L** - Liskov Substitution: Driver implementations properly substitute interface
- **I** - Interface Segregation: Minimal, focused interfaces
- **D** - Dependency Inversion: Depend on abstractions, not concrete classes

## Design Patterns

| Pattern | Implementation | Benefit |
|---------|---|---|
| **Factory** | DriverFactory, DeviceManagerFactory | Loose coupling |
| **Singleton** | DriverManager (ThreadLocal) | Single instance per thread |
| **Template Method** | BaseTest | Standardized lifecycle |
| **Strategy** | IDeviceManager implementations | Pluggable device managers |
| **Facade** | BasePage | Unified utilities interface |

