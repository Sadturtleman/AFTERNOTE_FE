#!/bin/bash

# SignUpScreen 로그 실시간 모니터링 스크립트
# 사용법: ./scripts/monitor-signup-logs.sh
# 종료: Ctrl+C

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
    echo "Android SDK 경로를 확인하거나 ANDROID_HOME 환경 변수를 설정하세요."
    exit 1
fi

echo "📱 SignUpScreen 로그 실시간 모니터링 시작..."
echo "=========================================="
echo "💡 '다음' 버튼을 클릭하면 로그가 표시됩니다."
echo "💡 종료하려면 Ctrl+C를 누르세요."
echo "=========================================="
echo ""

# 실시간 로그 모니터링 (SignUpScreen, VerifyEmailViewModel, OkHttp)
# -v time: 타임스탬프 표시
# -c: 기존 로그 버퍼 클리어 (선택사항, 주석 처리하면 최근 로그도 함께 표시)
# -s: 특정 태그만 필터링
echo "⚠️  기존 로그를 클리어합니다..."
$ADB_PATH logcat -c
echo "✅ 실시간 로그 모니터링 시작 (타임스탬프 포함)"
echo ""
$ADB_PATH logcat -v time -s SignUpScreen:D VerifyEmailViewModel:D OkHttp:D
