# API 기본 명세서

> **기준**: 스웨거(OpenAPI) 우선. 이 문서는 스웨거와 동기화 목적.
>
> - **Swagger UI**: https://afternote.kro.kr/swagger-ui/index.html
> - **OpenAPI JSON**: https://afternote.kro.kr/v3/api-docs (동일 내용 로컬: `docs/openapi.json`)
> - **API Base URL**: https://afternote.kro.kr/

---

## AUTH ENDPOINTS (8개)

인증 필요 API: `Authorization: Bearer {accessToken}` (security: bearer-key)

---

### 1. 이메일 인증번호 보내기 (Send Email Verification Code)

- **Method**: POST
- **URL**: `/auth/email/send`
- **Header Required**: No (포함 X)
- **Description**: 이메일로 인증 번호를 보냅니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Request** `EmailSendRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| email | string | O | 이메일 주소 |

```json
{ "email": "gka365@naver.com" }
```

**Response**

```json
{
  "status": 200,
  "code": 0,
  "message": "6자리 인증코드가 발송되었습니다.",
  "data": null
}
```

**HTTP Status Codes**:
- 200: 6자리 인증코드가 발송되었습니다.
- 400: (Error status)

---

### 2. 이메일 인증번호 확인 (Verify Email Code)

- **Method**: POST
- **URL**: `/auth/email/verify`
- **Header Required**: No (포함 X)
- **Description**: 받은 인증 번호로 인증을 합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Request** `EmailVerifyRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| email | string | O | 이메일 주소 |
| certificateCode | string | O | 인증번호 (스웨거: `certificateCode`) |

```json
{ "email": "gka365@naver.com", "certificateCode": "123456" }
```

**Response**

```json
{
  "status": 200,
  "code": 0,
  "message": "유대로 변호 인증에 성공하였습니다.",
  "data": null
}
```

**HTTP Status Codes**:
- 200: 유대로 변호 인증에 성공하였습니다.
- 400: (Error status)

---

### 3. 회원 가입 (Sign Up)

- **Method**: POST
- **URL**: `/auth/sign-up`
- **Header Required**: No (포함 X)
- **Description**: 이메일, 비밀 번호, 휴대폰 번호로 회원가입을 합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Request** `SignupRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| email | string | O | 이메일 주소 |
| password | string | O | 8~20자, 영문+숫자+특수문자(@$!%*#?&) 각 1자 이상 |
| name | string | O | 이름 (스웨거: `name`) |
| profileUrl | string | - | 프로필 이미지 URL (스웨거: `profileUrl`) |

```json
{
  "email": "user@example.com",
  "password": "password123!",
  "name": "홍길동",
  "profileUrl": "https://s3.~~"
}
```

**Response** `ApiResponse` <SignupResponse>

| data 필드 | 타입 | 설명 |
|-----------|------|------|
| userId | int64 | 가입한 회원 ID (스웨거: `userId`) |
| email | string | 가입 이메일 |

---

### 4. 로그인 (Login)

- **Method**: POST
- **URL**: `/auth/login`
- **Header Required**: No (포함 X)
- **Description**: email과 비밀 번호로 로그인 합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Request** `LoginRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| email | string | O | 이메일 |
| password | string | O | 비밀번호 |

**Response** `ApiResponse` <LoginResponse>

| data 필드 | 타입 | 설명 |
|-----------|------|------|
| accessToken | string | 액세스 토큰 |
| refreshToken | string | 리프레시 토큰 |

---

### 5. 토큰 재발급 (Token Reissue)

- **Method**: POST
- **URL**: `/auth/reissue`
- **Header Required**: No (포함 X)
- **Description**: 토큰을 다시 받습니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Request** `ReissueRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| refreshToken | string | O | 리프레시 토큰 |

**Response** `ApiResponse` <ReissueResponse>

| data 필드 | 타입 | 설명 |
|-----------|------|------|
| accessToken | string | 액세스 토큰 |
| refreshToken | string | 리프레시 토큰 |

---

### 6. 카카오 로그인 (Kakao Login)

- **Method**: POST
- **URL**: `/auth/kakao`
- **Header Required**: No (포함 X)
- **Description**: 카카오 로그인
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

---

### 7. 비밀번호 변경 (Change Password)

- **Method**: POST
- **URL**: `/auth/password/change`
- **Header Required**: Yes (포함 O)
- **Description**: 비밀번호를 변경합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Request** `PasswordChangeRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| currentPassword | string | O | 현재 비밀번호 |
| newPassword | string | O | 새 비밀번호 (형식: sign-up과 동일) |

**Response** `ApiResponse` <object>

---

### 8. 로그아웃 (Logout)

- **Method**: POST
- **URL**: `/auth/logout`
- **Header Required**: Yes (포함 O)
- **Description**: 로그아웃합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: (Not specified)
- **Frontend Manager**: (Not specified)

