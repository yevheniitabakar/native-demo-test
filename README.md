# Native Mobile Test Automation Framework

Production-ready mobile test automation framework using Appium, Java 17, TestNG, and Allure Reporting.

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 17 |
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

- **POM Architecture** with BasePage facade
- **Cross-platform** Android & iOS support
- **Device Management** with ADB and xcrun
- **SOLID Principles** fully implemented
- **Logging** via SLF4J + Logback
- **Allure Reporting** with screenshots on failure
- **ThreadLocal Drivers** for parallel execution

## Configuration

Edit `src/test/resources/config/appium.properties` to change platform, device, or app paths:

```properties
platformName=Android                    # Android or iOS
deviceName=Android Emulator             # Emulator/simulator name
platformVersion=13                      # OS version
appiumServerUrl=http://127.0.0.1:4723/wd/hub
app.android.path=testApps/android/android.wdio.native.app.v1.0.8.apk
app.ios.path=testApps/ios/Payload/wdiodemoapp.app
```

## Writing Tests

```java
public class LoginTest extends BaseTest {
    @Test(description = "Valid login")
    public void validLogin() {
        LoginPage page = new LoginPage();
        assertTrue(page.isPageLoaded());
        page.login("user@test.com", "password");
    }
}
```

## Project Structure

```
src/main/java/com/demo/framework/
├── config/        - AppiumConfig, ConfigProvider
├── drivers/       - DriverManager, DriverFactory, device management
├── exceptions/    - FrameworkException
├── pages/         - BasePage, HomePage, LoginPage
└── utils/         - Wait, Action, Screenshot, Gesture, etc.

src/test/java/com/demo/framework/
├── tests/         - BaseTest, smoke/, regression/
├── listeners/     - AllureTestListener
└── annotations/   - @Retry, @CaptureOnFailure
```

## Utilities

**WaitUtils**: `untilVisible()`, `untilClickable()`, `untilPresent()`  
**ActionUtils**: `click()`, `sendText()`, `getText()`  
**GestureUtils**: `swipeLeft()`, `swipeRight()`, `tap()`  
**ScreenshotUtils**: Automatic capture on failure  

## Device Management

```java
AndroidDeviceManager manager = new AndroidDeviceManager();
List<DeviceInfo> devices = manager.getAvailableDevices();
manager.startDevice("emulator-5554");
```

## Best Practices

- Use Page Objects for all element interactions
- Use explicit waits (WaitUtils), never Thread.sleep()
- Log all operations
- Use TestDataUtils for test data generation
- Add Allure annotations (@Feature, @Story, @Step)
- Handle exceptions with FrameworkException

## Documentation

- **QUICKSTART.md** - Setup and first test
- **PROJECT_STRUCTURE.md** - Architecture details
- **COMMANDS.md** - Build and test commands
- **CHECKLIST.md** - Feature checklist

---

**Version**: 1.0.0 | **Status**: ✅ Production Ready

