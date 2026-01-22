# API 테스트 가이드

Auth API를 Mock 모드와 실제 서버 모드로 테스트하는 방법입니다.

## 테스트 순서

### 1단계: Mock 모드로 테스트 (로컬 검증)

#### 설정
`gradle.properties`에서:
```properties
USE_MOCK_API=true
```

#### 테스트 방법
1. 앱 빌드 및 실행
2. 로그인/회원가입 기능 테스트
3. Logcat에서 Mock 응답 확인:
   ```
   이메일 인증번호가 발송되었습니다
   로그인에 성공했습니다
   ```

#### 확인 사항
- ✅ API 호출이 정상적으로 이루어지는지
- ✅ DTO → Domain Model 매핑이 올바른지
- ✅ UseCase가 정상 동작하는지
- ✅ ViewModel 상태 변경이 올바른지

---

### 2단계: 실제 서버로 전환

#### 설정
`gradle.properties`에서:
```properties
USE_MOCK_API=false
```

또는 주석 처리:
```properties
# USE_MOCK_API=false
```

#### Clean Build
설정 변경 후 반드시 Clean Build:
```bash
./gradlew clean build
```

또는 Android Studio에서:
- Build → Clean Project
- Build → Rebuild Project

#### 테스트 방법
1. 앱 재설치 및 실행
2. 실제 서버로 요청 전송 확인
3. Logcat에서 실제 서버 응답 확인:
   ```
   HttpLoggingInterceptor가 요청/응답을 로그로 출력합니다
   ```

#### 확인 사항
- ✅ 실제 서버 연결 성공 여부
- ✅ 실제 응답 형식이 예상과 일치하는지
- ✅ 에러 처리 (4xx, 5xx)가 올바른지
- ✅ 네트워크 타임아웃 처리

---

## 실제 서버 정보

- **Base URL**: `https://afternote.kro.kr/`
- **Swagger UI**: https://afternote.kro.kr/swagger-ui/index.html
- **OpenAPI JSON**: https://afternote.kro.kr/v3/api-docs

## Logcat 필터링

실제 서버 응답을 확인하려면 Logcat에서 다음 필터 사용:
- Tag: `OkHttp`
- 또는 검색어: `auth/login`, `auth/sign-up` 등

## 문제 해결

### 실제 서버 연결 실패
1. 네트워크 연결 확인
2. 서버 상태 확인 (Swagger UI 접속)
3. 타임아웃 설정 확인 (현재 15초)
4. SSL 인증서 문제 확인

### 응답 형식 불일치
1. Swagger UI에서 실제 응답 형식 확인
2. DTO 클래스와 실제 응답 비교
3. `ApiResponse` 구조 확인

### 에러 처리
- HTTP 4xx/5xx: `ApiException`으로 처리
- 네트워크 에러: `Result.failure()`로 반환
- `runCatching`으로 모든 예외를 `Result`로 변환

## 테스트 체크리스트

### Mock 모드 테스트
- [ ] 이메일 인증번호 발송
- [ ] 이메일 인증번호 확인
- [ ] 회원가입
- [ ] 로그인
- [ ] 토큰 재발급
- [ ] 로그아웃
- [ ] 비밀번호 변경

### 실제 서버 테스트
- [ ] 실제 서버 연결 성공
- [ ] 실제 응답 파싱 성공
- [ ] 에러 케이스 처리 (잘못된 이메일, 비밀번호 등)
- [ ] 네트워크 에러 처리
