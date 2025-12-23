#!/bin/bash

# Device Manager Script
# Handles Android emulator and iOS simulator lifecycle
# Auto-creates devices if they don't exist based on project configuration

PLATFORM="${1:-android}"
ACTION="${2:-check}"  # check, start, stop, list

# ============================================================================
# DEFAULT CONFIGURATION (matches appium.properties)
# ============================================================================
# Android defaults
ANDROID_API_LEVEL="34"           # Android 14
ANDROID_AVD_NAME="Pixel_7_API_34"
ANDROID_DEVICE_TYPE="pixel_7"
ANDROID_SYSTEM_IMAGE="system-images;android-${ANDROID_API_LEVEL};google_apis;arm64-v8a"
# Fallback for x86_64 architecture
ANDROID_SYSTEM_IMAGE_X86="system-images;android-${ANDROID_API_LEVEL};google_apis;x86_64"

# iOS defaults  
IOS_DEVICE_NAME="iPhone 16"
IOS_RUNTIME="26.1"               # iOS version - update to match your installed runtime

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ============================================================================
# UTILITY FUNCTIONS
# ============================================================================

log_info() {
    echo -e "${BLUE}ℹ${NC}  $1"
}

log_success() {
    echo -e "${GREEN}✓${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}⚠${NC}  $1"
}

log_error() {
    echo -e "${RED}✗${NC} $1"
}

# ============================================================================
# ANDROID FUNCTIONS
# ============================================================================

check_android_emulator() {
    local output=$(adb devices 2>/dev/null)
    local booted_count=$(echo "$output" | grep -c "emulator.*device" 2>/dev/null)
    booted_count=${booted_count:=0}

    if [ "$booted_count" -gt 0 ]; then
        log_success "Android emulator is booted"
        echo "$output" | grep "emulator"
        return 0
    else
        log_error "No Android emulator is booted"
        return 1
    fi
}

# Wait for Android emulator to be fully ready (not just booted)
wait_for_android_ready() {
    local max_wait="${1:-120}"  # Default 120 seconds
    local wait_count=0
    
    log_info "Waiting for Android emulator to be fully ready (max ${max_wait}s)..."
    
    # Phase 1: Wait for boot to complete
    while [ $wait_count -lt $max_wait ]; do
        local boot_complete=$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')
        local boot_anim=$(adb shell getprop init.svc.bootanim 2>/dev/null | tr -d '\r')
        
        if [ "$boot_complete" = "1" ] && [ "$boot_anim" = "stopped" ]; then
            echo ""
            log_info "Boot complete, checking system services..."
            break
        fi
        
        echo -n "."
        sleep 2
        ((wait_count+=2))
    done
    
    if [ $wait_count -ge $max_wait ]; then
        echo ""
        log_warning "Boot timeout reached, continuing anyway..."
        return 0
    fi
    
    # Phase 2: Wait for package manager to be ready
    local pm_wait=0
    local pm_max_wait=30
    while [ $pm_wait -lt $pm_max_wait ]; do
        # Try to list packages - if PM is ready, this works
        if adb shell pm list packages 2>/dev/null | grep -q "package:" ; then
            log_success "Package manager is ready"
            break
        fi
        echo -n "."
        sleep 2
        ((pm_wait+=2))
    done
    
    # Phase 3: Additional stabilization wait
    log_info "Waiting for system stabilization..."
    sleep 5
    
    log_success "Android emulator is fully ready"
    return 0
}

list_android_emulators() {
    echo "Available Android emulators:"
    emulator -list-avds 2>/dev/null || echo "No emulators found"
}

check_android_system_image() {
    local image="$1"
    sdkmanager --list 2>/dev/null | grep -q "$image"
    return $?
}

install_android_system_image() {
    local image="$1"
    log_info "Installing Android system image: $image"
    echo "y" | sdkmanager "$image" 2>/dev/null
    if [ $? -eq 0 ]; then
        log_success "System image installed successfully"
        return 0
    else
        log_error "Failed to install system image"
        return 1
    fi
}

create_android_emulator() {
    local avd_name="$1"
    local api_level="$2"
    
    log_info "Creating Android emulator: $avd_name (API $api_level)"
    
    # Determine architecture
    local arch=$(uname -m)
    local system_image=""
    
    if [ "$arch" = "arm64" ] || [ "$arch" = "aarch64" ]; then
        system_image="system-images;android-${api_level};google_apis;arm64-v8a"
    else
        system_image="system-images;android-${api_level};google_apis;x86_64"
    fi
    
    # Check if system image exists, install if not
    if ! check_android_system_image "$system_image"; then
        log_warning "System image not found, attempting to install..."
        if ! install_android_system_image "$system_image"; then
            log_error "Could not install system image: $system_image"
            return 1
        fi
    fi
    
    # Create the AVD
    log_info "Creating AVD with system image: $system_image"
    echo "no" | avdmanager create avd \
        -n "$avd_name" \
        -k "$system_image" \
        -d "$ANDROID_DEVICE_TYPE" \
        --force 2>/dev/null
    
    if [ $? -eq 0 ]; then
        log_success "Emulator '$avd_name' created successfully"
        return 0
    else
        log_error "Failed to create emulator"
        return 1
    fi
}

