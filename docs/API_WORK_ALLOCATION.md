# API 작업 배분 (구조도 기준)

> **원칙**: 아래 작업은 전부 **사진 구조도(Clean Architecture)** 에 맞춰 진행한다.  
> - **Data**: API → DataSource → RepositoryImpl, Entity, Mapper(매핑: DTO/Entity → Model)  
> - **Domain**: Repository(인터페이스), UseCase, Model  
> - **UI**: ViewModel, View, UIModel  
> - **의존성 방향**: Presentation → Domain ← Data (의존성 주입으로 연결)

---

## 1. 정일혁

### 1-1. 모든 API의 DTO → RepositoryImpl (29개 전체)

**담당 계층**: **Data Layer**  
**포함 API**: Auth, User, Afternote, Time-Letters, Received, Mind-Record, 기타 전부 (29개)

**할 일**:
| 구성요소 | 설명 |
|----------|------|
| **API** | Retrofit 등 서비스/엔드포인트 정의 (또는 기존 연동) |
| **DataSource** | API 호출, 응답 수신 |
| **Entity** | Data 계층 내부 데이터 구조 (필요 시) |
| **Mapper** | DTO/Entity → **Domain Model** 매핑 (nonnull·기본값 등 정제) |
| **RepositoryImpl** | Domain의 `Repository` **인터페이스 구현**, DataSource·Mapper 사용 |

> **중요**: `Repository` **인터페이스**는 Domain 계층이며, 각 Feature의 Domain 담당자(UseCase 작성자)가 정의합니다. 정일혁은 **RepositoryImpl만 구현**합니다.  
> 예외: Auth와 User는 정일혁이 Domain까지 담당하므로, 해당 Feature의 Repository 인터페이스도 정일혁이 정의합니다.

> 요약: DTO를 nonnull 등으로 정제하는 **반복적인 Data 계층**을 한 명이 담당.

---

### 1-2. Auth API (8개): UseCase → ViewModel + UIModel

**담당 계층**: **Domain + UI (Auth만)**  
**대상 API** (참고: `docs/API_SPECIFICATION.md`):

1. 이메일 인증번호 보내기 `POST /auth/email/send`
2. 이메일 인증번호 확인 `POST /auth/email/verify`
3. 회원 가입 `POST /auth/sign-up`
4. 로그인 `POST /auth/login`
5. 토큰 재발급 `POST /auth/reissue`
6. 카카오 로그인 `POST /auth/kakao`
7. 비밀번호 변경 `POST /auth/password/change`
8. 로그아웃 `POST /auth/logout`

**할 일**:
| 구성요소 | 설명 |
|----------|------|
| **Repository** | Auth `Repository` 인터페이스 정의 (Domain 계층) |
| **UseCase** | Auth `Repository` 인터페이스 사용, 비즈니스 로직 수행 |
| **ViewModel** | UseCase 호출, UI 상태·이벤트 관리 |
| **UIModel** | 화면에 필요한 데이터만 가공, View/ViewModel에서 사용 |

> Auth는 중요도가 높아, **DTO→RepoImpl(1-1) + Repository 인터페이스 정의 + UseCase→ViewModel+UIModel** 까지 한 명이 담당.

---

### 1-3. User API (4개): UseCase → ViewModel + UIModel

**담당 계층**: **Domain + UI (User)**  
**대상 API** (스웨거 등록 완료):

1. 내 프로필 조회 `GET /users/me`
2. 프로필 수정 `PATCH /users/me`
3. 푸시 알림 설정 조회 `GET /users/push-settings`
4. 푸시 알림 설정 수정 `PATCH /users/push-settings`

**할 일**:
| 구성요소 | 설명 |
|----------|------|
| **Repository** | User `Repository` 인터페이스 정의 (Domain 계층) |
| **UseCase** | User `Repository` 인터페이스 사용, 프로필·푸시 설정 비즈니스 로직 |
| **ViewModel** | UseCase 호출, UI 상태·이벤트 관리 |
| **UIModel** | 화면에 필요한 데이터만 가공, View/ViewModel에서 사용 |

