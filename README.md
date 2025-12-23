# AFTERNOTE-Android

AFTERNOTE Android 개발 Repository입니다.

git Flow 전략을 사용하며 commit, PR은 다음 규칙을 사용합니다.

초기에 git clone 후 npm install 해야 정상 작동합니다.

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