start_android_emulator() {
    local target_api="${1:-$ANDROID_API_LEVEL}"
    local avd_name="${2:-$ANDROID_AVD_NAME}"

    # First check if one is already booted
    if check_android_emulator > /dev/null 2>&1; then
        log_warning "Android emulator already booted"
        # Still wait for it to be fully ready
        wait_for_android_ready 60
        return 0
    fi

    log_info "Looking for Android emulator with API level $target_api..."

    # Get list of available emulators
    local emulators=$(emulator -list-avds 2>/dev/null)
    local avd_to_start=""

    if [ -n "$emulators" ]; then
        # Try to find an emulator that matches API level
        avd_to_start=$(echo "$emulators" | grep -i "api_${target_api}" | head -1)
        
        if [ -z "$avd_to_start" ]; then
            avd_to_start=$(echo "$emulators" | grep -E "[_-]${target_api}[^0-9]|[_-]${target_api}$" | head -1)
        fi
    fi

    # If no matching emulator found, create one
    if [ -z "$avd_to_start" ]; then
        log_warning "No emulator found for API $target_api"
        log_info "Creating new emulator: $avd_name"
        
        if ! create_android_emulator "$avd_name" "$target_api"; then
            log_error "Failed to create emulator"
            return 1
        fi
        avd_to_start="$avd_name"
    fi

    log_info "Starting emulator: $avd_to_start"
    emulator -avd "$avd_to_start" -no-snapshot-load > /dev/null 2>&1 &
    local emulator_pid=$!
    log_info "Emulator process started (PID: $emulator_pid)"

    echo "Waiting for emulator to boot (max 120 seconds)..."
    local wait_count=0
    local max_wait=60

    while [ $wait_count -lt $max_wait ]; do
        if check_android_emulator > /dev/null 2>&1; then
            log_success "Emulator detected by ADB"
            # Now wait for it to be fully ready
            wait_for_android_ready 90
            return 0
        fi
        echo -n "."
        sleep 2
        ((wait_count++))
    done

    echo ""
    log_error "Emulator failed to boot within 120 seconds"
    return 1
}

# ============================================================================
# iOS FUNCTIONS
# ============================================================================

check_ios_simulator() {
    local output=$(xcrun simctl list devices 2>/dev/null)

    if echo "$output" | grep -q "(Booted)"; then
        log_success "iOS simulator is booted"
        echo "$output" | grep "(Booted)"
        return 0
    else
        log_error "No iOS simulator is booted"
        return 1
    fi
}

# Wait for iOS simulator to be fully ready
wait_for_ios_ready() {
    local udid="$1"
    local max_wait="${2:-60}"
    local wait_count=0
    
    log_info "Waiting for iOS simulator to be fully ready (max ${max_wait}s)..."
    
    # Wait for simulator state to be "Booted"
    while [ $wait_count -lt $max_wait ]; do
        local state=$(xcrun simctl list devices 2>/dev/null | grep "$udid" | grep -o "Booted" || true)
        if [ -n "$state" ]; then
            echo ""
            log_info "Simulator booted, waiting for SpringBoard..."
            break
        fi
        echo -n "."
        sleep 2
        ((wait_count+=2))
    done
    
    # Wait for SpringBoard to be ready (indicates UI is responsive)
    local sb_wait=0
    local sb_max_wait=30
    while [ $sb_wait -lt $sb_max_wait ]; do
        # Check if SpringBoard is running - use simpler check
        if xcrun simctl spawn "$udid" launchctl list 2>/dev/null | grep -q "SpringBoard"; then
            log_success "SpringBoard is ready"
            break
        fi
        echo -n "."
        sleep 2
        ((sb_wait+=2))
    done
    
    # Additional stabilization
    log_info "Waiting for system stabilization..."
    sleep 5
    
    log_success "iOS simulator is fully ready"
    return 0
}

list_ios_simulators() {
    echo "Available iOS simulators:"
    xcrun simctl list devices available 2>/dev/null | grep -E "iPhone|iPad" | head -30 || echo "No simulators found"
}

