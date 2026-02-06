# Runtime Testing Guide (런타임 테스트 가이드)

이 문서는 User API와 TimeLetter API 구현을 런타임에서 테스트하는 방법을 설명합니다.

---

## 📋 목차

1. [실제 서버로 테스트](#1-실제-서버로-테스트)
2. [Logcat으로 API 호출 확인](#2-logcat으로-api-호출-확인)
3. [DevMode로 빠른 테스트](#3-devmode로-빠른-테스트)
4. [UI 화면에서 테스트](#4-ui-화면에서-테스트)

---

## 1. 실제 서버로 테스트

### 설정 방법

1. **Clean Build 및 앱 재설치**:
   ```bash
   ./gradlew clean
   ./gradlew :app:assembleDebug
   ```

2. **백엔드 서버 확인**:
   - 서버가 `https://afternote.kro.kr/`에서 실행 중이어야 합니다
   - 테스트 계정이 필요합니다 (예: `dnfjddk2@gmail.com` / `Ab@12345`)

---

## 2. Logcat으로 API 호출 확인

### Logcat 필터 설정

Android Studio의 Logcat에서 다음 필터를 사용하세요:

```
tag:OkHttp
또는
package:com.kuit.afternote
또는
UserRepositoryImpl|TimeLetterRepositoryImpl
```

### 확인할 로그

**User API 호출 시:**
```
D/UserRepositoryImpl: getMyProfile: userId=1
D/UserRepositoryImpl: getMyProfile: response=ApiResponse(...)
D/UserRepositoryImpl: getMyProfile: SUCCESS
```

**TimeLetter API 호출 시:**
```
D/TimeLetterRepositoryImpl: getTimeLetters: calling API
D/TimeLetterRepositoryImpl: getTimeLetters: response=ApiResponse(...)
```

**HTTP 요청/응답 (OkHttp 로깅):**
```
D/OkHttp: --> POST https://afternote.kro.kr/users/me?userId=1
D/OkHttp: Content-Type: application/json
D/OkHttp: {"name":"테스트 사용자",...}
D/OkHttp: <-- 200 OK (123ms)
D/OkHttp: {"status":200,"code":200,"message":"프로필 조회 성공","data":{...}}
```

---

## 3. DevMode로 빠른 테스트

### DevMode 화면 접근

1. 앱 실행 후 DevMode 화면으로 이동
2. 빠른 로그인 기능 사용:
   - 이메일: `dnfjddk2@gmail.com`
   - 비밀번호: `Ab@12345`

### DevMode에서 테스트 가능한 기능

- ✅ 로그인/로그아웃 테스트
- ✅ 토큰 저장/조회 확인
- ✅ 로그인 상태 확인

---

## 4. UI 화면에서 테스트

### User API 테스트

#### 프로필 조회/수정 (`ProfileViewModel`)

**화면 위치:**
- `ProfileEditScreen` (설정 → 프로필 수정)

**테스트 방법:**
1. 앱 실행 후 설정 화면으로 이동
2. "프로필 수정" 메뉴 클릭
3. **Logcat 확인**:
   ```
   D/UserRepositoryImpl: getMyProfile: userId=...
   D/OkHttp: --> GET /users/me?userId=...
   ```
4. 프로필 정보가 화면에 표시되는지 확인
5. 이름/연락처 수정 후 "등록" 버튼 클릭
6. **Logcat 확인**:
   ```
   D/UserRepositoryImpl: updateMyProfile: userId=...
   D/OkHttp: --> PATCH /users/me?userId=...
   ```

#### 푸시 알림 설정 (`PushSettingsViewModel`)

**화면 위치:**
- `PushToastSettingScreen` (설정 → 푸시 알림 설정)

**테스트 방법:**
1. 설정 화면에서 "푸시 알림 설정" 메뉴 클릭
2. **Logcat 확인**:
   ```
   D/UserRepositoryImpl: getMyPushSettings: userId=...
   D/OkHttp: --> GET /users/push-settings?userId=...
   ```
3. 푸시 알림 설정이 화면에 표시되는지 확인
4. 설정 변경 후 저장
5. **Logcat 확인**:
   ```
   D/UserRepositoryImpl: updateMyPushSettings: userId=...
   D/OkHttp: --> PATCH /users/push-settings?userId=...
   ```

### TimeLetter API 테스트

**화면 위치:**
- `TimeLetterScreen` (타임레터 목록)
- `TimeLetterWriterScreen` (타임레터 작성)

**테스트 방법:**
1. 타임레터 화면으로 이동
2. **Logcat 확인**:
   ```
   D/TimeLetterRepositoryImpl: getTimeLetters: calling API
   D/OkHttp: --> GET /time-letters
   ```
3. 타임레터 목록이 표시되는지 확인
4. 새 타임레터 작성 시:
   ```
   D/TimeLetterRepositoryImpl: createTimeLetter: title=..., status=DRAFT
   D/OkHttp: --> POST /time-letters
   ```

---

## ✅ 검증 체크리스트

### User API 검증

- [ ] Mock 모드에서 프로필 조회 성공 (Logcat 확인)
- [ ] Mock 모드에서 프로필 수정 성공 (Logcat 확인)
- [ ] Mock 모드에서 푸시 설정 조회 성공 (Logcat 확인)
- [ ] Mock 모드에서 푸시 설정 수정 성공 (Logcat 확인)
- [ ] 실제 서버 모드에서 프로필 조회 성공
- [ ] 실제 서버 모드에서 프로필 수정 성공
- [ ] 실제 서버 모드에서 푸시 설정 조회 성공
- [ ] 실제 서버 모드에서 푸시 설정 수정 성공
- [ ] 에러 처리 확인 (401, 404 등)

### TimeLetter API 검증

- [ ] Mock 모드에서 타임레터 목록 조회 성공
- [ ] 타임레터 생성 성공
- [ ] 타임레터 단일 조회 성공
- [ ] 타임레터 수정 성공
- [ ] 타임레터 삭제 성공
- [ ] 임시저장 목록 조회 성공
- [ ] 임시저장 전체 삭제 성공

---

## 🔧 문제 해결

### 실제 서버 연결 실패

1. **네트워크 연결 확인**
2. **서버 상태 확인**:
   ```bash
   curl https://afternote.kro.kr/v3/api-docs
   ```
3. **인증 토큰 확인** (Logcat에서):
   ```
   D/OkHttp: Authorization: Bearer ...
   ```

### Logcat에 로그가 보이지 않는 경우

1. **필터 확인**: `tag:OkHttp` 또는 `package:com.kuit.afternote`
2. **로그 레벨 확인**: Debug 레벨 로그가 활성화되어 있는지 확인
3. **앱 재시작**: 앱을 완전히 종료 후 재시작

---

## 📝 참고

- **Network Module**: `app/src/main/java/com/kuit/afternote/data/remote/NetworkModule.kt`
- **DevMode Screen**: `app/src/main/java/com/kuit/afternote/feature/dev/presentation/screen/DevModeScreen.kt`
