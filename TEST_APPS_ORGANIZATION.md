# Test Apps Organization

## Current Structure

```
testApps/
├── android/
│   └── android.wdio.native.app.v1.0.8.apk
├── ios/
│   └── Payload/
│       └── wdiodemoapp.app
└── README.md
```

## Android App

**Location**: `testApps/android/android.wdio.native.app.v1.0.8.apk`

- Ready for direct installation on Android devices/emulators
- Referenced in `appium.properties` as `app.android.path`

## iOS App

**Location**: `testApps/ios/Payload/wdiodemoapp.app`

- Extracted from original zip file
- `.app` bundle format required by Appium
- Referenced in `appium.properties` as `app.ios.path`

## Configuration

Both apps are configured in `src/test/resources/config/appium.properties`:

```properties
app.android.path=testApps/android/android.wdio.native.app.v1.0.8.apk
app.ios.path=testApps/ios/Payload/wdiodemoapp.app
```

Change the `app` property to point to desired app before running tests.

## Adding New Test Apps

1. Place APK files in `testApps/android/`
2. Place extracted `.app` bundles in `testApps/ios/Payload/`
3. Update `appium.properties` with new app path
4. Run tests

## Notes

- Do not commit zipped iOS apps; always extract to `.app` bundle
- APK files should be unsigned or signed with Appium test certificate
- Ensure apps are compatible with target platform versions in `appium.properties`

