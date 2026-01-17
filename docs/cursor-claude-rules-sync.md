# Cursor & Claude Code Rules Synchronization

이 프로젝트는 Cursor IDE와 Claude Code (CLI Agent) 간의 규칙을 자동으로 동기화합니다.

## 개요

- **Source of Truth**: `.cursorrules` 및 `.cursor/rules/*.mdc` 파일들
- **Target**: `CLAUDE.md` (Claude Code용 인덱스 파일)
- **동기화 방식**: `@` 문법을 통한 파일 참조 (실제 규칙은 Cursor 파일에서 직접 읽음)

## 파일 구조

```
프로젝트 루트/
├── .cursorrules                    # Cursor 핵심 규칙 요약
├── .cursor/
│   └── rules/                      # Cursor 상세 규칙
│       ├── workflow/
│       │   ├── git-policy.mdc
│       │   └── branch-workflow.mdc
│       ├── tech-stack/
│       │   ├── architecture.mdc
│       │   ├── compose.mdc
│       │   └── android-kotlin.mdc
│       └── quality/
│           └── code-quality.mdc
├── CLAUDE.md                       # 자동 생성 (수정 금지!)
└── scripts/
    ├── sync_rules.py               # 동기화 스크립트
    ├── setup-git-hooks.sh          # Git Hook 설정 (Unix/Mac)
    └── setup-git-hooks.ps1         # Git Hook 설정 (Windows)
```

## 사용 방법

### 1. 수동 동기화

규칙을 수정한 후 수동으로 동기화하려면:

```bash
# Unix/Mac/Linux
python3 scripts/sync_rules.py

# Windows
python scripts/sync_rules.py
```

### 2. 자동 동기화 (Git Hook 설정)

커밋할 때마다 자동으로 동기화되도록 설정:

#### Unix/Mac/Linux
```bash
bash scripts/setup-git-hooks.sh
```

#### Windows (PowerShell)
```powershell
powershell -ExecutionPolicy Bypass -File scripts/setup-git-hooks.ps1
```

설정 후, `git commit`을 실행하면 자동으로 `CLAUDE.md`가 업데이트되고 스테이징 영역에 추가됩니다.

### 3. Python 설치 확인

스크립트 실행을 위해 Python 3.6 이상이 필요합니다:

```bash
python3 --version  # 또는 python --version
```

