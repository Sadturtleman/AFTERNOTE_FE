# AFTERNOTE-Android

AFTERNOTE Android 개발 Repository입니다.

git Flow 전략을 사용하며 commit, PR은 다음 규칙을 사용합니다.

초기에 git clone 후 npm install 및 .husky 폴더에서 pre-commit, commit-msg 폴더를 github에서 다시 복제해야 정상 작동합니다.

## Java 설정 (필수)

프로젝트는 JDK 11을 사용합니다. JDK 25는 Kotlin 컴파일러 호환성 문제로 사용할 수 없습니다.

### 기본 동작

프로젝트는 `compileOptions`에서 Java 11을 명시적으로 설정합니다. Gradle은 시스템에 설치된 JDK를 자동으로 감지하여 사용합니다.

**추가 설정이 필요 없는 경우:**
- 시스템에 JDK 11이 설치되어 있고 `JAVA_HOME` 환경 변수가 올바르게 설정된 경우
- Android Studio가 자동으로 JDK를 감지하는 경우

### 수동 설정이 필요한 경우

Gradle이 자동으로 JDK를 찾지 못하거나 특정 JDK를 사용해야 하는 경우, 각자 로컬 설정 파일에 경로를 지정할 수 있습니다:

**Windows:**
`C:\Users\YourName\.gradle\gradle.properties` 파일 생성:
```properties
org.gradle.java.home=C:/Program Files/Eclipse Adoptium/jdk-11.x.x.x-hotspot
```

**macOS/Linux:**
`~/.gradle/gradle.properties` 파일 생성:
```properties
org.gradle.java.home=/Library/Java/JavaVirtualMachines/jdk-11.jdk/Contents/Home
```

### ⚠️ Windows 환경 주의사항

Windows에서 Git Bash를 통해 pre-commit hook을 실행할 때, Android Studio JBR을 사용하면 `java.lang.InternalError: Error loading java.security file` 에러가 발생할 수 있습니다.

**해결 방법:**
- Eclipse Adoptium JDK를 사용하세요 (Gradle Toolchain이 자동으로 찾습니다)
- 또는 위의 수동 설정 방법으로 `~/.gradle/gradle.properties`에 Eclipse Adoptium JDK 경로를 설정하세요

자세한 내용은 [트러블슈팅 문서](docs/troubleshooting-gradle-java-issues.md)를 참고하세요.

## 개발자 모드 테스트 계정 설정

개발자 모드에서 빠른 로그인 기능을 사용하려면 `local.properties`에 테스트 계정 정보를 설정해야 합니다.

**프로젝트 루트의 `local.properties` 파일에 추가:**
```properties
# Test credentials for DevMode quick login
TEST_EMAIL=your_test_email@example.com
TEST_PASSWORD=your_test_password
```

> **참고**: `local.properties`는 `.gitignore`에 포함되어 있어 Git에 커밋되지 않습니다.

### ⚠️ 중요: OS별 경로 하드코딩 금지

**절대 `gradle.properties`에 `org.gradle.java.home`을 커밋하지 마세요!**

프로젝트의 `gradle.properties`에는 OS별 경로를 포함하지 않습니다. 로컬 JDK 경로가 필요한 경우:
- 각자 `~/.gradle/gradle.properties` (또는 Windows: `C:\Users\YourName\.gradle\gradle.properties`)에 설정하세요
- Pre-commit hook이 실수로 커밋하는 것을 자동으로 방지합니다

**Git Hook 설정:**
```bash
bash scripts/setup-git-hooks.sh
```

# Commitlint 규칙 가이드

## 커밋 메시지 구조

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

## 주요 타입

### 필수 타입

- **feat**: 새로운 기능 추가 (Semantic Versioning의 MINOR에 해당)
- **fix**: 버그 수정 (Semantic Versioning의 PATCH에 해당)

### 권장 타입

- **build**: 빌드 시스템 또는 외부 종속성 변경
- **chore**: 기타 변경사항 (프로덕션 코드 변경 없음)
- **ci**: CI 설정 파일 및 스크립트 변경
- **docs**: 문서만 변경
- **style**: 코드 의미에 영향을 주지 않는 변경사항 (공백, 포맷팅 등)
- **refactor**: 버그 수정이나 기능 추가가 아닌 코드 변경
- **perf**: 성능 개선
- **test**: 테스트 추가 또는 수정

## Scope (선택사항)

커밋이 영향을 미치는 코드베이스의 섹션을 괄호 안에 명시합니다.

```
feat(parser): 배열 파싱 기능 추가
fix(auth): 로그인 토큰 만료 문제 해결
```

## Breaking Changes

