# Dev Mode Password Reset 개선 계획

> **Obsolete**: Dev mode was removed from the app. This document is kept for reference only.

## 피드백 요약

Perplexity 피드백에 따라 다음 개선이 필요합니다:

1. **Source Set 분리**: `src/debug`와 `src/release`로 분리하여 릴리즈 빌드에 개발 코드가 포함되지 않도록
2. **DI 모듈 분리**: Hilt 모듈을 debug/release로 분리
3. **아키텍처 분리**: 개발 전용 기능을 명확히 분리

## 구현 계획

### 1. Source Set 구조 변경

```
app/src/
├── main/                    # 공통 코드
│   └── java/.../
│       ├── AuthRepository.kt (devPasswordReset 제거)
│       └── ...
├── debug/                   # Debug 전용 코드
│   └── java/.../
│       ├── DevAuthRepository.kt (devPasswordReset 포함)
│       ├── DevPasswordResetUseCase.kt
│       └── di/DevModule.kt
└── release/                 # Release 전용 코드 (선택적)
    └── java/.../
        └── di/ReleaseModule.kt (No-op 구현)
```

### 2. 변경 사항

#### A. Repository 인터페이스 분리
- `AuthRepository`에서 `devPasswordReset` 제거
- `src/debug`에 `DevAuthRepository` 인터페이스 생성 (확장 인터페이스)
- `AuthRepositoryImpl`에서 `devPasswordReset` 구현을 debug source set으로 이동

#### B. UseCase 분리
- `DevPasswordResetUseCase`를 `src/debug`로 이동

#### C. API Service 분리
- `AuthApiService`에서 `devPasswordReset` 메서드를 debug source set으로 이동
- 또는 debug 전용 인터페이스로 분리

#### D. ViewModel 및 UI
- `DevModeViewModel.resetPassword()`는 이미 dev feature이므로 유지
- `DevModeScreen`의 password reset 버튼도 유지 (이미 dev mode 전용)

#### E. Hilt 모듈 분리
- `src/debug/di/DevModule.kt`: DevPasswordResetUseCase 주입
- `src/release/di/ReleaseModule.kt`: No-op 또는 빈 모듈

### 3. 구현 단계

1. ✅ Source Set 디렉토리 생성
2. ✅ Repository 인터페이스 분리
3. ✅ UseCase 이동
4. ✅ API Service 분리
5. ✅ Repository 구현 분리
6. ✅ Hilt 모듈 분리
7. ✅ 빌드 확인

### 4. 보안 강화

- **Proguard/R8**: Release 빌드에서 dev 관련 클래스 제거 확인
- **BuildConfig**: `BuildConfig.DEBUG` 체크 추가 (이중 안전장치)
- **코드 리뷰**: Release 빌드 체크리스트에 dev 코드 포함 여부 확인 추가

## 예상 효과

1. **보안**: Release APK에 dev 코드가 물리적으로 포함되지 않음
2. **사이즈**: Release 빌드 크기 감소
3. **명확성**: 개발 전용 기능이 명확히 구분됨
4. **유지보수**: 개발 기능과 프로덕션 기능의 혼동 방지
