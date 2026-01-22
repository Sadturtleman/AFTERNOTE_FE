#!/bin/bash

# SignUpScreen 로그 확인 스크립트
# 사용법: ./scripts/check-signup-logs.sh

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

echo "📱 SignUpScreen 로그 확인 중..."
echo "=========================================="
echo ""

# 타임스탬프와 함께 최근 로그 확인 (SignUpScreen, VerifyEmailViewModel, OkHttp)
# -v time: 타임스탬프 표시
# -d: 버퍼의 모든 로그 출력 후 종료
# -s: 특정 태그만 필터링
# tail -100: 최근 100줄만 표시
$ADB_PATH logcat -d -v time -s SignUpScreen:D VerifyEmailViewModel:D OkHttp:D | tail -100

echo ""
echo "=========================================="
echo "✅ 로그 확인 완료"
echo ""
echo "💡 참고: 로그 버퍼는 기기마다 다르지만 보통 최근 몇 시간~하루 정도의 로그를 보관합니다."
echo "💡 다음 버튼을 클릭한 후 이 스크립트를 다시 실행하면 최신 로그를 확인할 수 있습니다."
