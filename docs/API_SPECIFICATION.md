# API 기본 명세서 - 상세 정리

## Auth (인증) - 4개 API

### 1. 이메일 인증번호 보내기
- **Method**: POST
- **URL**: `/auth/email/send`
- **설명**: 이메일로 인증 번호를 보냅니다.
- **백엔드 관리자**: 황규운

**Request**
```json
{
  "email": "gka365@naver.com"
}
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

**Status**
- 200: "6자리 인증코드가 발송되었습니다."
- 400: (내용 미정)

***

### 2. 이메일 인증번호 확인
- **Method**: POST
- **URL**: `/auth/email/verify`
- **설명**: 받은 인증 번호로 인증을 합니다.
- **백엔드 관리자**: 황규운

**Request**
```json
{
  "email": "gka365@naver.com",
  "certificationCode": "123456"
}
```

**Response**
```json
{
  "status": 200,
  "code": 0,
  "message": "휴대폰 번호 인증에 성공하였습니다.",
  "data": {
    "isVerified": true
  }
}
```

**Status**
- 200: "휴대폰 번호 인증에 성공하였습니다."
- 400: (내용 미정)

***

### 3. 회원 가입
- **Method**: POST
- **URL**: `/auth/sign-up`
- **설명**: 이메일, 비밀 번호, 휴대폰 번호로 회원가입을 합니다.
- **백엔드 관리자**: 황규운
- **비고**: 비밀번호 확인은 프론트 측에서 처리 가능한 걸로 아는데 그걸로 부탁드립니다.

**Request**
```json
{
  "email": "user@example.com",
  "password": "strongPassword123!",
  "nickname": "멋진개발자",
  "profileImageUrl": "https://s3.ap-northeast-2.amazonaws.com/bucket/profile/image.jpg"
}
```

**Response**
```json
{
  "status": 200,
  "code": 0,
  "message": "회원가입이 완료되었습니다.",
  "data": {
    "memberId": 1,
    "email": "user@example.com"
  }
}
```

**Status**
- 200: (성공)
- 400: (내용 미정)

***

### 4. 로그인
- **Method**: POST
- **URL**: `/auth/login`
- **설명**: email과 비밀 번호로 로그인 합니다.
- **백엔드 관리자**: 황규운

***

## User (사용자) - 4개 API

### 1. 프로필 조회
- **Method**: GET
- **URL**: `/users/me`
- **백엔드 관리자**: 김소희

### 2. 프로필 이름/닉네임 수정
- **Method**: PATCH
- **URL**: `/users/me/profile`
- **백엔드 관리자**: 김소희

### 3. 이메일 주소 확인
- **Method**: GET
- **URL**: `/users/me/email`
- **백엔드 관리자**: 김소희

### 4. 비밀번호 변경
- **Method**: PATCH
- **URL**: `/users/me/password`
- **백엔드 관리자**: 김소희

***

## Afternote - 5개 API

### 1. 모든 afternote 목록
- **Method**: GET
- **URL**: `/afternotes?category=SOCIAL?page=0&size=10`
- **설명**: category 없으면 전체
- **백엔드 관리자**: 황규운

### 2. afternote 상세 목록
- **Method**: GET
- **URL**: `/afternotes/{afternote_id}`
- **설명**: 상세 목록
- **백엔드 관리자**: 황규운

### 3. afternote 생성
- **Method**: POST
- **URL**: `/afternotes/{afternote_id}`

### 4. afternote 수정
- **Method**: PATCH
- **URL**: `/afternotes/{afternote_id}`
- **백엔드 관리자**: 황규운

### 5. afternote 삭제
- **Method**: DELETE
- **URL**: `/afternotes/{afternote_id}`
- **백엔드 관리자**: 황규운

***

## Time-Letters - 7개 API

### 1. 전체 조회
- **Method**: GET
- **URL**: `/time-letters`
- **응답**: List<TimeLetterInfoResponse>
- **백엔드 관리자**: 영탁 조

### 2. 단일 조회, 임시저장 불러오기
- **Method**: GET
- **URL**: `/time-letters/{timeLetterId}`
- **백엔드 관리자**: 영탁 조

### 3. 등록
- **Method**: POST
- **URL**: `/time-letters`
- **내용**: 날짜, 시간, 제목, 내용, 첨부파일, 임시저장여부, 수신자
- **백엔드 관리자**: 영탁 조

### 4. 임시저장 전체 조회
- **Method**: GET
- **URL**: `/time-letters/temporary`
- **응답**: List<TimeLetterInfoResponse>
- **백엔드 관리자**: 영탁 조

### 5. 단일, 다건 (종류 무관) 삭제
- **Method**: POST
- **URL**: `/time-letters/delete`
- **Body**: List<TimeLetterId>
- **백엔드 관리자**: 영탁 조

### 6. 임시저장 전체 삭제
- **Method**: DELETE
- **URL**: `/time-letters/temporary`
- **백엔드 관리자**: 영탁 조

### 7. 수정
- **Method**: PATCH
- **URL**: `/time-letters/{timeLetterId}`
- **백엔드 관리자**: 영탁 조

***

## Received - 3개 API

### 1. Mind-Record 조회
- **Method**: GET
- **URL**: `receiver/mind-records/{등록자Id}`
- **비고**: 
  - 등록자 id가 필요한지 아닌지는 user 설계를 어떻게 하냐에 따라 달라짐
  - receiver가 하나의 recorder와 연결되어 있다면 등록자 id가 필요없고
  - 여러명과 연결된다면 id가 필요
- **응답**: List<MindRecordIReceiverResponse>

### 2. Time-Letter 조회
- **Method**: GET
- **URL**: `receiver/time-letters/{등록자Id}`
- **비고**: 이하동문
- **응답**: List<TimeLetterReceiverInfoResponse>

### 3. After-Note 조회
- **Method**: GET
- **URL**: `receiver/afternotes/{등록자Id}`
- **비고**: 이하동문
- **응답**: List<AfternoteReceiverInfoResponse>

***

## Mind-Record - 11개 API

### 1. 나의 모든 기록 조회
- **Method**: GET
- **URL**: `/mind-records`
- **응답**: 리스트형
- **백엔드 관리자**: 김소희

### 2. 월별 캘린더 조회
- **Method**: GET
- **URL**: `/mind-records/calendar`
- **백엔드 관리자**: 김소희

### 3. 데일리 질문 조회
- **Method**: GET
- **URL**: `/mind-records/daily-question`
- **백엔드 관리자**: 김소희

### 4. 데일리 질문 작성
- **Method**: POST
- **URL**: `/mind-records/daily-question`
- **백엔드 관리자**: 김소희

### 5. 데일리 질문 답변 수정
- **Method**: PATCH
- **URL**: `/mind-records/daily-question`
- **백엔드 관리자**: 김소희

### 6. 데일리 질문 답변 삭제
- **Method**: DELETE
- **URL**: `/mind-records/daily-question`
- **백엔드 관리자**: 김소희

### 7. 일기 조회
- **Method**: GET
- **URL**: `/mind-records/diaries`
- **백엔드 관리자**: 김소희

### 8. 일기 작성
- **Method**: POST
- **URL**: `/mind-records/diaries`
- **백엔드 관리자**: 김소희

### 9. 일기 수정
- **Method**: PATCH
- **URL**: `/mind-records/diaries`
- **백엔드 관리자**: 김소희

### 10. 일기 삭제
- **Method**: DELETE
- **URL**: `/mind-records/diaries`
- **백엔드 관리자**: 김소희

### 11. 주간리포트 조회
- **Method**: GET
- **URL**: `/mind-records/weekly-report`

***

## No 카테고리 - 1개 API

### 1. new Endpoint
- 세부 내용 미정

***

## 참고 사항

- **공통 오류 응답**: 별도 페이지에 정리되어 있음
- **파트 나누기**: 관련 정보 존재
- **erd**: ERD 문서 링크 존재
- **사용 예시 및 사용법**: 별도 섹션 존재

모든 API의 기본 구조는 다음과 같은 응답 형식을 따름:
```json
{
  "status": [HTTP 상태 코드],
  "code": [내부 코드],
  "message": "[메시지]",
  "data": [데이터 객체 또는 null]
}
```


