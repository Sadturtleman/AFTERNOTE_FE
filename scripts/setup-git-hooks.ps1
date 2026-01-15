# Setup Git Hooks for Cursor/Claude Rules Synchronization (Windows PowerShell)

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$GitHooksDir = Join-Path $ProjectRoot ".git" "hooks"
$PreCommitHook = Join-Path $GitHooksDir "pre-commit"
$SyncScript = Join-Path $ProjectRoot "scripts" "sync_rules.py"

# Create hooks directory if it doesn't exist
if (-not (Test-Path $GitHooksDir)) {
    New-Item -ItemType Directory -Path $GitHooksDir -Force | Out-Null
}

# Create pre-commit hook
$HookContent = @"
# Pre-commit hook: Sync Cursor rules to CLAUDE.md (Windows)

`$ProjectRoot = git rev-parse --show-toplevel
`$SyncScript = Join-Path `$ProjectRoot "scripts" "sync_rules.py"

# Check if sync script exists
if (-not (Test-Path `$SyncScript)) {
    Write-Host "⚠️  Warning: sync_rules.py not found. Skipping rules sync."
    exit 0
}

# Run sync script
python `$SyncScript

# If sync failed, don't block commit (just warn)
if (`$LASTEXITCODE -ne 0) {
    Write-Host "⚠️  Warning: Rules sync failed, but commit will proceed."
}

# Add CLAUDE.md to staging if it was modified
`$ClaudeMd = Join-Path `$ProjectRoot "CLAUDE.md"
if (Test-Path `$ClaudeMd) {
    git add `$ClaudeMd 2>$null
}

exit 0
"@

$HookContent | Out-File -FilePath $PreCommitHook -Encoding UTF8 -NoNewline

# Make hook executable (Unix-like systems)
if (Get-Command chmod -ErrorAction SilentlyContinue) {
    chmod +x $PreCommitHook
}

Write-Host "✅ Git hooks configured successfully!"
Write-Host "   Pre-commit hook will automatically sync rules before each commit."
