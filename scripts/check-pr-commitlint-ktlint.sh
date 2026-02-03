#!/bin/sh
# PR 통과 조건 확인: PR 제목(Conventional Commits 형식)과 브랜치 코드가 ktlint를 통과하는지 검사
# 사용: scripts/check-pr-commitlint-ktlint.sh
# PR의 base는 origin/main으로 가정 (필요 시 BASE=origin/develop ./scripts/check-pr-commitlint-ktlint.sh)

set -e
BASE="${BASE:-origin/main}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

MERGE_BASE=$(git merge-base "$BASE" HEAD 2>/dev/null || true)
if command -v npx >/dev/null 2>&1; then
    if [ -n "$GITHUB_EVENT_PATH" ] && [ -f "$GITHUB_EVENT_PATH" ]; then
        PR_TITLE=$(
            python3 -c 'import json,os; p=os.environ.get("GITHUB_EVENT_PATH"); d=json.load(open(p)); print((d.get("pull_request") or {}).get("title",""))' \
            2>/dev/null || true
        )
    fi

    if [ -n "$PR_TITLE" ]; then
        echo "► Commitlint (PR title):"
        printf "%s\n" "$PR_TITLE" | npx commitlint || { echo "❌ Commitlint failed."; exit 1; }
        echo "   OK"
    else
        echo "► Commitlint (HEAD commit only):"
        git log -1 --pretty=%B | npx commitlint || { echo "❌ Commitlint failed."; exit 1; }
        echo "   OK"
    fi
else
    echo "► Commitlint: skipped (npx not in PATH)."
    echo "   Tip: keep PR title in Conventional Commits format."
fi

echo "► Ktlint (branch code):"
./gradlew ktlintCheck --no-build-cache -q || { echo "❌ Ktlint failed."; exit 1; }
echo "   OK"
echo ""
if command -v npx >/dev/null 2>&1; then
    echo "✅ PR checks passed (commitlint + ktlint)."
else
    echo "✅ Ktlint passed."
fi
