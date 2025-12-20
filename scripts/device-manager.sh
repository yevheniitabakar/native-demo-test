#!/bin/bash

# Device Manager Script
# Handles Android emulator and iOS simulator lifecycle

PLATFORM="${1:-android}"
ACTION="${2:-check}"  # check, start, stop, list

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# ============================================================================
# ANDROID FUNCTIONS
# ============================================================================

check_android_emulator() {
    local output=$(adb devices 2>/dev/null)
    local booted_count=$(echo "$output" | grep -c "emulator.*device" 2>/dev/null)
    booted_count=${booted_count:=0}  # Default to 0 if empty

    if [ "$booted_count" -gt 0 ]; then
        echo -e "${GREEN}✓${NC} Android emulator is booted"
        echo "$output" | grep "emulator"
        return 0
    else
        echo -e "${RED}✗${NC} No Android emulator is booted"
        return 1
    fi
}

list_android_emulators() {
    echo "Available Android emulators:"
    emulator -list-avds 2>/dev/null || echo "No emulators found"
}

start_android_emulator() {
    local target_api="${1:-36}"  # Default to API 36 (Android 15) or Android 14 (API 34)

    # First check if one is already booted
    if check_android_emulator > /dev/null 2>&1; then
        echo -e "${YELLOW}⚠${NC}  Android emulator already booted"
        return 0
    fi

    echo "Attempting to start Android emulator with API level $target_api..."

    # Get list of available emulators
    local emulators=$(emulator -list-avds 2>/dev/null)

    if [ -z "$emulators" ]; then
        echo -e "${RED}✗${NC} No Android emulators found. Create one first using:"
        echo "   avdmanager create avd -n MyEmulator -k 'system-images;android-34;google_apis;x86_64'"
        return 1
    fi

    # Try to find an emulator that matches API level or just use the first one
    local avd_to_start=$(echo "$emulators" | head -1)

    echo "Starting emulator: $avd_to_start"
    emulator -avd "$avd_to_start" -no-snapshot-load > /dev/null 2>&1 &
    local emulator_pid=$!
    echo "Emulator process started (PID: $emulator_pid)"

    echo "Waiting for emulator to boot (max 120 seconds)..."
    local wait_count=0
    local max_wait=60  # 60 iterations * 2 seconds = 120 seconds

    while [ $wait_count -lt $max_wait ]; do
        if check_android_emulator > /dev/null 2>&1; then
            echo -e "${GREEN}✓${NC} Emulator booted successfully"
            return 0
        fi

        echo -n "."
        sleep 2
        ((wait_count++))
    done

    echo ""
    echo -e "${RED}✗${NC} Emulator failed to boot within 120 seconds"
    return 1
}

# ============================================================================
# iOS FUNCTIONS
# ============================================================================

check_ios_simulator() {
    local output=$(xcrun simctl list devices 2>/dev/null)

    if echo "$output" | grep -q "(Booted)"; then
        echo -e "${GREEN}✓${NC} iOS simulator is booted"
        echo "$output" | grep "(Booted)"
        return 0
    else
        echo -e "${RED}✗${NC} No iOS simulator is booted"
        return 1
    fi
}

list_ios_simulators() {
    echo "Available iOS simulators:"
    xcrun simctl list devices 2>/dev/null | grep -E "iPhone|iPad" | head -20 || echo "No simulators found"
}

start_ios_simulator() {
    # First check if one is already booted
    if check_ios_simulator > /dev/null 2>&1; then
        echo -e "${YELLOW}⚠${NC}  iOS simulator already booted"
        return 0
    fi

    echo "Attempting to start iOS simulator..."

    # Get list of available simulators
    local simulator_udid=$(xcrun simctl list devices | grep "iPhone" | grep -v "(Booted)" | head -1 | sed -E 's/.*\(([A-F0-9-]+)\).*/\1/')

    if [ -z "$simulator_udid" ]; then
        echo -e "${RED}✗${NC} No iOS simulators found"
        list_ios_simulators
        return 1
    fi

    echo "Starting simulator with UDID: $simulator_udid"
    open -a Simulator --args -CurrentDeviceUDID "$simulator_udid" > /dev/null 2>&1 &

    echo "Waiting for simulator to boot (max 120 seconds)..."
    local wait_count=0
    local max_wait=60  # 60 iterations * 2 seconds = 120 seconds

    while [ $wait_count -lt $max_wait ]; do
        if check_ios_simulator > /dev/null 2>&1; then
            echo -e "${GREEN}✓${NC} Simulator booted successfully"
            return 0
        fi

        echo -n "."
        sleep 2
        ((wait_count++))
    done

    echo ""
    echo -e "${RED}✗${NC} Simulator failed to boot within 120 seconds"
    return 1
}

# ============================================================================
# MAIN LOGIC
# ============================================================================

case "$PLATFORM" in
    android)
        case "$ACTION" in
            check)
                check_android_emulator
                ;;
            start)
                start_android_emulator "$3"
                ;;
            list)
                list_android_emulators
                ;;
            *)
                echo "Unknown action: $ACTION"
                echo "Usage: $0 android [check|start|list]"
                exit 1
                ;;
        esac
        ;;
    ios)
        case "$ACTION" in
            check)
                check_ios_simulator
                ;;
            start)
                start_ios_simulator
                ;;
            list)
                list_ios_simulators
                ;;
            *)
                echo "Unknown action: $ACTION"
                echo "Usage: $0 ios [check|start|list]"
                exit 1
                ;;
        esac
        ;;
    *)
        echo "Unknown platform: $PLATFORM"
        echo "Usage: $0 [android|ios] [check|start|list]"
        exit 1
        ;;
esac