Python이 설치되어 있지 않은 경우:
- [Python 공식 사이트](https://www.python.org/downloads/)에서 다운로드
- 또는 패키지 매니저 사용:
  - macOS: `brew install python3`
  - Ubuntu/Debian: `sudo apt install python3`
  - Windows: Chocolatey `choco install python`

## 규칙 수정 방법

⚠️ **중요**: 규칙을 수정할 때는 **반드시** 다음 파일들만 수정하세요:

1. `.cursorrules` - 핵심 규칙 요약
2. `.cursor/rules/**/*.mdc` - 상세 규칙 파일들

**절대 `CLAUDE.md`를 직접 수정하지 마세요!** 이 파일은 자동 생성되며, 수동 수정 사항은 다음 동기화 시 덮어씌워집니다.

## 동기화 스크립트 동작 방식

1. `.cursorrules` 파일 경로를 `@.cursorrules` 형식으로 참조 추가
2. `.cursor/rules/` 디렉토리의 모든 `.mdc` 파일을 스캔
3. 각 `.mdc` 파일의 YAML frontmatter를 파싱하여 설명 추출
4. 카테고리별로 그룹화 (workflow, tech-stack, quality)
5. 우선순위에 따라 정렬하여 `@` 참조 목록 생성:
   - `git-policy.mdc` (우선순위 1)
   - `branch-workflow.mdc` (우선순위 2)
   - `code-quality.mdc` (우선순위 3)
   - 기타 파일들 (알파벳 순)
6. `CLAUDE.md`를 인덱스 파일로 생성 (실제 규칙 내용은 포함하지 않음)

## Claude Code의 `@` Import 문법

Claude Code는 `@파일경로` 문법을 사용하여 다른 파일의 내용을 자동으로 읽어옵니다.

**예시:**
```markdown
- @.cursorrules
- @.cursor/rules/workflow/git-policy.mdc
```

이렇게 작성하면 Claude Code가 해당 파일들을 자동으로 읽어서 컨텍스트에 포함시킵니다.

**장점:**
- 규칙 내용을 중복하지 않음 (Single Source of Truth)
- Cursor와 Claude Code가 동일한 규칙 파일 공유
- 파일 크기 최소화 (인덱스만 포함)
- 컨텍스트 윈도우 효율적 사용

## 메타데이터 처리

`.mdc` 파일의 YAML frontmatter에서 `description`을 추출하여 `CLAUDE.md`의 참조 목록에 표시합니다.

**원본 (.mdc)**:
```yaml
---
description: Git 작업 및 브랜치 관리 정책
globs: "**/*"
alwaysApply: true
---
```

**변환된 (CLAUDE.md)**:
```markdown
- @.cursor/rules/workflow/git-policy.mdc - *Git 작업 및 브랜치 관리 정책*
```

**참고:** `globs`와 `alwaysApply` 메타데이터는 Cursor에서만 사용되며, Claude Code는 `@` 참조를 통해 파일을 읽을 때 전체 내용을 가져옵니다.

## 문제 해결

### 동기화 스크립트가 실행되지 않는 경우

1. **Python이 설치되어 있는지 확인**:
   ```bash
   python3 --version
   ```

2. **스크립트 실행 권한 확인** (Unix/Mac):
   ```bash
   chmod +x scripts/sync_rules.py
   ```

3. **경로 문제**: 프로젝트 루트에서 실행하는지 확인

### Git Hook이 작동하지 않는 경우

1. **Hook 파일이 생성되었는지 확인**:
   ```bash
   ls -la .git/hooks/pre-commit
   ```

2. **실행 권한 확인** (Unix/Mac):
   ```bash
   chmod +x .git/hooks/pre-commit
   ```

3. **수동으로 Hook 재설정**:
   ```bash
   # Unix/Mac
   bash scripts/setup-git-hooks.sh
   
   # Windows
   powershell -ExecutionPolicy Bypass -File scripts/setup-git-hooks.ps1
   ```

### CLAUDE.md가 너무 큰 경우

**이 문제는 더 이상 발생하지 않습니다!** 새로운 `@` 참조 방식에서는 `CLAUDE.md`가 인덱스 역할만 하므로 매우 작습니다 (수십 줄). 실제 규칙 내용은 필요할 때만 로드됩니다.

만약 참조된 파일들이 너무 많아서 문제가 된다면:
1. `scripts/sync_rules.py`의 `get_file_priority()` 함수를 수정하여 중요하지 않은 규칙을 제외
2. 또는 카테고리별로 별도의 `CLAUDE-{category}.md` 파일을 만들어 분리

## 베스트 프랙티스

1. **규칙 수정은 Cursor에서만**: 모든 규칙 수정은 `.cursorrules` 또는 `.cursor/rules/*.mdc`에서만 수행
2. **커밋 전 동기화**: Git Hook을 설정하여 커밋 전 자동으로 `CLAUDE.md` 인덱스 업데이트
3. **CLAUDE.md는 Git에 포함**: 동기화된 `CLAUDE.md`는 Git에 커밋하여 팀원들과 공유
4. **규칙 파일 구조 유지**: `.mdc` 파일의 YAML frontmatter 형식을 유지하여 스크립트 호환성 보장
5. **@ 참조 경로 확인**: 새로운 `.mdc` 파일을 추가한 후 `sync_rules.py`를 실행하여 `CLAUDE.md`에 참조가 추가되었는지 확인

## 참고 자료

- [Cursor Rules Documentation](https://cursor.sh/docs)
- [Claude Code Documentation](https://claude.com/blog/using-claude-md-files)
- [Git Hooks Documentation](https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks)