list_ios_runtimes() {
    echo "Available iOS runtimes:"
    xcrun simctl list runtimes 2>/dev/null | grep -E "iOS" || echo "No runtimes found"
}

get_ios_runtime_id() {
    local version="$1"
    # Try to find exact match first, then partial match
    local runtime_id=$(xcrun simctl list runtimes 2>/dev/null | grep "iOS $version" | head -1 | sed -E 's/.*\(([^)]+)\)[^)]*$/\1/')
    
    if [ -z "$runtime_id" ]; then
        # Try partial match (e.g., 18.1 matches 18.1.x)
        runtime_id=$(xcrun simctl list runtimes 2>/dev/null | grep "iOS ${version%.*}" | head -1 | sed -E 's/.*\(([^)]+)\)[^)]*$/\1/')
    fi
    
    echo "$runtime_id"
}

get_ios_device_type_id() {
    local device_name="$1"
    # Convert device name to device type identifier
    local device_type=$(xcrun simctl list devicetypes 2>/dev/null | grep -i "$device_name" | head -1 | sed -E 's/.*\(([^)]+)\)$/\1/')
    echo "$device_type"
}

create_ios_simulator() {
    local device_name="$1"
    local ios_version="$2"
    
    log_info "Creating iOS simulator: $device_name (iOS $ios_version)"
    
    # Get runtime ID
    local runtime_id=$(get_ios_runtime_id "$ios_version")
    if [ -z "$runtime_id" ]; then
        log_error "iOS runtime $ios_version not found"
        log_info "Available runtimes:"
        list_ios_runtimes
        return 1
    fi
    log_info "Found runtime: $runtime_id"
    
    # Get device type ID
    local device_type=$(get_ios_device_type_id "$device_name")
    if [ -z "$device_type" ]; then
        log_error "Device type '$device_name' not found"
        log_info "Available device types:"
        xcrun simctl list devicetypes 2>/dev/null | grep -i "iPhone" | head -10
        return 1
    fi
    log_info "Found device type: $device_type"
    
    # Create simulator with a unique name
    local sim_name="${device_name} - iOS ${ios_version}"
    log_info "Creating simulator: '$sim_name'"
    
    local new_udid=$(xcrun simctl create "$sim_name" "$device_type" "$runtime_id" 2>/dev/null)
    
    if [ -n "$new_udid" ]; then
        log_success "Simulator created successfully (UDID: $new_udid)"
        echo "$new_udid"
        return 0
    else
        log_error "Failed to create simulator"
        return 1
    fi
}

find_ios_simulator() {
    local device_name="$1"
    local ios_version="$2"
    
    # Log to stderr so UDID output stays clean
    echo -e "${BLUE}ℹ${NC}  Searching for simulator: '$device_name' with iOS $ios_version" >&2
    
    # Get runtime identifier for the iOS version
    local runtime_id=$(get_ios_runtime_id "$ios_version")
    
    if [ -z "$runtime_id" ]; then
        echo -e "${YELLOW}⚠${NC}  Runtime iOS $ios_version not found, will search all runtimes" >&2
    else
        echo -e "${BLUE}ℹ${NC}  Target runtime: $runtime_id" >&2
    fi
    
    # Parse simctl output to find exact device match
    local simulator_udid=""
    local current_runtime=""
    local in_target_runtime=false
    
    while IFS= read -r line; do
        # Check for runtime section headers (e.g., "-- iOS 18.1 --")
        if echo "$line" | grep -qE "^-- iOS"; then
            current_runtime=$(echo "$line" | sed -E 's/-- iOS ([0-9.]+) --/\1/')
            # Check if this is our target runtime
            if [ -n "$runtime_id" ] && echo "$line" | grep -qF "$ios_version"; then
                in_target_runtime=true
            elif [ -z "$runtime_id" ]; then
                # No specific runtime, accept any
                in_target_runtime=true
            else
                in_target_runtime=false
            fi
            continue
        fi
        
        # Skip if not in target runtime and we have a specific target
        if [ -n "$runtime_id" ] && [ "$in_target_runtime" = false ]; then
            continue
        fi
        
        # Look for EXACT device name match (not substring)
        # Device lines look like: "    iPhone 16 (UUID) (Shutdown)"
        if echo "$line" | grep -qE "^[[:space:]]+${device_name}[[:space:]]+\\("; then
            # Skip if already booted
            if echo "$line" | grep -q "Booted"; then
                continue
            fi
            # Extract UDID
            simulator_udid=$(echo "$line" | sed -E 's/.*\(([A-F0-9-]{36})\).*/\1/')
            if [ -n "$simulator_udid" ]; then
                echo -e "${GREEN}✓${NC} Found exact match: $device_name (iOS $current_runtime)" >&2
                echo "$simulator_udid"
                return 0
            fi
        fi
    done < <(xcrun simctl list devices available 2>/dev/null)
    
    echo -e "${YELLOW}⚠${NC}  No exact match found for '$device_name' iOS $ios_version" >&2
    return 1
}

