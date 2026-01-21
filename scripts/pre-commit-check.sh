#!/bin/sh
# Git Pre-commit Hook: gradle.properties에 OS별 경로 하드코딩 방지

# 검사할 금지 문자열 (Windows 경로 패턴 등)
FORBIDDEN="org.gradle.java.home"

# 스테이징된 파일 중 gradle.properties에서 금지 문자열 검색
if git diff --cached --name-only | grep -q "gradle.properties"; then
    if git diff --cached "gradle.properties" | grep -q "^+.*$FORBIDDEN"; then
        echo "❌ [Error] gradle.properties에 '$FORBIDDEN'이 포함되어 있습니다."
        echo "   로컬 경로는 절대 커밋하지 마세요. ~/.gradle/gradle.properties를 사용하세요."
        echo ""
        echo "   해결 방법:"
        echo "   1. git reset HEAD gradle.properties"
        echo "   2. gradle.properties에서 org.gradle.java.home 라인 제거"
        echo "   3. 필요시 ~/.gradle/gradle.properties에 개인 경로 설정"
        exit 1
    fi
fi

exit 0