API에 중대한 변경사항이 있는 경우 다음 두 가지 방법 중 하나로 표시합니다.

### 방법 1: `!` 사용

```
feat!: 사용자 API 응답 형식 변경
feat(api)!: 제품 배송 시 고객에게 이메일 전송
```

### 방법 2: Footer 사용

```
feat: 설정 객체가 다른 설정을 확장할 수 있도록 허용

BREAKING CHANGE: 설정 파일의 `extends` 키가 이제 다른 설정 파일 확장에 사용됩니다
```

## 커밋 메시지 예시

### 기본 커밋

```
docs: CHANGELOG 맞춤법 수정
```

### Scope를 포함한 커밋

```
feat(lang): 폴란드어 추가
```

### Body를 포함한 커밋

```
fix: 요청 경쟁 상태 방지

요청 ID와 최신 요청에 대한 참조를 도입합니다. 
최신 요청이 아닌 다른 요청의 응답은 무시합니다.

경쟁 상태를 완화하는 데 사용되었지만 이제 불필요한 타임아웃을 제거합니다.
```

### 여러 Footer를 포함한 커밋

```
fix: 요청 경쟁 상태 방지

요청 ID와 최신 요청 참조를 도입하여 최신 요청이 아닌 
응답은 무시하도록 처리합니다.

Reviewed-by: John Doe
Refs: #123
```

### Revert 커밋

```
revert: 누들 사건에 대해 다시는 언급하지 맙시다

Refs: 676104e, a215868
```

## Required checks before merge

Code must pass these checks before a PR is merged:

| Check | What | Verify locally |
|-------|------|----------------|
| **Commitlint** | PR title and commit messages (Conventional Commits, ≤100 chars/line) | `echo "type(scope): subject" \| npx commitlint` |
| **Ktlint** | Kotlin code style | `./gradlew ktlintCheck` |
| **Build** | Debug build must succeed (same as app distribution) | `./gradlew assembleDebug` |

Optional: run `./scripts/check-pr-commitlint-ktlint.sh` before pushing to verify commitlint + ktlint. See `.cursor/rules/workflow/pr-ci-checks.mdc` for details.

## 트러블슈팅

### Gradle Java 보안 파일 로드 문제

Windows에서 pre-commit hook 실행 시 `java.lang.InternalError: Error loading java.security file` 에러가 발생하는 경우:

1. Eclipse Adoptium JDK 11이 설치되어 있는지 확인
2. Gradle Toolchain이 자동으로 JDK를 찾지 못하는 경우, `~/.gradle/gradle.properties`에 Eclipse Adoptium JDK 경로 설정
3. 자세한 해결 방법은 [트러블슈팅 문서](docs/troubleshooting-gradle-java-issues.md) 참고

### Pre-commit Hook이 작동하지 않는 경우

1. `npm install` 실행하여 Husky 설정 확인
2. `.husky` 폴더의 hook 파일들이 실행 권한을 가지고 있는지 확인
3. Git Bash 환경에서 실행 중인 경우, Eclipse Adoptium JDK 사용 확인

## Cursor & Claude Code Rules 동기화

이 프로젝트는 Cursor IDE와 Claude Code 간의 규칙을 공유합니다. `CLAUDE.md`는 `@` 문법을 사용하여 Cursor 규칙 파일들을 참조합니다.

### 작동 방식

- **Source of Truth**: `.cursorrules` 및 `.cursor/rules/*.mdc` 파일들
- **CLAUDE.md**: 인덱스 파일로, `@파일경로` 문법으로 규칙 파일들을 참조
- **자동 로드**: Claude Code가 `CLAUDE.md`를 읽을 때 참조된 파일들을 자동으로 읽어옴

### 빠른 시작

1. **수동 동기화** (규칙 파일 추가/변경 시):
   ```bash
   python3 scripts/sync_rules.py  # 또는 python scripts/sync_rules.py
   ```

2. **자동 동기화 설정** (커밋 시 자동 실행):
   ```bash
   # Unix/Mac/Linux
   bash scripts/setup-git-hooks.sh
   
   # Windows (PowerShell)
   powershell -ExecutionPolicy Bypass -File scripts/setup-git-hooks.ps1
   ```

### 규칙 수정 방법

⚠️ **중요**: 규칙을 수정할 때는 **반드시** 다음 파일들만 수정하세요:
- `.cursorrules` - 핵심 규칙 요약
- `.cursor/rules/**/*.mdc` - 상세 규칙 파일들

**절대 `CLAUDE.md`를 직접 수정하지 마세요!** 이 파일은 자동 생성되는 인덱스 파일입니다.

자세한 내용은 [규칙 동기화 문서](docs/cursor-claude-rules-sync.md)를 참고하세요.

