# Test Applications

Mobile applications used for testing with this framework.

## Structure

```
testApps/
├── android/
│   └── android.wdio.native.app.v1.0.8.apk
├── ios/
│   └── Payload/
│       └── wdiodemoapp.app
└── README.md
```

## Android

**File**: `android.wdio.native.app.v1.0.8.apk`

- Native Android application
- Ready for installation on devices or emulators
- Referenced in `appium.properties` as `app.android.path`

## iOS

**Location**: `ios/Payload/wdiodemoapp.app`

- iOS app bundle (extracted from zip)
- Required format for Appium deployment
- Referenced in `appium.properties` as `app.ios.path`

## Usage

Configure `src/test/resources/config/appium.properties`:

```properties
# For Android
platformName=Android
app.android.path=testApps/android/android.wdio.native.app.v1.0.8.apk

# For iOS
platformName=iOS
app.ios.path=testApps/ios/Payload/wdiodemoapp.app
```

Then run: `./gradlew test`

## Adding New Apps

1. Place APK files in `android/` directory
2. Place `.app` bundles in `ios/Payload/` directory
3. Update app paths in `appium.properties`

