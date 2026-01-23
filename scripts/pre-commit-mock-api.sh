#!/bin/bash

# gradle.properties의 USE_MOCK_API를 false로 변경하는 pre-commit hook

GRADLE_PROPERTIES="gradle.properties"

if [ -f "$GRADLE_PROPERTIES" ]; then
    # USE_MOCK_API가 true인 경우 false로 변경
    if grep -q "USE_MOCK_API=true" "$GRADLE_PROPERTIES"; then
        sed -i '' 's/USE_MOCK_API=true/USE_MOCK_API=false/g' "$GRADLE_PROPERTIES"
        echo "✅ gradle.properties: USE_MOCK_API를 false로 변경했습니다."
    fi
fi

exit 0
