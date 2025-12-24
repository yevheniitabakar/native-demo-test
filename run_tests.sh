#!/bin/bash

echo "=========================================="
echo "Mobile Test Automation Framework Runner"
echo "=========================================="

# Get platform from argument or prompt user
if [ -n "$1" ]; then
    PLATFORM="$1"
else
    echo ""
    echo "Please select a platform:"
    echo "  1) android"
    echo "  2) ios"
    echo ""
    read -p "Enter your choice (android/ios or 1/2): " user_input
    
    case "$user_input" in
        1|android|Android|ANDROID)
            PLATFORM="android"
            ;;
        2|ios|iOS|IOS)
            PLATFORM="ios"
            ;;
        *)
            echo "❌ Invalid selection: $user_input"
            echo "Please enter 'android', 'ios', '1', or '2'"
            exit 1
            ;;
    esac
fi

# Validate platform parameter
if [[ "$PLATFORM" != "android" && "$PLATFORM" != "ios" ]]; then
    echo "❌ Invalid platform: $PLATFORM"
    echo "Usage: ./run_tests.sh [android|ios]"
    echo "Example: ./run_tests.sh android"
    exit 1
fi

echo "Target platform: $PLATFORM"

# Check and Start Device
echo ""
echo "1. Checking device for platform: $PLATFORM..."

# Make device-manager.sh executable
chmod +x scripts/device-manager.sh

if [ "$PLATFORM" = "android" ]; then
    echo "Checking Android emulator..."
    if ! bash scripts/device-manager.sh android check 2>/dev/null; then
        echo "No Android emulator is booted. Will start/create one..."
        if bash scripts/device-manager.sh android start; then
            echo "✓ Android emulator started and ready"
        else
            echo "❌ Failed to start/create Android emulator"
            echo "Available Android emulators:"
            bash scripts/device-manager.sh android list
            exit 1
        fi
    else
        echo "✓ Android emulator is already booted"
        # Ensure emulator is fully ready even if already booted
        echo "Verifying emulator is fully initialized..."
        BOOT_COMPLETE=$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')
        if [ "$BOOT_COMPLETE" != "1" ]; then
            echo "Waiting for emulator to fully initialize..."
            for i in {1..60}; do
                BOOT_COMPLETE=$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')
                if [ "$BOOT_COMPLETE" = "1" ]; then
                    echo "✓ Emulator fully initialized"
                    break
                fi
                sleep 2
            done
        fi
    fi

elif [ "$PLATFORM" = "ios" ]; then
    echo "Checking iOS simulator..."
    if ! bash scripts/device-manager.sh ios check 2>/dev/null; then
        echo "No iOS simulator is booted. Will start/create one..."
        if bash scripts/device-manager.sh ios start; then
            echo "✓ iOS simulator is ready"
        else
            echo "❌ Failed to start/create iOS simulator"
            echo "Available iOS simulators:"
            bash scripts/device-manager.sh ios list
            exit 1
        fi
    else
        echo "✓ iOS simulator is already booted"
    fi
fi

# Give device a moment to stabilize before Appium
if [ "$PLATFORM" = "android" ]; then
    echo "Waiting for Android system services to stabilize..."
    sleep 5
elif [ "$PLATFORM" = "ios" ]; then
    echo "Waiting for iOS simulator to stabilize..."
    sleep 3
fi

# Check Appium Server
echo ""
echo "2. Checking Appium Server..."
if ! curl -s http://127.0.0.1:4723/status > /dev/null 2>&1; then
    echo "⚠️  Appium server not running. Starting Appium..."
    nohup appium --log-level warn > /tmp/appium.log 2>&1 &
    APPIUM_PID=$!
    sleep 8

    if ! curl -s http://127.0.0.1:4723/status > /dev/null 2>&1; then
        echo "❌ Failed to start Appium server"
        exit 1
    fi
fi

if curl -s http://127.0.0.1:4723/status > /dev/null 2>&1; then
    echo "✓ Appium server is ready"
else
    echo "❌ Appium server is not responding"
    exit 1
