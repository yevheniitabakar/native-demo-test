#!/bin/bash

set -e

echo "=========================================="
echo "Mobile Test Automation Framework Runner"
echo "=========================================="

# Check Android Emulator
echo ""
echo "1. Checking Android Emulator..."
adb devices
EMULATOR_COUNT=$(adb devices | grep -c "emulator")
if [ "$EMULATOR_COUNT" -eq 0 ]; then
    echo "⚠️  No Android emulator running. Starting emulator..."
    # Find available emulator
    EMULATOR_NAME=$(emulator -list-avds | head -1)
    if [ -z "$EMULATOR_NAME" ]; then
        echo "❌ No Android Virtual Device found"
        exit 1
    fi
    echo "Starting emulator: $EMULATOR_NAME"
    emulator -avd "$EMULATOR_NAME" &
    sleep 30
fi
echo "✓ Android emulator is ready"

# Check Appium Server
echo ""
echo "2. Checking Appium Server..."
if ! curl -s http://127.0.0.1:4723/status > /dev/null 2>&1; then
    echo "⚠️  Appium server not running. Starting Appium..."
    nohup appium --log-level warn > /tmp/appium.log 2>&1 &
    sleep 8
fi
if curl -s http://127.0.0.1:4723/status > /dev/null 2>&1; then
    echo "✓ Appium server is ready"
else
    echo "❌ Failed to start Appium server"
    exit 1
fi

# Install app on emulator
echo ""
echo "3. Installing app on emulator..."
APK_PATH="testApps/android/android.wdio.native.app.v1.0.8.apk"
if [ -f "$APK_PATH" ]; then
    EMULATOR_ID=$(adb devices | grep emulator | head -1 | awk '{print $1}')
    if [ ! -z "$EMULATOR_ID" ]; then
        echo "Installing app on $EMULATOR_ID..."
        adb -s "$EMULATOR_ID" install -r "$APK_PATH" || echo "App might already be installed"
        echo "✓ App installed"
    fi
else
    echo "❌ App file not found: $APK_PATH"
    exit 1
fi

# Build and run tests
echo ""
echo "4. Building and running tests..."
./gradlew clean build

echo ""
echo "5. Running tests..."
./gradlew test

echo ""
echo "=========================================="
echo "✓ Test execution completed"
echo "=========================================="