**Request** `LogoutRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| refreshToken | string | O | 리프레시 토큰 |

**Response** `ApiResponse` <object>

---

## USER ENDPOINTS (7개)

---

### 1. 프로필 조회 (Get Profile)

- **Method**: GET
- **URL**: `/users/me`
- **Header Required**: Yes (포함 O)
- **Description**: 프로필 수정 진입 시 기존 정보를 표시합니다.
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

---

### 2. 프로필 수정 (Update Profile)

- **Method**: PATCH
- **URL**: `/users/me`
- **Header Required**: Yes (포함 O)
- **Description**: 프로필을 수정합니다.
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

---

### 3. 연결된 계정 조회 (Get Connected Accounts)

- **Method**: GET
- **URL**: `/users/connected-accounts`
- **Header Required**: Yes (포함 O)
- **Description**: 연결된 계정을 조회합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

---

### 4. 연결된 계정 해제 (Disconnect Account)

- **Method**: DELETE
- **URL**: `/users/connected-accounts/{provider}`
- **Header Required**: Yes (포함 O)
- **Description**: 연결된 계정을 해제합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

---

### 5. 푸시 알림 설정 (Push Notification Settings)

- **Method**: GET
- **URL**: `/users/push-settings`
- **Header Required**: Yes (포함 O)
- **Description**: 푸시 알림 설정을 조회합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

---

### 6. 푸시 알림 해제 (Disable Push Notifications)

- **Method**: (Not specified)
- **URL**: (Not specified)
- **Header Required**: Yes (포함 O)
- **Description**: 푸시 알림을 해제합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: (Not specified)
- **Frontend Manager**: (Not specified)

---

### 7. 수신인 목록 조회 (Get Recipient List)

- **Method**: (Not specified)
- **URL**: (Not specified)
- **Header Required**: Yes (포함 O)
- **Description**: 수신인 목록을 조회합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: (Not specified)
- **Frontend Manager**: (Not specified)

---

## AFTERNOTE ENDPOINTS (5개)

---

### 1. 모든 afternote 목록 (Get All Afternotes)

- **Method**: GET
- **URL**: `/afternotes?category=SOCIAL&page=0&size=10`
- **Header Required**: Yes (포함 O)
- **Description**: category 없으면 전체
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

---

### 2. afternote 상세 목록 (Get Afternote Details)

- **Method**: GET
- **URL**: `/afternotes/{afternote_id}`
- **Header Required**: Yes (포함 O)
- **Description**: 상세 목록
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

---

### 3. afternote 생성 (Create Afternote)

- **Method**: POST
- **URL**: `/afternotes`
- **Header Required**: Yes (포함 O)
- **Description**: afternote를 생성합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

---

### 4. afternote 수정 (Update Afternote)

- **Method**: PATCH
- **URL**: `/afternotes/{afternote_id}`
- **Header Required**: Yes (포함 O)
- **Description**: afternote를 수정합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

---

### 5. afternote 삭제 (Delete Afternote)

- **Method**: DELETE
- **URL**: `/afternotes/{afternote_id}`
- **Header Required**: Yes (포함 O)
- **Description**: afternote를 삭제합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

---

## TIME-LETTERS ENDPOINTS (7개)

---

### 1. 전체 조회 (Get All Time-Letters)

- **Method**: GET
- **URL**: `/time-letters`
- **Header Required**: Yes (포함 O)
- **Description**: List<TimeLetterInfoResponse>
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 영탁 조 (Youngtack Cho)

---

### 2. 단일 조회, 임시저장 불러오기 (Get Single, Load Draft)

- **Method**: GET
- **URL**: `/time-letters/{timeLetterId}`
- **Header Required**: Yes (포함 O)
- **Description**: 단일 조회 또는 임시저장 불러오기
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 영탁 조 (Youngtack Cho)

---

### 3. 등록 (Create)

- **Method**: POST
- **URL**: `/time-letters`
- **Header Required**: Yes (포함 O)
- **Description**: 날짜, 시간, 제목, 내용, 첨부파일, 임시저장여부, 수신자
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 영탁 조 (Youngtack Cho)

---

### 4. 임시저장 전체 조회 (Get All Drafts)

- **Method**: GET
- **URL**: `/time-letters/temporary`
- **Header Required**: Yes (포함 O)
- **Description**: List<TimeLetterInfoResponse>
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 영탁 조 (Youngtack Cho)

---

### 5. 단일, 다건 (종류 무관) 삭제 (Delete - Single or Multiple)

- **Method**: POST
- **URL**: `/time-letters/delete`
- **Header Required**: Yes (포함 O)
- **Description**: Body: List<TimeLetterId>
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 영탁 조 (Youngtack Cho)

---

### 6. 임시저장 전체 삭제 (Delete All Drafts)

- **Method**: DELETE
- **URL**: `/time-letters/temporary`
- **Header Required**: Yes (포함 O)
- **Description**: 임시저장 전체 삭제
- **Status**: (Not specified)
- **Backend Manager**: (Not specified)
- **Frontend Manager**: (Not specified)

---

### 7. 수정 (Update)

- **Method**: PATCH
- **URL**: `/time-letters/{timeLetterId}`
- **Header Required**: Yes (포함 O)
- **Description**: time-letter를 수정합니다.
- **Status**: (Not specified)
- **Backend Manager**: (Not specified)
- **Frontend Manager**: (Not specified)

---

## 공통 응답 형식

```json
{
  "status": [HTTP 상태 코드],
  "code": [내부 코드],
  "message": "[메시지]",
  "data": [데이터 객체 또는 null]
}
```

---

## 참고

- **Received API**와 **Mind-Record API**는 새 명세서에 포함되지 않았으나, 기존 명세는 유지됩니다.
- 스웨거 미등록 API는 스웨거 등록 후 확인·보완 예정입니다.