start_ios_simulator() {
    local device_name="${1:-$IOS_DEVICE_NAME}"
    local ios_version="${2:-$IOS_RUNTIME}"

    # First check if one is already booted
    if check_ios_simulator > /dev/null 2>&1; then
        log_warning "iOS simulator already booted"
        # Get the UDID of the booted simulator and ensure it's ready
        local booted_udid=$(xcrun simctl list devices 2>/dev/null | grep "(Booted)" | head -1 | sed -E 's/.*\(([A-F0-9-]{36})\).*/\1/')
        if [ -n "$booted_udid" ]; then
            log_info "Verifying booted simulator is ready (UDID: $booted_udid)"
            wait_for_ios_ready "$booted_udid" 60
        fi
        return 0
    fi

    log_info "Looking for iOS simulator: $device_name (iOS $ios_version)..."

    # Try to find existing simulator
    local simulator_udid=$(find_ios_simulator "$device_name" "$ios_version")

    # If no matching simulator found, create one
    if [ -z "$simulator_udid" ]; then
        log_warning "No matching simulator found"
        simulator_udid=$(create_ios_simulator "$device_name" "$ios_version")
        
        if [ -z "$simulator_udid" ] || [ $? -ne 0 ]; then
            log_error "Failed to create simulator, trying with first available..."
            simulator_udid=$(xcrun simctl list devices available | grep "iPhone" | grep -v "(Booted)" | head -1 | sed -E 's/.*\(([A-F0-9-]{36})\).*/\1/')
        fi
    fi

    if [ -z "$simulator_udid" ]; then
        log_error "No iOS simulators available"
        return 1
    fi

    log_info "Starting simulator with UDID: $simulator_udid"
    
    # Boot the simulator
    xcrun simctl boot "$simulator_udid" 2>/dev/null
    local boot_result=$?
    
    # Open Simulator app
    open -a Simulator 2>/dev/null
    
    # Wait for simulator to be fully ready
    wait_for_ios_ready "$simulator_udid" 90
    
    # Final verification
    if check_ios_simulator > /dev/null 2>&1; then
        log_success "Simulator is ready for testing"
        return 0
    else
        log_error "Simulator failed to boot properly"
        return 1
    fi
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
                start_android_emulator "${3:-$ANDROID_API_LEVEL}" "${4:-$ANDROID_AVD_NAME}"
                ;;
            create)
                create_android_emulator "${3:-$ANDROID_AVD_NAME}" "${4:-$ANDROID_API_LEVEL}"
                ;;
            list)
                list_android_emulators
                ;;
            *)
                echo "Unknown action: $ACTION"
                echo "Usage: $0 android [check|start|create|list] [api_level] [avd_name]"
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
                start_ios_simulator "${3:-$IOS_DEVICE_NAME}" "${4:-$IOS_RUNTIME}"
                ;;
            create)
                create_ios_simulator "${3:-$IOS_DEVICE_NAME}" "${4:-$IOS_RUNTIME}"
                ;;
            list)
                list_ios_simulators
                ;;
            runtimes)
                list_ios_runtimes
                ;;
            *)
                echo "Unknown action: $ACTION"
                echo "Usage: $0 ios [check|start|create|list|runtimes] [device_name] [ios_version]"
                exit 1
                ;;
        esac
        ;;
    *)
        echo "Unknown platform: $PLATFORM"
        echo ""
        echo "Usage: $0 [android|ios] [action] [options]"
        echo ""
        echo "Android actions:"
        echo "  check              - Check if emulator is booted"
        echo "  start [api] [name] - Start emulator (creates if not exists)"
        echo "  create [name] [api]- Create new emulator"
        echo "  list               - List available emulators"
        echo ""
        echo "iOS actions:"
        echo "  check                    - Check if simulator is booted"
        echo "  start [device] [version] - Start simulator (creates if not exists)"
        echo "  create [device] [version]- Create new simulator"
        echo "  list                     - List available simulators"
        echo "  runtimes                 - List available iOS runtimes"
        echo ""
        echo "Defaults (from project config):"
        echo "  Android: API $ANDROID_API_LEVEL ($ANDROID_AVD_NAME)"
        echo "  iOS: $IOS_DEVICE_NAME (iOS $IOS_RUNTIME)"
        exit 1
        ;;
esac

