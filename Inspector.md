# Appium Inspector Configuration

This document provides the capabilities required to connect to the mobile application using Appium Inspector.

## Prerequisites

- Appium Server running on `http://127.0.0.1:4723`
- Android Emulator or iOS Simulator running
- Application installed or available at the specified path

---

## Android Configuration

### Capabilities (JSON Format)

```json
{
  "platformName": "Android",
  "appium:platformVersion": "14",
  "appium:deviceName": "Android Emulator",
  "appium:automationName": "UiAutomator2",
  "appium:appPackage": "com.wdiodemoapp",
  "appium:appActivity": "com.wdiodemoapp.MainActivity",
  "appium:newCommandTimeout": 120
}
```

### Connection Details

- **Remote Host**: `127.0.0.1`
- **Remote Port**: `4723`
- **Remote Path**: `/`

---

## iOS Configuration

### Capabilities (JSON Format)

```json
{
  "platformName": "iOS",
  "appium:platformVersion": "18.1",
  "appium:deviceName": "iPhone 16",
  "appium:automationName": "XCUITest",
  "appium:bundleId": "org.reactjs.native.example.wdiodemoapp",
  "appium:newCommandTimeout": 120
}
```

### Connection Details

- **Remote Host**: `127.0.0.1`
- **Remote Port**: `4723`
- **Remote Path**: `/`

---

## Capability Descriptions

| Capability | Description |
|------------|-------------|
| `platformName` | Mobile OS platform (Android or iOS) |
| `appium:platformVersion` | Version of the mobile OS |
| `appium:deviceName` | Name of the device/emulator/simulator |
| `appium:automationName` | Automation engine (UiAutomator2 for Android, XCUITest for iOS) |
| `appium:appPackage` | Android app package identifier (e.g., com.wdiodemoapp) |
| `appium:appActivity` | Android main activity to launch (e.g., com.wdiodemoapp.MainActivity) |
| `appium:bundleId` | iOS app bundle identifier (e.g., org.reactjs.native.example.wdiodemoapp) |
| `appium:newCommandTimeout` | Timeout in seconds for new commands |

---

## Notes

- **The app must be already installed** on the device/simulator before connecting via Appium Inspector
- Ensure the device/simulator is running before connecting
- For Android: Use `adb devices` to verify the device is connected
- For iOS: Use `xcrun simctl list devices booted` to verify the simulator is running
- To get Android app package and activity: `aapt dump badging <path-to-apk> | grep -E 'package|launchable-activity'`
- To get iOS bundle ID: `osascript -e 'id of app "<path-to-app>"'` or check Info.plist