> User API도 스웨거 등록 완료로, **DTO→RepoImpl(1-1) + Repository 인터페이스 정의 + UseCase→ViewModel+UIModel** 까지 한 명이 담당.

---

## 2. 박경민

### 2-1. Time-Letters API (7개): Repository → UseCase → ViewModel + UIModel

**담당 계층**: **Domain + UI**  
**전제**: Time-Letters `Repository`(인터페이스)는 박경민이 정의, `RepositoryImpl`은 정일혁이 DTO→RepoImpl 작업에서 이미 구현.

**대상 API**:
1. 전체 조회 `GET /time-letters`
2. 단일 조회·임시저장 불러오기 `GET /time-letters/{timeLetterId}`
3. 등록 `POST /time-letters`
4. 임시저장 전체 조회 `GET /time-letters/temporary`
5. 단일·다건 삭제 `POST /time-letters/delete`
6. 임시저장 전체 삭제 `DELETE /time-letters/temporary`
7. 수정 `PATCH /time-letters/{timeLetterId}`

**할 일**:
| 구성요소 | 설명 |
|----------|------|
| **Repository** | Time-Letters `Repository` 인터페이스 정의 (Domain 계층) |
| **UseCase** | Time-Letters `Repository` 사용, 비즈니스 로직 |
| **ViewModel** | UseCase 호출, 상태·이벤트 처리 |
| **UIModel** | Time-Letters 화면용 모델 정의·가공 |

---

### 2-2. Received API (3개): Repository → UseCase → ViewModel + UIModel

**담당 계층**: **Domain + UI**  
**전제**: Received `Repository`(인터페이스)는 박경민이 정의, `RepositoryImpl`은 정일혁이 DTO→RepoImpl 작업에서 이미 구현.

**대상 API**:
1. Mind-Record 조회 `GET /receiver/mind-records/{등록자Id}`
2. Time-Letter 조회 `GET /receiver/time-letters/{등록자Id}`
3. After-Note 조회 `GET /receiver/afternotes/{등록자Id}`

**할 일**:
| 구성요소 | 설명 |
|----------|------|
| **Repository** | Received `Repository` 인터페이스 정의 (Domain 계층) |
| **UseCase** | Received `Repository` 사용, 수신자·연결 데이터 비즈니스 로직 |
| **ViewModel** | UseCase 호출, 수신자 화면 상태·이벤트 |
| **UIModel** | Received 화면용 모델 |

---

## 3. 안현지

### 3-1. Mind-Record API (11개): Repository → UseCase → ViewModel + UIModel

**담당 계층**: **Domain + UI**  
**전제**: Mind-Record `Repository`(인터페이스)는 안현지가 정의, `RepositoryImpl`은 정일혁이 DTO→RepoImpl 작업에서 이미 구현.

**대상 API**:
1. 나의 모든 기록 조회 `GET /mind-records`
2. 월별 캘린더 조회 `GET /mind-records/calendar`
3. 데일리 질문 조회 `GET /mind-records/daily-question`
4. 데일리 질문 작성 `POST /mind-records/daily-question`
5. 데일리 질문 답변 수정 `PATCH /mind-records/daily-question`
6. 데일리 질문 답변 삭제 `DELETE /mind-records/daily-question`
7. 일기 조회 `GET /mind-records/diaries`
8. 일기 작성 `POST /mind-records/diaries`
9. 일기 수정 `PATCH /mind-records/diaries`
10. 일기 삭제 `DELETE /mind-records/diaries`
11. 주간리포트 조회 `GET /mind-records/weekly-report`

**할 일**:
| 구성요소 | 설명 |
|----------|------|
| **Repository** | Mind-Record `Repository` 인터페이스 정의 (Domain 계층) |
| **UseCase** | Mind-Record `Repository` 사용, 기록·캘린더·일기·리포트 로직 |
| **ViewModel** | UseCase 호출, Mind-Record 화면 상태·이벤트 |
| **UIModel** | Mind-Record 화면용 모델 |

