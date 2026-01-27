#!/bin/bash
# Setup Git Hooks for Cursor/Claude Rules Synchronization

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
GIT_HOOKS_DIR="$PROJECT_ROOT/.git/hooks"
PRE_COMMIT_HOOK="$GIT_HOOKS_DIR/pre-commit"

# Create hooks directory if it doesn't exist
mkdir -p "$GIT_HOOKS_DIR"

# Create pre-commit hook
cat > "$PRE_COMMIT_HOOK" << 'EOF'
#!/bin/bash
# Pre-commit hook: Sync Cursor rules to CLAUDE.md and check gradle.properties

PROJECT_ROOT="$(git rev-parse --show-toplevel)"
SYNC_SCRIPT="$PROJECT_ROOT/scripts/sync_rules.py"
PRE_COMMIT_CHECK="$PROJECT_ROOT/scripts/pre-commit-check.sh"

# 1. Check gradle.properties for OS-specific paths (must pass)
if [ -f "$PRE_COMMIT_CHECK" ]; then
    bash "$PRE_COMMIT_CHECK"
    if [ $? -ne 0 ]; then
        exit 1
    fi
fi

# 2. Sync Cursor rules to CLAUDE.md (warning only)
if [ ! -f "$SYNC_SCRIPT" ]; then
    echo "⚠️  Warning: sync_rules.py not found. Skipping rules sync."
    exit 0
fi

# Run sync script
python3 "$SYNC_SCRIPT"

# If sync failed, don't block commit (just warn)
if [ $? -ne 0 ]; then
    echo "⚠️  Warning: Rules sync failed, but commit will proceed."
fi

# Add CLAUDE.md to staging if it was modified
if [ -f "$PROJECT_ROOT/CLAUDE.md" ]; then
    git add "$PROJECT_ROOT/CLAUDE.md" 2>/dev/null || true
fi

exit 0
EOF

# Make hook executable
chmod +x "$PRE_COMMIT_HOOK"

echo "✅ Git hooks configured successfully!"
echo "   Pre-commit hook will automatically sync rules before each commit."
