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
- **Frontend Manager**: 정일혁 (Jeong Ilhyuk)

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
- **Frontend Manager**: 정일혁 (Jeong Ilhyuk)

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
  "message": "휴대폰 번호 인증에 성공하였습니다.",
  "data": null
}
```

**HTTP Status Codes**:
- 200: 휴대폰 번호 인증에 성공하였습니다.
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
| nickname | string | O | 닉네임 (Private & Shared 명세) |
| profileUrl | string | - | 프로필 이미지 URL |

```json
{
  "email": "user@example.com",
  "password": "password123!",
  "nickname": "멋진개발자",
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
- **Frontend Manager**: 정일혁 (Jeong Ilhyuk)

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
- **Frontend Manager**: 정일혁 (Jeong Ilhyuk)

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
- **URL**: `/auth/social/login`
- **Header Required**: No (포함 X)
- **Description**: 소셜(카카오) 액세스 토큰으로 로그인합니다.
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 정일혁 (Jeong Ilhyuk)

**Request** (Body)

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| accessToken | string | O | 카카오 액세스 토큰 |

**Response** `ApiResponse` (data: accessToken, refreshToken)

**HTTP Status Codes**: 200, 400

---

### 7. 비밀번호 변경 (Change Password)

- **Method**: POST
- **URL**: `/auth/password/change`
- **Header Required**: Yes (포함 O)
- **Description**: 비밀번호를 변경합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 정일혁 (Jeong Ilhyuk)

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
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 정일혁 (Jeong Ilhyuk)

**Request** `LogoutRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| refreshToken | string | O | 리프레시 토큰 |

**Response** `ApiResponse` <object>

---

## USER ENDPOINTS (10개)

---

### 1. 프로필 조회 (Get Profile)

- **Method**: GET
- **URL**: `/users/me`
- **Header Required**: Yes (포함 O)
- **Description**: 프로필 수정 진입 시 기존 정보를 표시합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data 필드)

| 필드 | 타입 | Nullable | 설명 |
|------|------|----------|------|
| name | string | N | 사용자 이름 |
| email | string | N | 이메일 |
| phone | string | O | 연락처 (미설정 시 null) |
| profileImageUrl | string | O | 프로필 이미지 URL (미설정 시 null) |

**HTTP Status Codes**:
- 200: 프로필 조회 성공
- 400: 잘못된 요청 또는 인증 오류
- 401: 인증 필요

---

### 2. 프로필 수정 (Update Profile)

- **Method**: PATCH
- **URL**: `/users/me`
- **Header Required**: Yes (포함 O)
- **Description**: 프로필을 수정합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Request** (Body — 모든 필드 선택)

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| name | string | - | 사용자 이름 |
| phone | string | - | 연락처 |
| profileImageUrl | string | - | 프로필 이미지 URL |

**HTTP Status Codes**:
- 200: 프로필 수정 성공
- 400: 잘못된 요청
- 401: 인증 필요

---

### 3. 푸시 알림 상태 조회 (Get Push Notification Settings)

- **Method**: GET
- **URL**: `/users/push-settings`
- **Header Required**: Yes (포함 O)
- **Description**: 로그인한 사용자의 푸시 알림 수신 설정 현재 상태를 조회합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| timeLetter | boolean | 타임레터 푸시 알림 수신 여부 |
| mindRecord | boolean | 마음의 기록 푸시 알림 수신 여부 |
| afterNote | boolean | 애프터노트 푸시 알림 수신 여부 |

**HTTP Status Codes**:
- 200: 조회 성공
- 400: 잘못된 요청
- 401: 인증되지 않은 사용자

---

### 4. 푸시 알림 설정 변경 (Update Push Notification Settings)

- **Method**: PATCH
- **URL**: `/users/push-settings`
- **Header Required**: Yes (포함 O)
- **Description**: 푸시 알림 수신 설정을 변경합니다. 각 알림 종류별 최종 수신 여부(true/false)를 서버로 전달합니다. (해제는 해당 항목을 false로 설정)
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Request** (Body)

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| timeLetter | boolean | O | 타임레터 푸시 알림 수신 여부 |
| mindRecord | boolean | O | 마음의 기록 푸시 알림 수신 여부 |
| afterNote | boolean | O | 애프터노트 푸시 알림 수신 여부 |

**HTTP Status Codes**:
- 200: 설정 변경 성공
- 400: 잘못된 요청
- 401: 인증되지 않은 사용자

---

### 5. 수신인 목록 조회 (Get Recipient List)

- **Method**: GET
- **URL**: `/users/receivers`
- **Header Required**: Yes (포함 O)
- **Description**: 수신인 목록을 조회합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| receivers | array | 수신인 목록 |
| receivers[].receiverId | long | 수신인 ID |
| receivers[].name | string | 수신인 이름 |
| receivers[].relation | string | 수신인과의 관계 (ENUM) |

**HTTP Status Codes**:
- 200: 수신인 목록 조회 성공
- 401: 인증 실패 (토큰 없음 또는 만료)
- 500: 서버 내부 오류

---

### 6. 수신자 등록 (Register Receiver)

- **Method**: POST
- **URL**: `/users/receivers`
- **Header Required**: Yes (포함 O)
- **Description**: 수신인을 등록합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Request** (Body)

| 필드 | 타입 | 필수 | Nullable | 설명 |
|------|------|------|----------|------|
| name | string | O | N | 수신자 이름 |
| phone | string | - | Y | 전화번호 |
| relation | string (ENUM) | O | N | 사용자와의 관계 (예: DAUGHTER) |
| email | string | - | Y | 이메일 |

**Response** (data 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| receiverId | long | 생성된 수신자 ID |

**HTTP Status Codes**:
- 201: 수신자 등록 성공
- 400: 요청 값 검증 실패
- 401: 인증 실패
- 500: 서버 내부 오류

---

### 7. 수신인 상세 조회 (Get Receiver Detail)

- **Method**: GET
- **URL**: `/users/receivers/{receiverId}`
- **Header Required**: Yes (포함 O)
- **Description**: 특정 수신인의 상세 정보를 조회합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data 필드)

| 필드 | 타입 | Nullable | 설명 |
|------|------|----------|------|
| receiverId | long | N | 수신인 ID |
| name | string | N | 수신인 이름 |
| relation | string | N | 수신인과의 관계 |
| phone | string | Y | 전화번호 |
| email | string | Y | 이메일 |
| dailyQuestionCount | int | N | 데일리 질문 답변 개수 |
| timeLetterCount | int | N | 타임레터 개수 |
| afterNoteCount | int | N | 애프터노트 개수 |

**HTTP Status Codes**:
- 200: 수신인 상세 조회 성공
- 401: 인증 실패
- 404: 수신인을 찾을 수 없음
- 500: 서버 내부 오류

---

### 8. 수신인별 데일리 질문 답변 목록 조회 (Get Daily Question Answers by Receiver)

- **Method**: GET
- **URL**: `/users/receivers/{receiverId}/daily-questions`
- **Header Required**: Yes (포함 O)
- **Description**: 특정 수신인에게 전달될 데일리 질문 답변 목록을 최신순으로 조회합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| items | array | 데일리 질문 답변 목록 |
| items[].dailyQuestionAnswerId | long | 답변 ID |
| items[].question | string | 질문 내용 |
| items[].answer | string | 답변 내용 |
| items[].createdAt | string (LocalDate) | 작성 날짜 |

**HTTP Status Codes**:
- 200: 데일리 질문 답변 목록 조회 성공
- 401: 인증 실패
- 403: 내 수신인이 아님
- 404: 수신인을 찾을 수 없음
- 500: 서버 내부 오류

---

### 9. 수신인별 애프터노트 목록 조회 (Get After-Notes by Receiver)

- **Method**: GET
- **URL**: `/users/receivers/{receiverId}/after-notes`
- **Header Required**: Yes (포함 O)
- **Description**: 특정 수신인에게 전달될 애프터노트 출처별 목록을 조회합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| items | array | 애프터노트 출처 목록 |
| items[].sourceType | string (ENUM) | INSTAGRAM / GALLERY / GUIDE / NAVER_MAIL |
| items[].lastUpdatedAt | string (LocalDate) | 해당 출처의 최근 작성일 |

**HTTP Status Codes**:
- 200: 애프터노트 목록 조회 성공
- 401: 인증 실패
- 403: 내 수신인이 아님
- 404: 수신인을 찾을 수 없음
- 500: 서버 내부 오류

---

### 10. 수신인별 타임레터 목록 조회 (Get Time-Letters by Receiver)

- **Method**: GET
- **URL**: `/users/receivers/{receiverId}/time-letters`
- **Header Required**: Yes (포함 O)
- **Description**: 특정 수신인에게 전달될 타임레터 목록을 조회합니다.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data 필드)

| 필드 | 타입 | 설명 |
|------|------|------|
| items | array | 타임레터 목록 |
| items[].timeLetterId | long | 타임레터 ID |
| items[].receiverName | string | 타임레터 수신인 이름 |
| items[].sendAt | string (LocalDate) | 발송 예정일 |
| items[].title | string | 타임레터 제목 |
| items[].content | string | 타임레터 내용 전체 |

**HTTP Status Codes**:
- 200: 타임레터 목록 조회 성공
- 400: 잘못된 요청
- 401: 인증 실패
- 403: 접근 권한 없음
- 404: 수신인을 찾을 수 없음
- 500: 서버 내부 오류

---

## AFTERNOTE ENDPOINTS (5개)

---

### 1. 모든 afternote 목록 (Get All Afternotes)

- **Method**: GET
- **URL**: `/afternotes?category=SOCIAL&page=0&size=10`
- **Header Required**: Yes (포함 O)
- **Description**: category 없으면 전체. 필터링할 카테고리, 페이지, 사이즈로 목록 조회.
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|----------|------|------|------|------|
| category | string | 선택 | SOCIAL, GALLERY, MUSIC (없으면 전체) | SOCIAL |
| page | int | 선택 | 페이지 번호 (0부터) | 0 |
| size | int | 선택 | 한 번에 가져올 개수 (기본 10) | 10 |

**Response** (data 필드): content (array: afternoteId, title, category, createdAt), page, size, hasNext

**HTTP Status Codes**: 200, 400

---

### 2. afternote 상세 목록 (Get Afternote Details)

- **Method**: GET
- **URL**: `/afternotes/{afternote_id}`
- **Header Required**: Yes (포함 O)
- **Description**: 상세 목록. 공통: afternoteId, category, title, createdAt, updatedAt. 카테고리별: credentials (SOCIAL), receivers (GALLERY), processMethod, actions, leaveMessage, playlist (PLAYLIST).
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Path**: afternote_id (int)

**HTTP Status Codes**: 200, 400

---

### 3. afternote 생성 (Create Afternote)

- **Method**: POST
- **URL**: `/afternotes`
- **Header Required**: Yes (포함 O)
- **Description**: afternote를 생성합니다. 카테고리별 필수/null 필드: SOCIAL (category, title, processMethod, actions, credentials; receivers, playlist null), GALLERY (receivers 필수; credentials, playlist null), PLAYLIST (playlist 필수; processMethod, actions, leaveMessage, credentials, receivers null).
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Request** (Body): category, title, processMethod, actions, leaveMessage, credentials / receivers / playlist (카테고리별)

**Response** (data): afternote_id

**HTTP Status Codes**: 200, 400

---

### 4. afternote 수정 (Update Afternote)

- **Method**: PATCH
- **URL**: `/afternotes/{afternote_id}`
- **Header Required**: Yes (포함 O)
- **Description**: afternote를 수정합니다. 카테고리 변경 불가. 수정하지 않을 필드는 생략. SOCIAL: title, processMethod, actions, leaveMessage, credentials. GALLERY: title, processMethod, actions, leaveMessage, receivers. PLAYLIST: title, playlist.
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Request** (Body): 카테고리별 수정 가능 필드만 전송

**Response** (data): afternote_id

**HTTP Status Codes**: 200, 400

---

### 5. afternote 삭제 (Delete Afternote)

- **Method**: DELETE
- **URL**: `/afternotes/{afternote_id}`
- **Header Required**: Yes (포함 O)
- **Description**: afternote를 삭제합니다. Body 없음.
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 황규운 (Hwang Gyuwun)
- **Frontend Manager**: 황규운 (Hwang Gyuwun)

**Response** (data): afternote_id

**HTTP Status Codes**: 200, 400

---

## TIME-LETTERS ENDPOINTS (7개)

---

### 1. 전체 조회 (Get All Time-Letters)

- **Method**: GET
- **URL**: `/time-letters`
- **Header Required**: Yes (포함 O)
- **Description**: List<TimeLetterInfoResponse>
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

---

### 2. 단일 조회, 임시저장 불러오기 (Get Single, Load Draft)

- **Method**: GET
- **URL**: `/time-letters/{timeLetterId}`
- **Header Required**: Yes (포함 O)
- **Description**: 단일 조회 또는 임시저장 불러오기
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

---

### 3. 등록 (Create)

- **Method**: POST
- **URL**: `/time-letters`
- **Header Required**: Yes (포함 O)
- **Description**: 날짜, 시간, 제목, 내용, 첨부파일, 임시저장여부, 수신자
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

---

### 4. 임시저장 전체 조회 (Get All Drafts)

- **Method**: GET
- **URL**: `/time-letters/temporary`
- **Header Required**: Yes (포함 O)
- **Description**: List<TimeLetterInfoResponse>. 임시저장된 타임레터 목록 조회.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

**HTTP Status Codes**: 200, 400

---

### 5. 단일, 다건 (종류 무관) 삭제 (Delete - Single or Multiple)

- **Method**: POST
- **URL**: `/time-letters/delete`
- **Header Required**: Yes (포함 O)
- **Description**: Body: List<TimeLetterId>. 단일 또는 다건 삭제.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

**HTTP Status Codes**: 200, 400

---

### 6. 임시저장 전체 삭제 (Delete All Drafts)

- **Method**: DELETE
- **URL**: `/time-letters/temporary`
- **Header Required**: Yes (포함 O)
- **Description**: 임시저장 전체 삭제
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

**HTTP Status Codes**: 200, 400

---

### 7. 수정 (Update)

- **Method**: PATCH
- **URL**: `/time-letters/{timeLetterId}`
- **Header Required**: Yes (포함 O)
- **Description**: time-letter를 수정합니다.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

**HTTP Status Codes**: 200, 400

---

## MIND-RECORD ENDPOINTS (7개)

---

### 1. 마음의기록 목록 조회 (Get Mind-Record List)

- **Method**: GET
- **URL**: `/mind-records`
- **Header Required**: Yes (포함 O)
- **Description**: 기록 목록 조회. type 파라미터로 데일리 질문(DAILY_QUESTION), 일기(DIARY), 깊은생각(DEEP_THOUGHT) 구분. view=LIST 또는 CALENDAR. CALENDAR일 때 year, month 사용.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Query Parameters**

| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|----------|------|------|------|------|
| type | string | O | DAILY_QUESTION, DIARY, DEEP_THOUGHT | DIARY |
| view | string | O | LIST, CALENDAR | LIST |
| year | int | O (CALENDAR 시) | 연도 | 2025 |
| month | int | O (CALENDAR 시) | 1~12 | 1 |

**Response** (data): records (recordId, type, title, date, isDraft), markedDates (캘린더용)

**HTTP Status Codes**: 200, 400

---

### 2. 마음의기록 단건 수정화면 조회 (Get Mind-Record for Edit)

- **Method**: GET
- **URL**: `/mind-records/{recordId}`
- **Header Required**: Yes (포함 O)
- **Description**: 기록 수정 화면 진입 시 기존 기록 조회.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data): recordId, type, title, content, date, isDraft, category (DEEP_THOUGHT일 때만)

**HTTP Status Codes**: 200, 400

---

### 3. 마음의기록 작성 (Create Mind-Record)

- **Method**: POST
- **URL**: `/mind-records`
- **Header Required**: Yes (포함 O)
- **Description**: 기록 작성. type으로 DAILY_QUESTION / DIARY / DEEP_THOUGHT 구분. DAILY_QUESTION 시 questionId, DEEP_THOUGHT 시 category 포함.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Request** (Body): type, title, content, date, isDraft, questionId (DAILY_QUESTION), category (DEEP_THOUGHT)

**Response** (data): recordId

**HTTP Status Codes**: 200, 400

---

### 4. 마음의기록 수정 (Update Mind-Record)

- **Method**: PATCH
- **URL**: `/mind-records/{recordId}`
- **Header Required**: Yes (포함 O)
- **Description**: 기록 수정. 타입 변경 불가. type별 questionId/category 규칙 동일.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Request** (Body): title, content, date, isDraft, questionId, category (타입별)

**Response** (data): recordId

**HTTP Status Codes**: 200, 400, 404

---

### 5. 마음의기록 삭제 (Delete Mind-Record)

- **Method**: DELETE
- **URL**: `/mind-records/{recordId}`
- **Header Required**: Yes (포함 O)
- **Description**: 기록 ID 기준 삭제.
- **Status**: 개발 완료 (Development Complete)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**HTTP Status Codes**: 200, 400, 404

---

### 6. 데일리 질문 조회 (Get Daily Question)

- **Method**: GET
- **URL**: `/daily-question`
- **Header Required**: Yes (포함 O)
- **Description**: 날짜 기준 하루 1개 질문. 해당 날짜에 이미 노출된 질문 있으면 기존 반환, 없으면 서버에서 새 질문 생성 후 반환. 서버 시간 자정 기준.
- **Status**: 개발 진행 중 (In Progress)
- **Backend Manager**: 김소희 (Kim Sohee)
- **Frontend Manager**: 김소희 (Kim Sohee)

**Response** (data): questionId, content

**HTTP Status Codes**: 200, 400 (INVALID_INPUT_VALUE), 404 (DAILY_QUESTION_NOT_FOUND)

---

### 7. 주간 리포트 조회 (Get Weekly Report)

- **Method**: GET
- **URL**: `/mind-records/weekly`
- **Header Required**: Yes (포함 O)
- **Description**: 선택한 주차에 해당하는 사용자 기록 목록 조회. year, week (ISO week) 쿼리.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: (Not specified)
- **Frontend Manager**: (Not specified)

**Query Parameters**: year (int), week (int, ISO week)

**Response** (data): records (recordId, type, title, contentPreview, date)

**HTTP Status Codes**: 200, 400

---

## RECEIVER (수신인 화면) ENDPOINTS (3개)

수신자가 로그인 없이 열람하는 API. Header 포함 X.

---

### 1. After-Note 조회 (Receiver)

- **Method**: GET
- **URL**: `/receiver/afternotes/{등록자Id}`
- **Header Required**: No (포함 X)
- **Description**: 수신인 화면에서 등록자별 애프터노트 조회.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

**HTTP Status Codes**: 200, 400

---

### 2. Mind-Record 조회 (Receiver)

- **Method**: GET
- **URL**: `/receiver/mind-records/{등록자Id}`
- **Header Required**: No (포함 X)
- **Description**: 수신인 화면에서 등록자별 마음의 기록 조회.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

**HTTP Status Codes**: 200, 400

---

### 3. Time-Letter 조회 (Receiver)

- **Method**: GET
- **URL**: `/receiver/time-letters/{등록자Id}`
- **Header Required**: No (포함 X)
- **Description**: 수신인 화면에서 등록자별 타임레터 조회.
- **Status**: 시작 전 (Not Started)
- **Backend Manager**: 영탁 조 (Youngtack Cho)
- **Frontend Manager**: 박경민 (Park Kyungmin)

**HTTP Status Codes**: 200, 400

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

## 공통 오류 응답

### 인증/인가 관련

| HTTP | code | message |
|------|------|---------|
| 401 | 999 | 인증이 필요합니다. 로그인해주세요. (유효한 액세스 토큰 없음) |
| 403 | 999 | 권한이 부족합니다. |
| 404 | 999 | 존재하지 않는 엔드포인트입니다. |

### 토큰 관련 (status 400)

| code | message |
|------|---------|
| 400 | Authorization 헤더 미설정 |
| 401 | 쿠키 값 미설정 |
| 402 | 엑세스 토큰 만료 |
| 403 | 유효하지 않은 엑세스 토큰 |
| 404 | 엑세스 토큰 타입 미일치 |
| 405 | 리프레시 토큰 미설정 |
| 406 | 리프레시 토큰 만료 |
| 407 | 유효하지 않은 리프레시 토큰 |
| 408 | 리프레시 토큰 타입 미일치 |
| 409 | 사용이 제한된 리프레시 토큰 |

---

## ERD / 도메인 개요

(출처: `docs/Private & Shared/API 기본 명세서/erd`)

### 도메인 추출

**Core Domain (사람/관계)**

- **User**: 자기 자신의 정보만 조회, 수정
- **Receiver**: 받는 사람
- **UserReceiver**: 누구의 Receiver인지 — 이 Receiver는 어떤 User의 수신자인가를 나타냄

**Mind-Record Domain (기록)**

- **DailyQuestion**: 일일 질문
- **DailyQuestionAnswer**
- **DeepThought**
- **Diary**: 일기
- **Emotion**

**TimeLetter Domain**

- **TimeLetter**: 예약 발송되는 타임레터 본문
- **TimeLetterMedia**: 이미지, 미디어 첨부
- **TimeLetterReceiver**: 타임레터 ↔ 수신자 연결

**Afternote Domain**

- **Afternote**: 애프터노트 기본 정보
- **AfternoteCategory**
- **AfternoteSecureContent**: 아이디, 비번 같은 민감 정보
- **AfternoteAction**: 처리 방법
- **AfternoteActionMapping**: 애프터노트 ↔ 처리 방법 연결
- **AfternotePlaylist**

**Auth / Audit Domain**

- **EmailVerification**: 이메일 인증 코드 및 검증 정보
- **RecieverAuth**: 애프터노트, 타임레터 열람 전 인증
- **AccessLog**: 사용자/수신자의 접근 기록

### 관계 정의

**Core Domain**

- User ↔ Receiver: N:M (한 User는 여러 Receiver를 가질 수 있고, 한 Receiver는 여러 User의 수신자가 될 수 있음)
- UserReceiver: User와 Receiver의 관계 Entity 역할

**Mind-Record Domain**

- User ↔ DailyQuestionAnswer: 1:N (한 User는 여러 데일리 질문 답변 가질 수 있음, 답변은 반드시 한 User에 속함)
- DailyQuestion ↔ DailyQuestionAnswer: 1:N (하나의 질문에 여러 사용자 답변 가능)
- User ↔ Diary: 1:N (일기는 User 소유)
- User ↔ DeepThought: 1:N (깊은 생각은 User 소유)
- User ↔ Emotion: Emotion은 User 기록에서 추출됨

**TimeLetter Domain**

- User ↔ TimeLetter: 1:N (타임레터는 User 기준으로 생성)
- TimeLetter ↔ TimeLetterReceiver ↔ Receiver: N:M (하나의 타임레터를 여러 수신자에게 보낼 수 있고, 한 Receiver는 여러 타임레터 받을 수 있음)
- TimeLetter ↔ TimeLetterMedia: 1:N (하나의 타임레터에 여러 미디어 첨부 가능)

**Afternote Domain**

- User ↔ Afternote: 1:N (애프터노트는 User 소유)
- Afternote ↔ AfternoteCategory: N:1 (하나의 애프터노트는 하나의 카테고리에 속하고, 카테고리는 여러 애프터노트에 사용)
- Afternote ↔ AfternoteSecureContent: 1:1 (민감정보 분리)
- Afternote ↔ AfternoteAction ↔ AfternoteActionMapping: N:M (하나의 애프터노트에 여러 처리 방법 선택 가능, 처리 방법은 여러 애프터노트에서 재사용)
- Afternote ↔ AfternotePlaylist: 1:1

**Auth / Audit Domain**

- User ↔ EmailVerification: 1:N (이메일 인증은 여러 번 발생 가능)
- Receiver ↔ ReceiverAuth: 1:N (수신자는 로그인 없이 인증만)
- User / Receiver ↔ AccessLog: 1:N (사용자/수신자의 접근 기록 — 열람 대상은 MindRecord, TimeLetter, Afternote)

---

## 참고

- 본 명세는 `docs/Private & Shared/API 기본 명세서` 내용을 통합하였습니다. 중복 없이 정리했습니다.
- **Receiver (수신인 화면) API**는 수신인이 로그인 없이 열람하는 엔드포인트입니다.
- 스웨거(OpenAPI) 우선. 스웨거 미등록 API는 스웨거 등록 후 확인·보완 예정입니다.
