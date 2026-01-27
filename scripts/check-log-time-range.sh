#!/bin/bash

# 로그 버퍼의 시간 범위 확인 스크립트
# 사용법: ./scripts/check-log-time-range.sh

# Android SDK 경로 찾기
if [ -z "$ANDROID_HOME" ]; then
    if [ -d "$HOME/Library/Android/sdk" ]; then
        ANDROID_HOME="$HOME/Library/Android/sdk"
    elif [ -d "$HOME/Android/Sdk" ]; then
        ANDROID_HOME="$HOME/Android/Sdk"
    fi
fi

ADB_PATH="$ANDROID_HOME/platform-tools/adb"

if [ ! -f "$ADB_PATH" ]; then
    echo "❌ adb를 찾을 수 없습니다."
    exit 1
fi

echo "📱 로그 버퍼 시간 범위 확인 중..."
echo "=========================================="
echo ""

# 전체 로그의 첫 번째와 마지막 줄 확인
FIRST_LOG=$($ADB_PATH logcat -d -v time 2>/dev/null | head -1)
LAST_LOG=$($ADB_PATH logcat -d -v time 2>/dev/null | tail -1)

if [ -z "$FIRST_LOG" ]; then
    echo "❌ 로그를 읽을 수 없습니다. 디바이스가 연결되어 있는지 확인하세요."
    exit 1
fi

echo "🕐 가장 오래된 로그:"
echo "$FIRST_LOG"
echo ""
echo "🕐 가장 최신 로그:"
echo "$LAST_LOG"
echo ""

# SignUpScreen 관련 로그만 확인
echo "=========================================="
echo "📋 SignUpScreen 관련 로그 시간 범위:"
echo ""

SIGNUP_FIRST=$($ADB_PATH logcat -d -v time -s SignUpScreen:D VerifyEmailViewModel:D OkHttp:D 2>/dev/null | head -1)
SIGNUP_LAST=$($ADB_PATH logcat -d -v time -s SignUpScreen:D VerifyEmailViewModel:D OkHttp:D 2>/dev/null | tail -1)

if [ -n "$SIGNUP_FIRST" ]; then
    echo "🕐 가장 오래된 SignUpScreen 로그:"
    echo "$SIGNUP_FIRST"
    echo ""
    echo "🕐 가장 최신 SignUpScreen 로그:"
    echo "$SIGNUP_LAST"
else
    echo "⚠️  SignUpScreen 관련 로그가 없습니다."
fi

echo ""
echo "=========================================="