fi

# Install app on device
echo ""
echo "3. Installing app on device..."

if [ "$PLATFORM" = "android" ]; then
    APK_PATH="testApps/android/android.wdio.native.app.v1.0.8.apk"
    if [ -f "$APK_PATH" ]; then
        EMULATOR_ID=$(adb devices | grep "emulator" | awk '{print $1}' | head -1 || true)
        if [ -n "$EMULATOR_ID" ]; then
            echo "Installing app on $EMULATOR_ID..."
            if adb -s "$EMULATOR_ID" install -r "$APK_PATH" > /dev/null 2>&1; then
                echo "✓ App installed successfully"
            else
                echo "⚠️  App install warning (may already exist)"
            fi
        else
            echo "❌ Could not find active emulator"
            exit 1
        fi
    else
        echo "❌ App file not found: $APK_PATH"
        exit 1
    fi

elif [ "$PLATFORM" = "ios" ]; then
    IPA_PATH="testApps/ios/Payload/wdiodemoapp.app"
    if [ -d "$IPA_PATH" ]; then
        SIMULATOR_UDID=$(xcrun simctl list devices | grep "(Booted)" | head -1 | sed -E 's/.*\(([A-F0-9-]{36})\).*/\1/' || true)
        if [ -n "$SIMULATOR_UDID" ]; then
            echo "Installing app on iOS Simulator: $SIMULATOR_UDID..."
            if xcrun simctl install "$SIMULATOR_UDID" "$IPA_PATH" > /dev/null 2>&1; then
                echo "✓ App installed successfully"
            else
                echo "⚠️  App install warning (may already exist)"
            fi

            echo "Launching app on iOS Simulator..."
            BUNDLE_ID="org.reactjs.native.example.wdiodemoapp"
            if xcrun simctl launch "$SIMULATOR_UDID" "$BUNDLE_ID" > /dev/null 2>&1; then
                echo "✓ App launched"
            else
                echo "⚠️  App launch warning"
            fi
        else
            echo "❌ Could not find booted iOS simulator"
            exit 1
        fi
    else
        echo "❌ App directory not found: $IPA_PATH"
        exit 1
    fi
fi

# Run tests with error handling
echo ""
echo "4. Running tests for platform: $PLATFORM..."
echo "Sequential test execution (thread-count=1) ensures consistent device state"
echo "=========================================="

set -e
./gradlew test -Dplatform="$PLATFORM"
EXIT_CODE=$?

echo ""
echo "=========================================="
if [ $EXIT_CODE -eq 0 ]; then
    echo "✓ Test execution completed successfully"
else
    echo "❌ Test execution failed with exit code: $EXIT_CODE"
fi
echo "=========================================="

# Cleanup: Shutdown device
echo ""
echo "5. Cleaning up - shutting down device..."

if [ "$PLATFORM" = "android" ]; then
    EMULATOR_ID=$(adb devices | grep "emulator" | awk '{print $1}' | head -1 || true)
    if [ -n "$EMULATOR_ID" ]; then
        echo "Shutting down Android emulator: $EMULATOR_ID..."
        adb -s "$EMULATOR_ID" emu kill > /dev/null 2>&1 || true
        echo "✓ Android emulator shutdown initiated"
    else
        echo "No Android emulator to shutdown"
    fi

elif [ "$PLATFORM" = "ios" ]; then
    SIMULATOR_UDID=$(xcrun simctl list devices | grep "(Booted)" | head -1 | sed -E 's/.*\(([A-F0-9-]{36})\).*/\1/' || true)
    if [ -n "$SIMULATOR_UDID" ]; then
        echo "Shutting down iOS simulator: $SIMULATOR_UDID..."
        xcrun simctl shutdown "$SIMULATOR_UDID" > /dev/null 2>&1 || true
        echo "✓ iOS simulator shutdown completed"
    else
        echo "No iOS simulator to shutdown"
    fi
fi

echo ""
echo "=========================================="
echo "Cleanup complete. Exiting."
echo "=========================================="

exit $EXIT_CODE


