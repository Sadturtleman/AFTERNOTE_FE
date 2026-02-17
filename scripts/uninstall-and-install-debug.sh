#!/usr/bin/env bash
# Uninstall app from device/emulator and install fresh debug APK.
# Use this if you see NoClassDefFoundError for AfternoteApplication_GeneratedInjector
# (device may be using a stale code_cache overlay from a previous install).
set -e
cd "$(dirname "$0")/.."
echo "Uninstalling com.kuit.afternote..."
adb uninstall com.kuit.afternote 2>/dev/null || true
echo "Building debug APK..."
./gradlew assembleDebug --no-daemon -q
echo "Installing app-debug.apk..."
adb install -r app/build/outputs/apk/debug/app-debug.apk
echo "Done. Launch the app from the device."