---

### 3-2. Afternote API (5개): Repository → UseCase → ViewModel + UIModel

**담당 계층**: **Domain + UI**  
**전제**: Afternote `Repository`(인터페이스)는 안현지가 정의, `RepositoryImpl`은 정일혁이 DTO→RepoImpl 작업에서 이미 구현.

**대상 API**:
1. 모든 afternote 목록 `GET /afternotes?category=&page=&size=`
2. afternote 상세 `GET /afternotes/{afternote_id}`
3. afternote 생성 `POST /afternotes`
4. afternote 수정 `PATCH /afternotes/{afternote_id}`
5. afternote 삭제 `DELETE /afternotes/{afternote_id}`

**할 일**:
| 구성요소 | 설명 |
|----------|------|
| **Repository** | Afternote `Repository` 인터페이스 정의 (Domain 계층) |
| **UseCase** | Afternote `Repository` 사용, afternote CRUD·목록 로직 |
| **ViewModel** | UseCase 호출, Afternote 화면 상태·이벤트 |
| **UIModel** | Afternote 화면용 모델 |

---

## 4. 구조도 대조표

| 구분 | Data (API·DataSource·RepoImpl·Entity·Mapper) | Domain (Repository·UseCase·Model) | UI (ViewModel·View·UIModel) |
|------|-----------------------------------------------|-----------------------------------|------------------------------|
| **정일혁** | 29개 API 전체 (RepositoryImpl 포함) | Auth 8개, User 4개 (Repository·UseCase·Model) | Auth 8개, User 4개 (ViewModel·UIModel) |
| **박경민** | — | Time-Letters 7, Received 3 (Repository·UseCase·Model) | Time-Letters 7, Received 3 (ViewModel·UIModel) |
| **안현지** | — | Mind-Record 11, Afternote 5 (Repository·UseCase·Model) | Mind-Record 11, Afternote 5 (ViewModel·UIModel) |

- **미배분 구간**:
  - ~~**User API 7개**~~ → ✅ **배분 완료** (정일혁, 스웨거 등록된 4개 기준)
  - **No카테고리(1개)**: (내용 미확정)
  
  → 추후 팀 논의 후 배분 예정

---

## 5. 참고

- **API 상세**: `docs/API_SPECIFICATION.md`
- **아키텍처·의존성**: `.cursor/rules/tech-stack/architecture.mdc`
- **백엔드 명세가 잘 되어, 여러 Repository 조합이 필요 없으면** UseCase를 건너뛰고 ViewModel+UIModel만 분배하는 경우도 팀 합의 하에 가능.

---

## 6. API 구현 기준 및 배포 관련

### API 구현 기준
- **⚠️ Critical: Swagger-First Implementation**: API 구현은 반드시 스웨거(Swagger)에 문서화된 API만 진행해야 합니다. (상세 규칙: `.cursor/rules/workflow/api-implementation.mdc` 참조)
- **스웨거(OpenAPI)가 가장 정확한 기준**: 스웨거가 준비되면 스웨거를 기준으로 구현합니다.
- **명세서는 보조 자료**: `docs/API_SPECIFICATION.md`는 스웨거를 참고하여 작성한 문서로, 스웨거와 불일치 시 스웨거를 우선합니다.
- **스웨거 테스트**: 스웨거 UI에서 직접 API 테스트가 가능하므로 구현 전 테스트 권장.

### 프론트엔드 배포 URL 제공
- **배포 시 백엔드에 프론트엔드 배포 URL 제공 필요**
- **사용 목적**:
  - **CORS 설정**: 백엔드에서 프론트엔드 도메인을 허용 목록에 추가
  - **OAuth 리다이렉트**: 카카오 로그인 등 OAuth 인증 후 프론트엔드로 리다이렉트
  - **이메일 인증 링크**: 이메일 인증 완료 후 프론트엔드로 리다이렉트
- **제공 시기**: 프론트엔드 배포 완료 후 백엔드 담당자에게 전달
