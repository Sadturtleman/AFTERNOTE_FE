# API 기본 명세서

> **기준**: 스웨거(OpenAPI) 우선. 이 문서는 스웨거와 동기화 목적.
>
> - **Swagger UI**: https://afternote.kro.kr/swagger-ui/index.html
> - **OpenAPI JSON**: https://afternote.kro.kr/v3/api-docs (동일 내용 로컬: `docs/openapi.json`)

---

## Auth (인증) — 스웨거 기준 7개

인증 필요 API: `Authorization: Bearer {accessToken}` (security: bearer-key)

---

### 1. 이메일 인증번호 발송

- **Method**: POST
- **URL**: `/auth/email/send`
- **설명**: 이메일을 입력해 인증번호를 발송한다.

**Request** `EmailSendRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| email | string | O | 이메일 주소 |

```json
{ "email": "user@example.com" }
```

**Response** `ApiResponse` &lt;object&gt; (data: null 또는 object)

---

### 2. 이메일 인증번호 확인

- **Method**: POST
- **URL**: `/auth/email/verify`
- **설명**: 이메일과 인증코드로 확인한다.

**Request** `EmailVerifyRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| email | string | O | 이메일 주소 |
| certificateCode | string | O | 인증번호 (스웨거: `certificateCode`) |

```json
{ "email": "user@example.com", "certificateCode": "123456" }
```

**Response** `ApiResponse` &lt;object&gt; (data 구조는 스웨거 미정의, 실제 응답 확인)

---

### 3. 회원가입

- **Method**: POST
- **URL**: `/auth/sign-up`
- **설명**: 회원가입을 한다.

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

**Response** `ApiResponse` &lt;SignupResponse&gt;

| data 필드 | 타입 | 설명 |
|-----------|------|------|
| userId | int64 | 가입한 회원 ID (스웨거: `userId`) |
| email | string | 가입 이메일 |

---

### 4. 로그인

- **Method**: POST
- **URL**: `/auth/login`
- **설명**: 로그인한다.

**Request** `LoginRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| email | string | O | 이메일 |
| password | string | O | 비밀번호 |

**Response** `ApiResponse` &lt;LoginResponse&gt;

| data 필드 | 타입 | 설명 |
|-----------|------|------|
| accessToken | string | 액세스 토큰 |
| refreshToken | string | 리프레시 토큰 |

---

### 5. 토큰 재발급

- **Method**: POST
- **URL**: `/auth/reissue`
- **설명**: 리프레시 토큰을 넣어 재발급을 한다.

**Request** `ReissueRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| refreshToken | string | O | 리프레시 토큰 |

**Response** `ApiResponse` &lt;ReissueResponse&gt;

| data 필드 | 타입 | 설명 |
|-----------|------|------|
| accessToken | string | 액세스 토큰 |
| refreshToken | string | 리프레시 토큰 |

---

### 6. 로그아웃

- **Method**: POST
- **URL**: `/auth/logout`
- **설명**: 리프레시 토큰을 입력한다.

**Request** `LogoutRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| refreshToken | string | O | 리프레시 토큰 |

**Response** `ApiResponse` &lt;object&gt;

---

### 7. 비밀번호 변경

- **Method**: POST
- **URL**: `/auth/password/change`
- **설명**: 현재 비밀번호와 새 비밀번호를 입력한다.

**Request** `PasswordChangeRequest`

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| currentPassword | string | O | 현재 비밀번호 |
| newPassword | string | O | 새 비밀번호 (형식: sign-up과 동일) |

**Response** `ApiResponse` &lt;object&gt;

---

## User (사용자) — 4개 API

> 스웨거 미등록. 아래는 이전 명세. 스웨거 등록 후 확인·보완.

- GET `/users/me` — 프로필 조회
- PATCH `/users/me/profile` — 프로필 이름/닉네임 수정
- GET `/users/me/email` — 이메일 주소 확인
- PATCH `/users/me/password` — 비밀번호 변경 (스웨거에는 Auth에 POST `/auth/password/change` 있음)

---

## Afternote — 5개 API

> 스웨거 미등록. 이전 명세.

- GET `/afternotes?category=&page=&size=` — 목록 (category 없으면 전체)
- GET `/afternotes/{afternote_id}` — 상세
- POST `/afternotes` (또는 `/{id}`) — 생성
- PATCH `/afternotes/{afternote_id}` — 수정
- DELETE `/afternotes/{afternote_id}` — 삭제

---

## Time-Letters — 7개 API

> 스웨거 미등록. 이전 명세.

- GET `/time-letters` — 전체 조회
- GET `/time-letters/{timeLetterId}` — 단일 조회
- POST `/time-letters` — 등록
- GET `/time-letters/temporary` — 임시저장 목록
- POST `/time-letters/delete` (Body: List&lt;TimeLetterId&gt;) — 삭제
- DELETE `/time-letters/temporary` — 임시저장 전체 삭제
- PATCH `/time-letters/{timeLetterId}` — 수정

---

## Received — 3개 API

> 스웨거 미등록. 이전 명세.

- GET `receiver/mind-records/{등록자Id}` — Mind-Record 조회
- GET `receiver/time-letters/{등록자Id}` — Time-Letter 조회
- GET `receiver/afternotes/{등록자Id}` — After-Note 조회

---

## Mind-Record — 11개 API

> 스웨거 미등록. 이전 명세.

- GET `/mind-records` — 나의 모든 기록
- GET `/mind-records/calendar` — 월별 캘린더
- GET/POST/PATCH/DELETE `/mind-records/daily-question` — 데일리 질문
- GET/POST/PATCH/DELETE `/mind-records/diaries` — 일기
- GET `/mind-records/weekly-report` — 주간리포트

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
