#!/bin/sh
# PR 통과 조건 확인: PR 내 모든 커밋이 commitlint를, 브랜치 코드가 ktlint를 통과하는지 검사
# 사용: scripts/check-pr-commitlint-ktlint.sh
# PR의 base는 origin/main으로 가정 (필요 시 BASE=origin/develop ./scripts/check-pr-commitlint-ktlint.sh)

set -e
BASE="${BASE:-origin/main}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

MERGE_BASE=$(git merge-base "$BASE" HEAD 2>/dev/null || true)
if [ -z "$MERGE_BASE" ]; then
    echo "⚠️  Could not find merge base with $BASE. Skipping commitlint."
elif command -v npx >/dev/null 2>&1; then
    echo "► Commitlint (all commits in $BASE..HEAD):"
    npx commitlint --from="$MERGE_BASE" --to=HEAD || { echo "❌ Commitlint failed."; exit 1; }
    echo "   OK"
else
    echo "► Commitlint: skipped (npx not in PATH)."
    echo "   Run manually: npx commitlint --from=$MERGE_BASE --to=HEAD"
fi

echo "► Ktlint (branch code):"
./gradlew ktlintCheck --no-build-cache -q || { echo "❌ Ktlint failed."; exit 1; }
echo "   OK"
echo ""
if command -v npx >/dev/null 2>&1 && [ -n "$MERGE_BASE" ]; then
    echo "✅ PR checks passed (commitlint + ktlint)."
elif [ -n "$MERGE_BASE" ]; then
    echo "✅ Ktlint passed. Run commitlint manually (see above)."
else
    echo "✅ Ktlint passed."
fi
