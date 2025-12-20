#!/bin/bash

set -e

echo "=========================================="
echo "Mobile Test Automation Framework Runner"
echo "=========================================="

# Default platform is android if not specified
PLATFORM="${1:-android}"

# Validate platform parameter
if [[ "$PLATFORM" != "android" && "$PLATFORM" != "ios" ]]; then
    echo "❌ Invalid platform: $PLATFORM"
    echo "Usage: ./run_tests.sh [android|ios]"
    echo "Or: ./gradlew test -Dplatform=android|ios"
    exit 1
fi

echo "Target platform: $PLATFORM"

# Check Device Availability
echo ""
echo "1. Checking device availability..."

if [ "$PLATFORM" = "android" ]; then
    adb devices
    EMULATOR_COUNT=$(adb devices | grep -c "emulator")
    if [ "$EMULATOR_COUNT" -eq 0 ]; then
        echo "❌ No Android emulator running"
        echo "Please start an Android emulator before running tests"
        exit 1
    fi
    echo "✓ Android emulator is ready"
elif [ "$PLATFORM" = "ios" ]; then
    if ! xcrun simctl list | grep -q "Booted"; then
        echo "❌ No iOS simulator running"
        echo "Please start an iOS simulator before running tests"
        exit 1
    fi
    echo "✓ iOS simulator is ready"
fi

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

# Install app on device
echo ""
echo "3. Installing app on device..."

if [ "$PLATFORM" = "android" ]; then
    APK_PATH="testApps/android/android.wdio.native.app.v1.0.8.apk"
    if [ -f "$APK_PATH" ]; then
        EMULATOR_ID=$(adb devices | grep emulator | head -1 | awk '{print $1}')
        if [ ! -z "$EMULATOR_ID" ]; then
            echo "Installing app on $EMULATOR_ID..."
            adb -s "$EMULATOR_ID" install -r "$APK_PATH" || echo "App might already be installed"
            echo "✓ App installed"
        else
            echo "❌ Could not find emulator ID"
            exit 1
        fi
    else
        echo "❌ App file not found: $APK_PATH"
        exit 1
    fi
elif [ "$PLATFORM" = "ios" ]; then
    echo "ℹ️  iOS app installation handled by Appium"
fi

# Build and run tests
echo ""
echo "4. Building and running tests..."
./gradlew clean build

echo ""
echo "5. Running tests for platform: $PLATFORM..."
./gradlew test -Dplatform="$PLATFORM"

echo ""
echo "=========================================="
echo "✓ Test execution completed"
echo "=========================================="


