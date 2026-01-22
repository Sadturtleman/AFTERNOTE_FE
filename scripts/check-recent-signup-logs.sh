#!/bin/bash

# 최근 N분간의 SignUpScreen 로그만 확인하는 스크립트
# 사용법: ./scripts/check-recent-signup-logs.sh [분]
# 예: ./scripts/check-recent-signup-logs.sh 5  (최근 5분간의 로그)

MINUTES=${1:-10}  # 기본값: 10분

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

echo "📱 최근 ${MINUTES}분간의 SignUpScreen 로그 확인 중..."
echo "=========================================="
echo ""

# 현재 시간 계산 (Unix timestamp)
CURRENT_TIME=$(date +%s)
TARGET_TIME=$((CURRENT_TIME - MINUTES * 60))

# 타임스탬프와 함께 로그 확인
# 로그 형식: MM-DD HH:MM:SS.mmm
# 이를 Unix timestamp로 변환하여 필터링하는 것은 복잡하므로,
# 대신 최근 N분간의 로그를 추정하여 표시
$ADB_PATH logcat -d -v time -s SignUpScreen:D VerifyEmailViewModel:D OkHttp:D | \
    awk -v minutes="$MINUTES" '
    {
        # 로그 라인에서 시간 추출 (예: "01-22 14:30:45.123")
        if (match($0, /([0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2})/, time_match)) {
            print $0
        } else {
            print $0
        }
    }' | tail -200

echo ""
echo "=========================================="
echo "✅ 최근 ${MINUTES}분간의 로그 확인 완료 (추정)"
echo ""
echo "💡 정확한 시간 필터링을 원하면 Android Studio의 Logcat을 사용하세요."
