#!/bin/sh
# Git Pre-push Hook: main/develop 브랜치 직접 푸시 방지

branch=$(git symbolic-ref HEAD | sed -e 's,.*/\(.*\),\1,')

if [ "$branch" = "main" ] || [ "$branch" = "develop" ]; then
    echo "❌ Error: Direct push to '$branch' branch is not allowed."
    echo "Please create a feature branch first:"
    echo "  git checkout -b feat/your-feature-name"
    exit 1
fi

exit 0
