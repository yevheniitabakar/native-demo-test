# Quick Start Guide

## Prerequisites

- Java 17+
- Gradle 8.6+
- Appium Server 2.0+
- Android SDK or Xcode (optional)

## Setup

```bash
git clone <repository>
cd native-demo-test
./gradlew clean build -x test
```

## Configuration

Edit `src/test/resources/config/appium.properties`:

```properties
platformName=Android
deviceName=Android Emulator
platformVersion=13
automationName=UiAutomator2
appiumServerUrl=http://127.0.0.1:4723/wd/hub
```

## Running Tests

Start Appium server (required):
```bash
appium
```

Run tests:
```bash
./gradlew test                          # All tests
./gradlew test --tests "LoginTest"      # Specific test
./gradlew test --tests "*Smoke*"        # Pattern match
```

View report:
```bash
./gradlew allureServe
```

## First Test

```java
public class FirstTest extends BaseTest {
    @Test
    public void appLaunches() {
        LoginPage page = new LoginPage();
        assertTrue(page.isPageLoaded());
    }
}
```

Add to `src/test/resources/testng.xml`:
```xml
<class name="com.demo.framework.tests.smoke.FirstTest"/>
```

Run: `./gradlew test`

## Test Apps

**Android**: `testApps/android/android.wdio.native.app.v1.0.8.apk`  
**iOS**: `testApps/ios/Payload/wdiodemoapp.app`

Paths are configured in `appium.properties`.

## Troubleshooting

**Driver not initialized**: Ensure @BeforeMethod is called and Appium server is running.  
**Appium server not found**: Run `appium` in a separate terminal.  
**Device not found**: Check `adb devices` (Android) or `xcrun simctl list` (iOS).

